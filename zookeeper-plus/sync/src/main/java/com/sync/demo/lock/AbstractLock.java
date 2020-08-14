package com.sync.demo.lock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

//借鉴AQS的实现，使用模板方法
public abstract class AbstractLock implements Lock {

    private static final Unsafe unsafe = getUnsafe();
    private static long stateOffset;
    //同步状态
    private volatile int state;

    public static final Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);
            stateOffset = unsafe.objectFieldOffset
                    (AbstractLock.class.getDeclaredField("state"));
            return unsafe;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //释放锁
    protected boolean tryUnLock() {
        throw new UnsupportedOperationException();
    }

    //唤醒
    protected void notifyLock() {
        throw new UnsupportedOperationException();
    }


    public  final void unlock() {
        if (tryUnLock()) {
            //线程唤醒
            notifyLock();
        }
    }

    protected final int getState() {
        return state;
    }

    protected final void setState(int newState) {
        state = newState;
    }

    //CAS，借鉴AtomicInteger实现
    public final boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

}
