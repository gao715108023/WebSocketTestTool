package com.gcj.socket;

import com.gcj.utils.ConfigUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by gaochuanjun on 14-1-13.
 */
public class AgentAsServer {

    private static final Log LOG = LogFactory.getLog(AgentAsServer.class);

    private ServerSocket serverSocket;

    private String remoteHostS;

    public static final String CONFIGFILEPATH = "infoconfig.properties";

    public static final String LOCALHOSTS = "local_hosts";

    public void start() {

        try {
            serverSocket = new ServerSocket(1099);
            LOG.info("Server is listening on port 1099[Waiting for client connection]: starting");

            while (true) {
                Socket socket = serverSocket.accept();
                startSampleResult(socket);
                LOG.info("Accept a connection from " + socket.getRemoteSocketAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startSampleResult(Socket socket) {
        ConfigUtils conf = new ConfigUtils(CONFIGFILEPATH);
        remoteHostS = conf.getString(LOCALHOSTS);
        Agent agent = new Agent(remoteHostS, socket);
        new Thread(agent).start();
    }

    public static void main(String[] args) {
        AgentAsServer agentAsServer = new AgentAsServer();
        agentAsServer.start();
    }
}
