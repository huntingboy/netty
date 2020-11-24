package com.nomad.netty.dubborpc.publicinterface;

/**
 * @author nomad
 * @Description 接口，服务提供方和服务消费方都需要
 * @create 2020-11-24 4:34 PM
 */
public interface HelloService {
    String hello(String msg);
}
