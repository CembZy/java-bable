package com.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 服务端处理线程
 */
public class NioServerHandle implements Runnable {

    //处理器
    private Selector selector;

    //服务端通道
    private ServerSocketChannel serverSocketChannel;

    //判断服务端通道是否建立
    private volatile boolean started;

    private int Port;

    public NioServerHandle(int port) {
        this.Port = port;
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            //绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(Port));
            //注册获取连接事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            started = true;
            System.out.println("服务器已启动，端口号：" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        started = false;
    }


    @Override
    public void run() {
        //循环遍历selector
        while (started) {
            try {
                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                SelectionKey key;
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handler(key);
                    } catch (Exception e) {
                        if (key != null) {
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                            key.cancel();
                        }
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //selector关闭后会自动释放里面管理的资源
        if (selector != null)
            try {
                selector.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    //处理事件
    private void handler(SelectionKey key) {
        if (key.isValid()) {
            //判断是否是接收连接事件
            if (key.isAcceptable()) {
                //通道
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                try {
                    //客户端通道
                    SocketChannel accept = serverSocketChannel.accept();
                    System.out.println("服务端连接成功---");

                    accept.configureBlocking(false);
                    //注册读取事件
                    accept.register(selector, SelectionKey.OP_READ);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //读取客户端的数据
            } else if (key.isReadable()) {

                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                SocketChannel socketChannel = (SocketChannel) key.channel();

                try {
                    //将通道中的客户端的数据读取到缓冲区中
                    int read = socketChannel.read(byteBuffer);
                    //如果有数据
                    if (read > 0) {
                        byteBuffer.flip();
                        byte[] bytes = new byte[byteBuffer.remaining()];
                        byteBuffer.get(bytes);
                        String msg = new String(bytes, "UTF-8");
                        System.out.println("客户端传入的数据：" + msg);


                        //服务端向客户端响应数据
                        //处理数据
                        String result = Tool.response(msg);
                        write(socketChannel, result);
                    } else {
                        System.out.println("没有客户端传入的数据");
                        key.cancel();
                        socketChannel.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //向客户端响应数据
    private void write(SocketChannel channel, String msg) throws IOException {
        byte[] bytes = msg.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        channel.write(byteBuffer);
    }


}
