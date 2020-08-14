package com.aio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * 监听服务端的连接
 */
public class AioServerHandler implements Runnable {

    //服务端的socket通道
    public AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    //用于让服务端的线程等待，不让主线程执行完
    private CountDownLatch countDownLatch;

    private int Port;

    public AioServerHandler(int port) {
        Port = port;
        try {
            //绑定端口
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(Port));
            System.out.println("服务端启动----");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        //将获取客户端连接的操作交给自定义的异步处理器
        AioAcceptHandler aioAcceptHandler = new AioAcceptHandler(countDownLatch);
        asynchronousServerSocketChannel.accept(this, aioAcceptHandler);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
