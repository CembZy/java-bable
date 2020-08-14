package com.fns.producters.controller;


import com.fns.producters.dao.UserRepository;
import com.fns.producters.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("get/{id}")
    public User get(@PathVariable Long id){System.out.println("1");
        return this.userRepository.findById(id).orElse(null);
    }

}
