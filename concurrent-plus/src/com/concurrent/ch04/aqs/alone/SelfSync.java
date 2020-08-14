package com.concurrent.ch04.aqs.alone;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 使用AQS实现自定义的独占锁(注意，目前实现的是一个不可重入锁)
 */
public class SelfSync implements Lock {

    private Sync sync = new Sync();

    //使用内部类继承AQS，去具体实现锁的细节
    private static class Sync extends AbstractQueuedSynchronizer {

        //判断是否当前锁是获取到锁（state==1是获取到了锁，0是不是没有获取到锁）
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        //因为在获取锁的时候，是多个线程竞争来获取，所以需要使用CAS
        @Override
        protected boolean tryAcquire(int arg) {
            //获取到了锁
            if (compareAndSetState(0, 1)) {
                //给当前线程加锁
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            //健壮性判定，如果没有锁的情况，是不能释放锁的
            if (getState() == 0) {
                throw new RuntimeException();
            }
            setExclusiveOwnerThread(null);

            //因为释放锁不存在竞争，所以不需要使用CAS原子操作
            setState(0);
            return true;
        }


        //ConditionObject是AQS中的内部类，目的是创建Condition接口，提供显示锁的等待唤醒机制
        private Condition newCondition() {
            return new ConditionObject();
        }
    }


    @Override
    public void lock() {
        //外部调用这个方法获取锁的时候，AQS调用acquire这个模板方法，内部会去调用自己实现的tryAcquire方法
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        //外部调用这个方法释放锁的时候，AQS调用release这个模板方法，内部会去调用自己实现的tryRelease方法
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

    public static void main(String[] args) {

        String hex = "0X03";
        String hex2 = "0x0f";
        String hex3 = "0x01";
        Integer x = Integer.parseInt(hex.substring(2), 16);
        Integer x2 = Integer.parseInt(hex2.substring(2), 16);
        Integer x3 = Integer.parseInt(hex3.substring(2), 16);
        System.out.println(x3);


        System.out.println(Integer.toBinaryString(x));
        System.out.println(Integer.toBinaryString(x2));

        System.out.println((x & x2));
    }
}
