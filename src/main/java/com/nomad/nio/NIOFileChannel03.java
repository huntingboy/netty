package com.nomad.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author nomad
 * @Description FileChannel用一个ByteBuffer实现文件的拷贝
 * @create 2020-11-10 2:47 PM
 */
public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/filechannel.txt");
        FileChannel fileChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/filechannel_copy.txt");
        FileChannel fileChannel1 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while (true) {
            byteBuffer.clear(); //一定要有，标志位重置（limit->capacity），否则[position, limit)(position==limit)区间读取字节数一直返回0

            int read = fileChannel.read(byteBuffer);
            System.out.println("read = " + read);
            if (read == -1) {
                break;
            }

            byteBuffer.flip(); //读写转换,标志位重置

            fileChannel1.write(byteBuffer);
        }
    }
}
