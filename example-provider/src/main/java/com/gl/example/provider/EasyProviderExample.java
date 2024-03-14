package com.gl.example.provider;

import com.gl.example.common.service.UserService;
import com.gl.rpc.register.LocalRegister;
import com.gl.rpc.server.VertxHttpServer;

public class EasyProviderExample {

    public static void main(String[] args) {
        //提供服务

        LocalRegister.register(UserService.class.getName(),UserServiceImpl.class);

        VertxHttpServer server = new VertxHttpServer();
        server.doStart(8080);
    }
}
