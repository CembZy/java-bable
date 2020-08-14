package com.concurrent.ch04.template;


/**
 * 模板类的实现类
 */
public class SendSms extends Custom {
    @Override
    public void to() {
        System.out.println("to");
    }

    @Override
    public void from() {
        System.out.println("from");
    }

    @Override
    public void send() {
        System.out.println("send");
    }


    public static void main(String[] args) {
        Custom custom = new SendSms();

        //调用模板的框架方法
        custom.sendMessage();
    }
}
