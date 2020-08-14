package com.javaapi;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class TestCreateSession {

    private static final String SERVER = "192.168.254.128:2181";

    private static final int SESSION_OUTTIME = 30000;

    private static final CountDownLatch latch = new CountDownLatch(1);

    @Test
    public void testSession1() throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper(SERVER, SESSION_OUTTIME, null);

        System.out.println(zooKeeper);
        System.out.println(zooKeeper.getState());
    }


    @Test
    public void testSession2() throws IOException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper(SERVER, SESSION_OUTTIME, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                //当获得了连接之后
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }
            }
        });
        latch.await();
        System.out.println(zooKeeper);
        System.out.println(zooKeeper.getState());
    }

}
