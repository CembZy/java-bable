package com.cemb.unicast;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;


/**
 * 提问端处理器
 */
public class QuestoinHandler extends SimpleChannelInboundHandler<DatagramPacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {

        //获取内容
        String content = datagramPacket.content().toString(CharsetUtil.UTF_8);
        System.out.println(content);
        channelHandlerContext.close();
    }
}
