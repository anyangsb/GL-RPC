package com.gl.rpc.bootstrap;

import com.gl.rpc.RpcApplication;
import com.gl.rpc.config.RpcConfig;
import com.gl.rpc.model.ServiceMetaInfo;
import com.gl.rpc.model.ServiceRegisterInfo;
import com.gl.rpc.registry.LocalRegistry;
import com.gl.rpc.registry.Registry;
import com.gl.rpc.registry.RegistryFactory;
import com.gl.rpc.server.tcp.VertxTcpServer;

import java.util.List;

public class ProviderBootstrap {

    /**
     * 初始化
     * @param serviceRegisterInfoList
     */
    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList ){
        RpcApplication.init();
        //全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        //注册服务
        for(ServiceRegisterInfo<?> serviceRegisterInfo: serviceRegisterInfoList){
            String serviceName = serviceRegisterInfo.getServiceName();
            Class<?> implClass = serviceRegisterInfo.getImplClass();
            //本地注册
            LocalRegistry.register(serviceName , implClass);
            //注册到注册中心
            Registry registry = RegistryFactory.getRegistry(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + "服务注册失败");
            }
        }
        //启动服务器
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(rpcConfig.getServerPort());
    }

}
