package com.sync.demo.lock;

public interface Lock {

    //获取锁
    void lock();

    //释放锁
    void unlock();

}
