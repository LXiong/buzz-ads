package com.buzzinate.buzzads.billing.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.StatelessSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.buzzinate.buzzads.analytics.stats.AdCampaignDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.AdDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.AdOrderDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.AdminDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.PublisherDailyStatistic;
import com.buzzinate.buzzads.billing.dao.CpcStatsUpdateDao;
import com.buzzinate.buzzads.billing.dao.CpcStatsUpdateDao.Command;
import com.buzzinate.buzzads.core.bean.CpcTimeSegment;
import com.buzzinate.buzzads.core.service.AdCampaignBudgetService;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdEntryService;
import com.buzzinate.buzzads.core.service.AdOrderService;
import com.buzzinate.buzzads.core.service.AdvertiserBalanceService;
import com.buzzinate.buzzads.core.service.CpcClickSetService;
import com.buzzinate.buzzads.core.service.CpcClickSetService.CpcMetaTuple;
import com.buzzinate.buzzads.core.service.EventServices;
import com.buzzinate.buzzads.core.service.PublisherContactService;
import com.buzzinate.buzzads.domain.AdCampBudget;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdEntry;
import com.buzzinate.buzzads.domain.AdOrder;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.common.util.UuidUtil;


/**
 * Cpc statistic processing task class.
 * 
 * @author zyeming
 *
 */
public final class CpcStatsTask {
    
    private static Log log = LogFactory.getLog(CpcStatsTask.class);
    
    @Autowired
    private AdEntryService adEntryService;
    @Autowired
    private AdOrderService adOrderService;
    @Autowired
    private AdCampaignService adCampaignService;
    @Autowired
    private AdCampaignBudgetService adCampaignBudgetService;
    @Autowired
    private AdvertiserBalanceService advertiserBalanceService;
    @Autowired
    private EventServices eventServices;
    @Autowired
    private CpcStatsUpdateDao cpcStatsUpdateDao;
    @Autowired
    private CpcClickSetService cpcClickSetService;
    @Autowired
    private PublisherContactService publisherContactService;
    
