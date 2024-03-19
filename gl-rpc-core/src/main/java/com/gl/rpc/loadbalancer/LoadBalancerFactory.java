package com.gl.rpc.loadbalancer;

import com.gl.rpc.spi.SpiLoader;

public class LoadBalancerFactory {

    static {
        SpiLoader.load(LoadBalancer.class);
    }
    /**
     * 默认负载均衡器
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    public static LoadBalancer getLoadBalancer(String key){
        return SpiLoader.getInstance(LoadBalancer.class,key);
    }

}
