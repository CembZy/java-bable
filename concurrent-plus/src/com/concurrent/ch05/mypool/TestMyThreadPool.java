package com.concurrent.ch05.mypool;

import java.util.Random;

/**
 * 类说明：测试自定义线程池
 */
public class TestMyThreadPool {
    public static void main(String[] args) throws InterruptedException {
        // 创建3个线程的线程池
        MyPool t = new MyPool(3, 0);

        //执行5个任务
        t.execute(new MyTask("testA"));
        t.execute(new MyTask("testB"));
        t.execute(new MyTask("testC"));
        t.execute(new MyTask("testD"));
        t.execute(new MyTask("testE"));
        Thread.sleep(10000);
        t.destroy();// 所有线程都执行完成才destroy
    }

    // 任务类
    static class MyTask implements Runnable {

        private String name;
        private Random r = new Random();

        public MyTask(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public void run() {// 执行任务
            try {
                Thread.sleep(r.nextInt(1000) + 2000);
            } catch (InterruptedException e) {
                System.out.println("任务 " + name + " 被中断.......");
            }
            System.out.println("任务 " + name + " 完成");
        }
    }
}
