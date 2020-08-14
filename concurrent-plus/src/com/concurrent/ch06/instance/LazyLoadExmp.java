package com.concurrent.ch06.instance;

public class LazyLoadExmp {

    private String name;

    //这是一个非常大的数组，不经常使用
    private Integer[] integers;


    //这种就可以采用延迟加载模式
    private static class LazyHolder {
        private final static Integer[] integers = new Integer[100000000];
    }

    public Integer[] getIntegers() {
        return LazyHolder.integers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
