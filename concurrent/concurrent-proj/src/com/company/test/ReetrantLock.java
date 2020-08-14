package com.company.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 启动4到5个线程去争夺ReentrantLock，然后在AQS中设置断点，结合线程对锁的获取和释放，
 * 观察AQS中的head节点和同步队列的变化情况。然后手绘图形或用绘图软件，画出变化情况图，
 * 图中应该标识出每个线程在获取和释放锁后head节点和同步队列的变化
 */
public class ReetrantLock {
    /**
     * 线程数
     */
    private static final int THREAD_COUNT = 10;
    /**
     * 重入锁
     */
    private static final Lock LOCK = new ReentrantLock();
    /**
     * 发令枪，控制主线程等待其他线程运行完最后输出
     */
    private static final CountDownLatch LATCH = new CountDownLatch(THREAD_COUNT);
    /**
     * 线程共享变量
     */
    private static volatile int count = 0;

    public static void main(String[] args) throws InterruptedException {
        //启动所有线程
        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(new ReentrantLockThread()).start();
        }
        LATCH.await();
        //最后输出结果
        System.out.println("共享变量最后值：" + count);

    }

    private static class ReentrantLockThread implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println("线程 " + Thread.currentThread().getName() + " 开始获取锁!");
                //加锁
                LOCK.lock();//断点跟进去
                System.out.println("------------------------------------");
                System.out.println("线程 " + Thread.currentThread().getName() + "执行增加操作!");

                count = count + 1;
                System.out.println("线程 " + Thread.currentThread().getName() + "增加后的结果是：" + count);

                System.out.println("------------------------------------");
            } finally {
                LOCK.unlock();//断点跟进去
                LATCH.countDown();
            }
        }
    }
}
