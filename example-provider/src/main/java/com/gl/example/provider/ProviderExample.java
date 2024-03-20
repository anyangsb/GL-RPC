package com.gl.example.provider;



import com.gl.example.common.service.UserService;
import com.gl.rpc.bootstrap.ProviderBootstrap;
import com.gl.rpc.model.ServiceRegisterInfo;

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
