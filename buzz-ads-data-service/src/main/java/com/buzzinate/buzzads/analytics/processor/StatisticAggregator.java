package com.buzzinate.buzzads.analytics.processor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.buzzinate.buzzads.analytics.AdClick;
import com.buzzinate.buzzads.analytics.AdPageView;
import com.buzzinate.buzzads.analytics.AdShowUps;
import com.buzzinate.buzzads.analytics.stats.AdBasicStatistic;
import com.buzzinate.buzzads.analytics.stats.AdCampaignDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.AdDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.AdOrderDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.AdminDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.ChannelDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.PublisherDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.enums.AdStatisticType;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdEntryService;
import com.buzzinate.buzzads.core.service.AdOrderService;
import com.buzzinate.buzzads.core.service.ChannelService;
import com.buzzinate.buzzads.core.service.CpcClickSetService;
import com.buzzinate.buzzads.core.service.PublisherContactService;
import com.buzzinate.buzzads.core.util.UrlUtil;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdDetailCps;
import com.buzzinate.buzzads.domain.AdEntry;
import com.buzzinate.buzzads.domain.AdOrder;
import com.buzzinate.buzzads.domain.Channel;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.buzzads.enums.TradeConfirmEnum;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.UuidUtil;
import com.buzzinate.common.util.string.StringUtil;

/**
 * statistic aggregator.
 *
 * @author johnson
 */
public class StatisticAggregator {

    // 默认分成比例
    private Map<String, AdDailyStatistic> adDailyMap = new HashMap<String, AdDailyStatistic>();
    private Map<String, PublisherDailyStatistic> publishierDailyMap = new HashMap<String, PublisherDailyStatistic>();
    private Map<String, AdminDailyStatistic> adminDailyMap = new HashMap<String, AdminDailyStatistic>();
    private Map<String, AdOrderDailyStatistic> adOrderDailyMap = new HashMap<String, AdOrderDailyStatistic>();
    private Map<String, AdCampaignDailyStatistic> adCampaignDailyMap = new HashMap<String, AdCampaignDailyStatistic>();
    private Map<String, ChannelDailyStatistic> channelDailyMap = new HashMap<String, ChannelDailyStatistic>();
    private AdEntryService adEntryService;
    private AdOrderService adOrderService;
    private AdCampaignService adCampaignService;
    private CpcClickSetService cpcClickSetService;
    private ChannelService channelService;
    
    private PublisherContactService publisherContactService;

    private Map<Long, AdEntry> adEntryCache;
    private Map<Integer, AdOrder> adOrderCache;
    private Map<Integer, AdCampaign> adCampaignCache;

    /**
     * aggregator construtor.
     *
     * @param adEntryService
     * @param adOrderService
     * @param adCampaignService
     * @param cpcClickSetService
     * @param channelService
     */
    public StatisticAggregator(AdEntryService adEntryService,
            AdOrderService adOrderService, AdCampaignService adCampaignService,
            CpcClickSetService cpcClickSetService, ChannelService channelService,
            PublisherContactService publisherContactService) {
        this.adEntryService = adEntryService;
        this.adOrderService = adOrderService;
        this.adCampaignService = adCampaignService;
        this.cpcClickSetService = cpcClickSetService;
        this.channelService = channelService;
        this.publisherContactService = publisherContactService;
        
        this.adEntryCache = new HashMap<Long, AdEntry>(1000);
        this.adOrderCache = new HashMap<Integer, AdOrder>(1000);
        this.adCampaignCache = new HashMap<Integer, AdCampaign>(1000);
    }

    /**
     * handle stat.
     *
     * @param o showup, click, cps
     */
    public void handleStats(Object o) {
        if (o instanceof AdShowUps) {
            handleShowStats((AdShowUps) o);
        } else if (o instanceof AdClick) {
            handleClickStats((AdClick) o);
        } else if (o instanceof AdDetailCps) {
            handleCpsStats((AdDetailCps) o);
        } else if (o instanceof AdPageView) {
            handlePVStats((AdPageView) o);
        }
    }

    /**
     * handle pv stats
     *
     * @param pv
     */
    private void handlePVStats(AdPageView pv) {
        String publisherUuid = pv.getPublisherUuid();
        List<Channel> channels = channelService.listChannelByUuid(publisherUuid);
        for (Channel channel : channels) {
            if (channel.getLevel() == 1 || UrlUtil.isUrlUnderDomain(pv.getSourceUrl(), channel.getDomain())) {
                insertOrUpdateChannelDailyInMap(channel.getId(), pv.getCreateAt(), AdStatisticType.PAGEVIEW);
            }
        }
        this.insertOrUpdatePublishierDailyClickInMap(pv.getPublisherUuid(), pv.getNetwork(),
                pv.getCreateAt(), AdStatisticType.PAGEVIEW, null);
    }

