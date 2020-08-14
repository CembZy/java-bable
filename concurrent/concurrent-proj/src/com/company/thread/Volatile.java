package com.company.thread;

/**
 * volatile，最轻量的同步机制，每一个线程读取被volatile修饰的变量的时候，都会从主内存中读取，保证了变量的真实性
 * 适用于 一个线程写，多个线程读 的情况
 * 但是不能保证原子性 如:a++这种操作
 */
public class Volatile {

    public static class VolatileThread extends Thread {

        private volatile int a = 0;

        @Override
        public void run() {
            a++;
            System.out.println(a);

            a++;
            System.out.println(a);
        }
    }

    public static void main(String[] args) {
        VolatileThread synchThread = new VolatileThread();
        synchThread.start();
    }
}
