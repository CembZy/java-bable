package com.ch01.serializable.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProtoBufClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("准备生成数据===========>");

        //生成实体类
        PersonProto.Person.Builder builder = PersonProto.Person.newBuilder();
        builder.setName("CemB");
        builder.setId(1);
        builder.setEmail("CemB@email.cn");
        System.out.println("发送数据===========>" + builder.getName());

        //写往服务端，由编码器进行编码
        ctx.writeAndFlush(builder.build());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
