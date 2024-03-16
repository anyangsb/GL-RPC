package com.gl.example.provider;



import com.gl.example.common.service.UserService;
import com.gl.rpc.RpcApplication;
import com.gl.rpc.config.RpcConfig;
import com.gl.rpc.model.ServiceMetaInfo;
import com.gl.rpc.registry.LocalRegister;
import com.gl.rpc.registry.RegistryFactory;
import com.gl.rpc.registry.Registry;
import com.gl.rpc.server.VertxHttpServer;

/**
 * 服务提供者示例
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @learn <a href="https://codefather.cn">编程宝典</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public class ProviderExample {

    public static void main(String[] args) {
        RpcApplication.init();

        LocalRegister.register(UserService.class.getName(),UserServiceImpl.class);

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getRegistry(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(UserService.class.getName());
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());

        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        VertxHttpServer server = new VertxHttpServer();
        server.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
