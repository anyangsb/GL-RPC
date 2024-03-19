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

        NetServer server = vertx.createNetServer();

        server.connectHandler(socket -> {
            //处理请求
                //构造parser
                RecordParser parser = RecordParser.newFixed(8);
                parser.setOutput(new Handler<Buffer>() {
                    int size = -1;
                    //一次完整的读取（全部）
                    Buffer resultBuffer = Buffer.buffer();
                    @Override
                    public void handle(Buffer buffer) {
                        if(-1 == size){
                            size = buffer.getInt(4);
                            parser.fixedSizeMode(size);
                            //写入头信息到结果
                            resultBuffer.appendBuffer(buffer);
                        }else {
                            resultBuffer.appendBuffer(buffer);
                            System.out.println(resultBuffer.toString());
                            //重置一轮
                            parser.fixedSizeMode(8);
                            size = -1;
                            resultBuffer = Buffer.buffer();
                        }

                    }
                });
                socket.handler(parser);
        });


        //启动TCP服务器监听指定端口
        server.listen(port,result->{
            if(result.succeeded()){
                log.info("TCP server started on port：" + port);
            }else {
                log.error("Failed to start TCP server: " + result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
}
