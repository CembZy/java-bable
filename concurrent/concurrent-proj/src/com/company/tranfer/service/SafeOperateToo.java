package com.company.tranfer.service;

import com.company.SleepTools;
import com.company.tranfer.UserAccount;

import java.util.Random;


/**
 * 类说明：不会产生死锁的安全转账第二种方法，尝试拿锁--利用显示锁
 */
public class SafeOperateToo implements ITransfer {

    @Override
    public void transfer(UserAccount from, UserAccount to, int amount)
            throws InterruptedException {
        Random r = new Random();
        for (; ; ) {
            if (from.getLock().tryLock()) {
                try {
                    System.out.println(Thread.currentThread().getName()
                            + " get " + from.getName());
                    if (to.getLock().tryLock()) {
                        try {
                            System.out.println(Thread.currentThread().getName()
                                    + " get " + to.getName());
                            //两把锁都拿到了
                            from.flyMoney(amount);
                            to.addMoney(amount);
                            break;
                        } finally {
                            to.getLock().unlock();
                        }
                    }
                } finally {
                    from.getLock().unlock();
                }
            }

            //休眠一段时间，避免多个线程之间同时去获取锁，造成火锁现象。
            SleepTools.ms(r.nextInt(10));
        }
    }
}
