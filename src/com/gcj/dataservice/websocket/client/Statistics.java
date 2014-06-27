package com.gcj.dataservice.websocket.client;

import com.gcj.utils.DoubleUtils;
import com.gcj.utils.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by gaochuanjun on 14-2-17.
 */
public class Statistics implements Runnable {

    private static final Log LOG = LogFactory.getLog(Statistics.class);

    protected static double samplesForTime = 0;

    protected static long totalSpendTime = 0;

    protected static double samplesForCount = 0;

    protected static double errorCount = 0;

    protected static long maxResponseTime = Long.MIN_VALUE;

    private volatile boolean running = true;

    private int thinkTime = 3000;

    private double concurrency;

    public Statistics(double concurrency) {
        super();
        this.concurrency = concurrency;
    }

    protected static synchronized void setMaxResponseTime(long spendTime) {
        if (spendTime > maxResponseTime) {
            maxResponseTime = spendTime;
        }
    }

    public void printResult() {
        StringBuffer resultMsg = new StringBuffer();
        String samples = "发送请求数：" + samplesForCount;
        resultMsg.append(samples).append("\r\n");
        resultMsg.append("平均响应时间：");
        double avgResponseTime = (totalSpendTime / samplesForTime) / 1000;
        if (avgResponseTime < 60) {
            resultMsg.append(DoubleUtils.convert(avgResponseTime)).append("秒");
        } else {
            long minutes = (long) avgResponseTime / 60;
            long seconds = (long) avgResponseTime % 60;
            resultMsg.append(minutes).append("分").append(seconds).append("秒");
        }
        resultMsg.append("(").append(totalSpendTime).append("/").append(samplesForTime).append(")");
        resultMsg.append("\r\n");

        resultMsg.append("最大响应时间：");
        double maxResponseTime = this.maxResponseTime / 1000.0;
        if (maxResponseTime < 60) {
            resultMsg.append(DoubleUtils.convert(maxResponseTime)).append("秒");
        } else {
            long minutes = (long) maxResponseTime / 60;
            long seconds = (long) maxResponseTime % 60;
            resultMsg.append(minutes).append("分").append(seconds).append("秒");
        }
        resultMsg.append("\r\n");

        double qps = DoubleUtils.convert(concurrency / avgResponseTime);
        resultMsg.append("QPS: ").append(qps).append("(").append(concurrency).append("/").append(avgResponseTime).append(")");
        resultMsg.append("\r\n");

        double error = DoubleUtils.convert((errorCount / samplesForCount) * 100);
        resultMsg.append("错误率：").append(error).append("%").append("(").append(errorCount).append("/").append(samplesForCount).append(")");
        resultMsg.append("\r\n");
        FileUtils.writeFile("result.log", resultMsg.toString());
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(thinkTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            printResult();
        }
    }
}