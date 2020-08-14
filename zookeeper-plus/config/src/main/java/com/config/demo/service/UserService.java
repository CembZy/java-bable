package com.config.demo.service;


public interface UserService {
     String sayHello();

     boolean register(String username, String passwd);

     boolean login(String username, String passwd);


}
