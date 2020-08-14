package com.nio;

/**
 * 服务端
 */
public class NioServer {

    public static NioServerHandle nioServerHandle;

    public static void start() {
        if (nioServerHandle != null)
            nioServerHandle.stop();
        nioServerHandle = new NioServerHandle(Tool.DEFAULT_PORT);
        new Thread(nioServerHandle,"server").start();
    }

    public static void main(String[] args) {
        NioServer.start();
    }
}
