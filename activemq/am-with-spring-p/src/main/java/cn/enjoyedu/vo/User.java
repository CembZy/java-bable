package cn.enjoyedu.vo;

import java.io.Serializable;

/**
 * 类说明：实体
 */
public class User implements Serializable {

    private int id;
    private String name;
    private String age;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
