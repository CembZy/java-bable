package com.concurrent.ch06.instance;


/**
 * 懒汉式--双重加锁
 */
public class Lazy {

    private static volatile Lazy lazy;

    private Lazy() {
    }

    public static Lazy instance() {
        if (lazy == null) {
            synchronized (Lazy.class) {
                if (lazy == null) {
                    lazy = new Lazy();
                }
            }
        }
        return lazy;
    }

}
