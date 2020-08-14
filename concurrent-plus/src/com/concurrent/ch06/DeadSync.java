package com.concurrent.ch06;

import com.concurrent.SleepTools;

/**
 * 死锁
 */
public class DeadSync {

    //锁一
    static Object object1 = new Object();
    //锁二
    static Object object2 = new Object();


    //    先拿第一个锁，再拿第二个锁
    public static void test1() {
        synchronized (object1) {
            System.out.println("线程:" + Thread.currentThread().getName() + "拿到锁一........");
            SleepTools.ms(10);
            synchronized (object2) {
                System.out.println("线程:" + Thread.currentThread().getName() + "拿到锁二........");
            }
        }
    }

    //    先拿第二个锁，再拿第一个锁
    public static void test2() {
        synchronized (object2) {
            System.out.println("线程:" + Thread.currentThread().getName() + "拿到锁二........");
            SleepTools.ms(10);
            synchronized (object1) {
                System.out.println("线程:" + Thread.currentThread().getName() + "拿到锁一........");
            }
        }

    }


    static class SyncO extends Thread {
        @Override
        public void run() {
            test1();
        }
    }

    static class SyncT extends Thread {
        @Override
        public void run() {
            test2();
        }
    }


    public static void main(String[] args) {
        new SyncO().start();
        new SyncT().start();
    }

}
