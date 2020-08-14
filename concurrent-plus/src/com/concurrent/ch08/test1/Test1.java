package com.concurrent.ch08.test1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Test1 {

    //没的返回值
    public static class Thread1 extends Thread {
        @Override
        public void run() {
            System.out.println("线程...." + Thread.currentThread().getName());
        }
    }

    //没有返回值
    public static class Thread2 implements Runnable {

        @Override
        public void run() {
            System.out.println("线程...." + Thread.currentThread().getName());
        }
    }

    //有返回值
    public static class Thread3 implements Callable<String> {

        @Override
        public String call() throws Exception {
            System.out.println("线程...." + Thread.currentThread().getName());
            Thread.sleep(1000);
            return "成功";
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //Thread
        Thread1 thread1 = new Thread1();
        thread1.start();

        //Runnable
        Thread2 thread2 = new Thread2();
        Thread thread = new Thread(thread2);
        thread.start();

        //Callable
        Thread3 thread3 = new Thread3();
        //转换 Callable他通过FutureTask转Runnable
        FutureTask<String> future = new FutureTask<>(thread3);
        Thread thread4 = new Thread(future);
        thread4.start();

        String result = future.get();
        System.out.println(result);

        System.out.println("线程...." + Thread.currentThread().getName());
        Thread.sleep(1000);
    }
}
