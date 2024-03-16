package com.gl.rpc.proxy;

import java.lang.reflect.Proxy;

public class ServiceProxyFactory {


    public static<T> T getProxy(Class<T> serivceClass){
        return (T) Proxy.newProxyInstance(
                serivceClass.getClassLoader(),
                new Class[]{serivceClass},
                new ServiceProxy());
    }

    public static<T> T getMockProxy(Class<T> tClass){
        return (T) Proxy.newProxyInstance(
                tClass.getClassLoader(),
                new Class[]{tClass},
                new MockServiceProxy()
        );
    }

}
