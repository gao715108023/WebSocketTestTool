package com.gcj.test;

import com.gcj.utils.IntUtils;

import java.nio.ByteBuffer;

/**
 * Created by gaochuanjun on 14-1-23.
 */
public class TestByteBuffer {

    public static void main(String[] args) {
        int n = 4;
        byte[] bytes = IntUtils.int2Byte(n);
        System.out.print("原始byte数组为：[");
        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + ", ");
        }
        System.out.print("]\n");
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byte[] bytes1 = byteBuffer.array();
        System.out.print("新的byte数组为：[");
        for (int i = 0; i < bytes1.length; i++) {
            System.out.print(bytes1[i] + ", ");
        }
        System.out.print("]\n");
    }
}
