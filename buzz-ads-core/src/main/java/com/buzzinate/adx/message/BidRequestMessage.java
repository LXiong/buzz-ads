package com.buzzinate.adx.message;

import com.buzzinate.adx.entity.UserInfo;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * @author  kun
 * Date: 13-6-29
 * Time: 下午10:37
 * BidRequestMessage
 */
public final class BidRequestMessage implements Serializable {

    private static final long serialVersionUID = 5729553127106007401L;
    private final RTBMessage rtbMessage;
    private final UserInfo userInfo;

    public BidRequestMessage(RTBMessage rtbMessage, UserInfo userInfo) {
        this.rtbMessage = rtbMessage;
        this.userInfo = userInfo;
    }

    public RTBMessage getRtbMessage() {
        return rtbMessage;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public float getFloorPrice() {
        return rtbMessage.getFloorPrice();
    }
    
    public String getRequestId() {
        return rtbMessage.getRequestId();
    }
    
    public String getSendPath() {
        return rtbMessage.getSendPath();
    }

    @Override
    public String toString() {
        return "BidRequestMessage{" +
                "rtbMessage=" + rtbMessage +
                ", userInfo=" + userInfo +
                '}';
    }
}
