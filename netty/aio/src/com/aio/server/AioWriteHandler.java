package com.aio.server;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * 向客户端写入数据的异步处理器，其实AsynchronousSocketChannel就是客户端和服务端之间数据交互的通道
 */
public class AioWriteHandler implements CompletionHandler<Integer, ByteBuffer> {

    //客户端的socket通道
    public AsynchronousSocketChannel asynchronousSocketChannel;

    //用于让服务端的线程等待，不让主线程执行完
    private CountDownLatch countDownLatch;


    public AioWriteHandler(AsynchronousSocketChannel asynchronousSocketChannel, CountDownLatch countDownLatch) {
        this.asynchronousSocketChannel = asynchronousSocketChannel;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if (attachment.hasRemaining()) {
            //如果还有数据继续写入
            asynchronousSocketChannel.write(attachment, attachment, this);
        } else {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            asynchronousSocketChannel.read(byteBuffer, byteBuffer, new AioReadHandler(asynchronousSocketChannel, countDownLatch));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        countDownLatch.countDown();
        System.out.println("服务端写入客户端数据失败----");
        try {
            asynchronousSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exc.printStackTrace();
    }
}
