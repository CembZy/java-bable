package com.fns.consumers.controller;


import com.fns.consumers.entity.User;
import com.fns.consumers.fegin.ConsumersFeign;
import com.fns.consumers.fegin.ConsumersFeign2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {

    @Autowired
    private ConsumersFeign consumersFeign;

    @Autowired
    private ConsumersFeign2 consumersFeign2;
    @GetMapping("get/{id}")
    public User get(@PathVariable Long id){
        return consumersFeign.get(id);
    }

    @GetMapping("get2/{id}")
    public User gets(@PathVariable Long id){
        return consumersFeign.get2(id);
    }

    @GetMapping("get3/{id}")
    public User get1s(@PathVariable Long id){
        return consumersFeign2.get2(id);
    }
}
