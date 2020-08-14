package com.fns.producters.entity;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity//实体类
public class User{


    @Id//主建
    @GeneratedValue(strategy = GenerationType.AUTO)//自动递增
    private Long id;

    @Column
    private String username;

    @Column
    private String name;

    @Column
    private short age;

    @Column
    private BigDecimal balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getAge() {
        return age;
    }

    public void setAge(short age) {
        this.age = age;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
