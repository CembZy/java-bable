package com.company.atomic;


import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 原子操作类的使用，当高并发的情况下，对于基本数据类型或者引用数据类型的操作，避免多线程问题的处理方式一般有
 * 加锁，但是加锁会影响性能，所以这个时候可以考虑使用原子操作类。
 */
public class AtomicTest {

    static AtomicInteger atomicInteger = new AtomicInteger(1);
    static AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    static AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(
            new int[]{1, 2, 3}
    );

    //实现incrementAndGet原子操作
    public static int incrementAndGetTest() {
        for (; ; ) {
            int i = atomicInteger.get();
            boolean b = atomicInteger.compareAndSet(i, i + 1);
            if (b) {
                return i + 1;
            }
        }
    }

    //实现getAndIncrement原子操作
    public static int getAndIncrementTest() {
        for (; ; ) {
            int i = atomicInteger.get();
            boolean b = atomicInteger.compareAndSet(i, i + 1);
            if (b) {
                return i;
            }
        }
    }

    public static void main(String[] args) {

        //测试Integer的原子操作
        for (int i = 0; i < 3; i++) {
            new Thread() {
                @Override
                public void run() {
                    int i2 = atomicInteger.get();
//                    int andIncrement = atomicInteger.getAndIncrement();
//                    int i1 = atomicInteger.incrementAndGet();
                    int andIncrement = getAndIncrementTest();
                    int i1 = incrementAndGetTest();
                    System.out.println(i2);
                    System.out.println(andIncrement);
                    System.out.println(i1);
                    System.out.println("---------------");
                }
            }.start();
        }


        //测试Boolean的原子操作
        new Thread() {
            @Override
            public void run() {
                boolean b = atomicBoolean.get();
                //前面参数是跟原始值作比较，如果相等结果就是第二个参数的值，不相等就是false
                boolean b1 = atomicBoolean.compareAndSet(true, true);
                System.out.println(b);
                System.out.println(b1);
                System.out.println("---------------");
            }
        }.start();

        //测试数组的原子操作
        new Thread() {
            @Override
            public void run() {
                int b = atomicIntegerArray.get(0);
                //第一个参数是比较的下包，第二个参数是跟原始值作比较，如果相等结果就是true，
                // 不相等就是false
                boolean b1 = atomicIntegerArray.compareAndSet(0, 1, 3);
                System.out.println(b);
                System.out.println(b1);
                System.out.println("---------------");
            }
        }.start();

    }
}
