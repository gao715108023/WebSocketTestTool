package com.gcj.websocket;

import com.gcj.socket.Agent;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketTextListener;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by gaochuanjun on 14-1-13.
 */
public class ClientForNing implements Runnable {

    private static final Log LOG = LogFactory.getLog(ClientForNing.class);

    private AsyncHttpClient asyncHttpClient;

    private boolean initialized = false;

    private WebSocket webSocket;

    private String responseMessage;

    private int thinkTime;

    private int recvTimeout;

    private String msg;

    private volatile boolean RUNNING;

    private String protocol;

    private String domain;

    private String path;

    private String queryString;

    private int port;

    private final CountDownLatch mDoneSignal;

    private final int mThreadIndex;

    private long start;

    private long end;

    private int isFirst = 0;

    public ClientForNing(int thinkTime, int recvTimeout, String msg, final CountDownLatch doneSignal, final int threadIndex) {
        super();
        this.thinkTime = thinkTime;
        this.recvTimeout = recvTimeout;
        this.msg = msg;
        this.mDoneSignal = doneSignal;
        this.mThreadIndex = threadIndex;
        setRUNNING(true);
    }

    public void initialize() throws Exception {
        URI uri = getUri();
        LOG.debug(uri.toString());
        asyncHttpClient = new AsyncHttpClient();
        webSocket = asyncHttpClient.prepareGet(uri.toString()).execute(new WebSocketUpgradeHandler.Builder().addWebSocketListener(new WebSocketTextListener() {

            @Override
            public void onOpen(com.ning.http.client.websocket.WebSocket websocket) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onClose(com.ning.http.client.websocket.WebSocket websocket) {
                // TODO Auto-generated method stub
                LOG.warn("Websocket has cloased!");
            }

            @Override
            public void onError(Throwable t) {
                // TODO Auto-generated method stub
                t.printStackTrace();
            }

            @Override
            public void onMessage(String message) {
                // TODO Auto-generated method stub
                LOG.debug(message);
                responseMessage = message;
            }

//            @Override
//            public void onMessage(ByteBuffer message) {
//                // TODO Auto-generated method stub
//                LOG.debug(message);
//                responseMessage = message;
//            }

            @Override
            public void onFragment(String fragment, boolean last) {
                // TODO Auto-generated method stub

            }
        }).build()).get();
        webSocket.sendTextMessage(msg);
        initialized = true;
    }

    public void job() {
        Agent.samples++;
        start = System.currentTimeMillis();
        if (!initialized) {
            try {
                initialize();
            } catch (Exception e) {
                e.printStackTrace();
                Agent.successful = false;
                Agent.errors++;
                return;
            }
        } else if (webSocket != null) {
            webSocket.sendTextMessage(msg);
        } else {
            closeWebSocket();
            try {
                initialize();
            } catch (Exception e) {
                e.printStackTrace();
                Agent.successful = false;
                Agent.errors++;
                return;
            }
        }
        boolean result = getRecvMsg();
//        if (getRecvMsg()) {
//            Agent.corrects++;
//        }
        if (isFirst > 1 && !result) {
            Agent.errors++;
        }
        addResponseTime();
        return;
    }

    public void offerService() {
        while (RUNNING) {
            job();
            try {
                Thread.sleep(thinkTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        closeWebSocket();
        LOG.info("Thread " + mThreadIndex + " Done. Now:" + System.currentTimeMillis());
        mDoneSignal.countDown();// 完成以后计数减一,计数为0时，主线程接触阻塞，继续执行其他任务
    }

    private boolean getRecvMsg() {
        isFirst++;
        if (responseMessage == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            getRecvMsg();
            end = System.currentTimeMillis();
            if (end >= start) {
                if ((end - start) >= recvTimeout) {
                    return false;
                }
            } else {
                start = System.currentTimeMillis();
            }
        } else {
            return true;
        }
        return false;
    }

    private void addResponseTime() {
        end = System.currentTimeMillis();
        Agent.setResponseTime(end - start);
        Agent.end = end;
    }

    public void closeWebSocket() {
        if (webSocket != null)
            webSocket.close();
        if (asyncHttpClient != null)
            asyncHttpClient.close();
    }

    public URI getUri() {
        String protocol = getProtocol();
        String domain = getDomain();
        int port = getPort();
        String path = getPath();
        String queryString = getQueryString();
        try {
            if (port == 80)
                return new URI(protocol, null, domain, -1, path, queryString, null);
            else
                return new URI(protocol, null, domain, getPort(), path, queryString, null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setRUNNING(boolean RUNNING) {
        this.RUNNING = RUNNING;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        offerService();
    }

    public static void main(String[] args) {
        String msg = "/json/subscribe?/blockinfo/leaf?block=自选股1&source=local&field=&where=&start=1&count=0&response_times=1&response_mode=0&cache_timeout=0&request_timeout=-1&dc_wait_sync=0&ds_wait_sync=2&id=1469&cache_range_expand=0&cache_field_expand=0&request_code=ansi";
        CountDownLatch mDoneSignal = new CountDownLatch(1);
//        ClientForNing clientForNing = new ClientForNing(1, 5000, msg);
//
//        try {
//            clientForNing.initialize();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        clientForNing.offerService();
    }
}
