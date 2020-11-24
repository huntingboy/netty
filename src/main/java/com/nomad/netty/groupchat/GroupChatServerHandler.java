package com.nomad.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

/**
 * @author nomad
 * @Description pipeline中的自定义业务处理器handler
 * @create 2020-11-19 2:52 PM
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    //定义一个channel容器，管理所有的channel
    //GlobalEventExecutor.INSTANCE全局事件处理器，单例
    private static ChannelGroup cg = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //表示连接建立，一旦连接，地一个被执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        //将该客户端上线消息广播给其它客户端
        cg.writeAndFlush("[客户端]" + channel.remoteAddress() + " 加入聊天\n");
        cg.add(channel);
    }

    //链接断开，广播给其他客户端
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        cg.writeAndFlush("[客户端]" + channel.remoteAddress() + " 离开了\n");
        System.out.println("cg.size() = " + cg.size());
    }

    //表示活动状态，提示。。。上线了
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了～");
    }

    //表示非活动状态，提示。。。离线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了～");
    }

    //读取客户端发来的数据，广播给其它客户端
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();

        cg.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush("[客户端]" + channel.remoteAddress() + " 发送了消息:" + msg + "\n");
            } else {
                ch.writeAndFlush("[自己]发送了消息:" + msg + "\n");
            }
        });
    }
}
