package com.gl.rpc.server;

import com.gl.rpc.RpcApplication;
import com.gl.rpc.model.RpcRequest;
import com.gl.rpc.model.RpcResponse;
import com.gl.rpc.registry.LocalRegistry;
import com.gl.rpc.serializer.Serializer;
import com.gl.rpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

public class HttpHandlerServer implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest httpServerRequest) {
        final Serializer serializer = SerializerFactory.getSerializer(RpcApplication.getRpcConfig().getSerializer());

        System.out.println("Receive request"+ httpServerRequest.method() + httpServerRequest.uri());
        //异步处理http请求
        httpServerRequest.bodyHandler(body->{
           byte[] bytes = body.getBytes();
           RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes,RpcRequest.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            RpcResponse rpcResponse = new RpcResponse();
            if(rpcRequest == null){
                rpcResponse.setMessage("request is null");
                doResponse(httpServerRequest,rpcResponse,serializer);
                return;
            }
            try {
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object res = method.invoke(implClass.newInstance(),rpcRequest.getArgs());
                rpcResponse.setData(res);
                rpcResponse.setMessage("ok");
                rpcResponse.setDataType(method.getReturnType());
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setException(e);
                rpcResponse.setMessage(e.getMessage());
            }
            doResponse(httpServerRequest,rpcResponse,serializer);
        });
    }

    public void doResponse(HttpServerRequest httpServerRequest, RpcResponse rpcResponse, Serializer serializer){
        HttpServerResponse httpServerResponse = httpServerRequest.response()
                .putHeader("content-type", "application/json");
        try {
            byte[] bytes = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
