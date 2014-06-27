package com.gcj.message;

import com.gcj.utils.IntUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by gaochuanjun on 14-1-23.
 */
public class DeCodeMsg {

    public static byte[] unGZip(byte[] data) {
        byte[] b = null;
        ByteArrayInputStream bis = null;
        GZIPInputStream gzip = null;
        ByteArrayOutputStream baos = null;
        try {
            bis = new ByteArrayInputStream(data);
            gzip = new GZIPInputStream(bis);
            byte[] buf = new byte[1024];
            int num;
            baos = new ByteArrayOutputStream();
            while ((num = gzip.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, num);
            }
            b = baos.toByteArray();
            baos.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (baos != null)
                    baos.close();
                if (gzip != null)
                    gzip.close();
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    /**
     * 压缩GZip
     *
     * @param data
     * @return
     */
    public static byte[] gZip(byte[] data) {
        byte[] b = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(data);
            gzip.finish();
            gzip.close();
            b = bos.toByteArray();
            bos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return b;
    }

    private static void printBytesMsg(byte[] bytes, String info) {
        System.out.print(info);
        for (int i = 0; i < bytes.length - 1; i++) {
            System.out.print(bytes[i] + ", ");
        }
        System.out.print(bytes[bytes.length - 1] + "]\n");
    }

    public static void main(String[] args) {
        int n = 4;
        byte[] bytes = IntUtils.int2Byte(n);
        printBytesMsg(bytes, "未经gzip压缩前的值为：[");
        byte[] gzipBytes = gZip(bytes);
        printBytesMsg(bytes, "经过gzip压缩后的值为：[");
        byte[] unGzipBytes = unGZip(bytes);
        printBytesMsg(bytes, "解压缩后的值为：[");
    }
}