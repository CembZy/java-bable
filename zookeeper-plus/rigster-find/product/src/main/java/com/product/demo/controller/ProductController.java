package com.product.demo.controller;


import com.product.demo.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @GetMapping("product")
    public User product() {
        return new User("model", 18);
    }
}
