package com.product.demo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.common.demo.model.User;
import com.common.demo.service.UserService;
import com.product.demo.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;


//需要使用dubbo的service注册注解
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findById(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }
}
