package com.sync.demo.service;

import org.I0Itec.zkclient.ZkClient;

//zk连接
public class ZkConnection {
    // zk连接地址
    private static final String CONNECTSTRING = "127.0.0.1:2181";
    private static final int connectionTimeout = 50000;
    private static final int sessionTimeout = 50000;

    private ZkConnection() {
    }

    private static class ZkConnectionBuilder {
        private final static ZkClient zkConnection = new ZkClient(CONNECTSTRING, sessionTimeout, connectionTimeout);
    }

    public static final ZkClient getConnection() {
        return ZkConnectionBuilder.zkConnection;
    }

}
