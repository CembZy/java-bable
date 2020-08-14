package com.company.dcl;

/**
 * 饿汉式-不会出现安全问题
 */
public class SingleEHan {
    public static SingleEHan singleEHan = new SingleEHan();

    private SingleEHan() {
    }

    public static SingleEHan instance() {
        return singleEHan;
    }
}
