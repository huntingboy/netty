package com.nomad.nio;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author nomad
 * @Description 直接内存测试DirectByteBuffer是MappedByteBuffer抽象类的实现
 * @create 2020-11-10 5:14 PM
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("src/main/resources/filechannel.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        /**
         * 文件映射到直接内存，避免一次拷贝
         * 读写模式
         * 起始位置
         * 字节数
         */
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte) 'A');
        mappedByteBuffer.put(3, (byte) '好');
        System.out.println("修改完成");
        randomAccessFile.close();
    }
}
