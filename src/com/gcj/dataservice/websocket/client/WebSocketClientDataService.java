package com.gcj.dataservice.websocket.client;

import com.gcj.common.JabberServer;
import com.gcj.message.CodeMag;
import com.gcj.utils.ByteUtils;
import com.gcj.utils.ZLibUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.websocket.*;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by gaochuanjun on 14-1-16.
 */
@ClientEndpoint
public class WebSocketClientDataService {

    private static final Log LOG = LogFactory.getLog(WebSocketClientDataService.class);

    private String sendMsg;

    private CodeMag codeMag = new CodeMag();

    private int reqSeq = 0;

    private int thinkTime = 1000;

    private int idleTimeout = 5000;

    private ByteBuffer sendMsgByte;

    private boolean isFirst;

    private boolean isPush = false;

    private long start;

    private long end;

    private long spendTime;

    @OnOpen
    public void onOpen(Session session) {

        LOG.debug("Connected: " + session.getId());

        sendMsg = JabberServer.contentList.get(JabberServer.pos % JabberServer.contentSize);
        JabberServer.pos++;
        if (sendMsg == null) {
            LOG.error("sendMsg is null!");
            return;
        }
        if (sendMsg.contains("response_times=-1")) {
            isPush = true;
        }
        session.setMaxIdleTimeout(idleTimeout);
        LOG.debug("SendMsg-->" + sendMsg);
        //int maxBufferSize = session.getMaxBinaryMessageBufferSize();
        //LOG.info("Max Buffer Size: " + maxBufferSize);
        //ByteBuffer dmLogin = codeMag.dmLogin();
        try {
            //session.getBasicRemote().sendBinary(dmLogin);
            Statistics.samplesForCount++;
            start = System.currentTimeMillis();
            session.getBasicRemote().sendText(sendMsg);
        } catch (IOException e) {
            e.printStackTrace();
            Statistics.errorCount++;
        }
        isFirst = true;
    }

    @OnMessage
    public void onMessage(ByteBuffer message, Session session) {

        LOG.debug(Thread.currentThread().getName() + "-->" + message);
        reqSeq++;
        LOG.debug("ReqSeq: " + reqSeq);
        message.clear();
        try {
            if (isPush) {
                if (isFirst) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    isFirst = false;
                } else {
                    end = System.currentTimeMillis();//此处如果发生idleTimeout，则不会执行
                    spendTime = end - start;
                    if (spendTime < 0) {
                        LOG.error("SpendTime统计错误，出现小于0的情况！");
                    }
                    Statistics.samplesForTime++;
                    Statistics.totalSpendTime += spendTime;
                    Statistics.setMaxResponseTime(spendTime);
                    try {
                        Thread.sleep(thinkTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    start = System.currentTimeMillis();
                }
            } else {
                //sendMsgByte = codeMag.codeMsg(sendMsg.length() + 21, 1, sendMsg.length() + 8, reqSeq, sendMsg);
                //reqSeq++;
                //session.getBasicRemote().sendBinary(sendMsgByte);
                if (reqSeq % 2 == 0) {
                    end = System.currentTimeMillis();//此处如果发生idleTimeout，则不会执行
                    spendTime = end - start;
                    if (spendTime < 0) {
                        LOG.error("SpendTime统计错误，出现小于0的情况！");
                    }
                    Statistics.samplesForTime++;
                    Statistics.totalSpendTime += spendTime;
                    Statistics.setMaxResponseTime(spendTime);
                    try {
                        Thread.sleep(thinkTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Statistics.samplesForCount++;
                    start = System.currentTimeMillis();
                    session.getBasicRemote().sendText(sendMsg);
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            Statistics.errorCount++;
            e.printStackTrace();
        }
    }

//    @OnMessage
//    public void onMessage(String message, Session session) {
//        LOG.debug(Thread.currentThread().getName() + "-->" + message);
//        try {
//            if (isPush) {
//                if (isFirst) {
//                    //sendMsgByte = codeMag.codeMsg(sendMsg.length() + 21, 1, sendMsg.length() + 8, reqSeq, sendMsg);
//                    //session.getBasicRemote().sendBinary(sendMsgByte);
//                    //session.getBasicRemote().sendText(sendMsg);
//                    isFirst = false;
//                }
//            } else {
//                //sendMsgByte = codeMag.codeMsg(sendMsg.length() + 21, 1, sendMsg.length() + 8, reqSeq, sendMsg);
//                //reqSeq++;
//                //session.getBasicRemote().sendBinary(sendMsgByte);
//                session.getBasicRemote().sendText(sendMsg);
//                try {
//                    Thread.sleep(thinkTime);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        LOG.warn(String.format("Session %s close because of %s", session.getId(), closeReason.toString()));
        try {
            if (session != null)
                session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stopServer();
    }

    private void processMsg(ByteBuffer byteBuffer) {
        byte[] bytes = byteBuffer.array();
        byte[] data = new byte[bytes.length - 1];
        System.arraycopy(bytes, 1, data, 0, bytes.length - 1);
        if (bytes[0] == 0x01) {
            LOG.debug("该数据经过zlib压缩，请先解压缩再反解出来！");
            ByteUtils.printBytesMsg(bytes, "解压前的值为");
            byte[] unZip = ZLibUtils.uncompress(data);
            ByteUtils.printBytesMsg(unZip, "解压后的值为");
        } else if (bytes[0] == 0x00) {
            LOG.debug("该数据未经过压缩，可直接反解出来！");
            ByteUtils.printBytesMsg(bytes, "接受到的byte数组为");
        } else {
            LOG.error("数据接受错误，第一个字节一定为0x00或者0x01!");
        }
    }

    public void stopServer() {
        WebSocketClientMain.mDoneSignal.countDown();
    }
}