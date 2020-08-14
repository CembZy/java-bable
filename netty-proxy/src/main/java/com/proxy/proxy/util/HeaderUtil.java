package com.proxy.proxy.util;

import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @ClassName: HeaderUtil
 * @Auther: CemB
 * @Description:
 * @Email: 729943791@qq.com
 * @Company: 东方瑞云
 * @Date: 2018/12/20 17:35
 */
public class HeaderUtil {
    /**
     * @methodName: addHeaders
     * @description: 添加headers信息，响应客户端
     * @auther: CemB
     * @date: 2018/12/20 17:18
     */
    public static void addHeaders(ChannelFuture future, Object request) {
        if (request instanceof HttpRequest) {
            HttpRequest msg = (FullHttpRequest) request;
            msg.headers().set("111", "222");
            future.channel().writeAndFlush(msg);
        } else {
            future.channel().writeAndFlush(request);
        }
    }
}
