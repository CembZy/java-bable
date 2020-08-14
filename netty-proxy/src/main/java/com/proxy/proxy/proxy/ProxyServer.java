package com.proxy.proxy.proxy;

import com.proxy.proxy.proxy.handler.ProxyServerHandler;
import com.proxy.proxy.util.HeaderUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import java.net.InetSocketAddress;

/**
 * @ClassName: Proxy
 * @Auther: CemB
 * @Description:
 * @Email: 729943791@qq.com
 * @Company: 东方瑞云
 * @Date: 2018/12/20 15:14
 */
public class ProxyServer {


    private final String HOST;
    private final int PORT;
    private final Object msg;
    private final Channel channel;

    public ProxyServer(String HOST, int PORT, Object msg, Channel channel) {
        this.HOST = HOST;
        this.PORT = PORT;
        this.msg = msg;
        this.channel = channel;
    }


    public void start() {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new HttpClientCodec());
                        socketChannel.pipeline().addLast(new HttpObjectAggregator(6553600));
                        socketChannel.pipeline().addLast(new ProxyServerHandler(channel));
                    }
                })
                .connect(new InetSocketAddress(HOST, PORT))
                .addListener(new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            HeaderUtil.addHeaders(future, msg);
                        } else {
                            future.channel().close();
                        }
                    }
                });
    }


}
