package com.company.thread;

/**
 * synchronized锁机制
 */
public class Synch {

    public static class SynchThread extends Thread {

        @Override
        public void run() {
            test();
        }

        //共享同一个对象锁，this
        public synchronized void test() {
            System.out.println("test1.....");
        }

        //共享同一个对象锁，this
        public void test2() {
            synchronized (this) {
                System.out.println("test2.....");
            }
        }

        //共享同一个类锁，class对象
        public void test3() {
            synchronized (Synch.class) {
                System.out.println("test3.....");
            }
        }
    }

    public static void main(String[] args) {
        SynchThread synchThread = new SynchThread();
        SynchThread synchThread2 = new SynchThread();
        synchThread.start();
        synchThread2.start();
    }

}
