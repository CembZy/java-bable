package com.company.tranfer.service;


import com.company.tranfer.UserAccount;

/**
 * 类说明：不安全的转账动作的实现--会发生死锁现象
 */
public class TrasnferAccount implements ITransfer {

    @Override
    public void transfer(UserAccount from, UserAccount to, int amount)
            throws InterruptedException {
        synchronized (from) {//先锁转出
            System.out.println(Thread.currentThread().getName()
                    + " get" + from.getName());
            Thread.sleep(100);
            synchronized (to) {//再锁转入
                System.out.println(Thread.currentThread().getName()
                        + " get" + to.getName());
                from.flyMoney(amount);
                to.addMoney(amount);
            }
        }
    }
}
