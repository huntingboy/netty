package com.nomad.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author nomad
 * @Description
 * @create 2020-11-17 4:54 PM
 */
public class TestServerInitializer extends ChannelInitializer {

    //向管道加入处理器
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //往pipeline加入一个netty提供的处理器
        //HttpServerCodec作用：对http响应编码和请求解码
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        //往pipeline加入一个自定义处理器
        pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());
    }
}
