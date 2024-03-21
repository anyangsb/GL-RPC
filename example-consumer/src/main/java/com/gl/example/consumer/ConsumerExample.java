package com.gl.example.consumer;

import com.gl.example.common.model.User;
import com.gl.example.common.service.UserService;
import com.gl.rpc.bootstrap.ConsumerBootstrap;
import com.gl.rpc.proxy.ServiceProxyFactory;

import java.io.FileNotFoundException;


public class ConsumerExample {


    public static void main(String[] args) throws FileNotFoundException {
        ConsumerBootstrap.init();
        //获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("gl");
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println("请求调用成功，结果为 ： " + newUser.getName());
        } else {
            System.out.println("Null");
        }
    }
}
