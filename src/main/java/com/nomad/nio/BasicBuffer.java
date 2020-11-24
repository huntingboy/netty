package com.nomad.nio;

import java.nio.IntBuffer;

/**
 * @author nomad
 * @Description Buffer组件使用
 * @create 2020-11-09 10:36 PM
 */
public class BasicBuffer {
    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(10); //HeapIntBuffer, 可以放10个int整数, capacity（不变）->10, limit->10

        for (int i = 0; i < 10; i++) {
            intBuffer.put(i * 2); //不同于put(index, i)(绝对位置修改),这种方式会不断递增1个标志位（position）
        }

        intBuffer.flip(); //4个中的3个标志位重置(capacity不变)  limit->position, position->0,mark->-1，[position, limit)可访问

        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get()); //不同与get(index, i)（绝对位置取值），这种方式会不断递增1个标志位（position）
        }
    }
}
