package com.concurrent.ch04.template;

import java.util.Date;

/**
 * 模板类
 */
public abstract class Custom {

    public abstract void to();

    public abstract void from();

    public abstract void send();


    public void date() {
        System.out.println(new Date());
    }

    //框架方法，子类去自己实现各自的细节方法，框架流程由模板类控制
    public void sendMessage() {
        to();
        from();
        send();
        date();
    }

}
