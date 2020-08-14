package com.ch01.splicing;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * 客户端
 */
public class Client {


    private String host;

    private int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    //启动客户端连接
    public void start() {
        //线程组，客户端线程组只能有一个
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //客户端启动类
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)//加入线程组
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))//设置连接参数
                    //设置客户端连接的处理器
                    .handler(new ChannelInitializer<Channel>() {
                        protected void initChannel(Channel channel) throws Exception {
                            ByteBuf byteBuf = Unpooled.copiedBuffer(System.getProperty("line.separator").getBytes());
                            //使用自定义的分隔符，并限制传入的数据大小
//                            channel.pipeline().addLast(new DelimiterBasedFrameDecoder(100,byteBuf));

                            //使用系统自带的分隔符，并限制传入的数据大小
                            channel.pipeline().addLast(new LineBasedFrameDecoder(10));
                            channel.pipeline().addLast(new ClientHandler());
                        }
                    });

            //阻塞直到连接完成
            ChannelFuture connect = bootstrap.connect().sync();

            //阻塞直到连接成功关闭
            connect.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 9999);
        client.start();
    }
}
