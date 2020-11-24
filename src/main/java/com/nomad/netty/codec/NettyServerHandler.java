package com.nomad.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author nomad
 * @Description 管道pipeline中的一个处理器
 * @create 2020-11-17 11:20 AM
 */
//public class NettyServerHandler extends ChannelInboundHandlerAdapter {
public class NettyServerHandler extends SimpleChannelInboundHandler<StudentPOJO.Student> {

    /**
     * 读取客户端发来的数据
     * @param ctx 上下文对象， 含有channel,pipeline（链表，包含多个handler，双向链表）和地址
     * @param msg 客户端发来的数据
     * @throws Exception
     */
    @Override
    //public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    public void channelRead0(ChannelHandlerContext ctx, StudentPOJO.Student msg) throws Exception {
        //读取从客户端发来的StudentPOJO.Student对象
        //StudentPOJO.Student student = (StudentPOJO.Student) msg;
        StudentPOJO.Student student = msg;

        System.out.println("客户端发来的Student.id=" + student.getId());
        System.out.println("客户端发来的Student.name=" + student.getName());
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
