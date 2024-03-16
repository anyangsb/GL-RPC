package com.gl.rpc.serializer;

import com.gl.rpc.spi.SpiLoader;

import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;
import java.util.Map;

public class SerializerFactory {

//    private static final Map<String,Serializer> KEY_SERIALIZER_MAP = new HashMap<String,Serializer>(){{
//        put(SerializerKeys.JDK,new JdkSerializer());
//        put(SerializerKeys.JSON,new JsonSerializer());
//        put(SerializerKeys.KRYO,new KryoSerializer());
//        put(SerializerKeys.HESSIAN,new HessianSerializer());
//    }};
    static {
        SpiLoader.loadAll();
    }
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    public static Serializer getSerializer(String key){

        return SpiLoader.getInstance(Serializer.class,key);
    }

}
