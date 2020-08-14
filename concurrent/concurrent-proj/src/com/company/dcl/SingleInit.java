package com.company.dcl;

/**
 * 懒汉式-类初始化模式
 */
public class SingleInit {
    private SingleInit(){}

    //定义一个私有类，来持有当前类的实例
    private static class InstanceHolder{
        public static SingleInit instance = new SingleInit();
    }

    public static SingleInit getInstance(){
        return InstanceHolder.instance;
    }

}
