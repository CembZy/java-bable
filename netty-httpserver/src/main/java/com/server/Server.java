package com.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;

/**
 * http服务器
 */
public class Server {

    //线程组
    private static final EventLoopGroup group = new NioEventLoopGroup();

    //服务端启动类
    private static final ServerBootstrap bootstrap = new ServerBootstrap();

    //监听端口
    private static final int PORT = 6789;

    private static final ServerHandler serverHandler = new ServerHandler();

    //SSL开关
    private static final boolean SSL = true;

    public static void start() throws InterruptedException, CertificateException, SSLException {
        //配置SSL
        final SslContext sslCtx;
        if (SSL) {
            //netty为我们提供的ssl加密，缺省
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(),
                    ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        //设置线程组
        bootstrap.group(group)
                //设置Nio方式的通道监听客户端连接
                .channel(NioServerSocketChannel.class)
                //设置端口
                .localAddress(new InetSocketAddress(PORT))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        ChannelPipeline pipeline = nioSocketChannel.pipeline();

                        //验证SSL
                        if (sslCtx != null) {
                            pipeline.addLast(sslCtx.newHandler(nioSocketChannel.alloc()));
                        }

                        //对http请求进行解码
                        pipeline.addLast("decode", new HttpRequestDecoder());

                        //对http请求进行编码
                        pipeline.addLast("encode", new HttpResponseEncoder());

                        //对http进行聚合，设置最大聚合字节长度为10M
                        pipeline.addLast(new HttpObjectAggregator(10 * 1024 * 1024));

                        //开启http内容压缩
                        pipeline.addLast(new HttpContentCompressor());

                        //添加自定义处理器
                        pipeline.addLast(serverHandler);
                    }
                });

        System.out.println("服务端已经启动......");

        //阻塞方法，直到监听到指定的端口有连接
        ChannelFuture channel = bootstrap.bind().sync();

        //关闭通道
        channel.channel().closeFuture().sync();
    }


    public static void main(String[] args) throws InterruptedException, CertificateException, SSLException {
        Server.start();
    }

}
