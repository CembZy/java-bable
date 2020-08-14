package com.common.demo.model;

import java.io.Serializable;
import java.util.Date;


//dubbo是一个rpc框架，对象传输需要进行序列化和反序列化过程。
public class User implements Serializable {

    private Long id;

    private Date createtime;

    private String name;

    private String sex;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }
}