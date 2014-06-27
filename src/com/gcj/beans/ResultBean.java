package com.gcj.beans;

import java.io.Serializable;

/**
 * Created by gaochuanjun on 14-1-13.
 */
public class ResultBean implements Serializable {

    private double samples;
    private double responseTime;
    private double minResponseTime;
    private double maxResponseTime;
    private double corrects;
    private long start;
    private long end;
    private boolean isEnd;

    public ResultBean(double samples, double responseTime, double minResponseTime, double maxResponseTime, double corrects, long start, long end) {
        this.samples = samples;
        this.responseTime = responseTime;
        this.minResponseTime = minResponseTime;
        this.maxResponseTime = maxResponseTime;
        this.corrects = corrects;
        this.start = start;
        this.end = end;
    }

    public double getSamples() {
        return samples;
    }

    public void setSamples(double samples) {
        this.samples = samples;
    }

    public double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }

    public double getMinResponseTime() {
        return minResponseTime;
    }

    public void setMinResponseTime(double minResponseTime) {
        this.minResponseTime = minResponseTime;
    }

    public double getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(double maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public double getCorrects() {
        return corrects;
    }

    public void setCorrects(double corrects) {
        this.corrects = corrects;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }
}
