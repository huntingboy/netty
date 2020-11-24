package com.nomad.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author nomad
 * @Description FileChannel写文件
 * @create 2020-11-09 11:26 PM
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/filechannel.txt");

        FileChannel fileChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        byteBuffer.put("hello，测试！！".getBytes());

        byteBuffer.flip(); //标志位重置

        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
