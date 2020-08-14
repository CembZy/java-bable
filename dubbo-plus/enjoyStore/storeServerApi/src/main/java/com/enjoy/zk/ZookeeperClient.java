package com.enjoy.zk;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class ZookeeperClient {
    private static ZkClient zkClient;

    public static void main(String[] args) throws InterruptedException {
        zkClient = new ZkClient("172.17.0.2:2181", Integer.MAX_VALUE);
        listen();

        zkClient.createPersistent("/super", "1234");
        Thread.sleep(1000);
        zkClient.writeData("/super", "456", -1);
        Thread.sleep(1000);

        zkClient.delete("/super");
        Thread.sleep(1000);
    }

    public static void listen() {
        //监听数据的变化
        zkClient.subscribeDataChanges("/super", new IZkDataListener() {
            @Override
            public void handleDataDeleted(String path) throws Exception {
                System.out.println("删除的节点为:" + path);
            }


            @Override
            public void handleDataChange(String path, Object data) throws Exception {
                System.out.println("变更的节点为:" + path + ", 变更内容为:" + data);
            }
        });
    }

}
