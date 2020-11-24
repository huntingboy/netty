package com.nomad.netty.dubborpc.netty;

import com.nomad.netty.dubborpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author nomad
 * @Description NettyServer入站处理器，rpc具体业务处理
 * @create 2020-11-24 4:53 PM
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息，并调用相应的服务函数处理
        System.out.println("msg = " + msg);
        //客户端调用api时，需要定义一个协议
        //e.g. 每次发送消息都必须以某个字符串开头 ”HelloService#hello#****“
        if (msg.toString().startsWith("HelloService#hello#")) {
            String result = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}