    /**
     * handle show up stats.
     *
     * @param adShowups show up
     */
    private void handleShowStats(AdShowUps adShowups) {
        List<Long> adEntrys = adShowups.getAdEntryIds();
        for (Long adEntryId : adEntrys) {
            AdEntry entry = adEntryCache.get(adEntryId);
            if (entry == null) {
                entry = adEntryService.getEntryById(adEntryId.intValue());
                if (entry != null) {
                    adEntryCache.put(adEntryId, entry);
                } else {
                    continue;
                }
            }

            AdOrder order = adOrderCache.get(Integer.valueOf(entry.getOrderId()));
            if (order == null) {
                order = adOrderService.getOrderById(entry.getOrderId());
                if (order != null) {
                    adOrderCache.put(Integer.valueOf(entry.getOrderId()), order);
                } else {
                    continue;
                }
            }
            AdCampaign camp = adCampaignCache.get(Integer.valueOf(order.getCampaignId()));
            if (camp == null) {
                camp = adCampaignService.getAdCampaignByIdWithoutBudget(order.getCampaignId());
                if (camp != null) {
                    adCampaignCache.put(Integer.valueOf(order.getCampaignId()), camp);
                } else {
                    continue;
                }
            }

            // add cpm statistic
            AdBasicStatistic adStatistic = null;
            AdStatisticType statisticType = AdStatisticType.VIEW;
            // to adapt the below five method
            if (camp.getBidType() == BidTypeEnum.CPM) {
                adStatistic = new AdBasicStatistic();
                statisticType = AdStatisticType.CPM_VIEW;
                // 计算cpm单次点击的佣金()
                BigDecimal cpmTotalComm = new BigDecimal(order.getBidPrice()).divide(new BigDecimal(100000));
                adStatistic.setCpmTotalCommission(cpmTotalComm);
                int proportion = publisherContactService.getPublisherProportionByUUID(adShowups.getPublisherUuid());
                adStatistic.setCpmPubCommission(cpmTotalComm.multiply(new BigDecimal(proportion / 100.0)));
            } else if (camp.getBidType() == BidTypeEnum.CPC) {
                statisticType = AdStatisticType.CPC_VIEW;
            } else if (camp.getBidType() == BidTypeEnum.CPS) {
                statisticType = AdStatisticType.CPS_VIEW;
            }

            List<Channel> channels = channelService.listChannelByUuid(adShowups.getPublisherUuid());
            for (Channel channel : channels) {
                if (channel.getLevel() == 1 || UrlUtil.isUrlUnderDomain(adShowups.getSourceUrl(), channel.getDomain())) {
                    insertOrUpdateChannelDailyInMap(channel.getId(), adShowups.getCreateAt(), AdStatisticType.VIEW);
                }
            }

            this.insertOrUpdateAdDailyInMap(entry.getId(), adShowups.getNetwork(), UuidUtil.uuidToByteArray(adShowups.getPublisherUuid()),
                    adShowups.getCreateAt(), entry.getOrderId(), statisticType, adStatistic);
            this.insertOrUpdateAdOrderDailyInMap(entry.getOrderId(), adShowups.getNetwork(),
                    adShowups.getCreateAt(), statisticType, adStatistic);
            this.insertOrUpdateAdCampaignDailyInMap(entry.getCampaignId(), entry.getAdvertiserId(),
                    adShowups.getNetwork(), adShowups.getCreateAt(), statisticType, adStatistic);
            this.insertOrUpdatePublishierDailyClickInMap(adShowups.getPublisherUuid(), adShowups.getNetwork(),
                    adShowups.getCreateAt(), statisticType, adStatistic);
            this.insertOrUpdateAdminDailyClickInMap(adShowups.getNetwork(), adShowups.getCreateAt(),
                    statisticType, adStatistic);
        }
    }

