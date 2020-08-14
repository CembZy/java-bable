package com.proxy.proxy.server.handler;

import com.proxy.proxy.proxy.ProxyServer;
import com.proxy.proxy.server.Server;
import com.proxy.proxy.util.ProtoUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.proxy.proxy.util.ProtoUtil.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @ClassName: ServerHandler
 * @Auther: CemB
 * @Description:
 * @Email: 729943791@qq.com
 * @Company: 东方瑞云
 * @Date: 2018/12/20 15:20
 */

//线程间共享,但必须要保证此类线程安全
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final static Log LOG = LogFactory.getLog(ServerHandler.class);


    //保证线程安全
    private ThreadLocal<ChannelFuture> futureThreadLocal = new ThreadLocal<>();
    private final AtomicInteger PORT = new AtomicInteger(0);
    private final AtomicReference<String> HOST = new AtomicReference<String>("0.0.0.0");


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("服务器连接成功......");
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
        //http
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String name = request.method().name();
            RequestProto protoUtil = ProtoUtil.getRequestProto(request);
            String host = protoUtil.getHost();
            int port = protoUtil.getPort();
            PORT.set(port);
            HOST.set(host);
            request.headers().set("11", "222");
            if ("CONNECT".equalsIgnoreCase(name)) {//HTTPS建立代理握手
                HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, Server.SUCCESS);
                ctx.writeAndFlush(response);
                ctx.pipeline().remove("httpCodec");
                ctx.pipeline().remove("httpObject");
                return;
            }
            //开启代理服务器
            new ProxyServer(host, port, msg, ctx.channel()).start();
        } else { //https,只转发数据，不对数据做处理，所以不需要解密密文
            ChannelFuture future = futureThreadLocal.get();
            //代理连接还未建立
            if (future == null) {
                //连接至目标服务器
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(ctx.channel().eventLoop()) // 复用客户端连接线程池
                        .channel(ctx.channel().getClass()) // 使用NioSocketChannel来作为连接用的channel类
                        .handler(new ChannelInitializer() {
                            @Override
                            protected void initChannel(Channel ch) throws Exception {
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx0, Object msg) throws Exception {
                                        ctx.channel().writeAndFlush(msg);
                                    }

                                    @Override
                                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                        System.out.println("https 代理服务器连接成功...");
                                    }
                                });
                            }
                        });
                future = bootstrap.connect(HOST.get(), PORT.get());
                futureThreadLocal.set(future);
                future.addListener(new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            future.channel().writeAndFlush(msg);
                        } else {
                            ctx.channel().close();
                        }
                    }
                });
            } else {
                //代理建立连接之后，直接刷回数据
                future.channel().writeAndFlush(msg);
            }
        }

    }
}
