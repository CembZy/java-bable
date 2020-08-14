package com.proxy.proxy.server;

import com.proxy.proxy.server.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;

/**
 * @ClassName: Server
 * @Auther: CemB
 * @Description:
 * @Email: 729943791@qq.com
 * @Company: 东方瑞云
 * @Date: 2018/12/20 15:13
 */
public class Server {
    public final static HttpResponseStatus SUCCESS = new HttpResponseStatus(200,
            "Connection established");
    private final int PORT;
    private final EventLoopGroup workerStateEvent = new NioEventLoopGroup();
    private final EventLoopGroup bossStateEvent = new NioEventLoopGroup();
    private final ServerBootstrap bootstrap = new ServerBootstrap();
    private final ServerHandler serverHandler = new ServerHandler();

    public Server(int PORT) {
        this.PORT = PORT;
    }

    public void start() throws InterruptedException {
        bootstrap.group(bossStateEvent, workerStateEvent)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(PORT))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("httpCodec", new HttpServerCodec());
                        socketChannel.pipeline().addLast("httpObject", new HttpObjectAggregator(65536));
                        socketChannel.pipeline().addLast(serverHandler);
                    }
                });

        ChannelFuture channel = bootstrap.bind().sync();
        //关闭通道
        channel.channel().closeFuture().sync();
    }

}
