package com.aio.server;


import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * 获取客户端连接的异步处理器
 */
public class AioAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AioServerHandler> {

    //用于让服务端的线程等待，不让主线程执行完
    private CountDownLatch countDownLatch;

    public AioAcceptHandler(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }


    @Override
    public void completed(AsynchronousSocketChannel result, AioServerHandler attachment) {
        //连接的客户端数量++
        AioServer.ClientNums++;
        //重新注册监听，将其他客户端也可以进行连接
        attachment.asynchronousServerSocketChannel.accept(attachment, this);

        //开始读取数据，使用异步读取处理器
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        AioReadHandler aioReadHandler = new AioReadHandler(result, countDownLatch);
        result.read(byteBuffer, byteBuffer, aioReadHandler);
    }

    @Override
    public void failed(Throwable exc, AioServerHandler attachment) {
        System.out.println("服务端获取客户端连接失败----");
        countDownLatch.countDown();
    }
}
