package com.nomad.netty.codec2;

import com.nomad.netty.codec.StudentPOJO;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

/**
 * @author nomad
 * @Description 使用Netty框架的一个简单服务器端，tcp+proto传输数据, 随机发送student,worker中的一种类型
 * @create 2020-11-17 10:45 AM
 */
public class NettyServer {
    public static void main(String[] args) throws Exception {
        //1.创建2个线程组：bossGroup和workerGroup(默认都是2×cpu个线程)
        //2.bossGroup只处理ACCEPT事件，workerGroup处理IO事件
        //3.两个都是无线循环
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //链式配置
            bootstrap.group(bossGroup, workerGroup) //设置2个线程组
                    .channel(NioServerSocketChannel.class) //使用NioServerSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)  //设置线程队列得到的链接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动链接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道测试对象，给workerGroup的eventloop对应的管道设置处理器
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入ProtoBufDecoder解码器
                            pipeline.addLast("decoder", new ProtobufDecoder(MyDataInfo.MyMessage.getDefaultInstance()));

                            pipeline.addLast(new NettyServerHandler());
                        }
                    });

            System.out.println("服务器 is ready...");

            //绑定一个端口并监听同步
            ChannelFuture cf = bootstrap.bind(8000).sync();
            System.out.println("监听8000端口");
            cf.addListener((ChannelFutureListener) future -> System.out.println("监听8000端口 (来自监听器打印)"));

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
            System.out.println("对关闭通道进行监听");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
