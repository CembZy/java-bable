package com.company.tranfer.service;


import com.company.tranfer.UserAccount;

/**
 * 类说明：不会产生死锁的安全转账--利用系统的hashCode方法，自定义锁的顺序避免死锁
 */
public class SafeOperate implements ITransfer {
    private static Object tieLock = new Object();//加时赛锁

    @Override
    public void transfer(UserAccount from, UserAccount to, int amount)
            throws InterruptedException {

        int fromHash = System.identityHashCode(from);
        int toHash = System.identityHashCode(to);
        //先锁hash小的那个
        if (fromHash < toHash) {
            synchronized (from) {
                toPay(to, from, amount);
            }
        } else if (toHash < fromHash) {
            synchronized (to) {
                toPay(from, to, amount);
            }
        } else {//解决hash冲突的方法
            synchronized (tieLock) {
                synchronized (from) {
                    synchronized (to) {
                        from.flyMoney(amount);
                        to.addMoney(amount);
                    }
                }
            }
        }

    }

    private void toPay(UserAccount from, UserAccount to, int amount) throws InterruptedException {
        System.out.println(Thread.currentThread().getName()
                + " get" + to.getName());
        Thread.sleep(100);
        synchronized (from) {
            System.out.println(Thread.currentThread().getName()
                    + " get" + from.getName());
            from.flyMoney(amount);
            to.addMoney(amount);
        }
    }
}