    /**
     * handle click stats.
     *
     * @param adClick ad click
     */
    private void handleClickStats(AdClick adClick) {
        Long adEntryId = adClick.getAdEntryId();

        AdEntry entry = adEntryCache.get(adEntryId);
        if (entry == null) {
            entry = adEntryService.getEntryById(adEntryId.intValue());
            if (entry != null) {
                adEntryCache.put(Long.valueOf(adEntryId), entry);
            } else {
                return;
            }
        }

        AdOrder order = adOrderCache.get(Integer.valueOf(entry.getOrderId()));
        if (order == null) {
            order = adOrderService.getOrderById(entry.getOrderId());
            if (order != null) {
                adOrderCache.put(Integer.valueOf(entry.getOrderId()), order);
            } else {
                return;
            }
        }
        AdCampaign camp = adCampaignCache.get(Integer.valueOf(order.getCampaignId()));
        if (camp == null) {
            camp = adCampaignService.getAdCampaignByIdWithoutBudget(order.getCampaignId());
            if (camp != null) {
                adCampaignCache.put(Integer.valueOf(order.getCampaignId()), camp);
            } else {
                return;
            }
        }

        // add to CpcClickSet for CPC calculation
        AdStatisticType statisticType = AdStatisticType.CLICK;
        if (camp.getBidType() == BidTypeEnum.CPM) {
            statisticType = AdStatisticType.CPM_CLICK;
        } else if (camp.getBidType() == BidTypeEnum.CPC) {
            statisticType = AdStatisticType.CPC_CLICK;
            cpcClickSetService.addCpcClick(adClick);
        } else if (camp.getBidType() == BidTypeEnum.CPS) {
            statisticType = AdStatisticType.CPS_CLICK;
        }

        List<Channel> channels = channelService.listChannelByUuid(adClick.getPublisherUuid());
        for (Channel channel : channels) {
            if (channel.getLevel() == 1 || UrlUtil.isUrlUnderDomain(adClick.getSourceUrl(), channel.getDomain())) {
                insertOrUpdateChannelDailyInMap(channel.getId(), adClick.getCreateAt(), AdStatisticType.CLICK);
            }
        }

        this.insertOrUpdateAdDailyInMap(entry.getId(), adClick.getNetwork(),UuidUtil.uuidToByteArray(adClick.getPublisherUuid()),
                adClick.getCreateAt(), entry.getOrderId(), statisticType, null);
        this.insertOrUpdateAdOrderDailyInMap(entry.getOrderId(), adClick.getNetwork(),
                adClick.getCreateAt(), statisticType, null);
        this.insertOrUpdateAdCampaignDailyInMap(entry.getCampaignId(), entry.getAdvertiserId(),
                adClick.getNetwork(), adClick.getCreateAt(), statisticType, null);
        this.insertOrUpdatePublishierDailyClickInMap(adClick.getPublisherUuid(), adClick.getNetwork(),
                adClick.getCreateAt(), statisticType, null);
        this.insertOrUpdateAdminDailyClickInMap(adClick.getNetwork(), adClick.getCreateAt(),
                statisticType, null);
    }

    /**
     * handle cps stats.
     *
     * @param cps cps
     */
    private void handleCpsStats(AdDetailCps cps) {
        AdBasicStatistic adStatistic = new AdBasicStatistic();


        AdEntry entry = adEntryService.getEntryById(cps.getAdEntryId());
        if (entry == null) return;

        AdStatisticType type = AdStatisticType.CPS_FINISHED;
        Date date = new Date();
        if (cps.getStatus() == TradeConfirmEnum.NO_CONFIRM) {
            adStatistic.setCpsPubCommission(cps.getPubComm());
            adStatistic.setCpsTotalCommission(cps.getComm());
            adStatistic.setCpsTotalPrice(cps.getTotalPrice());
            date = cps.getTradeTime();
        } else if (cps.getStatus() == TradeConfirmEnum.CONFIRM_OK) {
            type = AdStatisticType.CPS_CONFIRMED;
            adStatistic.setCpsConfirmedCommission(cps.getPubComm());
            adStatistic.setCpsTotalConfirmedCommission(cps.getComm());
            date = cps.getConfirmTime();
        }

        this.insertOrUpdateAdDailyInMap(cps.getAdEntryId(), cps.getNetwork(), UuidUtil.uuidToByteArray(cps.getUuid()), date,
                        cps.getAdOrderId(), type, adStatistic);
        this.insertOrUpdateAdOrderDailyInMap(cps.getAdOrderId(), cps.getNetwork(), date, 
                        type, adStatistic);
        this.insertOrUpdateAdCampaignDailyInMap(entry.getCampaignId(), entry.getAdvertiserId(), 
                        cps.getNetwork(), date, type, adStatistic);
        this.insertOrUpdatePublishierDailyClickInMap(cps.getUuid(), cps.getNetwork(), date, 
                        type, adStatistic);
        this.insertOrUpdateAdminDailyClickInMap(cps.getNetwork(), date, type, adStatistic);
    }

    /**
     * increase ad stat in map.
     * @param adEntryId ad entry id
     * @param network network
     * @param uuid  站长uuid
     * @param date date
     * @param adOrderId ad order id
     * @param type type
     * @param currency currency or 0
     */
    private void insertOrUpdateAdDailyInMap(int adEntryId,
            AdNetworkEnum network, byte[] uuid, Date date,
            int adOrderId, AdStatisticType type,
            AdBasicStatistic adStatistic) {
        StringBuffer key = new StringBuffer(DateTimeUtil.formatDate(date))
                .append("_").append(network.getCode())
                .append("_").append(adEntryId);
        AdDailyStatistic statis = adDailyMap.get(key.toString());
        if (statis == null) {
            statis = new AdDailyStatistic(date, adEntryId, network, adOrderId);
            statis.setUuid(uuid);
            adDailyMap.put(key.toString(), statis);
        }
        statis.increaseStats(adStatistic, type);
    }

