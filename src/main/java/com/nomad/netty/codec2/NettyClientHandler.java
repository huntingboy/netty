package com.nomad.netty.codec2;

import com.nomad.netty.codec.StudentPOJO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.io.RandomAccessFile;
import java.util.Random;

/**
 * @author nomad
 * @Description 管道pipeline中的一个处理器
 * @create 2020-11-17 11:46 AM
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //随机发送一个Student/Worker对象到服务器
        int random = new Random().nextInt(3);
        MyDataInfo.MyMessage myMessage = null;

        if (random == 0) { //发送student对象
            myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.StudentType)
                    .setStudent(MyDataInfo.Student.newBuilder().setId(4).setName("李四").build()).build();
        } else {
            myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.WorkerType)
                    .setWorker(MyDataInfo.Worker.newBuilder().setName("工人").setAge(22).build()).build();
        }

        ctx.writeAndFlush(myMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器端说：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器地址：" + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
