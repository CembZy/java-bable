package com.company.tools;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 测试CyclicBarrier，CyclicBarrier是等待其他线程都运行到某处之后，所有线程才能一直向下执行
 */
public class CyclicBarrierTest {

    static CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new TestThread());

    //其他线程都跑完了之后，TestThread线程可以跑
    public static class TestThread extends Thread {

        @Override
        public void run() {
            System.out.println("test run....");
        }
    }

    public static class CyclicBarrierTestThread extends Thread {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "   wait....");
            try {
                //如果是第四个线程就让他休眠一会
                if (Thread.currentThread().getId() == 4) {
                    Thread.sleep(1000 + Thread.currentThread().getId());
                }
                //等待其他线程都运行完了，才继续执行
                cyclicBarrier.await();
                Thread.sleep(1000 + Thread.currentThread().getId());
                System.out.println(Thread.currentThread().getName() + "   run....");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {

        System.out.println("main....");
        for (int i = 0; i < 5; i++) {
            new CyclicBarrierTestThread().start();
        }
        System.out.println("main run...");
    }

}
