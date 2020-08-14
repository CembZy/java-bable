package com.cemb.unicast;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * 提问端
 */
public class UdpQuestionSide {

    public final static String QUESTION = "请你告诉我一句古诗";

    private static final EventLoopGroup group = new NioEventLoopGroup();

    private static final Bootstrap bs = new Bootstrap();

    public static void start() throws InterruptedException {
        try {
            bs.group(group)
                    .handler(new QuestoinHandler())
                    //UDP的channel
                    .channel(NioDatagramChannel.class);

            //不需要建立连接，绑定0端口是表示系统为我们设置端口监听
            Channel channel = bs.bind(0).sync().channel();

            //UDP使用DatagramPacket发送数据
            channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(QUESTION, CharsetUtil.UTF_8),
                    new InetSocketAddress("127.0.0.1", 8080)));

            //15秒后未获取响应就打印超时
            if (!channel.closeFuture().await(15000)) {
                System.out.println("查询超时！");
            }

        } finally {
            group.shutdownGracefully();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        UdpQuestionSide.start();
    }
}
