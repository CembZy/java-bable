package com.nio;

import java.util.Scanner;

/**
 * 客户端
 */
public class NioClient {

    public static NioClientHandle nioClientHandle;

    public static void start() {
        if (nioClientHandle != null)
            nioClientHandle.stop();
        nioClientHandle = new NioClientHandle(Tool.DEFAULT_SERVER_IP, Tool.DEFAULT_PORT);
        new Thread(nioClientHandle, "client").start();
    }

    public static boolean sendMsg(String msg) {
        nioClientHandle.senMesg(msg);
        return true;
    }

    public static void main(String[] args) {
        NioClient.start();
        while (NioClient.sendMsg(new Scanner(System.in).nextLine())) ;
    }

}
