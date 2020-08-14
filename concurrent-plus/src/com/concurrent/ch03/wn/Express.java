package com.concurrent.ch03.wn;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 快递实体类，使用Lock的Condition来实现等待换新
 */
public class Express {

    public static final String CITY = "shanghai";

    private int km;

    private String site;

    //锁，这里有km和site两种资源，可以用一把锁，也可以用两把锁，这使用一把锁
    private Lock lock = new ReentrantLock();
    //负责km的等待唤醒
    private Condition kmCondition = lock.newCondition();
    //负责site的等待唤醒
    private Condition siteCondition = lock.newCondition();


    public Express(int km, String site) {
        this.km = km;
        this.site = site;
    }

    //改变公里数
    public void changeKm() {
        lock.lock();
        try {
            this.km = 101;

            //显示锁的唤醒用single，可以精准唤醒
            kmCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    //改变地址
    public void changeSite() {
        lock.lock();
        try {
            this.site = "beijing";

            //显示锁的唤醒用single，可以精准唤醒,signalAll只能唤醒指定Condition上的等待的线程，其他线程也不能被唤醒，和notifyAll不同
            siteCondition.signal();
        } finally {
            lock.unlock();
        }
    }


    public void km() {
        lock.lock();
        try {
            while (km <= 100) {
                try {
                    //等待
                    System.out.println("km-线程" + Thread.currentThread().getName() + "正在等待.....");
                    kmCondition.await();
                    //被唤醒后
                    System.out.println("km-线程" + Thread.currentThread().getName() + "已被唤醒.....");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //被唤醒后
            System.out.println("km-线程" + Thread.currentThread().getName() + "被唤醒后.....");
        } finally {
            lock.unlock();
        }

    }

    public void site() {
        lock.lock();
        try {
            while (CITY.equals(site)) {
                try {
                    //等待
                    System.out.println("site-线程" + Thread.currentThread().getName() + "正在等待.....");
                    siteCondition.await();
                    System.out.println("site-线程" + Thread.currentThread().getName() + "已被唤醒.....");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //被唤醒后
            System.out.println("site-线程" + Thread.currentThread().getName() + "被唤醒后.....");
        } finally {
            lock.unlock();
        }

    }


}
