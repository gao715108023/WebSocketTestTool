package com.gcj.wordgame.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by gaochuanjun on 14-1-16.
 */
@ClientEndpoint
public class WordgameClientEndpoint {

    private static final Log LOG = LogFactory.getLog(WordgameClientEndpoint.class);

    private static CountDownLatch latch;

    @OnOpen
    public void onOpen(Session session) {
        LOG.info("Connected ... " + session.getId());
        try {
            session.getBasicRemote().sendText("start");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            LOG.info("Received ...." + message);
            String userInput = bufferRead.readLine();
            return userInput;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        LOG.info(String.format("Session %s close because of %s", session.getId(), closeReason.toString()));
        latch.countDown();
    }

    public static void main(String[] args) {
        latch = new CountDownLatch(1);
        ClientManager client = ClientManager.createClient();
        try {
            client.connectToServer(WordgameClientEndpoint.class, new URI("ws://localhost:8025/websockets/game"));
            latch.await();
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
