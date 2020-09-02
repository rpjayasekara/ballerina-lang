// Copyright (c) 2019 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/config;
import ballerina/http;
import ballerina/log;

http:ListenerConfiguration helloWorldEPConfig = {
    secureSocket: {
        trustStore: {
            path: config:getAsString("truststore"),
            password: "ballerina"
        },
        keyStore: {
            path: config:getAsString("keystore"),
            password: "ballerina"
        }
    },
   httpVersion: "2.0"

};

http:ClientConfiguration endpointConfig = {
    secureSocket: {
        trustStore: {
            path: config:getAsString("truststore"),
            password: "ballerina"
        }
    },
   httpVersion: "2.0"
};
http:Client clientEP = new("https://localhost:9093", endpointConfig);
listener http:Listener helloWorldEP = new(9107, helloWorldEPConfig);

@http:ServiceConfig {
    basePath: "/"
}
service passthrough on helloWorldEP {
    @http:ResourceConfig {
        path: "Chat/chat"
    }

    resource function hello(http:Caller caller, http:Request req) {
        log:printInfo("[http2-passthrough] Invoke http2 service");
        req.setHeader("Test", "mytest");
        var clientResponse = clientEP->forward("/Chat/chat", req);

        if (clientResponse is http:Response) {
            log:printInfo("[http2-passthrough] send client response");
            var result = caller->respond(clientResponse);
            if (result is error) {
               log:printError("[http2-passthrough] Error sending response", err = result);
               http:Response res = new;
               res.statusCode = 500;
               res.setPayload(<@untainted>result.message());
               result = caller->respond(res);
            }
        } else {
            log:printError("[http2-passthrough] Error forwarding the message", err = clientResponse);
            http:Response res = new;
            res.statusCode = 500;
            res.setPayload(clientResponse.message());
            var result = caller->respond(res);
            if (result is error) {
               log:printError("[http2-passthrough] Error sending response", err = result);
            }
        }
    }
}