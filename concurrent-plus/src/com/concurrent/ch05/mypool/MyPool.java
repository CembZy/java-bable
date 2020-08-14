package com.concurrent.ch05.mypool;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 自定义线程池
 */
public class MyPool {

    //默认的线程池大小
    private static final int DEFAULT_SIZE = 5;

    //默认处理的任务数量
    private static final int DEFAULT_TASKS = 100;

    //用户传入的线程池大小
    private int size;

    //存放任务线程的阻塞队列
    private BlockingQueue<Runnable> blockingQueue;

    //线程池
    private WorkThread[] workThreads;

    public MyPool() {
        this(DEFAULT_SIZE, DEFAULT_TASKS);
    }

    public MyPool(int size, int tasks) {
        if (size <= 0) size = DEFAULT_SIZE;
        if (tasks <= 0) tasks = DEFAULT_TASKS;
        this.size = size;
        //初始化任务线程阻塞队列
        blockingQueue = new ArrayBlockingQueue<>(tasks);
        workThreads = new WorkThread[size];
        for (int i = 0; i < size; i++) {
            workThreads[i] = new WorkThread();
            workThreads[i].start();
        }
    }

    //执行任务
    public void execute(Runnable runnable) {
        try {
            //放入队列即可
            blockingQueue.put(runnable);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //销毁线程池,该方法要保证在所有任务都完成的情况下才销毁所有线程，否则等待任务完成才销毁
    public void destroy() {
        for (int i = 0; i < size; i++) {
            workThreads[i].stopThread();
            workThreads[i] = null; // help gc，防止内存泄漏
        }
        //清空任务队列
        blockingQueue.clear();
    }


    //执行任务的线程
    private class WorkThread extends Thread {

        @Override
        public void run() {
            Runnable runnable = null;
            try {
                while (!isInterrupted()) {
                    //从阻塞队列中拿任务执行
                    runnable = blockingQueue.take();
                    if (runnable != null) {
                        System.out.println("当前线程：" + getName() + ".....获取到任务并开始执行");
                        runnable.run();
                    }
                    runnable = null;// help gc，防止内存泄漏
                }
            } catch (Exception e) {
                System.out.println("当前线程：" + getName() + "获取任务失败.....");
            }
        }

        //提供一个中断次线程的方法
        private void stopThread() {
            interrupt();
        }
    }

}
