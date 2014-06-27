package com.gcj.websocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.URI;
import javax.websocket.*;

public class App {

    private static final Log LOG = LogFactory.getLog(App.class);

    public Session session;

    protected void start() throws IOException, DeploymentException, InterruptedException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String uri = "wss://10.15.107.147:80/dataproxy";
        LOG.info("Connecting to " + uri);
        session = container.connectToServer(MyClient.class, URI.create(uri));
        String url = "/json/subscribe?/quote/kline?k_period=1d&where=obj=SH600000.stk&start=-100&count=0&response_times=1&response_mode=0";
        while (true) {
            session.getBasicRemote().sendText(url);
            //LOG.info("Sending info: /quote/kline?k_period=1d&where=obj=SH600000.stk&start=-100&count=0&response_times=1&response_mode=0");
            //client.session.getBasicRemote().sendText(url);
            //LOG.info("Sending info: /quote/kline?k_period=1d&where=obj=SH600000.stk&start=-100&count=0&response_times=1&response_mode=0");
            Thread.sleep(1000);
        }
    }

    public static void main(String args[]) throws IOException, InterruptedException, DeploymentException {
        App client = new App();
        client.start();
//        String url = "/json/subscribe?/quote/kline?k_period=1d&where=obj=SH600000.stk&start=-100&count=0&response_times=1&response_mode=0";
//        while (true) {
//            client.session.getBasicRemote().sendText(url);
//            //LOG.info("Sending info: /quote/kline?k_period=1d&where=obj=SH600000.stk&start=-100&count=0&response_times=1&response_mode=0");
//            //client.session.getBasicRemote().sendText(url);
//            //LOG.info("Sending info: /quote/kline?k_period=1d&where=obj=SH600000.stk&start=-100&count=0&response_times=1&response_mode=0");
//            Thread.sleep(1000);
//        }
    }
}