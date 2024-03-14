package com.gl.example.consumer;

import com.gl.example.common.model.User;
import com.gl.example.common.service.UserService;
import com.gl.rpc.proxy.ServiceProxyFactory;

/**
 * 消费者实例
 */
public class EasyConsumerExample {

    public static void main(String[] args) {
        //todo 需获取该对象实例
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("gl");
        User newUser = userService.getUser(user);
        if(newUser !=null){
            System.out.println(newUser.getName());
        }else{
            System.out.println("Null");
        }
    }
}
