package com.sync.demo.lock;


import com.sync.demo.service.ZkConnection;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

//分布式锁第一种实现：进程之间都通过监听同一个节点实现，这种容易引起羊群效应
public class ZkLock extends AbstractLock {
    private static final String PATH = "/lock";
    private static final ZkClient zkClient = ZkConnection.getConnection();
    private static final Lock lock = new ReentrantLock();

    public void lock() {
        //首先保证进程内线程的安全
        lock.lock();
        //获取分布式锁
        createNode();
    }

    @Override
    public boolean tryUnLock() {
        if (getState() == 0) {
            throw new RuntimeException("释放锁失败!");
        }
        try {
            return zkClient.delete(PATH);
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public void notifyLock() {
        setState(0);
        lock.unlock();
    }


    //创建节点
    public boolean createNode() {
        try {
            //创建的zk节点作为分布式锁
            zkClient.createEphemeral(PATH);
            //监听节点
            zkClient.subscribeDataChanges(PATH, new IZkDataListener() {
                public void handleDataChange(String s, Object o) {
                }

                //释放锁的时候唤醒其他线程
                public void handleDataDeleted(String s) {
                    notifyLock();
                }
            });
            if (compareAndSet(0, 1)) {
                return true;
            }

        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
