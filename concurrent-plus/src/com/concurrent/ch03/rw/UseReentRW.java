package com.concurrent.ch03.rw;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 使用读写锁
 */
public class UseReentRW implements GoodsService {

    //读写锁
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    //获取读锁
    private Lock readLock = lock.readLock();

    //获取写锁
    private Lock writeLock = lock.writeLock();

    private GoodsInfo goodsInfo;

    public UseReentRW(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    @Override
    public GoodsInfo getNum() {
        //获取锁
        readLock.lock();
        try {
            goodsInfo.getStoreNumber();
            return goodsInfo;
        } finally {
            //释放锁
            readLock.unlock();
        }
    }

    @Override
    public void setNum(int number) {
        //获取锁
        writeLock.lock();
        try {
            goodsInfo.changeNumber(number);
        } finally {
            //释放锁
            writeLock.unlock();
        }
    }
}
