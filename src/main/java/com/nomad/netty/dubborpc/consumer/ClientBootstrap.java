package com.nomad.netty.dubborpc.consumer;

import com.nomad.netty.dubborpc.netty.NettyClient;
import com.nomad.netty.dubborpc.publicinterface.HelloService;

/**
 * @author nomad
 * @Description 客户端启动类，启动一个服务消费者
 * @create 2020-11-24 5:40 PM
 */
public class ClientBootstrap {
    //自定义一个协议头
    private static final String providerName = "HelloService#hello#";

    public static void main(String[] args) throws Exception {

        //创建一个消费者
        NettyClient consumer = new NettyClient();

        //创建代理对象
        HelloService service = (HelloService) consumer.getBean(HelloService.class, providerName);

        for (; ; ) {
            Thread.sleep(5 * 1000);
            //通过代理对象调用服务提供者的方法
            String res = service.hello("你好，dubob");
            System.out.println("调用的结果 res=" + res);
        }
    }
}
