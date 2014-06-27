package com.gcj.wordgame.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.websocket.CloseReason;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.CloseReason.CloseCodes;
import java.io.IOException;

/**
 * Created by gaochuanjun on 14-1-16.
 */
@ServerEndpoint(value = "/game")
public class WordgameServerEndpoint {

    private static final Log LOG = LogFactory.getLog(WordgameServerEndpoint.class);

    private int index = 0;

    @OnOpen
    public void onOpen(Session session) {
        LOG.info("Connected ... " + session.getId());
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        LOG.info("Receive a message: " + message);
        if (message.equalsIgnoreCase("quit")) {
            try {
                session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "Game ended"));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        message += index;
        index++;
        return message;
    }

    public void onClose(Session session, CloseReason closeReason) {
        LOG.info(String.format("Session %s closed because of %s", session.getId(), closeReason.toString()));
    }
}
