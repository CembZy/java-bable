package com.concurrent.ch01;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


/**
 * 测试Callable
 */
public class CallableDemo {


    public static class CallableClass implements Callable<String> {

        @Override
        public String call() throws Exception {
            System.out.println("callable run .... ");
            return "callable";
        }
    }

    public static void main(String[] args) {
        CallableClass callable = new CallableClass();
        FutureTask<String> futureTask = new FutureTask<>(callable);
        try {
            new Thread(futureTask).start();

            //阻塞的方法，看源码可以看出，FutureTask的get方法内部加了一个自旋锁，
            //不断自旋判断Callable线程是否执行完，只有执行完了才会结束自旋
            String s = futureTask.get();

            System.out.println(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
