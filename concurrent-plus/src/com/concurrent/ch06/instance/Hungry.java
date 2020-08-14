package com.concurrent.ch06.instance;


/**
 * 饿汉式
 */
public class Hungry {

    private static Hungry hungry = new Hungry();

    private Hungry() {
    }

    public static Hungry instance() {
        return hungry;
    }
}
