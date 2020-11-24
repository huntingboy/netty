package com.nomad.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author nomad
 * @Description FileChannel读文件
 * @create 2020-11-10 2:41 PM
 */
public class NIOFileChannel02 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/filechannel.txt");

        FileChannel fileChannel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        fileChannel.read(byteBuffer);

        System.out.println(new String(byteBuffer.array()));
    }
}
