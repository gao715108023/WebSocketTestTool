package com.gcj.beans;

import java.io.Serializable;

/**
 * Created by gaochuanjun on 14-1-13.
 */
public class SampleBean implements Serializable {

    private String serverIP;
    private int serverPort;
    private String path;
    private String protocol;
    private String contentEncoding;
    private int thinkTime;
    private int recvTimeout;
    private int numbersOfThreads;
    private int rampUpPeriod;
    private int loopCount;
    private int duration;
    private int startupDelay;
    private String remoteHostS;

    public SampleBean(String serverIP, int serverPort, String path, String protocol, String contentEncoding, int thinkTime, int recvTimeout, int numbersOfThreads, int rampUpPeriod, int loopCount, int duration, int startupDelay, String remoteHostS) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.path = path;
        this.protocol = protocol;
        this.contentEncoding = contentEncoding;
        this.thinkTime = thinkTime;
        this.recvTimeout = recvTimeout;
        this.numbersOfThreads = numbersOfThreads;
        this.rampUpPeriod = rampUpPeriod;
        this.loopCount = loopCount;
        this.duration = duration;
        this.startupDelay = startupDelay;
        this.remoteHostS = remoteHostS;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public int getThinkTime() {
        return thinkTime;
    }

    public void setThinkTime(int thinkTime) {
        this.thinkTime = thinkTime;
    }

    public int getRecvTimeout() {
        return recvTimeout;
    }

    public void setRecvTimeout(int recvTimeout) {
        this.recvTimeout = recvTimeout;
    }

    public int getNumbersOfThreads() {
        return numbersOfThreads;
    }

    public void setNumbersOfThreads(int numbersOfThreads) {
        this.numbersOfThreads = numbersOfThreads;
    }

    public int getRampUpPeriod() {
        return rampUpPeriod;
    }

    public void setRampUpPeriod(int rampUpPeriod) {
        this.rampUpPeriod = rampUpPeriod;
    }

    public int getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getStartupDelay() {
        return startupDelay;
    }

    public void setStartupDelay(int startupDelay) {
        this.startupDelay = startupDelay;
    }

    public String getRemoteHostS() {
        return remoteHostS;
    }

    public void setRemoteHostS(String remoteHostS) {
        this.remoteHostS = remoteHostS;
    }
}
