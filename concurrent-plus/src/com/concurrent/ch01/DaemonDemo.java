package com.concurrent.ch01;


/**
 * Daemon守护线程测试
 */
public class DaemonDemo {

    public static class DaemontClass extends Thread {

        @Override
        public void run() {
            System.out.println("线程" + Thread.currentThread().getName() + "正在运行.....");
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("线程" + Thread.currentThread().getName() + "结束运行.....");
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        //测试守护线程，守护线程，随着主线程的结束而结束
        DaemontClass daemontClass = new DaemontClass();
        //设置为守护线程
        daemontClass.setDaemon(true);
        daemontClass.start();

        System.out.println("zhuxiancheng......");
    }
}
