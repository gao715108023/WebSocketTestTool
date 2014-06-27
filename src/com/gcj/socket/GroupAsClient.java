package com.gcj.socket;

import com.gcj.beans.SampleBean;
import com.gcj.utils.ConfigUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by gaochuanjun on 14-1-13.
 */
public class GroupAsClient {

    private static final Log LOG = LogFactory.getLog(GroupAsClient.class);

    public static final String SERVER_IP = "server_ip";
    public static final String SERVER_PORT = "server_port";
    public static final String PATH = "path";
    public static final String PROTOCOL = "protocol";
    public static final String CONTENT_ENCODING = "content_encoding";
    public static final String THINK_TIME = "think_time";
    public static final String RECV_TIMEOUT = "recv_timeout";
    public static final String NUMBERS_OF_THREADS = "numbers_of_threads";
    public static final String RAMP_UP_PERIOD = "ramp_up_period";
    public static final String LOOP_COUNT = "loop_count";
    public static final String DURATION = "duration";
    public static final String STARTUP_DELAY = "startup_delay";
    public static final String REMOTE_HOSTS = "remote_hosts";
    public static final String CONFIGFILEPATH = "infoconfig.properties";
    public static final String LOCALHOSTS = "local_hosts";
    public static final int UNIT = 1000;

    private String remoteHosts;

    public void start() {
        ConfigUtils conf = new ConfigUtils(CONFIGFILEPATH);
        String serverIP = conf.getString(SERVER_IP);
        int serverPort = conf.getInt(SERVER_PORT);
        String path = conf.getString(PATH);
        String protocol = conf.getString(PROTOCOL);
        String contentEncoding = conf.getString(CONTENT_ENCODING);
        int thinkTime = conf.getInt(THINK_TIME) * UNIT;
        int recvTimeout = conf.getInt(RECV_TIMEOUT) * UNIT;
        int numbersOfThreads = conf.getInt(NUMBERS_OF_THREADS);
        int rampUpPeriod = conf.getInt(RAMP_UP_PERIOD) * UNIT;
        int loopCount = conf.getInt(LOOP_COUNT);
        int duration = conf.getInt(DURATION) * UNIT;
        int startupDelay = conf.getInt(STARTUP_DELAY) * UNIT;
        String localHosts = conf.getString(LOCALHOSTS);
        remoteHosts = conf.getString(REMOTE_HOSTS);
        LOG.info("数据层或者nginx的IP地址: " + serverIP);
        LOG.info("数据层或者nginx提供的websocket协议端口号: " + serverPort);
        LOG.info("数据层或者nginx提供的供websocket协议的路径: " + path);
        LOG.info("数据层或者nginx使用的协议，默认为ws: " + protocol);
        LOG.info("websocket协议之间信息传递的编码方式: " + contentEncoding);
        LOG.info("性能测试的思考时间: " + thinkTime + "ms");
        LOG.info("性能测试的请求超时时间: " + recvTimeout + "ms");
        LOG.info("线程数（即并发用户数）: " + numbersOfThreads);
        LOG.info("线程启动时间: " + rampUpPeriod + "ms");
        LOG.info("线程循环次数: " + loopCount);
        LOG.info("线程持续时间: " + duration + "ms");
        LOG.info("线程在设置的时间之后启动: " + startupDelay + "ms");
        LOG.info("本地jmeter的IP地址和端口号: " + localHosts);
        LOG.info("远程Jmeter服务的IP地址和端口号，端口默认为1099: " + remoteHosts);
        String[] array = remoteHosts.split(",");
        LOG.info("开启服务线程，用来接受来自远程Jmeter的监控信息......");
        new Thread(new Group(array.length, localHosts, duration)).start();
        SampleBean sampleBean = new SampleBean(serverIP, serverPort, path, protocol, contentEncoding, thinkTime, recvTimeout, numbersOfThreads, rampUpPeriod, loopCount, duration, startupDelay, localHosts);
        connectToServer(sampleBean);
    }

    public void connectToServer(SampleBean sampleBean) {

        String[] array = remoteHosts.split(",");
        for (int i = 0; i < array.length; i++) {
            String[] array1 = array[i].split(":");
            Socket socket = null;
            ObjectOutputStream oos = null;
            try {
                socket = new Socket(array1[0], Integer.parseInt(array1[1]));
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(sampleBean);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (oos != null)
                        oos.close();
                    if (socket != null)
                        socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        GroupAsClient groupAsClient = new GroupAsClient();
        groupAsClient.start();
    }
}
