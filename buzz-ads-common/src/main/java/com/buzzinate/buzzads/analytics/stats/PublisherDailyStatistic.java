package com.buzzinate.buzzads.analytics.stats;


import com.buzzinate.buzzads.analytics.stats.enums.AdStatisticType;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.string.StringUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * 站长每日数据
 *
 * @author Jacky Lu<jacky.lu@buzzinate.com>
 *         <p/>
 *         2012-12-11
 */
public class PublisherDailyStatistic extends AdBasicStatistic implements Serializable {

    private static final long serialVersionUID = -7833634266747418043L;
    private int id;
    private byte[] uuid;
    private AdNetworkEnum network;
    private int pageview;
    //站长得cpc和cps佣金
    private int commission;
    // 网站名，用于页面展示
    private String siteName;

    public PublisherDailyStatistic() {
    }

    public PublisherDailyStatistic(byte[] uuid, long cpcClickNo, long cpcPubCommission,
                                   long cpsOrderNo, long cpsConfirmedCommission, long cpmViewNo, BigDecimal cpmPubCommission, long pageview) {
        this.uuid = uuid;
        this.commission = ((Long) cpcPubCommission).intValue() + ((Long) cpsConfirmedCommission).intValue() +
                cpmPubCommission.multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_DOWN).intValue();
        setCpcClickNo(((Long) cpcClickNo).intValue());
        setCpcPubCommission(((Long) cpcPubCommission).intValue());
        setCpsOrderNo(((Long) cpsOrderNo).intValue());
        setCpsConfirmedCommission(((Long) cpsConfirmedCommission).intValue());
        setCpmViewNo(((Long) cpmViewNo).intValue());
        setCpmPubCommission(cpmPubCommission);
        setPageview(((Long) pageview).intValue());
    }

    public PublisherDailyStatistic(Date dateDay, byte[] uuid, AdNetworkEnum network) {
        this.dateDay = dateDay;
        this.uuid = uuid;
        this.network = network;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return DateTimeUtil.formatDate(dateDay);
    }

    public AdNetworkEnum getNetwork() {
        return network;
    }

    public void setNetwork(AdNetworkEnum network) {
        this.network = network;
    }

    public byte[] getUuid() {
        return uuid;
    }

    public void setUuid(byte[] uuid) {
        this.uuid = uuid;
    }

    public String getUuidString() {
        return StringUtil.byteArrayToUuid(uuid);
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public int getPageview() {
        return pageview;
    }

    public void setPageview(int pageview) {
        this.pageview = pageview;
    }

    @Override
    public void increaseStats(AdBasicStatistic adStats, AdStatisticType type) {
        switch (type) {
            case PAGEVIEW:
                pageview++;
                break;
            default:
                super.increaseStats(adStats, type);
                break;
        }
    }
}