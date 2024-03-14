package com.gl.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.gl.example.common.model.User;
import com.gl.example.common.service.UserService;
import com.gl.rpc.model.RpcRequest;
import com.gl.rpc.model.RpcResponse;
import com.gl.rpc.serializer.JdkSerializer;
import com.gl.rpc.serializer.Serializer;

import java.io.IOException;

public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {
        Serializer serializer = new JdkSerializer();
        RpcRequest rpcRequest = RpcRequest.builder().serviceName(UserService.class.getName())
                .methodName("getUser").parameterTypes(new Class[]{User.class}).args(new Object[]{user}).build();
        try {
            byte[] bytes = serializer.serialize(rpcRequest);
            byte[] result;
            try(HttpResponse httpResponse = HttpRequest.post("http://localhost:8080").
                    body(bytes).execute()){
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result,RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
