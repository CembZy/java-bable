package com.company.thread;

//继承Thread
//线程是一个协同式的
public class ExThread extends Thread {


    /**
     * interrupt是发送线程中断请求，线程不会马上中断
     * isInterrupted用于判断当前线程是否接受到主线程发来的中断请求
     * interrupted表示当前线程已经中断了
     */
    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("thread name is " + threadName);
        }
        System.out.println("thread already interrupt");
    }
}
