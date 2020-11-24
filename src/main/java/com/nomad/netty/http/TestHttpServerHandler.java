package com.nomad.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author nomad
 * @Description 服务器端管道pipeline里面的处理器
 * @create 2020-11-17 4:52 PM
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //从客户端读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            System.out.println("pipeline hashcode=" + ctx.pipeline().hashCode() +
                    " TestHttpServerHandler hashcode=" + this.hashCode());

            System.out.println("msg类型=" + msg.getClass());
            System.out.println("客户端地址=" + ctx.channel().remoteAddress());

            //回复信息给浏览器[http协议]
            ByteBuf buf = Unpooled.copiedBuffer("hello， 我是服务器", CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK, buf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());

            ctx.writeAndFlush(response);
        }
    }
}
