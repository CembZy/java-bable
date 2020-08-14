package com.cemb.unicast;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;


/**
 * 应答者处理器
 */
public class AnswerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    String answer = "只要功夫深，铁棒磨成针。" +
            "旧时王谢堂前燕,飞入寻常百姓家。";

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        String content = datagramPacket.content().toString(CharsetUtil.UTF_8);
        if (UdpQuestionSide.QUESTION.equals(content)) {
            //提问者的地址
            InetSocketAddress address = datagramPacket.sender();

            channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(answer, CharsetUtil.UTF_8),
                    address));
        }
    }
}
