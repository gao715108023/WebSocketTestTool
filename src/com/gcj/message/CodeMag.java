package com.gcj.message;

import com.gcj.utils.IntUtils;
import com.gcj.utils.StringUtils;

import java.nio.ByteBuffer;

/**
 * Created by gaochuanjun on 14-1-16.
 */
public class CodeMag {

    /**
     * 协议报文包头结构
     *
     * @param len  整个数据报文的大小，包括包头，占4个字节
     * @param type 数据报文的类型，占4个字节
     * @return 协议报文包头
     */
    private byte[] getHead(int len, int type) {

        byte[] head = new byte[13];

        //载入包头的版本号
        head[0] = 0x01;  //包头的版本号

        //载入整个数据报文的大小
        byte[] lenByte = IntUtils.int2Byte(len);
        System.arraycopy(lenByte, 0, head, 1, lenByte.length);

        //载入数据报文的类型
        byte[] typeByte = IntUtils.int2Byte(type);
        System.arraycopy(typeByte, 0, head, 1 + lenByte.length, typeByte.length);

        //载入数据报文的标志位
        byte[] flagByte = IntUtils.int2Byte(0);
        System.arraycopy(flagByte, 0, head, 1 + lenByte.length + typeByte.length, flagByte.length);
        return head;
    }

    public ByteBuffer codeMsg(int len, int type, int reqLen, int reqSeq, String url) {
        byte[] resultMsg = new byte[reqLen + 13];
        byte[] head = getHead(len, type);
        System.arraycopy(head, 0, resultMsg, 0, head.length);
        byte[] dmRequest = new byte[reqLen];

        //载入请求号，占4位
        byte[] reqSeqByte = IntUtils.int2Byte(reqSeq);
        System.arraycopy(reqSeqByte, 0, dmRequest, 0, reqSeqByte.length);

        //载入数据的长度，占4位
        byte[] dataLenByte = IntUtils.int2Byte(url.length());
        System.arraycopy(dataLenByte, 0, dmRequest, reqSeqByte.length, dataLenByte.length);

        //载入数据，占url.length()位
        byte[] dataByte = StringUtils.string2Byte(url);
        System.arraycopy(dataByte, 0, dmRequest, reqSeqByte.length + dataLenByte.length, dataByte.length);
        System.arraycopy(dmRequest, 0, resultMsg, head.length, dmRequest.length);
        return ByteBuffer.wrap(resultMsg);
    }

    /**
     * 向服务端发送登录请求，并获取由服务端返回的响应
     *
     * @return 服务端的登录响应
     */
    public ByteBuffer dmLogin() {

        byte[] resultDMLogin = new byte[277];
        byte[] exePathByte = StringUtils.string2Byte(StringUtils.getFixedStr(260));//客户端进程路径，占260个字节
        byte[] reservedByte = StringUtils.string2Byte(StringUtils.getFixedStr(3)); //保留位，占3个字节
        byte[] expectClientIdByte = StringUtils.string2Byte("0"); //期望的客户端ID。 占1个字节
        byte[] headSrc = getHead(277, 0);   //获取协议头部

        //将数据拷贝至dmLogin数组
        byte[] dmLogin = new byte[264];
        System.arraycopy(exePathByte, 0, dmLogin, 0, exePathByte.length);
        System.arraycopy(reservedByte, 0, dmLogin, exePathByte.length, reservedByte.length);
        System.arraycopy(expectClientIdByte, 0, dmLogin, exePathByte.length + reservedByte.length, expectClientIdByte.length);

        System.arraycopy(headSrc, 0, resultDMLogin, 0, headSrc.length);
        System.arraycopy(dmLogin, 0, resultDMLogin, headSrc.length, dmLogin.length);
        //os.write(headSrc);
        //os.write(dmLogin);
        //os.flush();//发送登录请求

        //获取协议头
        //byte[] headDst = new byte[13];
        //is.read(headDst);

//            for (int i = 0; i < headDst.length; i++) {
//                System.err.print(headDst[i] + " ");
//            }
//            System.err.print("\n");

        //byte[] lenByte2 = new byte[4];
        //System.arraycopy(headDst, 1, lenByte2, 0, 4);
//            for (int i = 0; i < lenByte2.length; i++) {
//                System.err.print(lenByte2[i] + " ");
//            }
//            System.err.print("\n");
        //int len2 = ByteUtils.byte2Int(lenByte2);  //获取整个协议包的大小
        //LOG.debug("The entire protocol packet size: " + len2);


        //byte[] receive = new byte[len2 - 13]; //接受整个协议包
        //is.read(receive);
        //analyHeadPackage(headDst);
        //return receive;
        return ByteBuffer.wrap(resultDMLogin);
    }
}
