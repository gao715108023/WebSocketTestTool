package com.gcj.utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by gaochuanjun on 14-1-16.
 */
public class StringUtils {

    private static String CODE = "gbk";

    public static byte[] string2Byte(String str) {
        try {
            return str.getBytes(CODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); // To change body of catch statement use File |
            // Settings | File Templates.
        }
        return new byte[0];
    }

    public static String getFixedStr(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(0);
        }
        return sb.toString();
    }
}