    public void start() {
        log.info("Start cpc stats process task.");
        
        // 处理上一个已完成的时间段的CPC数据和Budget
        CpcTimeSegment prevSegment = CpcTimeSegment.getPreviousSegment();
        Set<Integer> processedCampaignIds = processFinishedCpcSegment(prevSegment);
        
        // 处理当前未完成时间段的Budget
        CpcTimeSegment currSegment = CpcTimeSegment.getNextSegment(prevSegment);
        processCurrentCpcSegment(currSegment, processedCampaignIds);
        
        log.info("Finish cpc stats process task.");
    }
    
    
    public Set<Integer> processFinishedCpcSegment(CpcTimeSegment prevSegment) {
        log.info("Start processing last cpc time segment.");
        Map<Integer, Integer> advertiserDebits = new HashMap<Integer, Integer>();
        Set<Integer> processedCampaignIds = new HashSet<Integer>();
        
        // 获取所有有更新的广告、站长和Network组，在一个事务中更新CPC数据。
        List<CpcMetaTuple> metaList = cpcClickSetService.getMetaList(prevSegment);
        log.info("Number of cpc meta tuples: " + metaList.size());
        for (CpcMetaTuple tuple : metaList) {
            AdEntry entry = adEntryService.getEntryById(tuple.getEntryId());
            if (entry == null) {
                log.warn("Invalid entry for tuple: " + cpcClickSetService.convertCpcMetaKey(tuple));
                continue;
            }
            AdOrder order = adOrderService.getOrderById(entry.getOrderId());
            if (order == null) {
                log.warn("Invalid order for tuple: " + cpcClickSetService.convertCpcMetaKey(tuple) + 
                                ", orderId: " + entry.getOrderId());
                continue;
            }
            
            AdCampaign camp = adCampaignService.getAdCampaignById(entry.getCampaignId());
            if (camp == null) {
                log.warn("Invalid camp for tuple: " + cpcClickSetService.convertCpcMetaKey(tuple) + 
                        ", campId: " + entry.getCampaignId());
                continue;
            } else if (camp.getBidType() != BidTypeEnum.CPC) {
                log.warn("No an CPC camp. tuple: " + cpcClickSetService.convertCpcMetaKey(tuple) + 
                        ", campId: " + entry.getCampaignId());
                continue;
            }
            
            processedCampaignIds.add(order.getCampaignId());
            
            int cpcClicks = cpcClickSetService.getCpcClicks(prevSegment, tuple);
            log.info(prevSegment.toString() + "-" + tuple.toString() + " " + "cpcClicks: " + cpcClicks);
            int comm = cpcClicks * order.getBidPrice();
            int proportion = publisherContactService.getPublisherProportionByUUID(tuple.getPublisherUuid());
            int pubComm = comm * proportion / 100;
            
            try {
                updateStats(prevSegment, tuple, order, cpcClicks, comm, pubComm);
                // 清空活动开销缓存，以免获取Budget时重复计算
                adCampaignBudgetService.delSegmentCampCostCache(order.getCampaignId(), prevSegment);
                // 处理成功，移除对应的Meta条目和CpcClick集合
                cpcClickSetService.remMeta(prevSegment, tuple);
                cpcClickSetService.delCpcClicks(prevSegment, tuple);
                
                // 统计广告主在这个时间段的开销
                Integer debits = advertiserDebits.get(order.getAdvertiserId());
                if (debits == null) {
                    advertiserDebits.put(order.getAdvertiserId(), comm);
                } else {
                    advertiserDebits.put(order.getAdvertiserId(), debits + comm);
                }
            } catch (Exception e) {
                // 事务出错回滚，此Tuple留在缓存中下次Retry
                log.error("Failed to handle CPC click: " + cpcClickSetService.convertCpcMetaKey(tuple), e);
                continue;
            }
        }
        
        log.info("Number of advertisers: " + advertiserDebits.size());
        for (Map.Entry<Integer, Integer> entry : advertiserDebits.entrySet()) {
            int advertiserId = entry.getKey();
            int debits = entry.getValue();
            // 更新广告主的balance，并且生成一条billing记录
            try {
                advertiserBalanceService.updateBillingAndBalanceForDebits(advertiserId, debits, prevSegment.getDay());
            } catch (Exception e) {
                log.error("Failed to update advertiser billing and balance. [AdvertiserId=" + advertiserId +
                                ", Debits=" + debits, e);
                continue;
            }
        }
        log.info("Finish processing last cpc time segment.");
        
        return processedCampaignIds;
    }

    
    public void processCurrentCpcSegment(CpcTimeSegment currSegment, Set<Integer> processedCampaignIds) {
        log.info("Start processing current cpc time segment.");
        // 计算当前时间段每个Campaign的开销
        Map<Integer, Integer> campaignCpcMap = getCampaignCostInSegment(currSegment);
        // 将上个时间段的Campaign也处理下，进行Suspend等操作
        for (int campId : processedCampaignIds) {
            if (campaignCpcMap.containsKey(campId)) {
                campaignCpcMap.put(campId, 0);
            }
        }
        
        // 记录会被Suspend的Campaign
        Set<Integer> suspendCampaignIds = new TreeSet<Integer>();
        // 记录会被预警的Campaign
        Set<Integer> warningCampaignIds = new TreeSet<Integer>();
        // 统计每个广告主的实时余额
        Map<Integer, Long> advertiserBalances = new HashMap<Integer, Long>();
        
        // 依次处理每个Campaign
        log.info("Number of campaigns: " + campaignCpcMap.size());
        for (Map.Entry<Integer, Integer> campaignCpcCost : campaignCpcMap.entrySet()) {
            int campaignId = campaignCpcCost.getKey();
            int cost = campaignCpcCost.getValue();
            
            AdCampaign campaign = adCampaignService.getAdCampaignById(campaignId);
            if (campaign == null) {
                log.warn("Invalid campaign. CampaignId: " + campaignId);
                continue;
            }
            
            // 更新活动开销缓存
            adCampaignBudgetService.setSegmentCampCostCache(campaignId, currSegment, cost);
            
            // 计算广告活动实时的当日预算和总预算，此时上个Segment的Budget已经更新进DB了（除非出错下次再重试）
            AdCampBudget campBudget = adCampaignBudgetService.getDayCampBudget(campaignId, currSegment.getDay());
            campBudget.addCost(currSegment.getDay(), cost);
            
            if (campBudget.isExceedBudget()) {
                // 预算不足
                log.info("Campaign suspended. [id=" + campaignId + ", name=" + campaign.getName() + "]");
                suspendCampaignIds.add(campaignId);
            } else if (BudgetUtils.isBudgetWarning(campBudget)) {
                // 预算预警
                log.info("Campaign budget warning. [id=" + campaignId + ", name=" + campaign.getName() + "]");
                warningCampaignIds.add(campaignId);
            }
            
            // 更新广告主的最新余额，即余额减去当前时间段广告主所有活动的开销
            updateAdvertiserCurrentBalance(advertiserBalances, cost, campaign.getAdvertiserId());
        }
        
        // 将余额不足的广告主的活动加入Suspend列表
        log.info("Number of advertiser: " + advertiserBalances.size());
        for (Map.Entry<Integer, Long> entry : advertiserBalances.entrySet()) {
            int advertiserId = entry.getKey();
            long balance = entry.getValue();
            if (balance < 0) {
                // 余额不足
                List<Integer> campIds = adCampaignService.listCampaignIdsByAdvertiserId(advertiserId);
                log.info("Advertiser out of balance. [id=" + advertiserId + ", numberOfCampaigns=" + 
                                campIds.size() + "]");
                suspendCampaignIds.addAll(campIds);
            } else if (BudgetUtils.isBalanceWarning(balance)) {
                // 余额预警
                List<Integer> campIds = adCampaignService.listCampaignIdsByAdvertiserId(advertiserId);
                log.info("Advertiser balance warning. [id=" + advertiserId + ", numberOfCampaigns=" + 
                                campIds.size() + "]");
                warningCampaignIds.addAll(campIds);
            }
        }
        
        // 更新广告状态为Suspended，并且发送消息到广告引擎
        for (int campId : suspendCampaignIds) {
            adCampaignService.updateAdCampaignStatus(campId, AdStatusEnum.SUSPENDED);
        }
        // 发送预算预警消息给广告引擎
        eventServices.sendCampaignBudgetWarnEvent(new ArrayList<Integer>(warningCampaignIds));
        // 发送预算改变消息给广告引擎
        eventServices.sendCampaignBudgetModifyEvent(new ArrayList<Integer>(campaignCpcMap.keySet()));
    }

    
    private void updateAdvertiserCurrentBalance(Map<Integer, Long> advertiserBalances, 
                    long segCost, int advertiserId) {
        Long balance = advertiserBalances.get(advertiserId);
        if (balance == null) {
            balance = advertiserBalanceService.getByAdvertiserId(advertiserId).getBalance();
        } 
        balance -= segCost;
        advertiserBalances.put(advertiserId, balance);
    }

