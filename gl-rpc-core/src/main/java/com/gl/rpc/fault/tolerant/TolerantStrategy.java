package com.gl.rpc.fault.tolerant;

import com.gl.rpc.model.RpcResponse;

import java.util.Map;

public interface TolerantStrategy {

    /**
     * 容错机制
     * @param context
     * @param e
     * @return
     */
    RpcResponse doTolerant(Map<String,Object> context , Exception e);

}
