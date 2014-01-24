package com.buzzinate.buzzads.domain;

import com.buzzinate.buzzads.enums.AdEntryPosEnum;
import com.buzzinate.buzzads.enums.AdEntrySizeEnum;
import com.buzzinate.buzzads.enums.AdEntryTypeEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.stats.BaseDailyStats;

import java.io.Serializable;
import java.util.Date;

/**
 * 广告单元
 *
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *         <p/>
 *         2012-11-23
 */
public class AdEntry extends BaseDailyStats implements Serializable {

    private static final long serialVersionUID = 2798112832569679533L;
    private int id;
    private int orderId;
    private int campaignId;
    private int advertiserId;
    private String name;
    private String link;
    private AdEntryTypeEnum resourceType = AdEntryTypeEnum.UNKNOWN;
    private String resourceUrl = "";
    private String title;
    private String displayUrl;
    private String description;
    private AdEntrySizeEnum size = AdEntrySizeEnum.SIZE80x80;
    private AdEntryPosEnum position = AdEntryPosEnum.UNKNOWN;
    private AdStatusEnum status = AdStatusEnum.VERIFYING;
    private Date updateAt = new Date();
    //即广告链接所最终定向的realUrl的主域名
    private String destination;
    //其他补充属性
    private String campName;
    private String orderName;
    private String advertiserName;
    private String statusDesc = "";

    public String getAdvertiserName() {
        return advertiserName;
    }

    public void setAdvertiserName(String advertiserName) {
        this.advertiserName = advertiserName;
    }

    public int getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(int advertiserId) {
        this.advertiserId = advertiserId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AdEntrySizeEnum getSize() {
        return size;
    }

    public void setSize(AdEntrySizeEnum size) {
        this.size = size;
    }

    public AdStatusEnum getStatus() {
        return status;
    }

    public void setStatus(AdStatusEnum status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public AdEntryTypeEnum getResourceType() {
        return resourceType;
    }

    public void setResourceType(AdEntryTypeEnum resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = (Date) updateAt;
    }

    public String getCampName() {
        return campName;
    }

    public void setCampName(String campName) {
        this.campName = campName;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getStatusName() {
        return this.statusDesc + AdStatusEnum.getCnName(this.status);
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public AdEntryPosEnum getPosition() {
        return position;
    }

    public void setPosition(AdEntryPosEnum position) {
        this.position = position;
    }
}
