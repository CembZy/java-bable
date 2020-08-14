package com.company.aqs;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 根据AQS实现一个自己的类ReentrantLock
 */
public class SelfLock implements Lock {


    public static class Sync extends AbstractQueuedSynchronizer {

        /**
         * 查看当前是否有线程占用锁
         * 根据status状态值判断，status==1是有线程占用锁，status==0没有
         *
         * @return
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }


        /**
         * 获取独占锁acquire
         *
         * @param arg
         * @return
         */
        @Override
        protected boolean tryAcquire(int arg) {
            // 获取锁
            if (compareAndSetState(0, 1)) {
                // 将当前线程设置进去
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }


        /**
         * 释放独占锁
         *
         * @param arg
         * @return
         */
        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == 0) {
                throw new UnsupportedOperationException();
            }
            //将线程设置为null
            setExclusiveOwnerThread(null);

            //这里不适用CAS原子操作，是因为这里是独占锁，能够保证当前只有一个线程，就不会出现安全问题，所以就不需要原子操作。
            setState(0);
            return true;
        }


        Condition newCondition() {
            return new ConditionObject();
        }
    }


    private final Sync sync = new Sync();

    @Override
    public void lock() {
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
        sync.release(0);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
