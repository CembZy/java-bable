package com.aio.cilent;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * 客户端读取服务端数据的异步处理器
 */
public class AioClientReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    //客户端Socket通道
    private AsynchronousSocketChannel asynchronousSocketChannel;
    //用于让客户端和服务端通信的线程等待，不让主线程执行完
    private CountDownLatch countDownLatch;

    public AioClientReadHandler(AsynchronousSocketChannel asynchronousSocketChannel, CountDownLatch countDownLatch) {
        this.asynchronousSocketChannel = asynchronousSocketChannel;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        //转为读取状态
        attachment.flip();

        //获取服务端返回的数据
        byte[] aByte = new byte[attachment.remaining()];
        attachment.get(aByte);
        String msg;
        try {
            msg = new String(aByte, "UTF-8");
            System.out.println("服务端返回的数据: " + msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.out.println("客户端读取失败-----");
        countDownLatch.countDown();
        try {
            asynchronousSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exc.printStackTrace();
    }
}
