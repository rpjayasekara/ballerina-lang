/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except 
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ballerinalang.persistence.serializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ballerinalang.bre.bvm.CallableWorkerResponseContext;
import org.ballerinalang.bre.bvm.WorkerData;
import org.ballerinalang.bre.bvm.WorkerExecutionContext;
import org.ballerinalang.bre.bvm.WorkerState;
import org.ballerinalang.persistence.PersistenceUtils;
import org.ballerinalang.util.codegen.CallableUnitInfo;
import org.ballerinalang.util.codegen.PackageInfo;
import org.ballerinalang.util.codegen.ProgramFile;
import org.ballerinalang.util.codegen.ResourceInfo;
import org.ballerinalang.util.codegen.ServiceInfo;
import org.ballerinalang.util.codegen.WorkerInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a serializable Ballerina execution context.
 *
 * @since 0.976.0
 */
public class SerializableContext {

    public String contextKey;

    public String parent;

    public String respContextKey;

    public WorkerState state = WorkerState.CREATED;

    public HashMap<String, Object> globalProps = new HashMap<>();

    public HashMap<String, Object> localProps = new HashMap<>();

    public int ip;

    public SerializableWorkerData workerLocal;

    public SerializableWorkerData workerResult;

    public int[] retRegIndexes;

    public boolean runInCaller;

    public boolean interruptible;

    public String enclosingServiceName;

    public String callableUnitName;

    public String callableUnitPkgPath;

    public String workerName;

