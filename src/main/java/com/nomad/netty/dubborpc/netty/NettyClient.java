package com.nomad.netty.dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author nomad
 * @Description 服务消费者, netty实现的一个客户端，用于tcp通信
 * @create 2020-11-24 5:15 PM
 */
public class NettyClient {
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler client;

    private int count;

    //代理模式，获取一个代理对象
    public Object getBean(final Class<?> serviceClass, final String providerName) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{serviceClass}, (proxy, method, args) -> {
                    //{}部分代码会被多次调用，客户端没调用一次hello，就会进入该代码
                    count++;
                    System.out.println("第" + count + "次别调用~");

                    if (client == null) {
                        initClient();
                    }

                    //设置要发送给服务器的消息
                    //providerName:协议头
                    //args[0]:客户端调用api hello(???)的参数
                    client.setPara(providerName + args[0]);
                    return executor.submit(client).get();
                });
    }

    /**
     * 初始化客户端
     */
    private static void initClient(){
        client = new NettyClientHandler();

        NioEventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("encoder", new StringEncoder());
                        pipeline.addLast("decoder", new StringDecoder());
                        pipeline.addLast(client);
                    }
                });

        try {
            bootstrap.connect("127.0.0.1", 8000).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
