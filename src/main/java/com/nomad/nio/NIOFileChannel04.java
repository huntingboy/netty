package com.nomad.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * @author nomad
 * @Description FileChannel使用transferFrom完成文件的拷贝
 * @create 2020-11-10 3:44 PM
 */
public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/pic.png");
        FileChannel srcCh = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/pic_copy.png");
        FileChannel desCh = fileOutputStream.getChannel();

        desCh.transferFrom(srcCh, 0, srcCh.size());
        fileInputStream.close();
        fileOutputStream.close();
    }
}
