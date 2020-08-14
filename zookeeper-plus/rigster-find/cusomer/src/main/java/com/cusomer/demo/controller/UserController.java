package com.cusomer.demo.controller;


import com.cusomer.demo.model.User;
import com.cusomer.demo.pull.PullServices;
import com.cusomer.demo.util.LoadBalance;
import com.cusomer.demo.util.RamdomLoadBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class UserController {

    @Autowired
    private RestTemplate restTemplate;
    private static final ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) PullServices.services;

    @GetMapping("test")
    public String getUser() {
        List<String> services = new LinkedList<>();
        concurrentHashMap.forEach((key, value) -> {
            services.add(((String) value));
        });
        LoadBalance balance = new RamdomLoadBalance(services);
        String host = balance.choseServiceHost();
        User user = restTemplate.getForObject("http://" + host, User.class);
        return host + ".........." + user;
    }

}
