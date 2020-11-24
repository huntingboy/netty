package com.nomad.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author nomad
 * @Description Buffer数组的读写操作，即Scattering和Gathering
 * @create 2020-11-10 4:42 PM
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8000);

        serverSocketChannel.socket().bind(inetSocketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLen = 8;
        while (true) {
            //从socket读取数据
            int byteRead = 0;
            while (byteRead < messageLen) {
                long read = socketChannel.read(byteBuffers);
                byteRead += read;
                System.out.println("byteRead = " + byteRead);
                //流打印，看每个buffer的position和limit
                Arrays.asList(byteBuffers).stream().map(byteBuffer -> "position = " +
                        byteBuffer.position() + ", limit = " + byteBuffer.limit()).forEach(System.out::println);
            }

            //将所有buffer进行flip
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());

            //吧数据写到socket
            int byteWrite = 0;
            while (byteWrite < messageLen) {
                long write = socketChannel.write(byteBuffers);
                byteWrite += write;
                System.out.println("byteWrite = " + byteWrite);
            }

            //将所有的buffer进行clear操作，防止一直读0个字节死循环
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());

        }

    }
}
