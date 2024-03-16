package com.gl.example.common.service;

import com.gl.example.common.model.User;

public interface UserService {

    User getUser(User user);

    /**
     * 默认测试方法
     */
    default short getNumber(){
        return 1;
    }
}
