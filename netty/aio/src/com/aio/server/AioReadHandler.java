package com.aio.server;

import com.aio.Tool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * 服务端读取客户端数据的异步处理器
 */
public class AioReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    //客户端的socket通道
    public AsynchronousSocketChannel asynchronousSocketChannel;

    //用于让服务端的线程等待，不让主线程执行完
    private CountDownLatch countDownLatch;


    public AioReadHandler(AsynchronousSocketChannel asynchronousSocketChannel, CountDownLatch countDownLatch) {
        this.asynchronousSocketChannel = asynchronousSocketChannel;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        //客户端主动终止了TCP套接字，这时服务端终止就可以了
        if (result == -1) {
            try {
                asynchronousSocketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //读取数据
        attachment.flip();
        byte[] bytes = new byte[attachment.remaining()];
        attachment.get(bytes);

        String msg;
        try {
            System.out.println("result: " + result);
            msg = new String(bytes, "UTF-8");
            System.out.println("读取到客户端传来的数据:" + msg);

            //向客户端写数据
            String responseStr = Tool.response(msg);
            byte[] bytes1 = responseStr.getBytes();
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes1.length);
            byteBuffer.put(bytes1);
            byteBuffer.flip();

            AioWriteHandler aioWriteHandler = new AioWriteHandler(asynchronousSocketChannel, countDownLatch);
            asynchronousSocketChannel.write(byteBuffer, byteBuffer, aioWriteHandler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        countDownLatch.countDown();
        System.out.println("服务端读取客户端数据失败----");
        try {
            asynchronousSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exc.printStackTrace();
    }
}
