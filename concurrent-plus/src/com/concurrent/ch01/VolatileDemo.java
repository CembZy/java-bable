package com.concurrent.ch01;


import com.concurrent.SleepTools;

/**
 * Volatile保证可见性，不保证原子性测试
 */
public class VolatileDemo {


    public static class VolatileClass extends Thread {

        private  volatile int i = 0;

        @Override
        public void run() {
            SleepTools.ms(5);
            i = i + 1;
            System.out.println("线程" + Thread.currentThread().getName() + "正在运行.....i=" + i);
        }
    }


    public static void main(String[] args) {
        VolatileClass volatileClass =new VolatileClass();
        new Thread(volatileClass).start();
        new Thread(volatileClass).start();
        new Thread(volatileClass).start();
        new Thread(volatileClass).start();
        SleepTools.ms(20);
    }

}
