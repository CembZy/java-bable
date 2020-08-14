package com.concurrent.ch01;


import com.concurrent.SleepTools;

/**
 * Synchronized类锁和对象锁测试
 */
public class SynchronizedDemo {

    private static class SynchronizedClassO implements Runnable {

        @Override
        public void run() {
            System.out.println("线程" + Thread.currentThread().getName() + "正在运行.....");
            //使用类锁
            synchronClass();
            System.out.println("线程" + Thread.currentThread().getName() + "退出锁.....");
        }
    }

    private static class SynchronizedClassT implements Runnable {

        private SynchronizedDemo synchronizedDemo;

        public SynchronizedClassT(SynchronizedDemo synchronizedDemo) {
            this.synchronizedDemo = synchronizedDemo;
        }

        @Override
        public void run() {
            System.out.println("线程" + Thread.currentThread().getName() + "正在运行.....");
            //使用对象锁
            synchronizedDemo.synchronObj();
            System.out.println("线程" + Thread.currentThread().getName() + "退出锁.....");
        }
    }

    //使用对象锁，锁的是对象实例
    private synchronized void synchronObj() {
        System.out.println("1,线程" + Thread.currentThread().getName() + "被对象锁锁住.....");
        SleepTools.second(3);
        System.out.println("2,线程" + Thread.currentThread().getName() + "被对象锁锁住.....");
    }

    //使用类锁，要用static修饰，锁的是Class对象，Class对象在内存中每个对象只有一个
    private static synchronized void synchronClass() {
        System.out.println("1,线程" + Thread.currentThread().getName() + "被类锁锁住.....");
        SleepTools.second(3);
        System.out.println("2,线程" + Thread.currentThread().getName() + "被类锁锁住.....");
    }


    public static void main(String[] args) {
        //测试对象锁，结果：对象锁锁的是对象实例，多个线程访问同一对象锁，才能同步
        SynchronizedDemo synchronizedDemo = new SynchronizedDemo();
        SynchronizedDemo synchronizedDemo2 = new SynchronizedDemo();
        SynchronizedClassT synchronizedClassT1 = new SynchronizedClassT(synchronizedDemo);
        SynchronizedClassT synchronizedClassT2 = new SynchronizedClassT(synchronizedDemo2);
        //锁同一对象才能同步
        //SynchronizedClassT synchronizedClassT2 = new SynchronizedClassT(synchronizedDemo);
        new Thread(synchronizedClassT1).start();
        new Thread(synchronizedClassT2).start();
        SleepTools.second(1);


        //测试类锁，结果：类锁锁的是Class对象，每个线程都是访问的同一类锁，所以是同步的
        SynchronizedClassO synchronizedClass1 = new SynchronizedClassO();
        SynchronizedClassO synchronizedClass2 = new SynchronizedClassO();
        new Thread(synchronizedClass1).start();
        new Thread(synchronizedClass2).start();
        SleepTools.second(1);

    }

}
