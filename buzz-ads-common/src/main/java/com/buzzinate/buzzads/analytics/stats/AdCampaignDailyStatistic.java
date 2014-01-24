package com.buzzinate.buzzads.analytics.stats;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.buzzinate.buzzads.enums.AdNetworkEnum;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2013-2-27
 */
public class AdCampaignDailyStatistic extends AdBasicStatistic implements Serializable {

    private static final long serialVersionUID = -6349760295302969655L;

    private int id;
    private int campaignId;
    private AdNetworkEnum network;
    private int advertiserId;
    
    private String campName;
    private String advName;
    
    public AdCampaignDailyStatistic() { }
    
    public AdCampaignDailyStatistic(int advertiserId, long cpcTotalCommission) {
        this.advertiserId = advertiserId;
        setCpcTotalCommission(((Long) cpcTotalCommission).intValue());
    }
    public AdCampaignDailyStatistic(int campaignId, Date dateDay, AdNetworkEnum network, int advertiserId) {
        this.campaignId = campaignId;
        this.dateDay = dateDay;
        this.network = network;
        this.advertiserId = advertiserId;
    }
    
    public AdCampaignDailyStatistic(Date dateDay, long views, long clicks, long cpcClickNo, long cpmViewNo,
            long cpcTotalCommission, long cpsTotalCommission, BigDecimal cpmTotalCommission) {
        super(views, clicks, cpcClickNo, cpmViewNo, cpcTotalCommission, cpsTotalCommission, cpmTotalCommission);
        this.dateDay = dateDay;
    } 

    public AdCampaignDailyStatistic(Date dateDay, long views, long clicks, long cpcClickNo,
            long cpcTotalCommission, long cpsTotalCommission) {
        super(views, clicks, cpcClickNo, cpcTotalCommission, cpsTotalCommission);
        this.dateDay = dateDay;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public AdNetworkEnum getNetwork() {
        return network;
    }

    public void setNetwork(AdNetworkEnum network) {
        this.network = network;
    }

    public int getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(int advertiserId) {
        this.advertiserId = advertiserId;
    }

    public String getCampName() {
        return campName;
    }

    public void setCampName(String campName) {
        this.campName = campName;
    }

    public String getAdvName() {
        return advName;
    }

    public void setAdvName(String advName) {
        this.advName = advName;
    }

}
