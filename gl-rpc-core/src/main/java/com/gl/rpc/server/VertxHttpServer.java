package com.gl.rpc.server;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer{
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();
        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();

        //处理请求
        httpServer.requestHandler(new HttpHandlerServer());

        httpServer.listen(port,result->{
            if(result.succeeded()){
                System.out.println("listening now" + port);
            }else{
                System.out.println("Fail to start the server" + result.cause());
            }
        });
    }
}
