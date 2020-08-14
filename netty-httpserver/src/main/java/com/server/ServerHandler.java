package com.server;


import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * 服务端处理器
 */

@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static String result = "";


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("已经获取到客户端连接......");
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        FullHttpRequest request = (FullHttpRequest) msg;
        try {
            String uri = request.uri();
            HttpMethod method = request.method();

            if (!"/test".equalsIgnoreCase(uri)) {
                result = "路径找不到";
                send(ctx, HttpResponseStatus.BAD_REQUEST, result);
                return;
            }

            //get请求
            if (HttpMethod.GET.equals(method)) {
                result = "get请求: " + System.getProperty("line.separator") + RespConstant.getNews();
                send(ctx, HttpResponseStatus.OK, result);
            } else if (HttpMethod.POST.equals(method)) {
                //.....
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            request.release();
        }

    }

    //响应
    private void send(ChannelHandlerContext ctx, HttpResponseStatus status, String result) {
        //聚合响应，并设置http配置
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(result, CharsetUtil.UTF_8)
        );

        //设置响应headers
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");

        //添加监听，等所有数据全部响应完了之后再关闭资源
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
