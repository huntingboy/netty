package com.nomad.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author nomad
 * @Description 使用Netty框架的一个简单客户端
 * @create 2020-11-17 11:34 AM
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        //客户端需要一个事件循环组
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        try {
            //创建客户端的启动对象，配置参数
            Bootstrap bootstrap = new Bootstrap();

            //链式配置
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            System.out.println("客户端ok");

            //链接服务器
            //ChannelFuture 异步模型
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 8000).sync();
            System.out.println("链接服务器ok");

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
            System.out.println("对关闭通道进行监听");
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
