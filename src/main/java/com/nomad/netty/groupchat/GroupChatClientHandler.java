package com.nomad.netty.groupchat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author nomad
 * @Description pipeline中的自定义业务处理器handler
 * @create 2020-11-19 3:24 PM
 */
public class GroupChatClientHandler extends SimpleChannelInboundHandler<String> {
    //把服务器响应的消息显示在控制台
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg.trim());
    }
}
