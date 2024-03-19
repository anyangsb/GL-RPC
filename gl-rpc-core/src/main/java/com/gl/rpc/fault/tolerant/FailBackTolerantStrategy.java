package com.gl.rpc.fault.tolerant;

import com.gl.rpc.model.RpcResponse;

import java.util.Map;

public class FailBackTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        return null;
    }
}
