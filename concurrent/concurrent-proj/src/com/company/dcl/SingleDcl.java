package com.company.dcl;

/**
 * 懒汉式-双重检查-其实也会产生安全问题，
 */
public class SingleDcl {
    private volatile static SingleDcl singleDcl;

    //在实例初始化完之后（初始化分为类的初始化和实例的初始化），这个变量可能还没初始化成功，所以可能会产生空指针
    //private User user;
    private SingleDcl() {
    }

    public static SingleDcl getInstance() {
        if (singleDcl == null) {
            synchronized (SingleDcl.class) {//类锁
                if (singleDcl == null) {
                    singleDcl = new SingleDcl();
                }
            }
        }
        return singleDcl;
    }
}
