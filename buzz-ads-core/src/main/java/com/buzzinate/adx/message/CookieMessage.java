package com.buzzinate.adx.message;

import com.buzzinate.adx.entity.UserInfo;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: kun
 * Date: 13-6-30
 * Time: 下午8:26
 * Cookie信息
 */
public final class CookieMessage implements Serializable {

    private static final long serialVersionUID = -1141447577873875559L;
    private final RTBMessage rtbMessage;
    private volatile UserInfo userInfo;

    public CookieMessage(RTBMessage rtbMessage, UserInfo userInfo) {
        this.rtbMessage = rtbMessage;
        this.userInfo = userInfo;
    }

    public CookieMessage(RTBMessage rtbMessage) {
        this(rtbMessage, null);
    }

    public RTBMessage getRtbMessage() {
        return rtbMessage;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "CookieMessage{" +
                "rtbMessage=" + rtbMessage +
                ", userInfo=" + userInfo +
                '}';
    }
}
