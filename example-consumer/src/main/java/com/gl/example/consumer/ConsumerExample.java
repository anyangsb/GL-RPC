package com.gl.example.consumer;

import com.gl.example.common.model.User;
import com.gl.example.common.service.UserService;
import com.gl.rpc.RpcApplication;
import com.gl.rpc.config.RpcConfig;
import com.gl.rpc.proxy.ServiceProxyFactory;
import com.gl.rpc.serializer.Serializer;
import com.gl.rpc.utils.ConfigUtils;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class ConsumerExample {


    public static void main(String[] args) throws FileNotFoundException {
//        UserService userService = ServiceProxyFactory.getMockProxy(UserService.class);
//        User user = new User();
//        user.setName("gl");
//        User newUser = userService.getUser(user);
//        if(newUser !=null){
//            System.out.println(newUser.getName());
//        }else{
//            System.out.println("Null");
//        }
//
//        long number = userService.getNumber();
//        System.out.println(number);
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
    }
}
