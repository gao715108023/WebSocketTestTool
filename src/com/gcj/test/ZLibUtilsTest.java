package com.gcj.test;

import com.gcj.utils.ZLibUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by gaochuanjun on 14-1-23.
 */
public class ZLibUtilsTest {
    @Test
    public final void testBytes() {
        System.err.println("字节压缩／解压缩测试");
        String inputStr = "snowolf@zlex.org;dongliang@zlex.org;zlex.dongliang@zlex.org";
        System.err.println("输入字符串:\t" + inputStr);
        byte[] input = inputStr.getBytes();
        System.err.println("输入字节长度:\t" + input.length);

        byte[] data = ZLibUtils.compress(input);
        System.err.println("压缩后字节长度:\t" + data.length);

        byte[] output = ZLibUtils.decompress(data);
        System.err.println("解压缩后字节长度:\t" + output.length);
        String outputStr = new String(output);
        System.err.println("输出字符串:\t" + outputStr);

        //
        // assertEquals(inputStr, outputStr);
    }

    @Test
    public final void testFile() {
        String filename = "zlib";
        File file = new File(filename);
        System.err.println("文件压缩／解压缩测试");
        String inputStr = "snowolf@zlex.org;dongliang@zlex.org;zlex.dongliang@zlex.org";
        System.err.println("输入字符串:\t" + inputStr);
        byte[] input = inputStr.getBytes();
        System.err.println("输入字节长度:\t" + input.length);

        try {

            FileOutputStream fos = new FileOutputStream(file);
            ZLibUtils.compress(input, fos);
            fos.close();
            System.err.println("压缩后字节长度:\t" + file.length());
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] output = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            output = ZLibUtils.decompress(fis);
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("解压缩后字节长度:\t" + output.length);
        String outputStr = new String(output);
        System.err.println("输出字符串:\t" + outputStr);

        //assertEquals(inputStr, outputStr);
    }

}
