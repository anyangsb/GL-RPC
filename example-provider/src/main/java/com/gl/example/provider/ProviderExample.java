package com.gl.example.provider;



import com.gl.example.common.service.UserService;
import com.gl.rpc.RpcApplication;
import com.gl.rpc.bootstrap.ProviderBootstrap;
import com.gl.rpc.config.RpcConfig;
import com.gl.rpc.model.ServiceMetaInfo;
import com.gl.rpc.model.ServiceRegisterInfo;
import com.gl.rpc.registry.LocalRegister;
import com.gl.rpc.registry.RegistryFactory;
import com.gl.rpc.registry.Registry;
import com.gl.rpc.server.VertxHttpServer;
import com.gl.rpc.server.tcp.VertxTcpServer;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务提供者示例
 */
public class ProviderExample {

    public static void main(String[] args) {
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();

        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo(UserService.class.getName(), UserServiceImpl.class);

        serviceRegisterInfoList.add(serviceRegisterInfo);

        ProviderBootstrap.init(serviceRegisterInfoList);
//        server.doStart(8084);
    }
}
