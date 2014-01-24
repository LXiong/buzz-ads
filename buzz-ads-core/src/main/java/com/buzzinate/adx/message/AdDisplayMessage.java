package com.buzzinate.adx.message;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * @author kun
 * Date: 13-7-30
 * Time: 下午11:45
 * 广告被展示消息
 */
public class AdDisplayMessage implements Serializable {
    private static final long serialVersionUID = -5040770973849394044L;
    private String requestId;
    private int dspId;
    private int adId;
    private Date timestamp;

    public AdDisplayMessage(String requestId, int dspId, int adId) {
        this.requestId = requestId;
        this.dspId = dspId;
        this.adId = adId;
        this.timestamp = new Date();
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getDspId() {
        return dspId;
    }

    public void setDspId(int dspId) {
        this.dspId = dspId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    @Override
    public String toString() {
        return "AdDisplayMessage{" +
                "requestId='" + requestId + '\'' +
                ", dspId=" + dspId +
                ", adId=" + adId +
                ", timestamp=" + timestamp +
                '}';
    }
}
