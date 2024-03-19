package com.gl.rpc.config;

import com.gl.rpc.fault.retry.RetryStrategyKeys;
import com.gl.rpc.fault.tolerant.TolerantStrategyKeys;
import com.gl.rpc.loadbalancer.LoadBalancerKeys;
import com.gl.rpc.serializer.Serializer;
import com.gl.rpc.serializer.SerializerFactory;
import com.gl.rpc.serializer.SerializerKeys;
import lombok.Data;

@Data
public class RpcConfig {

    private String name = "gl-rpc";

    private String version = "1.0";

    private String serverHost = "localhost";

    private Integer serverPort = 8080;

    /**
     * 是否调用mock
     */
    private Boolean mock = false;

    /**
     * 配置默认序列器
     */
    private String serializer = SerializerKeys.JDK;
    /**
     * 注册中心
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     */
    private String retryStrategy = RetryStrategyKeys.NO;


    /**
     * 容错机制
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;
}
