package com.concurrent.ch01;

/**
 * Interrupt测试
 */
public class InterruptDemo {


    public static class InterrputClassO extends Thread {

        @Override
        public void run() {
            //未中断
            while (!isInterrupted()) {
                System.out.println("线程" + Thread.currentThread().getName() + "正在运行.....");
            }

            //中断了
            System.out.println("线程" + Thread.currentThread().getName() + "已经中断.....");
        }
    }

    public static class InterrputClassT implements Runnable {

        @Override
        public void run() {
            //未中断，这里中断的方法需要用Thread去调用Thread.currentThread().interrupted()
            //因为Runnable接口中没有这个方法
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("线程" + Thread.currentThread().getName() + "正在运行.....");
            }
            //中断了
            System.out.println("线程" + Thread.currentThread().getName() + "已经中断.....");
        }
    }

    public static class InterrputClassF implements Runnable {

        @Override
        public void run() {
            //未中断
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("线程" + Thread.currentThread().getName() + "正在运行.....");
                try {
                    Thread.currentThread().sleep(100);

                    System.out.println("1，中断标志位：" + Thread.currentThread().isInterrupted());

                    //当抛出InterruptedException的时候，会自动将中断标志位重置为false，导致无法中断
                } catch (InterruptedException e) {
                    System.out.println("2，中断标志位：" + Thread.currentThread().isInterrupted());

                    //所以当要使用中断的时候,如果发生了这个异常，需要手动的在catch异常里面再次开启中断
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }

            //中断了
            System.out.println("3，中断标志位：" + Thread.currentThread().isInterrupted());
        }
    }

    public static class InterrputClassS implements Runnable {

        //使用自定义变量加上volatile貌似也可以，但是因为不能保证原子性，所以可能会发生线程安全问题，所以也不行
        public volatile boolean falg = false;

        @Override
        public void run() {
            //未中断
            while (!falg) {
                try {
                    System.out.println("线程" + Thread.currentThread().getName() + "正在运行....." + falg);
                    Thread.currentThread().sleep(10000);
                    System.out.println("线程" + Thread.currentThread().getName() + "正在运行....." + falg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //中断了
            System.out.println("线程" + Thread.currentThread().getName() + "已经中断....." + falg);
        }
    }


    public static void main(String[] args) throws InterruptedException {
        //测试Thread方式的中断
        InterrputClassO interrputClassO = new InterrputClassO();
        interrputClassO.start();
        Thread.currentThread().sleep(500);
        interrputClassO.interrupt();


        //测试Runnable方式的中断
        InterrputClassT interrputClassT = new InterrputClassT();
        Thread thread = new Thread(interrputClassT);
        thread.start();
        Thread.currentThread().sleep(500);
        thread.interrupt();


        //测试抛出InterruptedException的时候的情况
        InterrputClassF interrputClassF = new InterrputClassF();
        Thread thread2 = new Thread(interrputClassF);
        thread2.start();
        thread2.currentThread().sleep(500);
        thread2.interrupt();


        //测试自定义中断标志位
        InterrputClassS interrputClassS = new InterrputClassS();
        Thread thread1 = new Thread(interrputClassS);
        Thread thread3 = new Thread(interrputClassS);
        thread1.start();
        thread3.start();
        Thread.currentThread().sleep(5);
        interrputClassS.falg = true;
    }


}
