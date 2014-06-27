package com.gcj.dataservice.websocket.client;

import com.gcj.common.JabberServer;
import com.gcj.message.CodeMag;
import com.gcj.socket.Agent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by gaochuanjun on 14-1-28.
 */
public class AsynSendMsg implements Runnable {

    private static final Log LOG = LogFactory.getLog(AsynSendMsg.class);

    private Session session;

    private volatile boolean running = true;

    private boolean isFirst;

    private String sendMsg;

    private ByteBuffer sendMsgByte;

    private CodeMag codeMag;

    private int reqSeq = 1;

    private int thinkTime = 1000;

    private long start;

    private long end;

    private long spendTime;

    public AsynSendMsg(Session session, String sendMsg, CodeMag codeMag) {
        super();
        this.session = session;
        this.sendMsg = sendMsg;
        this.codeMag = codeMag;
    }

    public void sendMsg() {
        while (running) {
            if (JabberServer.isPush) {
                if (isFirst) {
                    LOG.info("推送url: " + sendMsg);
                    int reqLen = sendMsg.length() + 8;
                    int totalLen = 13 + reqLen;
                    sendMsgByte = codeMag.codeMsg(totalLen, 1, reqLen, reqSeq, sendMsg);
                    session.getAsyncRemote().sendBinary(sendMsgByte);
                    reqSeq++;
                    Agent.samples++;
                    isFirst = false;
                }
            } else {
                if (isFirst) {
                    LOG.info("请求url: " + sendMsg);
                    isFirst = false;
                }
                sendMsgByte = codeMag.codeMsg(sendMsg.length() + 21, 1, sendMsg.length() + 8, reqSeq, sendMsg);
                try {
                    Thread.sleep(thinkTime);
                    session.getBasicRemote().sendBinary(sendMsgByte);
                    LOG.info("[" + reqSeq + "]" + sendMsg);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reqSeq++;
                Agent.samples++;
            }
        }
    }

    @Override
    public void run() {
        sendMsg();
    }
}