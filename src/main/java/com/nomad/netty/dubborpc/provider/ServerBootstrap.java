package com.nomad.netty.dubborpc.provider;

import com.nomad.netty.dubborpc.netty.NettyServer;

/**
 * @author nomad
 * @Description NettyServer服务启动类，可以用不同的方式启动一个服务提供者，此处只写了ip:port方式
 * @create 2020-11-24 4:41 PM
 */
public class ServerBootstrap {
    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1", 8000);
    }
}
