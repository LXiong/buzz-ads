package com.buzzinate.buzzads.domain;

import java.io.Serializable;

import com.buzzinate.buzzads.enums.AdStatusEnum;

/**
 * 成果网广告
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-11-19
 */
public class ChanetDTO implements Serializable {

    private static final long serialVersionUID = -7503649807820947992L;
    
    private int orderId;
    private String advertiser;
    // 广告活动Id
    private Long campaignId;
    // 广告活动名称
    private String campaignName;
    // 广告活动主域名
    private String campaignDomain;
    // 网站ID
    private String siteId;
    // 网站名称
    private String siteName;
    /*
     * 都采用 utf-8编码
     */
    // 直链
    private String advertiserLink;
    // 成果网直链
    private String chanetLink;
    private String ruleXml;
    private AdStatusEnum status;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(String advertiser) {
        this.advertiser = advertiser;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignDomain() {
        return campaignDomain;
    }

    public void setCampaignDomain(String campaignDomain) {
        this.campaignDomain = campaignDomain;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAdvertiserLink() {
        return advertiserLink;
    }

    public void setAdvertiserLink(String advertiserLink) {
        this.advertiserLink = advertiserLink;
    }

    public String getChanetLink() {
        return chanetLink;
    }

    public void setChanetLink(String chanetLink) {
        this.chanetLink = chanetLink;
    }

    public String getRuleXml() {
        return ruleXml;
    }

    public void setRuleXml(String ruleXml) {
        this.ruleXml = ruleXml;
    }

    public AdStatusEnum getStatus() {
        return status;
    }

    public void setStatus(AdStatusEnum status) {
        this.status = status;
    }

}
