package com.gl.example.provider;


import com.gl.example.common.model.User;
import com.gl.example.common.service.UserService;

public class UserServiceImpl implements UserService {
    public User getUser(User user) {
        System.out.println("用户名" + user.getName());
        return user;
    }
}