    private Map<Integer, Integer> getCampaignCostInSegment(CpcTimeSegment currSegment) {
        Map<Integer, Integer> campaignCpcMap = new HashMap<Integer, Integer>();
        List<CpcMetaTuple> metaList = cpcClickSetService.getMetaList(currSegment);
        for (CpcMetaTuple tuple : metaList) {
            AdEntry entry = adEntryService.getEntryById(tuple.getEntryId());
            if (entry == null) {
                log.warn("Invalid entry for tuple: " + cpcClickSetService.convertCpcMetaKey(tuple));
                continue;
            }
            AdOrder order = adOrderService.getOrderById(entry.getOrderId());
            if (order == null) {
                log.warn("Invalid order for tuple: " + cpcClickSetService.convertCpcMetaKey(tuple) + 
                                ", orderId: " + entry.getOrderId());
                continue;
            }
            AdCampaign camp = adCampaignService.getAdCampaignById(entry.getCampaignId());
            if (camp == null) {
                log.warn("Invalid camp for tuple: " + cpcClickSetService.convertCpcMetaKey(tuple) + 
                        ", campId: " + entry.getCampaignId());
                continue;
            } else if (camp.getBidType() != BidTypeEnum.CPC) {
                log.warn("No an CPC camp. tuple: " + cpcClickSetService.convertCpcMetaKey(tuple) + 
                        ", campId: " + entry.getCampaignId());
                continue;
            }
            
            
            int cpcClicks = cpcClickSetService.getCpcClicks(currSegment, tuple);
            int comm = cpcClicks * order.getBidPrice();
            
            // add to campaign budget
            Integer campaignBudget = campaignCpcMap.get(entry.getCampaignId());
            if (campaignBudget == null) {
                campaignCpcMap.put(entry.getCampaignId(), comm);
            } else {
                campaignCpcMap.put(entry.getCampaignId(), campaignBudget + comm);
            }
        }
        return campaignCpcMap;
    }
    
    
    private void updateStats(final CpcTimeSegment prevSegment, final CpcMetaTuple tuple, 
                    final AdOrder order, final int cpcClicks, final int comm, final int pubComm) {
        cpcStatsUpdateDao.executeUpdate(new Command() {
            @Override
            public Object execute(StatelessSession session) {
                Date day = prevSegment.getDay();
                
                updateAdDailyCpc(session, day, tuple, cpcClicks, comm, order, pubComm);
                updatePublisherDailyCpc(session, day, tuple, cpcClicks, comm, order, pubComm);
                updateAdminDailyCpc(session, day, tuple, cpcClicks, comm, order, pubComm);
                updateCampaignDailyCpc(session, day, tuple, cpcClicks, comm, order, pubComm);
                updateOrderDailyCpc(session, day, tuple, cpcClicks, comm, order, pubComm);
                
                // 更新CampaignBudget数据
                AdCampBudget campBudget = adCampaignBudgetService.getCampBudget(order.getCampaignId());
                campBudget.addCost(day, comm);
                adCampaignBudgetService.updateCampBudget(session, campBudget);

                return null;
            }
        });
    }
    
