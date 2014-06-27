package com.gcj.socket;

import com.gcj.common.JabberServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gaochuanjun on 14-1-14.
 */
public class Group implements Runnable {

    private static final Log LOG = LogFactory.getLog(Group.class);

    private ServerSocket serverSocket;

    private int size;

    protected static final String FLAG = ";";

    protected ConcurrentHashMap<SocketAddress, String> sampleMap = new ConcurrentHashMap<SocketAddress, String>();

    private String localHostS;

    private int duration;

    private String responseMsg = "Message Received!";

    public Group(int size, String localHostS, int duration) {
        super();
        this.size = size;
        this.localHostS = localHostS;
        this.duration = duration;
    }

    public void start() {
        try {
            String[] array = localHostS.split(":");
            int port = Integer.parseInt(array[1]);
            serverSocket = new ServerSocket(port);
            LOG.info("Server is listening on port " + port + "[Waiting for client connection]: starting");
            for (int i = 0; i < size; i++) {
                Socket socket = serverSocket.accept();
                LOG.info("[" + i + "] Accept a connection from " + socket.getRemoteSocketAddress());
                Client client = new Client(socket);
                new Thread(client).start();
            }
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            responseMsg = JabberServer.END;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Client implements Runnable {

        private Socket socket;

        private BufferedReader br;

        private PrintWriter pw;

        protected static final int SLEEP_TIME = 3000;

        private SocketAddress socketAddress;

        public Client(Socket socket) {
            this.socket = socket;
            this.socketAddress = socket.getRemoteSocketAddress();
            initial();
        }

        public void initial() {
            try {
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

            try {
                while (true) {
                    String str = br.readLine();
                    LOG.info("Receive a message from " + socketAddress + ": " + str);
                    recvFromRemoteHost(socketAddress, str);
                    Thread.sleep(SLEEP_TIME);
                    pw.println(responseMsg);
                    pw.flush();
                    printInfo();
                    if (responseMsg.equalsIgnoreCase(JabberServer.END)) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                closeSocket();
                printInfo();
            }
        }
    }

    public synchronized void recvFromRemoteHost(SocketAddress socketAddress, String str) {
        sampleMap.put(socketAddress, str);
    }

    public synchronized void printInfo() {
        synchronized (sampleMap) {
            long start = Long.MAX_VALUE;
            long end = Long.MIN_VALUE;
            long responseTime = 0;
            double samples = 0;
            //double corrects = 0;
            double errors = 0;
            long minResponseTime = Long.MAX_VALUE;
            long maxResponseTime = Long.MIN_VALUE;
            Set<SocketAddress> socketAddressSet = sampleMap.keySet();
            //(samples).append(FLAG).append(responseTime).append(FLAG).append(corrects).append(FLAG).append(end).append(FLAG).append(start).append(FLAG).append(minResponseTime).append(FLAG).append(maxResponseTime);
            for (SocketAddress socketAddress : socketAddressSet) {
                String resultSample = sampleMap.get(socketAddress);
                String[] array = resultSample.split(FLAG);
                samples += Double.parseDouble(array[0]);
                responseTime += Double.parseDouble(array[1]);
                errors += Double.parseDouble(array[2]);
                long curEnd = Long.parseLong(array[3]);
                if (curEnd > end) {
                    end = curEnd;
                }
                long curStart = Long.parseLong(array[4]);
                if (curStart < start) {
                    start = curStart;
                }

                long curMinResponseTime = Long.parseLong(array[5]);
                if (curMinResponseTime < minResponseTime) {
                    minResponseTime = curMinResponseTime;
                }

                long curMaxResponseTime = Long.parseLong(array[6]);
                if (curMaxResponseTime > maxResponseTime) {
                    maxResponseTime = curMaxResponseTime;
                }
            }
            double average = (double) responseTime / (samples - errors);
            double min = (double) minResponseTime;
            double max = (double) maxResponseTime;
//            double error = 0;
//            if (samples >= (corrects - 1)) {
//                error = ((samples - corrects - 1) / samples) * 100;
//            }
            double error = (errors / samples) * 100;
            double throughput = 0;
            if (end > start) {
                long spendTime = end - start;
                double spendTimeD = (double) spendTime;
                double spendTimeS = spendTimeD / 1000;
                throughput = samples / spendTimeS;
            }
            try {
                LOG.info("Samples: " + convert(samples) + "  Average: " + convert(average) + " Min: " + convert(min) + "   Max: " + convert(max) + "    Error: " + convert(error) + "%" + "    Throughput: " + convert(throughput));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private float convert(double value) {
        return (float) ((Math.round(value * 100)) / 100.0);
    }

    @Override
    public void run() {
        start();
    }

    public static void main(String[] args) {
//        Group group = new Group(1);
//        new Thread(group).start();
    }
}
