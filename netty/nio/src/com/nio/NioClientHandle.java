package com.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 客户端处理线程
 */
public class NioClientHandle implements Runnable {

    //选择器
    private Selector selector;

    //客户端通道
    private SocketChannel socketChannel;

    private int Port;

    private String Ip;

    //判断通道是否建立
    private volatile boolean started;

    public NioClientHandle(String Ip, int Port) {
        this.Port = Port;
        this.Ip = Ip;
        try {
            //开启选择器
            selector = Selector.open();
            //开启通道
            socketChannel = SocketChannel.open();
            //设置通道为非阻塞模式
            socketChannel.configureBlocking(false);
            started = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        started = false;
    }


    @Override
    public void run() {
        //连接服务端
        doConnection();
        System.out.println("客户端已请求连接--");
        //循环遍历selector
        while (started) {
            try {
                //此方法是阻塞的，当至少有一个事件被注册的时候才会释放执行权
                selector.select();
                //获取所有被注册的事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key;
                while (it.hasNext()) {
                    key = it.next();
                    //处理完一个就将之前的事件注销了
                    it.remove();
                    try {
                        //处理事件
                        handler(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
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
        //判断事件是否有效
        if (key.isValid()) {
            //客户端，首先判断是否是连接事件
            if (key.isConnectable()) {
                try {
                    //判断是否连接完成
                    if (!socketChannel.finishConnect())
                        System.exit(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //判断是否是读取事件，是的话读取服务端响应的数据
            } else if (key.isReadable()) {

                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                try {
                    //将通道中的数据读取到缓冲区中
                    int read = socketChannel.read(byteBuffer);
                    //如果服务端有响应数据
                    if (read > 0) {
                        //将缓冲区中的数据转成字符串
                        byteBuffer.flip();
                        byte[] bytes = new byte[byteBuffer.remaining()];
                        byteBuffer.get(bytes);
                        String msg = new String(bytes, "UTF-8");

                        System.out.println("服务端返回的数据：" + msg);

                        //没有响应数据
                    } else {
                        System.out.println("没有服务端响应数据");
                        key.cancel();
                        socketChannel.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //连接
    private void doConnection() {
        try {
            //因为如果是连接本地服务端，可能连接会很快，所以这个时候不需要去注册事件，
            //如果是连接外网服务端，连接很慢的时候，就需要去注册连接事件
            if (!socketChannel.connect(new InetSocketAddress(Ip, Port))) {
                //注册连接事件
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
        } catch (IOException e) {
            //连接失败的时候，直接关闭程序
            System.exit(1);
            e.printStackTrace();
        }
    }

    //向服务端发送数据
    public void doWrite(String msg) {
        byte[] bytes = msg.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();

        try {
            //通过通道写入数据
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //向服务端发送数据
    public void senMesg(String msg) {
        try {
            //注册读取事件，用于读取服务端响应的数据
            socketChannel.register(selector, SelectionKey.OP_READ);

            //向服务端发送数据
            doWrite(msg);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }
}
