package com.cemb.springboot.springbootredis.model;

import java.io.Serializable;

/**
 * @ClassName: User
 * @Auther: CemB
 * @Description: 测试实体
 * @Email: 729943791@qq.com
 * @Date: 2018/7/11 17:42
 */
public class User implements Serializable {
    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
