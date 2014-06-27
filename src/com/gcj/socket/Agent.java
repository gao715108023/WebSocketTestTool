package com.gcj.socket;

import com.gcj.beans.SampleBean;
import com.gcj.common.JabberServer;
import com.gcj.dataservice.websocket.client.WebSocketClientDataService;
import com.gcj.utils.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gaochuanjun on 14-1-13.
 */
public class Agent implements Runnable {

    private static final Log LOG = LogFactory.getLog(Agent.class);

    public static boolean successful;

    public static long start;

    public static long end;

    public static long responseTime;

    public static double samples = 0;

    //public static double corrects = 0;

    public static double errors = 0;

    public static long minResponseTime = Long.MAX_VALUE;

    public static long maxResponseTime = Long.MIN_VALUE;

    private BufferedReader br = null;

    private PrintWriter pw = null;

    private ObjectInputStream ois;

    private Socket socket;

    private String remoteHostS;

    protected static final int SLEEP_TIME = 3000;

    public static volatile boolean RUNNING = false;

    protected static final String FLAG = ";";

    private ExecutorService executorService;

    protected static final String URLFILEPATH = "url-100r-clopen.dat";

    //public final List<ClientForNing> clientForNingList = new ArrayList<ClientForNing>();
    public final List<WebSocketClientDataService> webSocketClientDataServiceList = new ArrayList<WebSocketClientDataService>();

    private CountDownLatch mDoneSignal;

    public Agent(String remoteHostS) {
        super();
        this.remoteHostS = remoteHostS;
    }

    public Agent(String remoteHostS, Socket socket) {
        super();
        this.remoteHostS = remoteHostS;
        this.socket = socket;
    }

    public void recvConnFromClient() {
        try {
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doMsgFromClient() {
        try {
            SampleBean sampleBean = (SampleBean) ois.readObject();
            LOG.info("Receive a message from " + socket.getRemoteSocketAddress() + ": " + sampleBean);
            LOG.info("Reading the file of " + URLFILEPATH);
            //List<String> urlList = FileUtils.getUrlListFromFile(URLFILEPATH, sampleBean.getContentEncoding());

            JabberServer.contentList = FileUtils.getUrlVectorFromFile(URLFILEPATH, sampleBean.getContentEncoding());
            JabberServer.contentSize = JabberServer.contentList.size();
            //int size = JabberServer.contentList.size();
            LOG.info("Total URL: " + JabberServer.contentSize);
            executorService = Executors.newFixedThreadPool(sampleBean.getNumbersOfThreads());
            mDoneSignal = new CountDownLatch(sampleBean.getNumbersOfThreads());
            for (int i = 0; i < sampleBean.getNumbersOfThreads(); i++) {
//                ClientForNing clientForNing = new ClientForNing(sampleBean.getThinkTime(), sampleBean.getRecvTimeout(), urlList.get(i % size), mDoneSignal, i);
//                clientForNing.setDomain(sampleBean.getServerIP());
//                clientForNing.setPort(sampleBean.getServerPort());
//                clientForNing.setPath(sampleBean.getPath());
//                clientForNing.setProtocol(sampleBean.getProtocol());
//                clientForNing.setQueryString("");
//                clientForNingList.add(clientForNing);
//                executorService.execute(clientForNing);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null)
                    ois.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sampleStart() {
        start = System.currentTimeMillis();
        RUNNING = true;
        LOG.info("Start Test: " + start);
    }

    public void connectToServer() {
        String[] array = remoteHostS.split(":");
        try {
            socket = new Socket(array[0], Integer.parseInt(array[1]));
            LOG.info("Sucessfully connected to " + remoteHostS);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getResult() throws IOException {
        pw.println(codeInfo());
        pw.flush();
        String str = br.readLine();
        try {
            if (str.equalsIgnoreCase(JabberServer.END)) {
//                for (ClientForNing clientForNing : clientForNingList) {
//                    clientForNing.setRUNNING(false);
//                }
                try {
                    mDoneSignal.await();// 等待所有工作线程结束
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                RUNNING = false;
                LOG.info("All workers have finished now.");
                LOG.info("Main Thread Now:" + System.currentTimeMillis());
            }
        } catch (NullPointerException e) {
            RUNNING = false;
            LOG.warn("Server stopped!");
            LOG.info("End Test: " + System.currentTimeMillis());
        }

        LOG.debug(str);
    }

    //(samples).append(FLAG).append(responseTime).append(FLAG).append(corrects).append(FLAG).append(end).append(FLAG).append(start).append(FLAG).append(minResponseTime).append(FLAG).append(maxResponseTime);
    private String codeInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append(samples).append(FLAG).append(responseTime).append(FLAG).append(errors).append(FLAG).append(end).append(FLAG).append(start).append(FLAG).append(minResponseTime).append(FLAG).append(maxResponseTime);
        return sb.toString();
    }


    public static synchronized void setResponseTime(long value) {
        responseTime += value;
        if (value > maxResponseTime)
            maxResponseTime = value;
        if (value < minResponseTime)
            minResponseTime = value;
    }

    public void closeSocket() {
        try {
            if (pw != null)
                pw.close();
            if (br != null)
                br.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        recvConnFromClient();
        doMsgFromClient();
        sampleStart();
        connectToServer();
        try {
            while (RUNNING) {
                getResult();
                Thread.sleep(SLEEP_TIME);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            closeSocket();
        }
    }

    public static void main(String[] args) {
        Agent.samples = 1;
        Agent.errors = 1;
        Agent agent = new Agent("127.0.0.1");
        new Thread(agent).start();
    }
}
