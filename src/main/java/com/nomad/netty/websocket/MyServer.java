package com.nomad.netty.websocket;

import com.nomad.netty.heartbeat.MyserverHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author nomad
 * @Description netty ws示例服务器
 * @create 2020-11-19 3:34 PM
 */
public class MyServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            //因为基于http协议，使用http的编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            //以块的方式写，添加ChunkedWriteHandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());

                            /**
                             * http数据传输过程中是分段
                             * HttpObjectAggregator聚合多个段
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            /**
                             * 对于websocket, 它的数据是以帧的形式传递
                             * WebSocketFrame下面有六个子类
                             * 对应浏览器请求的uri:ws://localhost:7000/hello
                             * WebSocketServerProtocolHandler作用：将http协议升级为ws协议（通过一个状态码 101），保持长链接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            pipeline.addLast(new MyTextWebSocketFrameHandler());
                        }
                    });

            ChannelFuture cf = bootstrap.bind(8000).sync();
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
