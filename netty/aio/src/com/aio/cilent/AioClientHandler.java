package com.aio.cilent;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * 连接服务端的异步处理器
 */
public class AioClientHandler implements Runnable, CompletionHandler<Void, AioClientHandler> {


    //客户端Socket通道
    private AsynchronousSocketChannel asynchronousSocketChannel;
    //用于让客户端和服务端通信的线程等待，不让主线程执行完
    private CountDownLatch countDownLatch;

    private int Port;
    private String Ip;

    public AioClientHandler(int port, String ip) {
        Port = port;
        Ip = ip;
        //获取通道
        try {
            asynchronousSocketChannel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            countDownLatch = new CountDownLatch(1);
            //连接服务器，并将当前处理器传入，用于异步连接服务端操作执行完之后，系统通知
            asynchronousSocketChannel.connect(new InetSocketAddress(Ip, Port), null, this);
            countDownLatch.await();
            asynchronousSocketChannel.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //回调，执行写操作
    @Override
    public void completed(Void result, AioClientHandler attachment) {
        System.out.println("客户端连接成功---");
    }

    @Override
    public void failed(Throwable exc, AioClientHandler attachment) {
        System.out.println("客户端连接失败---");
        exc.printStackTrace();
        countDownLatch.countDown();
    }

    //向服务端写入数据
    public void sendMessag(String msg) {
        byte[] bytes = msg.getBytes();
        //将数据的字节数组放入字节缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        //将字节缓冲区的写入操作转成读操作
        byteBuffer.flip();

        //写入数据，将数据传入异步处理器去执行吸入操作
        AioClientWriteHandler aioClientWriteHandler = new AioClientWriteHandler(asynchronousSocketChannel, countDownLatch);
        asynchronousSocketChannel.write(byteBuffer, byteBuffer, aioClientWriteHandler);
    }

}
