package com.gcj.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by gaochuanjun on 14-1-23.
 */
public class ByteUtils {

    private static final Log LOG = LogFactory.getLog(ByteUtils.class);

    public static int byte2Int(byte[] bRefArr) {
        int iOutcome = 0;
        byte bLoop;

        for (int i = 0; i < bRefArr.length; i++) {
            bLoop = bRefArr[i];
            iOutcome += (bLoop & 0xFF) << (8 * i);
        }
        return iOutcome;
    }

    public static void printBytesMsg(byte[] bytes, String info) {
        StringBuffer sb = new StringBuffer();
        sb.append(info).append(":[");
        for (int i = 0; i < bytes.length - 1; i++) {
            sb.append(bytes[i]).append(", ");
        }
        sb.append(bytes[bytes.length - 1]).append("]\n");
        LOG.debug(sb.toString());
    }
}