package com.concurrent.ch02;


import com.concurrent.SleepTools;

/**
 * 测试Join方法
 */
public class JoinDemo {

    private static class JoinClass implements Runnable {
        private Thread thread;

        public JoinClass(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
            try {
                //将当前线程的执行权交给thread线程
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("线程：" + Thread.currentThread().getName() + "重新获取到执行权....");
        }
    }


    public static void main(String[] args) {
        Thread thread = Thread.currentThread();
        for (int i = 0; i < 10; i++) {
            Thread thread1 = new Thread(new JoinClass(thread), "thread-" + i);
            System.out.println("线程：" + thread1 + "插入队列....");
            thread1.start();
            thread = thread1;
        }
        SleepTools.second(1);
        System.out.println("线程：" + Thread.currentThread().getName() + "重新获取到执行权....");
    }

}
