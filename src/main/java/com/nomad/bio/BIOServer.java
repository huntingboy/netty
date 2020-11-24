package com.nomad.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author nomad
 * @Description BIO服务器
 * @create 2020-11-06 9:25 PM
 */
public class BIOServer {
    public static final int PORT = 6666;
    public static final ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(PORT);
        System.out.println("BIO服务器启动了");

        while (true) {
            System.out.println("线程Id = " + Thread.currentThread().getId() +
                    "线程名字 = " + Thread.currentThread().getName());
            System.out.println("等待连接。。。");
            final Socket client = ss.accept();
            System.out.println("连接到一个客户端");

            newCachedThreadPool.execute(() -> handler(client));

        }
    }

    private static void handler(Socket client) {
        System.out.println("线程Id = " + Thread.currentThread().getId() +
                "线程名 = " + Thread.currentThread().getName());
        byte[] bytes = new byte[1024];
        try {
            InputStream inputStream = client.getInputStream();
            while (true) {
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