    private void updateAdDailyCpc(StatelessSession session, Date day, CpcMetaTuple tuple, 
                    int cpcClicks, int comm, AdOrder order, int pubComm) {
        AdDailyStatistic stats = new AdDailyStatistic();
        stats.setUuid(UuidUtil.uuidToByteArray(tuple.getPublisherUuid()));
        stats.setAdEntryId(tuple.getEntryId());
        stats.setAdOrderId(order.getId());
        stats.setDateDay(day);
        stats.setNetwork(tuple.getNetwork());
        stats.setCpcClickNo(cpcClicks);
        stats.setCpcTotalCommission(comm);
        //stats.setCpcPubCommission(comm * DEFAULT_PROPORTION / 100);
        cpcStatsUpdateDao.doSaveAdDailyCpc(session, stats);
    }
    
    
    private void updateCampaignDailyCpc(StatelessSession session, Date day, CpcMetaTuple tuple, 
                    int cpcClicks, int comm, AdOrder order, int pubComm) {
        AdCampaignDailyStatistic stats = new AdCampaignDailyStatistic();
        stats.setAdvertiserId(order.getAdvertiserId());
        stats.setCampaignId(order.getCampaignId());
        stats.setDateDay(day);
        stats.setNetwork(tuple.getNetwork());
        stats.setCpcClickNo(cpcClicks);
        stats.setCpcTotalCommission(comm);
        //stats.setCpcPubCommission(comm * DEFAULT_PROPORTION / 100);
        cpcStatsUpdateDao.doSaveCampaignDailyCpc(session, stats);
    }
    
    
    private void updateOrderDailyCpc(StatelessSession session, Date day, CpcMetaTuple tuple, 
                    int cpcClicks, int comm, AdOrder order, int pubComm) {
        AdOrderDailyStatistic stats = new AdOrderDailyStatistic();
        stats.setOrderId(order.getId());
        stats.setDateDay(day);
        stats.setNetwork(tuple.getNetwork());
        stats.setCpcClickNo(cpcClicks);
        stats.setCpcTotalCommission(comm);
        //stats.setCpcPubCommission(comm * DEFAULT_PROPORTION / 100);
        cpcStatsUpdateDao.doSaveOrderDailyCpc(session, stats);
    }
    
    
    private void updatePublisherDailyCpc(StatelessSession session, Date day, CpcMetaTuple tuple, 
                    int cpcClicks, int comm, AdOrder order, int pubComm) {
        PublisherDailyStatistic stats = new PublisherDailyStatistic();
        stats.setUuid(UuidUtil.uuidToByteArray(tuple.getPublisherUuid()));
        stats.setDateDay(day);
        stats.setNetwork(tuple.getNetwork());
        stats.setCpcClickNo(cpcClicks);
        stats.setCpcTotalCommission(comm);
        stats.setCpcPubCommission(pubComm);
        cpcStatsUpdateDao.doSavePublisherDailyCpc(session, stats);
    }
    
    
    private void updateAdminDailyCpc(StatelessSession session, Date day, CpcMetaTuple tuple, 
                    int cpcClicks, int comm, AdOrder order, int pubComm) {
        AdminDailyStatistic stats = new AdminDailyStatistic();
        stats.setDateDay(day);
        stats.setNetwork(tuple.getNetwork());
        stats.setCpcClickNo(cpcClicks);
        stats.setCpcTotalCommission(comm);
        stats.setCpcPubCommission(pubComm);
        cpcStatsUpdateDao.doSaveAdminDailyCpc(session, stats);
    }


    public void setAdEntryService(AdEntryService adEntryService) {
        this.adEntryService = adEntryService;
    }

    public void setAdOrderService(AdOrderService adOrderService) {
        this.adOrderService = adOrderService;
    }

    public void setAdCampaignService(AdCampaignService adCampaignService) {
        this.adCampaignService = adCampaignService;
    }

    public void setAdCampaignBudgetService(AdCampaignBudgetService adCampaignBudgetService) {
        this.adCampaignBudgetService = adCampaignBudgetService;
    }

    public void setAdvertiserBalanceService(AdvertiserBalanceService advertiserBalanceService) {
        this.advertiserBalanceService = advertiserBalanceService;
    }

    public void setEventServices(EventServices eventServices) {
        this.eventServices = eventServices;
    }

    public void setCpcStatsUpdateDao(CpcStatsUpdateDao cpcStatsUpdateDao) {
        this.cpcStatsUpdateDao = cpcStatsUpdateDao;
    }

    public void setCpcClickSetService(CpcClickSetService cpcClickSetService) {
        this.cpcClickSetService = cpcClickSetService;
    }

}
