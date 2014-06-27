package com.gcj.websocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.websocket.*;
import java.io.IOException;

@ClientEndpoint
public class MyClient {
    private static final Log LOG = LogFactory.getLog(MyClient.class);

    @OnOpen
    public void onOpen(Session session) throws IOException {
        session.addMessageHandler(new MessageHandler(

        ) {
        });
        LOG.info("Connected to endpoint: " + session.getBasicRemote());
        LOG.info("WebSocket opened: " + session.getId());
        //session.getBasicRemote().sendText("/json/subscribe?/quote/kline?k_period=1d&where=obj=SH600000.stk&start=-100&count=0&response_times=1&response_mode=0");
    }

    @OnMessage
    public void onMessage(String message) {
        LOG.info(message);
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }
}