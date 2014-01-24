package com.buzzinate.buzzads.data.operations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdEntryService;
import com.buzzinate.buzzads.core.service.AdOrderService;
import com.buzzinate.buzzads.core.service.ChannelService;
import com.buzzinate.buzzads.data.converter.AdItemConverter;
import com.buzzinate.buzzads.data.converter.AdStatusEnumConverter;
import com.buzzinate.buzzads.data.converter.BidTypeEnumConverter;
import com.buzzinate.buzzads.data.converter.NetworkEnumConverter;
import com.buzzinate.buzzads.data.converter.ProvinceNameConverter;
import com.buzzinate.buzzads.data.thrift.TAdCriteria;
import com.buzzinate.buzzads.data.thrift.TAdItem;
import com.buzzinate.buzzads.data.thrift.TPagination;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdEntry;
import com.buzzinate.buzzads.domain.AdItem;
import com.buzzinate.buzzads.domain.AdOrder;
import com.buzzinate.buzzads.domain.Channel;
import com.buzzinate.buzzads.domain.ScheduleTime;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.common.util.DateTimeUtil;
import com.twitter.util.ExceptionalFunction0;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Feb 26; 2013 2:09:48 PM
 *
 */
public class FindAdItemsOp extends ExceptionalFunction0<List<TAdItem>> {

    private int campaignId;
    private int orderId;
    private int entryId;
    private int advertiserId;
    private Set<AdStatusEnum> status = new HashSet<AdStatusEnum>();
    private Set<AdNetworkEnum> network = new HashSet<AdNetworkEnum>();
    private Set<BidTypeEnum> bidType = new HashSet<BidTypeEnum>();
    private int start;
    private int count;

    private AdEntryService adEntryService;
    private AdOrderService adOrderService;
    private AdCampaignService adCampaignService;
    private ChannelService channelService;

    public FindAdItemsOp(AdEntryService adEntryService, AdOrderService adOrderService,
                         AdCampaignService adCampaignService, ChannelService channelService,
                         TAdCriteria criteria, TPagination pagination) {
        this.adEntryService = adEntryService;
        this.adOrderService = adOrderService;
        this.adCampaignService = adCampaignService;
        this.channelService = channelService;

        if (criteria.getCampaignIdOption().isDefined())
            campaignId = criteria.getCampaignId();
        if (criteria.getOrderIdOption().isDefined())
            orderId = criteria.getOrderId();
        if (criteria.getEntryIdOption().isDefined())
            entryId = criteria.getEntryId();
        if (criteria.getAdvertiserIdOption().isDefined())
            advertiserId = criteria.getAdvertiserId();

        if (criteria.getStatusOption().isDefined() && !criteria.getStatus().isEmpty()) {
            status.addAll(AdStatusEnumConverter.fromThrift(criteria.getStatus()));
        }
        if (criteria.getNetworkOption().isDefined() && !criteria.getNetwork().isEmpty()) {
            network.addAll(NetworkEnumConverter.fromThrift(criteria.getNetwork()));
        }
        if (criteria.getBidTypeOption().isDefined() && !criteria.getBidType().isEmpty()) {
            bidType.addAll(BidTypeEnumConverter.fromThrift(criteria.getBidType()));
        }
        start = pagination.getStart();
        count = pagination.getCount();
    }

    @Override
    public List<TAdItem> applyE() {
        List<TAdItem> res = new ArrayList<TAdItem>();
        if (entryId != 0) {
            res.add(getAdItemByEntryId(entryId));
        } else if (orderId != 0) {
            res.addAll(getAdItemsByOrderId(orderId));
        } else if (campaignId != 0) {
            res.addAll(getAdItemsByCampaignId(campaignId));
        } else if (advertiserId != 0) {
            res.addAll(getAdItemsByAdvertiserId(advertiserId));
        } else {
            res.addAll(getAdItemsByCondntion(network, bidType));
        }
        if (res.size() < start || res.isEmpty())
            return new ArrayList<TAdItem>();
        return res.subList(start, Math.min(res.size(), start + count));
    }

    private TAdItem getAdItemByEntryId(int adEntryId) {
        AdEntry adEntry = adEntryService.getEntryById(adEntryId);
        if (adEntry != null) {
            AdOrder adOrder = adOrderService.getOrderById(adEntry.getOrderId());
            if (adOrder != null) {
                AdCampaign adCampaign = adCampaignService.getAdCampaignById(adEntry.getCampaignId());
                if (adCampaign != null && verifyCondition(adCampaign) && verifyStatus(adEntry, adOrder, adCampaign))
                    return AdItemConverter.toThrift(getAdItem(adEntry, adOrder, adCampaign));
            }
        }
        return null;
    }

