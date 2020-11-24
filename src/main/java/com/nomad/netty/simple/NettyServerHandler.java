package com.nomad.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author nomad
 * @Description 管道pipeline中的一个处理器
 * @create 2020-11-17 11:20 AM
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取客户端发来的数据
     * @param ctx 上下文对象， 含有channel,pipeline（链表，包含多个handler，双向链表）和地址
     * @param msg 客户端发来的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程" + Thread.currentThread().getName());
        System.out.println("server ctx = " + ctx);
        System.out.println("看看pipeline和channel的关系"); //2者相互包含
        ChannelPipeline pipeline = ctx.pipeline(); //本质是一个双向链接
        Channel channel = ctx.channel();

        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发来的数据：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + channel.remoteAddress());

        //用户程序自定义的普通任务（模拟耗时）
        //任务提交到taskQueue
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("server执行刚刚在执行普通耗时任务，久等了～～～", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }

            }
        });


        //用户自定义定时任务（模拟耗时）
        //任务提交到scheduledTaskQueue
        ctx.channel().eventLoop().schedule(() -> {
            try {
                Thread.sleep(10 * 1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("server执行刚刚在执行用户自定义定时任务，久等了～～～", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }, 5, TimeUnit.SECONDS);
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~", CharsetUtil.UTF_8));
    }

    //处理异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
