package com.fns.consumers.controller;


import com.fns.consumers.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MovieController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("get/{id}")
    public User get(@PathVariable Long id){
        return restTemplate.getForObject("http://usermanager/get/"+id,User.class);
    }
}