    private List<TAdItem> getAdItemsByOrderId(int adOrderId) {
        List<TAdItem> res = new ArrayList<TAdItem>();
        AdOrder adOrder = adOrderService.getOrderById(adOrderId);
        if (adOrder != null) {
            AdCampaign adCampaign = adCampaignService.getAdCampaignById(adOrder.getCampaignId());
            if (adCampaign != null  && verifyCondition(adCampaign)) {
                List<AdEntry> adEntrys = adEntryService.listEntriesByOrderId(adOrderId);
                for (AdEntry adEntry : adEntrys) {
                    if (verifyStatus(adEntry, adOrder, adCampaign)) {
                        res.add(AdItemConverter.toThrift(getAdItem(adEntry, adOrder, adCampaign)));
                    }
                }
            }
        }
        return res;
    }

    private List<TAdItem> getAdItemsByCampaignId(int adCampaignId) {
        List<TAdItem> res = new ArrayList<TAdItem>();
        AdCampaign adCampaign = adCampaignService.getAdCampaignById(adCampaignId);
        if (adCampaign != null) {
            List<AdOrder> adOrders = adOrderService.getAdOrdersByCampaignId(adCampaign.getId());
            if (!adOrders.isEmpty()) {
                List<Integer> orderIds = getOrderIds(adOrders);
                List<AdEntry> adEntrys = adEntryService.listEntriesByOrderIds(orderIds);
                List<AdCampaign> adCampaigns = new ArrayList<AdCampaign>();
                adCampaigns.add(adCampaign);
                res.addAll(addAdItems(adCampaigns, adOrders, adEntrys, true));
            }
        }
        return res;
    }

    private List<TAdItem> getAdItemsByAdvertiserId(int adAdvertiserId) {
        List<TAdItem> res = new ArrayList<TAdItem>();
        // 取出该广告主下所有的活动
        List<AdCampaign> adCampaigns = adCampaignService.getCampaignsByAdvertiserId(adAdvertiserId);
        if (!adCampaigns.isEmpty()) {
            List<Integer> campaignIds = getCampaignIds(adCampaigns);
            // 根据活动id列表取出所有的广告订单
            List<AdOrder> adOrders = adOrderService.getAdOrdersByCampaignIds(campaignIds);
            if (!adOrders.isEmpty()) {
                List<Integer> orderIds = getOrderIds(adOrders);
                // 根据广告订单id列表,取出所有的广告
                List<AdEntry> adEntrys = adEntryService.listEntriesByOrderIds(orderIds);
                res.addAll(addAdItems(adCampaigns, adOrders, adEntrys, true));
            }
        }
        return res;
    }

    private List<TAdItem> getAdItemsByCondntion(Set<AdNetworkEnum> networks, Set<BidTypeEnum> bidTypes) {
        List<TAdItem> res = new ArrayList<TAdItem>();
        List<AdOrder> adOrders = adOrderService.getAdOrdersByCondition(new ArrayList<AdNetworkEnum>(networks),
                new ArrayList<BidTypeEnum>(bidTypes));
        if (!adOrders.isEmpty()) {
            List<Integer> orderIds = new ArrayList<Integer>();
            List<Integer> campaignIds = new ArrayList<Integer>();
            for (AdOrder adOrder : adOrders) {
                orderIds.add(adOrder.getId());
                campaignIds.add(adOrder.getCampaignId());
            }
            // 根据广告订单id列表,取出所有的广告
            List<AdEntry> adEntrys = adEntryService.listEntriesByOrderIds(orderIds);
            List<AdCampaign> adCampaigns = adCampaignService.getCampaigns(campaignIds);
            res.addAll(addAdItems(adCampaigns, adOrders, adEntrys, false));
        }
        return res;
    }

    /**
     * 根据adCampgaign列表,adOrder列表和adEntry列表,进行检测，然后填充到结果集中 考虑到目前数据量比较小,暂时采用n的3次方时间复杂度计算
     *
     * @param res
     * @param adCampaigns
     * @param adOrders
     * @param adEntrys
     * @param conditionCheck
     */
    private List<TAdItem> addAdItems(List<AdCampaign> adCampaigns, List<AdOrder> adOrders, List<AdEntry> adEntrys,
                                     boolean conditionCheck) {
        List<TAdItem> res = new ArrayList<TAdItem>();
        for (AdCampaign adCampaign : adCampaigns) {
            for (AdOrder adOrder : adOrders) {
                for (AdEntry adEntry : adEntrys) {
                    boolean firstCheck = adCampaign.getId() == adOrder.getCampaignId() &&
                            adOrder.getId() == adEntry.getOrderId() &&
                            verifyStatus(adEntry, adOrder, adCampaign);
                    if (firstCheck && (!conditionCheck || (conditionCheck && verifyCondition(adCampaign)))) {
                        res.add(AdItemConverter.toThrift(getAdItem(adEntry, adOrder, adCampaign)));
                    }
                }
            }
        }
        return res;
    }

    /**
     * 取出orderId列表
     *
     * @param adOrders
     * @return
     */
    private List<Integer> getOrderIds(List<AdOrder> adOrders) {
        List<Integer> orderIds = new ArrayList<Integer>();
        for (AdOrder adOrder : adOrders) {
            orderIds.add(adOrder.getId());
        }

        return orderIds;
    }

