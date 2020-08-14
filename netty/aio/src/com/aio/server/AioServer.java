package com.aio.server;

import com.aio.Tool;


/**
 * 服务端主程序
 */
public class AioServer {

    //监听客户端连接的线程
    private static AioServerHandler aioServerHandler;

    //客户端连接数
    public static volatile int ClientNums = 0;

    public static void start() {
        if (aioServerHandler != null)
            return;
        aioServerHandler = new AioServerHandler(Tool.DEFAULT_PORT);
        new Thread(aioServerHandler).start();
    }

    public static void main(String[] args) {
        AioServer.start();
    }

}
