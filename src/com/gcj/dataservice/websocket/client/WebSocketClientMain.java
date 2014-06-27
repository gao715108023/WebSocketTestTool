package com.gcj.dataservice.websocket.client;

import com.gcj.common.JabberServer;
import com.gcj.utils.ConfigUtils;
import com.gcj.utils.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by gaochuanjun on 14-1-23.
 */
public class WebSocketClientMain implements Runnable {

    private static final Log LOG = LogFactory.getLog(WebSocketClientMain.class);

    protected static CountDownLatch mDoneSignal;

    private String url;

    private volatile boolean running = true;

    public WebSocketClientMain(String url) {
        super();
        this.url = url;
    }

    @Override
    public void run() {
        ClientManager client = ClientManager.createClient();
        try {
            while (running) {
                mDoneSignal = new CountDownLatch(1);
                client.connectToServer(WebSocketClientDataService.class, new URI(url));
                mDoneSignal.await();
            }
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ConfigUtils conf = new ConfigUtils("infoconfig.properties");
        String serverIP = conf.getString("server_ip");
        int port = conf.getInt("server_port");
        String path = conf.getString("path");
        String protocol = conf.getString("protocol");
        String contentEncoding = conf.getString("content_encoding");
        String url = protocol + "://" + serverIP + ":" + port + path;
        LOG.info("数据层的IP地址：" + serverIP);
        LOG.info("数据层WebSocket协议暴露的端口号：" + port);
        LOG.info("WebSocket协议的路径：" + path);
        LOG.info("WebSocket所使用的通信协议：" + protocol);
        LOG.info("读取文件所采用的编码格式：" + contentEncoding);
        LOG.info("WebSocket协议的url格式：" + url);
        if (args == null || args.length == 0) {
            LOG.error("参数错误，请输入参数！");
            System.exit(0);
        } else if (args.length == 2) {
            JabberServer.contentList = FileUtils.getUrlVectorFromFile(args[0], contentEncoding);
            JabberServer.contentSize = JabberServer.contentList.size();
            LOG.info("共有" + JabberServer.contentSize + "条记录！");
            for (int i = 0; i < JabberServer.contentSize; i++) {
                LOG.info(JabberServer.contentList.get(i));
            }
            if (Integer.parseInt(args[1]) == -1) {
                JabberServer.isPush = true;
            } else if (Integer.parseInt(args[1]) == 1) {
                JabberServer.isPush = false;
            } else {
                LOG.error("参数错误，第二个参数为1或者-1！");
                System.exit(0);
            }
            new Thread(new WebSocketClientMain(url)).start();
        } else {
            JabberServer.contentList = FileUtils.getUrlVectorFromFile(args[0], contentEncoding, true);
            JabberServer.contentSize = JabberServer.contentList.size();
            if (Integer.parseInt(args[2]) == -1) {
                JabberServer.isPush = true;
            } else if (Integer.parseInt(args[2]) == 1) {
                JabberServer.isPush = false;
            } else {
                LOG.error("参数错误，第三个参数为1或者-1！");
                System.exit(0);
            }
            int threadNum = Integer.parseInt(args[1]);

            Statistics statistics = new Statistics(threadNum);
            Thread statisticsThread = new Thread(statistics);
            statisticsThread.setDaemon(true);
            statisticsThread.start();

            for (int i = 0; i < threadNum; i++) {
                WebSocketClientMain webSocketClientMain = new WebSocketClientMain(url);
                Thread t = new Thread(webSocketClientMain);
                t.setName("Thread " + i);
                t.start();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}