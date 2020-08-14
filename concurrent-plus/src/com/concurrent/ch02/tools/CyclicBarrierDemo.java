package com.concurrent.ch02.tools;


import com.concurrent.SleepTools;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier栅栏演示，由一组线程本身控制放行
 */
public class CyclicBarrierDemo {

    //定义一个栅栏，在栅栏放行的时候，同时可以运行CollectThread这个线程
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new CollectThread());

    private static ConcurrentHashMap<Long, String> concurrentHashMap = new ConcurrentHashMap();

    private static class CollectThread extends Thread {

        @Override
        public void run() {
            System.out.println(concurrentHashMap.size());
        }
    }

    private static class SubThread extends Thread {
        @Override
        public void run() {
            try {
                Random random = new Random();
                long id = Thread.currentThread().getId();
                String name = Thread.currentThread().getName();
                concurrentHashMap.put(id, name);
                if (random.nextBoolean()) {
                    System.out.println("线程" + name + "正在处理中....");
                    SleepTools.second(2);
                }
                System.out.println("线程" + name + "处理结束，等待其他线程处理结束....");

                //每个子线程都会在这里阻塞，等待所有的子线程都执行到这里，才会一起放行
                cyclicBarrier.await();
                System.out.println("线程" + name + "处理完毕....");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new SubThread().start();
        }

        //CyclicBarrier，不会阻塞主线程
        System.out.println("主线程.....");
    }
}
