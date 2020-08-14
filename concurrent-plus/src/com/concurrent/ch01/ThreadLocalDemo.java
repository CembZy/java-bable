package com.concurrent.ch01;


import com.concurrent.SleepTools;

/**
 * ThreadLocal保证线程安全测试
 */
public class ThreadLocalDemo {


    //看底层源码，其实ThreadLocal底层就封装了一个自定义的ThreadLocalMap的Map对象，每个线程作为key，
    //定义的num变量作为value，每个线程都独享一个副本，所以就不存在线程安全问题了。
    static ThreadLocal<Integer> num = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 1;
        }
    };

    public static class ThreadLocalClass extends Thread {

        private int i;

        public ThreadLocalClass(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            SleepTools.ms(5);
            i = i + num.get();
            System.out.println("线程" + Thread.currentThread().getName() + "正在运行.....i =" + i);
            num.set(i);
            System.out.println("线程" + Thread.currentThread().getName() + "正在运行.....num =" + num.get());
        }
    }


    public static void main(String[] args) {

        for (int i = 0; i < 5; i++) {
            ThreadLocalClass threadLocalClass = new ThreadLocalClass(i);
            threadLocalClass.start();
        }

    }

}
