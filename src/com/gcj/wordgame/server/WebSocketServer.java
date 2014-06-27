package com.gcj.wordgame.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.tyrus.server.Server;

import javax.websocket.DeploymentException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by gaochuanjun on 14-1-16.
 */
public class WebSocketServer {

    private static final Log LOG = LogFactory.getLog(WebSocketServer.class);

    public static void runServer() {
        Server server = new Server("localhost", 8025, "/websockets", WordgameServerEndpoint.class);
        try {
            server.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            LOG.info("Please press a key to stop the server.");
            reader.readLine();
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }


    public static void main(String[] args) {
        runServer();
    }
}
