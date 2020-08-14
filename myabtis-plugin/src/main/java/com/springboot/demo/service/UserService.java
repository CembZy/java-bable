package com.springboot.demo.service;

import com.springboot.demo.mapper.UserMapper;
import com.springboot.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: UserService
 * @Auther: CemB
 * @Description:
 * @Email: 729943791@qq.com
 * @Company: 东方瑞云
 * @Date: 2018/12/23 15:28
 */

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User getUser() {
        User user = userMapper.selectByPrimaryKey(Long.parseLong("1"));
        return user;
    }
}
