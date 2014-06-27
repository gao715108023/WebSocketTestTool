package com.gcj.utils;

/**
 * Created by gaochuanjun on 14-2-17.
 */
public class DoubleUtils {

    public static double convert(double value) {
        long l1 = Math.round(value * 100);
        double ret = l1 / 100.0;
        return ret;
    }

    public static void main(String[] args) {
        System.out.println(convert(3));
    }
}
