package com.concurrent.ch02.wn;


/**
 * 快递实体类
 */
public class Express {

    public static final String CITY = "shanghai";

    private int km;

    private String site;


    public Express(int km, String site) {
        this.km = km;
        this.site = site;
    }

    //改变公里数
    public synchronized void changeKm() {
        km = 101;
        notify();
    }

    //改变地址
    public synchronized void changeSite() {
        site = "beijing";

        //notifyAll会唤醒所有等待中的线程
        //notifyAll();

        //会唤醒等待队列中的第一个线程，一旦这个线程不是需要被唤醒的线程，因为唤醒信号只有一个，所以其他线程也就不能被唤醒了，
        //这种情况就发生了死锁，所以建议使用notifyAll();
        notify();
    }


    public synchronized void km() {
        while (km <= 100) {
            try {
                //等待
                System.out.println("km-线程" + Thread.currentThread().getName() + "正在等待.....");
                wait();
                //被唤醒后
                System.out.println("km-线程" + Thread.currentThread().getName() + "已被唤醒.....");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //被唤醒后
        System.out.println("km-线程" + Thread.currentThread().getName() + "被唤醒后.....");
    }

    public synchronized void site() {
        while (CITY.equals(site)) {
            try {
                //等待
                System.out.println("site-线程" + Thread.currentThread().getName() + "正在等待.....");
                wait();
                System.out.println("site-线程" + Thread.currentThread().getName() + "已被唤醒.....");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //被唤醒后
        System.out.println("site-线程" + Thread.currentThread().getName() + "被唤醒后.....");
    }


}
