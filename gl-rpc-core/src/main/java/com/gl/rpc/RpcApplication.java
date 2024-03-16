package com.gl.rpc;

import com.gl.rpc.config.RegistryConfig;
import com.gl.rpc.config.RpcConfig;
import com.gl.rpc.constant.RpcConstant;
import com.gl.rpc.registry.RegistryFactory;
import com.gl.rpc.registry.Registry;
import com.gl.rpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig newConfig){
        rpcConfig = newConfig;
        log.info("rpc init,config = {}",newConfig.toString());

        //注册中心初始化
        RegistryConfig registryConfig = newConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getRegistry(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("register init,config = {}",registryConfig);
        Runtime.getRuntime().addShutdownHook(
                new Thread(registry::destroy)
        );
    }

    public static void init(){
        RpcConfig newConfig;
        try {
            newConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch (Exception e){
            newConfig = new RpcConfig();
        }
        init(newConfig);
    }

    public static RpcConfig getRpcConfig(){
        if(rpcConfig == null){
            synchronized (RpcApplication.class){
                if(rpcConfig == null){
                    init();
                }
            }
        }
        return rpcConfig;
    }

}
