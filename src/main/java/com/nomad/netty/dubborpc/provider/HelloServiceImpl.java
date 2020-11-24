package com.nomad.netty.dubborpc.provider;

import com.nomad.netty.dubborpc.publicinterface.HelloService;

/**
 * @author nomad
 * @Description 当有消费方调用该方法时，就返回一个结果
 * @create 2020-11-24 4:37 PM
 */
public class HelloServiceImpl  implements HelloService {

    private static int count;

    @Override
    public String hello(String msg) {
        System.out.println("收到客户端消息=" + msg + "第"+(++count)+"次");
        if (msg != null) {
            return "你好客户端，我已经收到你的消息[" + msg + "]";
        } else {
            return "你好客户端，我已经收到你的消息";
        }
    }
}
