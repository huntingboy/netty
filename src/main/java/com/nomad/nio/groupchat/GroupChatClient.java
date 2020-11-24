package com.nomad.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author nomad
 * @Description NIO实现的群聊系统客户端
 * @create 2020-11-10 10:01 PM
 */
public class GroupChatClient {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8000;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient () throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);

        System.out.println(username + " 已经连上服务器");
    }

    //向服务器发送信息
    public void sendInfo(String info) {
        info = username + "说:" + info;

        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取服务器的消息
    public void readInfo(){
        try {
            int count = selector.select(1000);
            if (count > 0) {
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        socketChannel.read(byteBuffer);
                        System.out.println(new String(byteBuffer.array()));
                    }

                    keyIterator.remove();
                }
            } else {
                //System.out.println("没有可用的通道.......");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        GroupChatClient chatClient = new GroupChatClient();

        new Thread(){
            @Override
            public void run() {
                while (true) {
                    chatClient.readInfo();
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String msg = sc.nextLine();
            chatClient.sendInfo(msg);
        }
    }
}
