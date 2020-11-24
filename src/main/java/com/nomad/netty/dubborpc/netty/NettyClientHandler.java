package com.nomad.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @author nomad
 * @Description NettyClient入站处理器，发送请求，得到rpc结果，同时也是一个Task，被提交时执行run方法
 * @create 2020-11-24 5:03 PM
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;
    private String result; //服务器返回的结果
    private String para; //客户端发送的参数

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        notify(); //唤醒等待的线程
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }

    //被代理对象调用,设置参数，发送数据给服务器->wait->被唤醒->返回结果
    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(para);
        wait();
        return result;
    }

    public void setPara(String para) {
        this.para = para;
    }
}
