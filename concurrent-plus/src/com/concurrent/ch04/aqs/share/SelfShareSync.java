package com.concurrent.ch04.aqs.share;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 自定义共享锁
 */
public class SelfShareSync implements Lock {

    private Sync sync;

    public SelfShareSync(int state) {
        this.sync = new Sync(state);
    }

    private static final class Sync extends AbstractQueuedSynchronizer {


        public Sync(int state) {
            if (state < 0) {
                throw new RuntimeException();
            }
            //state是共享锁的数量，当线程获取到一个锁的时候就减一
            setState(state);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            for (; ; ) {
                int state = getState();
                //可能存在一次性获取多个锁，所以要得还出剩下的锁
                int newState = state - arg;
                //获取到锁  或者 已经没有锁了  都需要将当前还剩余的锁数量返回给AQS  ，如果是没锁了，当前线程就会进入同步队列
                if (newState < 0 || compareAndSetState(state, newState)) {
                    return newState;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            for (; ; ) {
                int state = getState();
                //释放锁就是加上当前要释放锁的数量
                int newState = state + arg;
                if (compareAndSetState(state, newState)) {
                    return true;
                }
            }
        }

        final ConditionObject newCondition() {
            return new ConditionObject();
        }
    }

    @Override
    public void lock() {
        sync.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquireShared(1) >= 0;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }


}
