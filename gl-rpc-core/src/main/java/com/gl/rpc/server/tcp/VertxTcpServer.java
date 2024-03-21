package com.gl.rpc.server.tcp;

import com.gl.rpc.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VertxTcpServer implements HttpServer {



    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();

//        NetServer server = vertx.createNetServer();
        NetServer server = vertx.createNetServer();
        server.connectHandler(new TcpServerHandler());


        //启动TCP服务器监听指定端口
        server.listen(port,result->{
            if(result.succeeded()){
                log.info("正在监听" + port);
            }else {
                log.error("监听失败" + result.cause());
            }
        });
    }

}
