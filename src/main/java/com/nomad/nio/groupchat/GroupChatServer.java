package com.nomad.nio.groupchat;

import com.sun.corba.se.impl.oa.poa.POAImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author nomad
 * @Description NIO实现的群聊服务器端, 对NIOServer的部分修改
 * @create 2020-11-10 9:25 PM
 */
public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 8000;

    public GroupChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen(){
        try {
            while (true) {
                int count = selector.select(1000);
                if (count > 0) {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();

                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            System.out.println(socketChannel.getRemoteAddress() + "上线 ");
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        }
                        if (key.isReadable()) {
                            readData(key);
                        }

                        keyIterator.remove();
                    }
                } else {
                    System.out.println("等待事件的发生。。。");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    //读取客户读信息
    private void readData(SelectionKey key) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int read = socketChannel.read(byteBuffer);
            if (read > 0) {
                String msg = new String(byteBuffer.array());
                System.out.println("客户端说:" + msg);

                //向其它客户端广播该条消息
                sendInfoToOtherClients(msg, socketChannel);
            }
        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress() + " 离线了 .. ");
                //取消注册
                key.cancel();
                //关闭通道
                socketChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    //向其它客户端广播该条消息
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器广播消息中。。。");

        for (SelectionKey key : selector.keys()) {
            Channel targetChannel = key.channel();

            //排除自己,，广播消息
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                SocketChannel socketChannel = (SocketChannel) targetChannel;
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                socketChannel.write(byteBuffer);
            }
        }
    }


    public static void main(String[] args) {
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }
}
