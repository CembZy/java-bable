package com.concurrent.ch03.atomic;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * AtomicInteger演示
 */
public class AtomicIntegerDemo {

    static AtomicInteger atomicInteger = new AtomicInteger(1);

    public static void main(String[] args) throws InterruptedException {
//        test();
        Synch synch = new Synch();
        synch.start();
        synch.join();
        atomic();

//        System.out.println(add());
    }

    //使用加锁累加
    static class Synch extends Thread {
        @Override
        public void run() {
            //使用原子操作类统计
            long startTime = System.currentTimeMillis();
            int count = 1;
//            synchronized (this) {
//                for (int i = 0; i < 1000000000; i++) {
//                    count++;
//                }
//            }
            for (int i = 0; i < 1000000000; i++) {
                synchronized (this) {
                    count++;
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println("使用锁累加花费的时间：" + (endTime - startTime) + "......count = " + count);
        }
    }


    //使用原子操作类统计
    private static void atomic() {
        long startTime = System.currentTimeMillis();
        AtomicInteger atomicInteger = new AtomicInteger(1);
        for (int i = 0; i < 1000000000; i++) {
            atomicInteger.incrementAndGet();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("原子操作类累加花费的时间：" + (endTime - startTime) + "......count = " + atomicInteger.get());
    }

    //测试基本用法
    private static void test() {
        AtomicInteger num = new AtomicInteger(1);

        //++i
        System.out.println(num.incrementAndGet());

        //i++
        System.out.println(num.getAndIncrement());

        //CAS
        System.out.println(num.compareAndSet(3, 4));
        System.out.println(num.get());
    }

    //实现自定义的原子递增方法
    private static int add() {
        for (; ; ) {
            int i = atomicInteger.get();
            boolean b = atomicInteger.compareAndSet(i, i + 1);
            if (b) {
                return atomicInteger.get();
            }
        }
    }
}
