package com.buzzinate.adx.entity;


import java.io.Serializable;
import java.util.Date;

import com.buzzinate.adx.enums.BidStatus;

/**
 * Created with IntelliJ IDEA.
 * @author kun
 * Date: 13-6-29
 * Time: 下午11:31
 * DSP返回的广告信息
 */
public class AdInfo implements Serializable, Comparable<AdInfo> {
    private static final long serialVersionUID = 1838377125997552429L;
    private String requestId;
    private int dspId;
    private int adId;
    private String segment;
    private long cost;
    private String link;
    private int bannerId;
    private float price;
    private float secprice;
    private float servicefee;
    private String notifyUrl;
    private Date timestamp = new Date();
    private BidStatus status = BidStatus.FAILURE;

    public AdInfo() {
    }

    public AdInfo(String requestId, int dspId, int adId, int bannerId, float price) {
        this.requestId = requestId;
        this.adId = adId;
        this.bannerId = bannerId;
        this.dspId = dspId;
        this.price = price;
        if (price == 0) {
            status = BidStatus.NO_BID;
        }
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getDspId() {
        return dspId;
    }

    public void setDspId(int dspId) {
        this.dspId = dspId;
    }

    public BidStatus getStatus() {
        return status;
    }

    public void setStatus(BidStatus status) {
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
    
    public float getSecprice() {
        return secprice;
    }

    public void setSecprice(float secprice) {
        this.secprice = secprice;
    }

    public float getServicefee() {
        return servicefee;
    }

    public void setServicefee(float servicefee) {
        this.servicefee = servicefee;
    }

    @Override
    public int compareTo(AdInfo o) {
        if (this.bannerId > o.getBannerId()) {
            return -1;
        } else if (this.bannerId == o.getBannerId()) {
            if (this.getPrice() >= o.getPrice()) {
                this.setSecprice(o.getPrice());
                return -1;
            } else {
                return 1;
            }
        }
        return 1;
    }

    @Override
    public String toString() {
        return "AdInfo [requestId=" + requestId + ", dspId=" + dspId + ", adId=" + adId + ", segment=" + segment +
                ", cost=" + cost + ", link=" + link + ", bannerId=" + bannerId + ", price=" + price + ", secprice=" +
                secprice + ", servicefee=" + servicefee + ", notifyUrl=" + notifyUrl + ", timestamp=" + timestamp +
                ", status=" + status + "]";
    }
}
