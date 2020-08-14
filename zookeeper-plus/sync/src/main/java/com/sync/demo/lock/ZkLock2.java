package com.sync.demo.lock;


import com.sync.demo.service.ZkConnection;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//分布式锁第二种实现：进程之间都通过监听前一个节点实现，这种容易避免了羊群效应
public class ZkLock2 extends AbstractLock {
    private static final String PATH = "/lock";
    private static final ZkClient zkClient = ZkConnection.getConnection();
    private static final Lock lock = new ReentrantLock();

    //当前节点
    private String currentNode;
    //前一个节点
    private String prevNode;

    public ZkLock2() {
        if (!this.zkClient.exists(PATH)) {
            this.zkClient.createPersistent(PATH);
        }
    }

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
            if (currentNode == null || currentNode.length() < 0) {
                //创建临时顺序节点
                currentNode = zkClient.createEphemeralSequential(PATH + "/", "lock2");
            }

            //获取所有临时节点并排序，临时节点名称为自增长的字符串如：0000000400
            List<String> childrens = this.zkClient.getChildren(PATH);
            Collections.sort(childrens);


            prevNode = childrens.get(0);
            if (!currentNode.equals(prevNode)) {
                //监听节点
                zkClient.subscribeDataChanges(PATH + "/" + prevNode, new IZkDataListener() {
                    public void handleDataChange(String s, Object o) {
                    }

                    //释放锁的时候唤醒其他线程
                    public void handleDataDeleted(String s) {
                        notifyLock();
                    }
                });
                return false;
            } else {
                if (compareAndSet(0, 1)) {
                    return true;
                }
            }

        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
