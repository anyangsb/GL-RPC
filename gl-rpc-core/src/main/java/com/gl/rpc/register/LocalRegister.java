package com.gl.rpc.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRegister {

    private static final Map<String,Class<?>> map = new ConcurrentHashMap<>();

    //注册服务
    public static void register(String name , Class<?> ImplClass){
        map.put(name,ImplClass);
    }

    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
    }

    public static void remove(String serviceName) {
        map.remove(serviceName);
    }

}