    /**
     * increase order stats in map
     *
     * @param adOrderId
     * @param network
     * @param date
     * @param type
     * @param adStatistic
     */
    private void insertOrUpdateAdOrderDailyInMap(int adOrderId, AdNetworkEnum network, Date date,
                                                 AdStatisticType type, AdBasicStatistic adStatistic) {
        StringBuffer key = new StringBuffer(DateTimeUtil.formatDate(date))
                .append("_").append(network.getCode())
                .append("_").append(adOrderId);
        AdOrderDailyStatistic statis = adOrderDailyMap.get(key.toString());
        if (statis == null) {
            statis = new AdOrderDailyStatistic(date, network, adOrderId);
            adOrderDailyMap.put(key.toString(), statis);
        }
        statis.increaseStats(adStatistic, type);
    }

    /**
     * increase campaign stats in map
     *
     * @param adCampaignId
     * @param advertiserId
     * @param network
     * @param date
     * @param type
     * @param adStatistic
     */
    private void insertOrUpdateAdCampaignDailyInMap(int adCampaignId, int advertiserId, AdNetworkEnum network,
                                                    Date date, AdStatisticType type, AdBasicStatistic adStatistic) {
        StringBuffer key = new StringBuffer(DateTimeUtil.formatDate(date))
                .append("_").append(network.getCode())
                .append("_").append(adCampaignId);
        AdCampaignDailyStatistic stats = adCampaignDailyMap.get(key.toString());
        if (stats == null) {
            stats = new AdCampaignDailyStatistic(adCampaignId, date, network, advertiserId);
            adCampaignDailyMap.put(key.toString(), stats);
        }
        stats.increaseStats(adStatistic, type);
    }

    /**
     * increase publish stat in map.
     *
     * @param uuid        publisher uuid
     * @param network     ad network
     * @param date        date
     * @param type        stat type
     * @param adStatistic AdBasicStatistic
     */
    private void insertOrUpdatePublishierDailyClickInMap(String uuid, AdNetworkEnum network,
                                                         Date date, AdStatisticType type, AdBasicStatistic adStatistic) {
        StringBuffer key = new StringBuffer(uuid).append("_")
                .append(DateTimeUtil.formatDate(date))
                .append("_").append(network.getCode());
        PublisherDailyStatistic statis = publishierDailyMap.get(key.toString());
        if (statis == null) {
            statis = new PublisherDailyStatistic(date, StringUtil.uuidToByteArray(uuid), network);
            publishierDailyMap.put(key.toString(), statis);
        }
        statis.increaseStats(adStatistic, type);
    }

    /**
     * increase channel stat in map
     */
    private void insertOrUpdateChannelDailyInMap(int channelId, Date date, AdStatisticType type) {
        String key = channelId + "_" + DateTimeUtil.formatDate(date);
        ChannelDailyStatistic stats = channelDailyMap.get(key);
        if (stats == null) {
            stats = new ChannelDailyStatistic(channelId, date);
            channelDailyMap.put(key, stats);
        }
        stats.increaseStats(type);
    }

    /**
     * increase admin stats in map.
     *
     * @param network     ad network
     * @param date        data
     * @param type        stat type
     * @param adStatistic AdBasicStatistic
     */
    private void insertOrUpdateAdminDailyClickInMap(
            AdNetworkEnum network, Date date, AdStatisticType type, AdBasicStatistic adStatistic) {
        StringBuffer key = new StringBuffer(DateTimeUtil.formatDate(date))
                .append("_").append(network.getCode());
        AdminDailyStatistic statis = adminDailyMap.get(key.toString());
        if (statis == null) {
            statis = new AdminDailyStatistic(date, network);
            adminDailyMap.put(key.toString(), statis);
        }
        statis.increaseStats(adStatistic, type);
    }

    public Map<String, AdDailyStatistic> getAdDailyMap() {
        return adDailyMap;
    }

    public Map<String, PublisherDailyStatistic> getPublishierDailyMap() {
        return publishierDailyMap;
    }

    public Map<String, AdminDailyStatistic> getAdminDailyMap() {
        return adminDailyMap;
    }

    public Map<String, AdOrderDailyStatistic> getAdOrderDailyMap() {
        return adOrderDailyMap;
    }

    public Map<String, AdCampaignDailyStatistic> getAdCampaignDailyMap() {
        return adCampaignDailyMap;
    }

    public Map<String, ChannelDailyStatistic> getChannelDailyMap() {
        return channelDailyMap;
    }

}
