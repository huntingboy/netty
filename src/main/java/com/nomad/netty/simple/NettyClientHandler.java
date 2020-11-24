package com.nomad.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

/**
 * @author nomad
 * @Description 管道pipeline中的一个处理器
 * @create 2020-11-17 11:46 AM
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client ctx = " + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, server ,hhhh~", CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端读取线程" + Thread.currentThread().getName());
        System.out.println("看看pipeline和channel的关系");//2者相互包含
        ChannelPipeline pipeline = ctx.pipeline(); //本质是一个双向链接
        Channel channel = ctx.channel();

        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器端说：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器地址：" + channel.remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
