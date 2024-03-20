package com.gl.example.provider;

import com.gl.example.common.service.UserService;
import com.gl.rpc.RpcApplication;
import com.gl.rpc.registry.LocalRegistry;
import com.gl.rpc.server.VertxHttpServer;

@Deprecated
public class EasyProviderExample {

    public static void main(String[] args) {
        //提供服务

        RpcApplication.init();

        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);

        VertxHttpServer server = new VertxHttpServer();
        server.doStart(8080);
    }
}
