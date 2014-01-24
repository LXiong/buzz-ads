package com.buzzinate.buzzads.domain;

import java.io.Serializable;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2013-5-28
 */
public class UserAuthority implements Serializable {

    private static final long serialVersionUID = 7073199420239569794L;

    private int userId;

    //1 开通定向功能 0或null 未开通
    private Integer channelTarget;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getChannelTarget() {
        return channelTarget;
    }

    public void setChannelTarget(Integer channelTarget) {
        this.channelTarget = channelTarget;
    }
}
