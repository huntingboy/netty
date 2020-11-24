package com.nomad.netty.heartbeat;

import com.nomad.netty.groupchat.GroupChatServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author nomad
 * @Description netty心跳机制示例服务器
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
                            //加入一个netty提供的处理器IdleStateHandler:处理空闲状态的处理器
                            //3:表示3s没有读，就发送一个心跳检测包检测是否链接
                            //5:表示5s没有写，就发送一个心跳检测包检测是否链接
                            //7:表示7s没有读写，就发送一个心跳检测包检测是否链接
                            //当IdleStateEvent触发后，就会传递给通道的下一个handler去处理,userEventTriggered()方法中进一步判断处理
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));

                            pipeline.addLast(new MyserverHandler());
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
