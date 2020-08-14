package com.company.tools;

import java.util.concurrent.CountDownLatch;

/**
 * 测试CountDownLatch，CountDownLatch是等其他线程都运行完了之后才能继续运行
 */
public class CountDownLatchTest {

    static CountDownLatch countDownLatch = new CountDownLatch(5);

    public static class CountDownLatchTestThread extends Thread {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "   wait....");
            //减值
            countDownLatch.countDown();
            System.out.println(Thread.currentThread().getName() + "   run....");
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("main....");
        for (int i = 0; i < 5; i++) {
            new CountDownLatchTestThread().start();
        }

        //主线程等待其他线程countDownLatch.countDown() 完了之后才能运行
        countDownLatch.await();
        System.out.println("main run...");
    }

}
