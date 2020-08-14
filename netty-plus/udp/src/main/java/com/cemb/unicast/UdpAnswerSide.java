package com.cemb.unicast;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * 应答者
 */
public class UdpAnswerSide {

    private static final EventLoopGroup group = new NioEventLoopGroup();

    private static final Bootstrap bs = new Bootstrap();

    public static void start() throws InterruptedException {
        try {
            bs.group(group)
                    .handler(new AnswerHandler())
                    //UDP的channel
                    .channel(NioDatagramChannel.class);

            //没有接受客户端连接的过程，监听本地端口即可
            Channel channel = bs.bind(8080).sync().channel();

            System.out.println("应答服务已启动.....");
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        UdpAnswerSide.start();
    }


}
