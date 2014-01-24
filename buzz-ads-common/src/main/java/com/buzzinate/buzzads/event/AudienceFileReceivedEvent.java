package com.buzzinate.buzzads.event;

import com.buzzinate.buzzads.enums.PartnerEnum;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-5-28
 * Time: 下午4:04
 * Audience更新事件
 */
public class AudienceFileReceivedEvent implements Serializable {
    private static final long serialVersionUID = 0L;
    private long timestamp;
    private int partnerEnumCode;
    private int advertiserId;
    private String data;

    public AudienceFileReceivedEvent(long timestamp, PartnerEnum partnerEnum, int advertiserId, String data) {
        this.timestamp = timestamp;
        this.partnerEnumCode = partnerEnum.getCode();
        this.advertiserId = advertiserId;
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getPartnerEnumCode() {
        return partnerEnumCode;
    }

    public String getData() {
        return data;
    }

    public int getAdvertiserId() {
        return advertiserId;
    }

    @Override
    public String toString() {
        return "AudienceFileReceivedEvent{" +
                "timestamp=" + timestamp +
                ", partnerEnumCode=" + partnerEnumCode +
                ", advertiserId=" + advertiserId +
                ", data='" + data + '\'' +
                '}';
    }
}
