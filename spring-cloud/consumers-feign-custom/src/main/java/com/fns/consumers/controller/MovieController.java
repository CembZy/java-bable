package com.fns.consumers.controller;


import com.fns.consumers.entity.User;
import com.fns.consumers.fegin.ConsumersFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {

    @Autowired
    private ConsumersFeign consumersFeign;


    @GetMapping("get/{id}")
    public User get(@PathVariable Long id){
        return consumersFeign.get(id);
    }
}