    /**
     * 取出campaignId列表
     *
     * @param adCampaigns
     * @return
     */
    private List<Integer> getCampaignIds(List<AdCampaign> adCampaigns) {
        List<Integer> campaignIds = new ArrayList<Integer>();
        for (AdCampaign adOrder : adCampaigns) {
            campaignIds.add(adOrder.getId());
        }

        return campaignIds;
    }

    /**
     * 计算出adItem的状态,并判断是否满足查询条件中的status条件
     *
     * @param adEntry
     * @param adOrder
     * @param adCampaign
     * @return
     */
    private boolean verifyStatus(AdEntry adEntry, AdOrder adOrder, AdCampaign adCampaign) {
        return status.isEmpty() || (!status.isEmpty() &&
                status.contains(getAdItemStatus(adEntry, adOrder, adCampaign)));
    }

    /**
     * 检查是否满足查询条件中的network和bidType条件
     *
     * @param adOrder
     * @return
     */
    private boolean verifyCondition(AdCampaign adCampaign) {
        return ((!network.isEmpty() &&
                network.containsAll(adCampaign.getNetwork())) || network.isEmpty()) &&
                ((!bidType.isEmpty() && bidType.contains(adCampaign.getBidType())) || bidType.isEmpty());
    }

    /**
     * 根据adCampaign,adOrder和anEntry计算出adItem的状态
     *
     * @param adEntry
     * @param adOrder
     * @param adCampaign
     * @return
     */
    private AdStatusEnum getAdItemStatus(AdEntry adEntry, AdOrder adOrder, AdCampaign adCampaign) {
        if (!adCampaign.getStatus().equals(AdStatusEnum.ENABLED))
            return adCampaign.getStatus();
        else {
            if (!adOrder.getStatus().equals(AdStatusEnum.ENABLED))
                return adOrder.getStatus();
            else
                return adEntry.getStatus();
        }
    }

    /**
     * 根据adCampaign,adOrder和anEntry组装出adItem
     *
     * @param adEntry
     * @param adOrder
     * @param adCampaign
     * @return
     */
    private AdItem getAdItem(AdEntry adEntry, AdOrder adOrder, AdCampaign adCampaign) {
        AdItem adItem = new AdItem();
        adItem.setEntryId(adEntry.getId());
        adItem.setAdvertiserId(adEntry.getAdvertiserId());
        adItem.setCampaignId(adEntry.getCampaignId());
        adItem.setOrderId(adEntry.getOrderId());
        // 设置item状态
        adItem.setStatus(getAdItemStatus(adEntry, adOrder, adCampaign));
        adItem.setLink(adEntry.getLink());
        adItem.setDescription(adEntry.getDescription());
        adItem.setResourceType(adEntry.getResourceType());
        adItem.setResourceUrl(adEntry.getResourceUrl());
        adItem.setDisplayUrl(adEntry.getDisplayUrl());
        adItem.setTitle(StringUtils.defaultIfEmpty(adEntry.getTitle(),adEntry.getName()));
        adItem.setResourceSize(adEntry.getSize());

        adItem.setDestination(adEntry.getDestination());
        adItem.setAudienceCategories(adOrder.getAudienceCategories());

        adItem.setNetwork(adCampaign.getNetwork());
        adItem.setBidType(adCampaign.getBidType());
        adItem.setBidPrice(adOrder.getBidPrice());
        adItem.setStartDate(adOrder.getStartDate());
        if (adOrder.getEndDate() != null)
            adItem.setEndDate(DateTimeUtil.plusDays(adOrder.getEndDate(), 1));
        adItem.setScheduleDay(adOrder.getScheduleDay());
        if (!StringUtils.isBlank(adOrder.getScheduleTimeStr())) {
            Set<ScheduleTime> scheduleTimes = new HashSet<ScheduleTime>();
            scheduleTimes.add(adOrder.getScheduleTime());
            adItem.setScheduleTime(scheduleTimes);
        }
        adItem.setKeywords(adOrder.getKeywords());
        adItem.setOrderFrequency(adOrder.getOrderFrequency());
        adItem.setEntryFrequency(adOrder.getEntryFrequency());
        adItem.setAdsType(adOrder.getAdsType());
        if (!StringUtils.isBlank(adOrder.getChannelsTarget())) {
            Set<String> channelSet = new HashSet<String>();
            String[] channelids = adOrder.getChannelsTarget().split(",");
            for (String idStr : channelids) {
                Channel channel = channelService.getChannelById(Integer.valueOf(idStr).intValue());
                if (channel != null) {
                    channelSet.add(channel.getDomain());
                }
            }
            adItem.setChannels(channelSet);
        }

        adItem.setLocations(ProvinceNameConverter.toStringSet(adCampaign.getLocations()));

        return adItem;
    }
}
