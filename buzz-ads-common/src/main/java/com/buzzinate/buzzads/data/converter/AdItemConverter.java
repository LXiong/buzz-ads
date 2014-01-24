package com.buzzinate.buzzads.data.converter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.buzzinate.buzzads.data.thrift.AdEntrySizeEnum;
import com.buzzinate.buzzads.data.thrift.TAdItem;
import com.buzzinate.buzzads.data.thrift.TAdItem.Builder;
import com.buzzinate.buzzads.data.thrift.TAdStatusEnum;
import com.buzzinate.buzzads.data.thrift.TAdsTypeEnum;
import com.buzzinate.buzzads.data.thrift.TBidTypeEnum;
import com.buzzinate.buzzads.data.thrift.TResourceTypeEnum;
import com.buzzinate.buzzads.domain.AdItem;
import com.buzzinate.buzzads.enums.AdEntryTypeEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.AdsTypeEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> 
 * Feb 26, 2013 2:20:22 PM
 * 
 */
public final class AdItemConverter {
    private AdItemConverter() {
    }

    /**
     * Convert form thrift object to AdItem domain object
     * 
     * @param tItem
     * @return
     */
    public static AdItem fromThrift(TAdItem tItem) {
        AdItem item = new AdItem();
        item.setCampaignId(tItem.getCampaignId());
        item.setOrderId(tItem.getOrderId());
        item.setEntryId(tItem.getEntryId());
        item.setAdvertiserId(tItem.getAdvertiserId());
        item.setStatus(AdStatusEnum.findByValue(tItem.getStatus().getValue()));
        item.setAdsType(AdsTypeEnum.findByValue(tItem.getAdsType().getValue()));
        item.setOrderFrequency(tItem.getOrderFrequency());
        item.setEntryFrequency(tItem.getEntryFrequency());
        item.setResourceSize(com.buzzinate.buzzads.enums.AdEntrySizeEnum.findByValue(
                tItem.getResourceSize().getValue()));
        mergeThrift(item, tItem);
        return item;
    }

    public static TAdItem toThrift(AdItem item) {
        Builder builder = new TAdItem.Builder()
            .campaignId(item.getCampaignId())
            .orderId(item.getOrderId())
            .entryId(item.getEntryId())
            .advertiserId(item.getAdvertiserId())
            .orderFrequency(item.getOrderFrequency())
            .entryFrequency(item.getEntryFrequency())
            .adsType(TAdsTypeEnum.findByValue(item.getAdsType().getCode()))
            .resourceSize(AdEntrySizeEnum.findByValue(item.getResourceSize().getCode()))
            .status(TAdStatusEnum.findByValue(item.getStatus().getCode()));
        
        if (item.getBidPrice() != 0)
            builder.bidPrice(item.getBidPrice());
        if (item.getLocations() != null)
            builder.locations(item.getLocations());
        if (item.getResourceType() != null)
            builder.resourceType(TResourceTypeEnum.findByValue(item.getResourceType().getCode()));
        if (item.getNetwork() != null)
            builder.network(NetworkEnumConverter.toThrift(item.getNetwork()));
        if (item.getBidType() != null)
            builder.bidType(TBidTypeEnum.findByValue(item.getBidType().getCode()));
        if (item.getScheduleDay() != null)
            builder.scheduleDay(WeekDayConverter.toThrift(item.getScheduleDay()));
        if (StringUtils.isNotBlank(item.getKeywords()))
            builder.keywords(item.getKeywords());
        if (StringUtils.isNotBlank(item.getLink()))
            builder.link(item.getLink());
        if (StringUtils.isNotBlank(item.getResourceUrl()))
            builder.resourceUrl(item.getResourceUrl());
        if (StringUtils.isNotBlank(item.getDisplayUrl()))
            builder.displayUrl(item.getDisplayUrl());
        if (StringUtils.isNotBlank(item.getTitle()))
            builder.title(item.getTitle());
        if (StringUtils.isNotBlank(item.getDescription()))
            builder.description(item.getDescription());
        if (item.getScheduleTime() != null)
            builder.scheduleTime(ScheduleTimeConverter.toThrift(item.getScheduleTime()));
        if (item.getStartDate() != null) {
            builder.startDate(item.getStartDate().getTime());
        }
        if (item.getEndDate() != null) {
            builder.endDate(item.getEndDate().getTime());
        }
        if (item.getDestination() != null) {
            builder.destination(item.getDestination());
        }
        if (item.getAudienceCategories() != null) {
            builder.audienceCategories(getCategoriesSet(item.getAudienceCategories()));
        }
        if (item.getChannels() != null) {
            builder.channels(item.getChannels());
        }
        return builder.build();
    }
    /**
     * Merge thrift object into existing AdItem
     * 
     * @param item
     * @param tItem
     * @return
     */
    public static void mergeThrift(AdItem item, TAdItem tItem) {
        if (tItem.getBidPriceOption().isDefined())
            item.setBidPrice(tItem.getBidPrice());
        if (tItem.getKeywordsOption().isDefined())
            item.setKeywords(tItem.getKeywords());
        if (tItem.getLinkOption().isDefined())
            item.setLink(tItem.getLink());
        if (tItem.getResourceTypeOption().isDefined())
            item.setResourceType(AdEntryTypeEnum.findByValue(tItem.getResourceType().getValue()));
        if (tItem.getResourceUrlOption().isDefined())
            item.setResourceUrl(tItem.getResourceUrl());
        if (tItem.getDisplayUrlOption().isDefined())
            item.setDisplayUrl(tItem.getDisplayUrl());
        if (tItem.getTitleOption().isDefined())
            item.setTitle(tItem.getTitle());
        if (tItem.getDescriptionOption().isDefined())
            item.setDescription(tItem.getDescription());
        if (tItem.getDestinationOption().isDefined())
            item.setDestination(tItem.getDestination());
        if (tItem.getNetworkOption().isDefined())
            item.setNetwork(NetworkEnumConverter.fromThrift(tItem.getNetwork()));
        if (tItem.getBidTypeOption().isDefined())
            item.setBidType(BidTypeEnum.findByValue(tItem.getBidType().getValue()));
        if (tItem.getStartDateOption().isDefined()) {
            item.setStartDate(new Date(tItem.getStartDate()));
        }
        if (tItem.getEndDateOption().isDefined()) {
            item.setEndDate(new Date(tItem.getEndDate()));
        }
        if (tItem.getScheduleDayOption().isDefined())
            item.setScheduleDay(WeekDayConverter.fromThrift(tItem.getScheduleDay()));
        if (tItem.getScheduleTimeOption().isDefined())
            item.setScheduleTime(ScheduleTimeConverter.fromThrift(tItem.getScheduleTime()));
        if (tItem.getLocationsOption().isDefined())
            item.setLocations(tItem.getLocations());
        if (tItem.getAudienceCategoriesOption().isDefined()) {
            item.setAudienceCategories(getCategories(tItem.getAudienceCategories()));
        }
    }
    
    private static Set<Integer> getCategoriesSet(String categories) {
        Set<Integer> categoriesSet = new HashSet<Integer>();
        if (!StringUtils.isEmpty(categories)) {
            String[] list = categories.split(",");
            
            for (String id : list) {
                categoriesSet.add(Integer.valueOf(id));
            }
        }
        return categoriesSet; 
    }
    
    private static String getCategories(Set<Integer> categoriesSet) {
        String categories = "";
        for (Integer id : categoriesSet) {
            categories += id + ",";
        }
        categories = StringUtils.removeEnd(categories, ",");
        return categories;
    }
}
