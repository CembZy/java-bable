package com.common.demo.service;


import com.common.demo.model.User;

//对外暴露的接口
public interface UserService {

    User findById(Long id);

}