    public static SerializableContext deserialize(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, SerializableContext.class);
    }

    private void populateGlobalProps(Map<String, Object> props, SerializableState state) {
        if (props == null) {
            return;
        }
        for (String key : props.keySet()) {
            Object v = props.get(key);
            if (v == null) {
                globalProps.put(key, null);
            }
            Object s = state.serialize(v);
            globalProps.put(key, s);
        }
    }

    private void populateLocalProps(Map<String, Object> props, SerializableState state) {
        if (props == null) {
            return;
        }
        for (String key : props.keySet()) {
            Object v = props.get(key);
            if (v == null) {
                globalProps.put(key, null);
            } else {
                Object s = state.serialize(v);
                if (s != null) {
                    globalProps.put(key, s);
                } else {
                    LocalPropKey localPropKey = new LocalPropKey(key, v.getClass().getName());
                    localProps.put(key, localPropKey);
                }
            }
        }
    }

    private Map<String, Object> prepareLocalProps(
            SerializableState state, WorkerExecutionContext context, ProgramFile programFile) {
        Map<String, Object> props = new HashMap<>();
        if (localProps != null) {
            for (String key : localProps.keySet()) {
                Object v = localProps.get(key);
                if (v == null) {
                    props.put(key, null);
                    continue;
                }
                if (v instanceof LocalPropKey) {
                    PersistenceUtils.getDataMapper().mapLocalProp(state.getSerializationId(),
                                                                       (LocalPropKey) v, context);
                } else {
                    state.deserialize(v, programFile);
                    props.put(key, v);
                }
            }
        }
        return props;
    }

    private Map<String, Object> prepareGlobalProps(SerializableState state, ProgramFile programFile) {
        Map<String, Object> props = new HashMap<>();
        if (globalProps != null) {
            for (String key : globalProps.keySet()) {
                Object v = globalProps.get(key);
                if (v == null) {
                    props.put(key, null);
                    continue;
                }
                state.deserialize(v, programFile);
                props.put(key, v);
            }
        }
        return props;
    }

    public SerializableContext(String contextKey, WorkerExecutionContext ctx, SerializableState state) {
        this.contextKey = contextKey;
        ip = ctx.ip;
        populateGlobalProps(ctx.globalProps, state);
        populateLocalProps(ctx.localProps, state);
        retRegIndexes = ctx.retRegIndexes;
        runInCaller = ctx.runInCaller;
        interruptible = ctx.interruptible;

        if (ctx.callableUnitInfo != null) {
            if (ctx.callableUnitInfo instanceof ResourceInfo) {
                enclosingServiceName = ((ResourceInfo) ctx.callableUnitInfo).getServiceInfo().getName();
            }
            callableUnitName = ctx.callableUnitInfo.getName();
            callableUnitPkgPath = ctx.callableUnitInfo.getPkgPath();
        }
        if (ctx.workerInfo != null) {
            workerName = ctx.workerInfo.getWorkerName();
        }
        if (ctx.respCtx != null) {
            if (ctx.respCtx instanceof CallableWorkerResponseContext) {
                CallableWorkerResponseContext callableRespCtx =
                        (CallableWorkerResponseContext) ctx.respCtx;
                respContextKey = state.addRespContext(callableRespCtx);
            }
        }

        if (ctx.workerLocal != null) {
            workerLocal = new SerializableWorkerData(ctx.workerLocal, state);
        }
        if (ctx.workerResult != null) {
            workerResult = new SerializableWorkerData(ctx.workerResult, state);
        }
        if (ctx.parent != null) {
            parent = state.addContext(ctx.parent);
        }
    }

    public WorkerExecutionContext getWorkerExecutionContext(ProgramFile programFile, SerializableState state) {

        if (PersistenceUtils.getTempContexts().containsKey(contextKey)) {
            return PersistenceUtils.getTempContexts().get(contextKey);
        }

        CallableUnitInfo callableUnitInfo = null;
        WorkerInfo workerInfo = null;
        WorkerData workerLocalData = null;
        WorkerData workerResultData = null;

        if (workerLocal != null) {
            workerLocalData = workerLocal.getWorkerData(programFile, state);
        }
        if (workerResult != null) {
            workerResultData = workerResult.getWorkerData(programFile, state);
        }

        if (callableUnitPkgPath != null) {
            PackageInfo packageInfo = programFile.getPackageInfo(callableUnitPkgPath);
            if (enclosingServiceName != null) {
                ServiceInfo serviceInfo = packageInfo.getServiceInfo(enclosingServiceName);
                callableUnitInfo = serviceInfo.getResourceInfo(callableUnitName);
                if ("default".equals(workerName)) {
                    workerInfo = callableUnitInfo.getDefaultWorkerInfo();
                } else {
                    workerInfo = callableUnitInfo.getWorkerInfo(workerName);
                }
            } else {
                callableUnitInfo = packageInfo.getFunctionInfo(callableUnitName);
                if ("default".equals(workerName)) {
                    workerInfo = callableUnitInfo.getDefaultWorkerInfo();
                } else {
                    workerInfo = callableUnitInfo.getWorkerInfo(workerName);
                }
            }
        }

        WorkerExecutionContext workerExecutionContext;
        if (parent == null) {
            // this is the root context
            workerExecutionContext = new WorkerExecutionContext(programFile);
            workerExecutionContext.workerLocal = workerLocalData;
            workerExecutionContext.workerResult = workerResultData;
        } else {
            WorkerExecutionContext parentCtx = state.getContext(parent, programFile);
            CallableWorkerResponseContext respCtx =
                    state.getResponseContext(respContextKey, programFile, callableUnitInfo);
            workerExecutionContext = new WorkerExecutionContext(
                    parentCtx, respCtx, callableUnitInfo, workerInfo, workerLocalData, workerResultData, retRegIndexes,
                    runInCaller);
        }
        workerExecutionContext.globalProps = prepareGlobalProps(state, programFile);
        workerExecutionContext.localProps = prepareLocalProps(state, workerExecutionContext, programFile);
        workerExecutionContext.ip = ip;
        workerExecutionContext.interruptible = interruptible;

        PersistenceUtils.getTempContexts().put(contextKey, workerExecutionContext);

        return workerExecutionContext;
    }

    public String serialize() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public WorkerState getState() {
        return state;
    }

    public void setState(WorkerState state) {
        this.state = state;
    }

    public int getIp() {
        return ip;
    }

    public void setIp(int ip) {
        this.ip = ip;
    }

    public SerializableWorkerData getWorkerLocal() {
        return workerLocal;
    }

    public void setWorkerLocal(SerializableWorkerData workerLocal) {
        this.workerLocal = workerLocal;
    }

    public int[] getRetRegIndexes() {
        return retRegIndexes;
    }

    public void setRetRegIndexes(int[] retRegIndexes) {
        this.retRegIndexes = retRegIndexes;
    }
}
