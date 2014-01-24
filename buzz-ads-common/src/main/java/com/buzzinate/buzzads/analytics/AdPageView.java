package com.buzzinate.buzzads.analytics;

import com.buzzinate.buzzads.enums.AdNetworkEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-6-27
 * Time: 下午5:17
 * 原始PV信息.
 */
public class AdPageView implements Serializable {
    private static final long serialVersionUID = -7431204962982792486L;
    private AdNetworkEnum network;
    private String sourceUrl;
    private String publisherUuid;
    private Date createAt;
    private String cookieId;
    private String ip;
    private String ua;

    public AdNetworkEnum getNetwork() {
        return network;
    }

    public void setNetwork(AdNetworkEnum network) {
        this.network = network;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getPublisherUuid() {
        return publisherUuid;
    }

    public void setPublisherUuid(String publisherUuid) {
        this.publisherUuid = publisherUuid;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getCookieId() {
        return cookieId;
    }

    public void setCookieId(String cookieId) {
        this.cookieId = cookieId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    @Override
    public String toString() {
        return "AdPageView{" +
                "network=" + network +
                ", sourceUrl='" + sourceUrl + '\'' +
                ", publisherUuid='" + publisherUuid + '\'' +
                ", createAt=" + createAt +
                ", cookieId='" + cookieId + '\'' +
                ", ip='" + ip + '\'' +
                ", ua='" + ua + '\'' +
                '}';
    }
}
