// Copyright (c) 2018 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
import ballerina/grpc;
import ballerina/io;

endpoint grpc:Listener ep3 {
    host:"localhost",
    port:9092
};

service HelloWorld3 bind ep3 {

    testIntArrayInput(endpoint caller, TestInt req) {
        io:println(req);
        int[] numbers = req.values;
        int result = 0;
        foreach number in numbers {
            result = result + number;
        }
        error? err = caller->send(result);
        if (err is error) {
            io:println("Error from Connector: " + err.reason());
        } else {
            io:println("Result: " + result);
        }
        _ = caller->complete();
    }

    testStringArrayInput(endpoint caller, TestString req) {
        io:println(req);
        string[] values = req.values;
        string result = "";
        foreach value in values {
            result = result + "," + value;
        }
        error? err = caller->send(result);
        if (err is error) {
            io:println("Error from Connector: " + err.reason());
        } else {
            io:println("Result: " + result);
        }
        _ = caller->complete();
    }

    testFloatArrayInput(endpoint caller, TestFloat req) {
        io:println(req);
        float[] values = req.values;
        float result = 0.0;
        foreach value in values {
            result = result + value;
        }
        error? err = caller->send(result);
        if (err is error) {
            io:println("Error from Connector: " + err.reason());
        } else {
            io:println("Result: " + result);
        }
        _ = caller->complete();
    }

    testBooleanArrayInput(endpoint caller, TestBoolean req) {
        io:println(req);
        boolean[] values = req.values;
        boolean result = false;
        foreach value in values {
            result = result || value;
        }
        error? err = caller->send(result);
        if (err is error) {
            io:println("Error from Connector: " + err.reason());
        } else {
            io:println("Result: " + result);
        }
        _ = caller->complete();
    }

    testStructArrayInput(endpoint caller, TestStruct req) {
        io:println(req);
        A[] values = req.values;
        string result = "";
        foreach value in values {
            result = result + "," + value.name;
        }
        error? err = caller->send(result);
        if (err is error) {
            io:println("Error from Connector: " + err.reason());
        } else {
            io:println("Result: " + result);
        }
        _ = caller->complete();
    }

    testIntArrayOutput(endpoint caller) {
        TestInt intArray = {values:[1, 2, 3, 4, 5]};
        error? err = caller->send(intArray);
        if (err is error) {
            io:println("Error from Connector: " + err.reason());
        } else {
            io:println(intArray);
        }
        _ = caller->complete();
    }

    testStringArrayOutput(endpoint caller) {
        TestString stringArray = {values:["A", "B", "C"]};
        error? err = caller->send(stringArray);
        if (err is error) {
            io:println("Error from Connector: " + err.reason());
        } else {
            io:println(stringArray);
        }
        _ = caller->complete();
    }

    testFloatArrayOutput(endpoint caller) {
        TestFloat floatArray = {values:[1.1, 1.2, 1.3, 1.4, 1.5]};
        error? err = caller->send(floatArray);
        if (err is error) {
            io:println("Error from Connector: " + err.reason());
        } else {
            io:println(floatArray);
        }
        _ = caller->complete();
    }

    testBooleanArrayOutput(endpoint caller) {
        TestBoolean booleanArray = {values:[true, false, true]};
        error? err = caller->send(booleanArray);
        if (err is error) {
            io:println("Error from Connector: " + err.reason());
        } else {
            io:println(booleanArray);
        }
        _ = caller->complete();
    }

    testStructArrayOutput(endpoint caller) {
        A a1 = {name:"Sam"};
        A a2 = {name:"John"};
        TestStruct structArray = {values:[a1, a2]};
        error? err = caller->send(structArray);
        if (err is error) {
            io:println("Error from Connector: " + err.reason());
        } else {
            io:println(structArray);
        }
        _ = caller->complete();
    }
}

type TestInt record {
    int[] values = [];
};

type TestString record {
    string[] values = [];
};

type TestFloat record {
    float[] values = [];
};

type TestBoolean record {
    boolean[] values = [];
};

type TestStruct record {
    A[] values = [];
};

type A record {
    string name = "";
};
