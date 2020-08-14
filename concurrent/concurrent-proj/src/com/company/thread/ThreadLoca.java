package com.company.thread;

public class ThreadLoca {

    /**
     * ThreadLocal可以保证每个线程单独使用这个值，互相之间不影响，底层是一个Map
     */
    static ThreadLocal<Integer> num = new ThreadLocal<Integer>() {

        //初始化这个值
        @Override
        protected Integer initialValue() {
            return 1;
        }
    };

    public static class Local extends Thread {

        @Override
        public void run() {
        }
    }

    public static void main(String[] args) {
        Local synchThread = new Local();
        synchThread.start();
    }
}
