package com.aio.cilent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * 客户端向服务端写数据的异步处理器
 */
public class AioClientWriteHandler implements CompletionHandler<Integer, ByteBuffer> {
    //客户端Socket通道
    private AsynchronousSocketChannel asynchronousSocketChannel;
    //用于让客户端和服务端通信的线程等待，不让主线程执行完
    private CountDownLatch countDownLatch;

    public AioClientWriteHandler(AsynchronousSocketChannel asynchronousSocketChannel, CountDownLatch countDownLatch) {
        this.asynchronousSocketChannel = asynchronousSocketChannel;
        this.countDownLatch = countDownLatch;
    }


    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        //判断是否还有 剩余的字节数据
        if (attachment.hasRemaining()) {
            //如果有数据，就继续调用处理器进行写入
            asynchronousSocketChannel.write(attachment, attachment, this);
        } else {
            //如果没有数据，就调用异步的读处理器，进行读取服务端的数据
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            AioClientReadHandler aioClientReadHandler = new AioClientReadHandler(asynchronousSocketChannel, countDownLatch);
            asynchronousSocketChannel.read(byteBuffer, byteBuffer, aioClientReadHandler);
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.out.println("客户端写入失败-----");
        countDownLatch.countDown();
        try {
            asynchronousSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exc.printStackTrace();
    }
}
