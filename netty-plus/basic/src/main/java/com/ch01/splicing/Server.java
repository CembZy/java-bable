package com.ch01.splicing;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * 服务端
 */
public class Server {

    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {

        //服务端线程组可以有多个
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            //服务端共用一个Handler
            final ServerHandler serverHandler = new ServerHandler();

            sb.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        /*接收到连接请求，新启一个socket通信，也就是channel，每个channel
                         * 有自己的事件的handler*/
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ByteBuf byteBuf = Unpooled.copiedBuffer(System.getProperty("line.separator").getBytes());
                            //使用自定义的分隔符，并限制传入的数据大小
//                            SocketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(100,byteBuf));

                            //使用系统自带的分隔符，并限制传入的数据大小
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(10));
                            socketChannel.pipeline().addLast(serverHandler);
                        }
                    });

            ChannelFuture sync = sb.bind().sync();

            sync.channel().closeFuture().sync();
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
        new Server(9999).start();
    }
}
