package com.buzzinate.buzzads.event;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-5-15
 * Time: 上午11:59
 * 媒体事件
 */
public abstract class ChannelBaseEvent extends AdBaseEvent {
    private static final long serialVersionUID = -3654360128021581155L;
    private String uuid;
    private String domain;
    private int level;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
