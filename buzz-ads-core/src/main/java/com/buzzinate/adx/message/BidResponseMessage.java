package com.buzzinate.adx.message;


import java.io.Serializable;
import java.util.Arrays;

import com.buzzinate.adx.entity.AdInfo;

/**
 * Created with IntelliJ IDEA.
 * @author kun
 * Date: 13-6-29
 * Time: 下午11:37
 * BidResponseMessage
 */
public final class BidResponseMessage implements Serializable {


    private static final long serialVersionUID = -8364816215495865389L;
    private final BidRequestMessage bidRequestMessage;
    private final AdInfo[] adInfos;

    public BidResponseMessage(BidRequestMessage bidRequest, AdInfo[] adInfoList) {
        this.bidRequestMessage = bidRequest;
        this.adInfos = adInfoList;
    }

    public BidResponseMessage(BidRequestMessage bidRequest) {
        this(bidRequest, new AdInfo[0]);
    }

    public BidRequestMessage getBidRequestMessage() {
        return bidRequestMessage;
    }

    public AdInfo[] getAdInfos() {
        return adInfos;
    }
    
    public String getRequestId() {
        return bidRequestMessage.getRequestId();
    }
    
    public String getSendPath() {
        return bidRequestMessage.getSendPath();
    }

    @Override
    public String toString() {
        return "BidResponseMessage{" +
                "bidRequestMessage=" + bidRequestMessage +
                ", adInfos=" + Arrays.toString(adInfos) +
                '}';
    }
}
