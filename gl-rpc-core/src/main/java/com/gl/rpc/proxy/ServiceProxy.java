package com.gl.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.gl.rpc.RpcApplication;
import com.gl.rpc.config.RpcConfig;
import com.gl.rpc.constant.RpcConstant;
import com.gl.rpc.fault.retry.RetryStrategy;
import com.gl.rpc.fault.retry.RetryStrategyFactory;
import com.gl.rpc.fault.tolerant.TolerantStrategy;
import com.gl.rpc.fault.tolerant.TolerantStrategyFactory;
import com.gl.rpc.loadbalancer.LoadBalancer;
import com.gl.rpc.loadbalancer.LoadBalancerFactory;
import com.gl.rpc.model.RpcRequest;
import com.gl.rpc.model.RpcResponse;
import com.gl.rpc.model.ServiceMetaInfo;
import com.gl.rpc.protocol.*;
import com.gl.rpc.registry.RegistryFactory;
import com.gl.rpc.registry.Registry;
import com.gl.rpc.serializer.Serializer;
import com.gl.rpc.serializer.SerializerFactory;
import com.gl.rpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;


import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Serializer serializer = SerializerFactory.getSerializer(RpcApplication.getRpcConfig().getSerializer());
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

            //从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getRegistry(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(method.getDeclaringClass().getName());
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);

            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if(CollUtil.isEmpty(serviceMetaInfoList)){
                throw new RuntimeException("暂无服务地址");
            }
            //应用负载均衡器
            LoadBalancer loadBalancer = LoadBalancerFactory.
                    getLoadBalancer(rpcConfig.getLoadBalancer());
            Map<String,Object> requestParams = new HashMap<>();
            requestParams.put("methodName",rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
            //发送TCP请求
            //使用重试机制

        RpcResponse rpcResponse;
        try {
            RetryStrategy retryStrategy = RetryStrategyFactory.getRetryStrategy(rpcConfig.getRetryStrategy());
            rpcResponse = VertxTcpClient.doRequest(rpcRequest,selectedServiceMetaInfo);

        } catch (Exception e) {
            //容错机制
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getTolerantStrategy(RpcApplication.
                    getRpcConfig().getTolerantStrategy());
            rpcResponse = tolerantStrategy.doTolerant(null,e);
        }
        return rpcResponse.getData();
    }
}
