package com.buzzinate.adx.message;


import com.buzzinate.adx.entity.AdInfo;
import com.buzzinate.adx.entity.BannerInfo;
import com.buzzinate.adx.entity.MediaInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @author kun
 * Date: 13-6-29
 * Time: 下午10:21
 * RTB Event
 */
public final class RTBMessage implements Serializable {

    private static final long serialVersionUID = -6449730327783318171L;
    private final String requestId;
    private final String cookieId;
    private final List<BannerInfo> bannerInfos;
    private final MediaInfo mediaInfo;
    private final String ip;
    private final String userAgent;
    private final String sendPath;
    
    //only for test
    private final List<AdInfo> dspBids;

    public RTBMessage(String requestId, String cookieId, List<BannerInfo> bannerInfos, MediaInfo mediaInfo, String ip,
            String userAgent, String sendPath, List<AdInfo> dspBids) {
        this.requestId = requestId;
        this.cookieId = cookieId;
        this.bannerInfos = bannerInfos;
        this.mediaInfo = mediaInfo;
        this.ip = ip;
        this.userAgent = userAgent;
        this.sendPath = sendPath;
        this.dspBids = dspBids;
    }
    
    public RTBMessage(RTBMessage message, String sendPath) {
        this.requestId = message.getRequestId();
        this.cookieId = message.getCookieId();
        this.bannerInfos = message.getBannerInfos();
        this.mediaInfo = message.getMediaInfo();
        this.ip = message.getIp();
        this.userAgent = message.getUserAgent();
        this.dspBids = message.getDspBids();
        this.sendPath = sendPath;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getCookieId() {
        return cookieId;
    }

    public List<BannerInfo> getBannerInfos() {
        return bannerInfos;
    }

    public MediaInfo getMediaInfo() {
        return mediaInfo;
    }

    public String getIp() {
        return ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getSendPath() {
        return sendPath;
    }
    
    public float getFloorPrice() {
        return mediaInfo.getFloorPrice();
    }

    public List<AdInfo> getDspBids() {
        return dspBids;
    }

    @Override
    public String toString() {
        return "RTBMessage{" +
                "requestId=" + requestId +
                ", cookieId='" + cookieId + '\'' +
                ", bannerInfos=" + bannerInfos +
                ", mediaInfo=" + mediaInfo +
                ", ip='" + ip + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }


}
