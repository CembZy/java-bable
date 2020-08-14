package com.cusomer.demo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.common.demo.model.User;
import com.common.demo.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("user")
public class UserController {

    //dubbo注入注解
    @Reference
    private UserService userService;

    @GetMapping("userInfoById")
    public String userInfoById(Long id) {
        User user = userService.findById(id);
        return user != null ? user.getName() : "获取id失败";
    }

}
