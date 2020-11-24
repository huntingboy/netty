package com.nomad.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author nomad
 * @Description NIO通信客户端 非阻塞
 * @create 2020-11-10 8:51 PM
 */
public class NIOClient {
    public static void main(String[] args) throws Exception {
        //1.创建通道SocketChannel并配置为非阻塞
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        //2.服务器地址设置
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8000);

        //3.连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("连接需要时间，客户端无需阻塞，此处可以用来做其他的事情");
            }
        }

        //链接成功就给服务iq发送消息
        ByteBuffer byteBuffer = ByteBuffer.wrap("hello，服务器，你好呀~".getBytes());
        socketChannel.write(byteBuffer);
        System.in.read(); //停住
    }
}
