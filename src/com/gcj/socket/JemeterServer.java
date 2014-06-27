package com.gcj.socket;

/**
 * Created by gaochuanjun on 14-1-15.
 */
public class JemeterServer {

    public static void main(String[] args) {
        AgentAsServer agentAsServer = new AgentAsServer();
        agentAsServer.start();
    }
}