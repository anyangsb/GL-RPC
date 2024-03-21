package com.gl.rpc.protocol;

import com.gl.rpc.model.RpcRequest;
import com.gl.rpc.model.RpcResponse;
import com.gl.rpc.serializer.Serializer;
import com.gl.rpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

public class ProtocolMessageDecoder {

    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        byte magic = buffer.getByte(0);
        if(magic!= ProtocolConstant.PROTOCOL_MAGIC){
            throw new RuntimeException("消息 magic 非法");
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));
        //解决粘包问题
        byte[] bytes = buffer.getBytes(17, 17 + header.getBodyLength());
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if(serializerEnum == null){
            throw new RuntimeException("序列化消息的协议不存在");
        }
        Serializer serializer = SerializerFactory.getSerializer(serializerEnum.getValue());
        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getEnumByKey(header.getType());
        if(messageTypeEnum == null){
            throw new RuntimeException("序列化消息类型不存在");
        }
        switch (messageTypeEnum){
            case REQUEST:
                RpcRequest rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
                return new ProtocolMessage<>(header,rpcRequest);
            case RESPONSE:
                RpcResponse rpcResponse = serializer.deserialize(bytes, RpcResponse.class);
                return new ProtocolMessage<>(header,rpcResponse);
            case HEART_BEAT:
            case OTHERS:
            default:
                throw new RuntimeException("暂不支持该消息类型");
        }
    }

}
