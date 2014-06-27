package com.gcj.utils;

/**
 * Created by gaochuanjun on 14-1-16.
 */
public class IntUtils {
    public static byte[] int2Byte(int i) {
        byte[] result = new byte[4];
        result[3] = (byte) ((i >> 24) & 0xFF);
        result[2] = (byte) ((i >> 16) & 0xFF);
        result[1] = (byte) ((i >> 8) & 0xFF);
        result[0] = (byte) (i & 0xFF);
        return result;
    }
}
