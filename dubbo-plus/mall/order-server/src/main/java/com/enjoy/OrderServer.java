package com.enjoy;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Hello world!
 */
public class OrderServer {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:dubbo.xml");

        ctx.start();


        System.out.println("---------dubbo启动成功--------");


        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
