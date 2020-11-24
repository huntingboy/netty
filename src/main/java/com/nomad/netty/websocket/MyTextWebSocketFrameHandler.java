package com.nomad.netty.websocket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutorGroup;

import java.time.LocalDateTime;

/**
 * @author nomad
 * @Description netty ws示例服务器中pipeline里的TextWebSocketFrame处理器
 * @create 2020-11-19 5:19 PM
 */
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器收到消息：" + msg.text());

        //回复浏览器消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间" + LocalDateTime.now() + "\n" + msg.text()));
    }

    //web浏览器链接后
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded被调用" + ctx.channel().id().asLongText());
        System.out.println("handlerAdded被调用" + ctx.channel().id().asShortText());
    }

    //web浏览器断开后
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved被调用" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生" + cause.getMessage());
        ctx.close();
    }
}
