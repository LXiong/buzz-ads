package com.buzzinate.buzzads.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.buzzinate.buzzads.enums.AdEntrySizeEnum;
import com.buzzinate.buzzads.enums.AdEntryTypeEnum;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.AdsTypeEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.buzzads.enums.WeekDay;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> 
 * Feb 26; 2013 2:21:51 PM
 * 
 */
public class AdItem implements Serializable {
    private static final long serialVersionUID = -72333515733333408L;

    private int campaignId;
    private int orderId;
    private int entryId;
    private int advertiserId;
    private AdStatusEnum status;
    private int bidPrice;
    private String keywords;
    private String link;
    private AdEntryTypeEnum resourceType;
    private String resourceUrl;
    private String displayUrl;
    private String title;
    private String description;
    private Set<AdNetworkEnum> network;
    private BidTypeEnum bidType;
    private Date startDate;
    private Date endDate;
    private Set<WeekDay> scheduleDay;
    private Set<ScheduleTime> scheduleTime;
    private Set<String> locations;
    //用逗号分隔开的category串
    private String audienceCategories;
    //即广告链接所最终定向的realUrl的主域名
    private String destination;
    private AdsTypeEnum adsType;
    //投放的媒体列表
    private Set<String> channels;
    private int orderFrequency;
    private int entryFrequency;
    private AdEntrySizeEnum resourceSize = AdEntrySizeEnum.SIZE80x80;

    public String getAudienceCategories() {
        return audienceCategories;
    }
    public void setAudienceCategories(String audienceCategories) {
        this.audienceCategories = audienceCategories;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public int getCampaignId() {
        return campaignId;
    }
    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public int getEntryId() {
        return entryId;
    }
    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }
    public int getAdvertiserId() {
        return advertiserId;
    }
    public void setAdvertiserId(int advertiserId) {
        this.advertiserId = advertiserId;
    }
    public int getBidPrice() {
        return bidPrice;
    }
    public void setBidPrice(int bidPrice) {
        this.bidPrice = bidPrice;
    }
    public AdStatusEnum getStatus() {
        return status;
    }
    public void setStatus(AdStatusEnum status) {
        this.status = status;
    }
    public String getKeywords() {
        return keywords;
    }
    public void setKeywords(String keywords) {
        this.keywords = keywords;
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
    public String getDisplayUrl() {
        return displayUrl;
    }
    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Set<AdNetworkEnum> getNetwork() {
        return network;
    }
    public void setNetwork(Set<AdNetworkEnum> network) {
        this.network = network;
    }
    public BidTypeEnum getBidType() {
        return bidType;
    }
    public void setBidType(BidTypeEnum bidType) {
        this.bidType = bidType;
    }
    public Set<WeekDay> getScheduleDay() {
        return scheduleDay;
    }
    public void setScheduleDay(Set<WeekDay> scheduleDay) {
        this.scheduleDay = scheduleDay;
    }
    public Set<ScheduleTime> getScheduleTime() {
        return scheduleTime;
    }
    public void setScheduleTime(Set<ScheduleTime> scheduleTime) {
        this.scheduleTime = scheduleTime;
    }
    public void setLocations(Set<String> locations) {
        this.locations = locations;
    }
    public Set<String> getLocations() {
        return locations;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public AdsTypeEnum getAdsType() {
        return adsType;
    }
    public void setAdsType(AdsTypeEnum adsType) {
        this.adsType = adsType;
    }
    public Set<String> getChannels() {
        return channels;
    }
    public void setChannels(Set<String> channels) {
        this.channels = channels;
    }
    public int getOrderFrequency() {
        return orderFrequency;
    }
    public void setOrderFrequency(int orderFrequency) {
        this.orderFrequency = orderFrequency;
    }
    public int getEntryFrequency() {
        return entryFrequency;
    }
    public void setEntryFrequency(int entryFrequency) {
        this.entryFrequency = entryFrequency;
    }
    public AdEntrySizeEnum getResourceSize() {
        return resourceSize;
    }
    public void setResourceSize(AdEntrySizeEnum resourceSize) {
        this.resourceSize = resourceSize;
    }

}
