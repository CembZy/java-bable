package com.fns.producters.controller;


import com.fns.producters.dao.UserRepository;
import com.fns.producters.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController2 {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("get2/{id}")
    public User get2(@PathVariable Long id){
        System.out.println("2");return this.userRepository.findById(id).orElse(null);
    }
}
