package com.buzzinate.buzzads.analytics.stats;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.buzzinate.buzzads.enums.AdNetworkEnum;

/**
 * 广告每日数据
 * 
 * @author Jacky Lu<jacky.lu@buzzinate.com>
 * 
 *         2012-12-11
 */
public class AdDailyStatistic extends AdBasicStatistic implements Serializable {

    private static final long serialVersionUID = 1767864094356163051L;

    private int id;
    private int adEntryId;
    private int adOrderId;
    private AdNetworkEnum network;
    
    // 冗余字段供admin报表使用
    private String adTitle;
    private byte[] uuid;

    public AdDailyStatistic() {
    }
    
    
    public AdDailyStatistic(Date dateDay, long views, long clicks, long cpcClickNo,
            long cpcTotalCommission, long cpsTotalCommission) {
        super(views, clicks, cpcClickNo, cpcTotalCommission, cpsTotalCommission);
        this.dateDay = dateDay;
    }

    public AdDailyStatistic(int adEntryId, long views, long clicks, long cpcClickNo,
            long cpcTotalCommission, long cpsTotalCommission) {
        super(views, clicks, cpcClickNo, cpcTotalCommission, cpsTotalCommission);
        this.adEntryId = adEntryId;
        this.clicks = (int) clicks;
    }

    public AdDailyStatistic(int adEntryId, long views, long clicks, long cpcClickNo,
            long cpmViewNo, long cpcTotalCommission, long cpsTotalCommission,
            BigDecimal cpmTotalCommission) {
        this(adEntryId, views, clicks, cpcClickNo, cpcTotalCommission, cpsTotalCommission);
        setCpmViewNo(((Long) cpmViewNo).intValue());
        setCpmTotalCommission(cpmTotalCommission);
    }
    
    public AdDailyStatistic(int orderId, Date dateDay, int network, long cpcTotalCommission, long cpsTotalCommission,
            BigDecimal cpmTotalCommission) {
        super(0, 0, 0, 0, cpcTotalCommission, cpsTotalCommission, cpmTotalCommission);
        setAdOrderId(orderId);
        setDateDay(dateDay);
        setNetwork(AdNetworkEnum.findByValue(network));
    }

    public AdDailyStatistic(Date dateDay, int adEntryId, AdNetworkEnum network, int adOrderId) {
        this.dateDay = dateDay;
        this.adEntryId = adEntryId;
        this.network = network;
        this.adOrderId = adOrderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdEntryId() {
        return adEntryId;
    }

    public void setAdEntryId(int adEntryId) {
        this.adEntryId = adEntryId;
    }

    public int getAdOrderId() {
        return adOrderId;
    }

    public void setAdOrderId(int adOrderId) {
        this.adOrderId = adOrderId;
    }

    public AdNetworkEnum getNetwork() {
        return network;
    }

    public void setNetwork(AdNetworkEnum network) {
        this.network = network;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public byte[] getUuid() {
        return uuid;
    }

    public void setUuid(byte[] uuid) {
        this.uuid = uuid;
    }

}