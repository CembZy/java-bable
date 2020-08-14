package com.ch01.fix;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 客户端Handler
 */


public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    public static final String s = "Hello,Netty!";

    //连接成功后，接收服务端响应的数据
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("接收到服务端响应的数据......" + byteBuf.toString(CharsetUtil.UTF_8));
    }


    //客户端连接成功后调用的
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("客户端成功连接，并向服务端发送数据......");
        ByteBuf o = null;
        for (int i = 0; i < 10; i++) {
            //固定长度
            o = Unpooled.buffer(s.length());
            o.writeBytes(s.getBytes());

            ctx.writeAndFlush(o);
        }
    }


    //连接失败后调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端连接失败");
        cause.printStackTrace();
        //关闭资源
        ctx.close();
    }
}
