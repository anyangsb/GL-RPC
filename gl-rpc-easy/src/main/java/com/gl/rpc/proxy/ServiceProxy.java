package com.gl.rpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.gl.rpc.model.RpcRequest;
import com.gl.rpc.model.RpcResponse;
import com.gl.rpc.serializer.JdkSerializer;
import com.gl.rpc.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Serializer serializer = new JdkSerializer();
        RpcRequest rpcRequest = RpcRequest.builder().serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName()).parameterTypes(method.getParameterTypes())
                .args(args).build();
        try {
            byte[] bytes = serializer.serialize(rpcRequest);
            byte[] result;
            //发送请求
            //todo 这里不应该硬编码，需要搭配注册中心和服务发现机制来实现
            try(HttpResponse httpResponse = HttpRequest.post("http://localhost:8080").
                    body(bytes).execute()){
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result,RpcResponse.class);
            return rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
