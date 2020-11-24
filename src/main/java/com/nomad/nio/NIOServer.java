package com.nomad.nio;

import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author nomad
 * @Description NIO实现的一个非阻塞服务器端
 * @create 2020-11-10 8:11 PM
 */
public class NIOServer {
    public static void main(String[] args) throws Exception {
        //1.创建selector
        Selector selector = Selector.open();

        //2.创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //3.绑定端口并监听，配置非阻塞
        serverSocketChannel.socket().bind(new InetSocketAddress(8000));
        serverSocketChannel.configureBlocking(false);

        //4.把Channel注册到Selector，关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1s，无感兴趣的事件发生");
                continue;
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();

                if (key.isAcceptable()) { //链接事件
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成一个socketChannel " + socketChannel.hashCode());
                    socketChannel.configureBlocking(false); //配置非阻塞，否则IllegalBlockingModeException
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024)); //注册该通道
                }
                if (key.isReadable()) { //读写事件
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    channel.read(byteBuffer);
                    System.out.println("客户端说：" + new String(byteBuffer.array()));
                }

                //删除当前已经处理的key, 防止重复处理
                keyIterator.remove();
            }
        }
    }
}
