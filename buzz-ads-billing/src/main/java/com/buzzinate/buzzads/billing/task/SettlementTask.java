package com.buzzinate.buzzads.billing.task;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.buzzinate.buzzads.core.bean.PublisherSettlementStats;
import com.buzzinate.buzzads.core.dao.StatsCampaignDailyDao;
import com.buzzinate.buzzads.core.service.AdCampaignBudgetService;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdvertiserBalanceService;
import com.buzzinate.buzzads.core.service.PublisherSettlementService;
import com.buzzinate.buzzads.core.service.ReviseCommissionService;
import com.buzzinate.buzzads.core.service.SiteService;
import com.buzzinate.buzzads.core.service.StatsPublisherServices;
import com.buzzinate.buzzads.domain.AdCampBudget;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.Site;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.UuidUtil;

/**
 * Campaign reset task.
 * 
 * @author zyeming
 *
 */
public class SettlementTask {
    
    private static Log log = LogFactory.getLog(SettlementTask.class);
    
    @Autowired
    private AdCampaignService adCampaignService;
    @Autowired
    private AdCampaignBudgetService adCampaignBudgetService;
    @Autowired
    private ReviseCommissionService reviseCommissionService;
    @Autowired
    private AdvertiserBalanceService advertiserBalanceService;
    @Autowired
    private StatsPublisherServices statsPublisherServices;
    @Autowired
    private PublisherSettlementService publisherSettlementService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private StatsCampaignDailyDao statsCampaignDailyDao;

    public void start() {
        log.info("Start settlement task.");
        Date day = DateTimeUtil.getDateDay(DateTimeUtil.subtractDays(new Date(), 1));

        //订正站长前一天的佣金，减少精度误差
        reviseCommissionService.revisePublisherCommission(day);

        //订正站长的佣金, 时间从前天所在月的月初到前天的时间间隔
        Date publisherRiviseDay = DateTimeUtil.subtractDays(day, 1);
        reviseCommissionService.revisePublisherSettlement(publisherRiviseDay);

        updatePublisherSettlement(day);
        updateAvertiserBillingForCpm(day);
        
        // TODO: 订正广告主的前一天的支出
        //reviseCommissionService.reviseAdvertiserBilling(day);
        
        // 重置挂起的广告活动
        resetSuspendedCampaigns();
        log.info("Finished settlement task.");
    }
    
    
    private void updateAvertiserBillingForCpm(Date day) {
        log.info("Start update advertiser billing for CPM.");
        
        //获取广告商在day的广告支出数组列表，
        List<Object[]> advertiserPayList = statsCampaignDailyDao.getAdvertiserPayByDay(day);
        log.info("Number of advertisers: " + advertiserPayList.size());
        for (Object[] advertiserPay : advertiserPayList) {
            int advertiserId = (Integer) advertiserPay[0];
            int cpmComm = (advertiserPay[2] == null ? new BigDecimal(0) : (BigDecimal) advertiserPay[2]).multiply(
                    new BigDecimal(100)).intValue();

            try {
                // 更新广告主的balance信息并向billing表中插入消费记录
                advertiserBalanceService.updateBillingAndBalanceForDebits(advertiserId, cpmComm, day);
            } catch (Exception e) {
                log.error("Failed update advertiser billing and balance. [advertiserId=" + 
                                advertiserId + "]");
            }
        }

        log.info("Finish update advertiser billing for CPM.");
    }

    public void updatePublisherSettlement(Date day) {
        log.info("Start update publish settlement.");
        List<PublisherSettlementStats> settlementStats = statsPublisherServices.
                        getPublisherDailyStatsForSettlement(day);
        
        
        Map<Integer, PublisherSettlementStats> publisherStatsMap = new HashMap<Integer, PublisherSettlementStats>(); 
        for (PublisherSettlementStats stats : settlementStats) {
            String uuid = UuidUtil.byteArrayToUuid(stats.getUuid());
            Site s = siteService.getUuidSiteByUuid(uuid);
            if (s == null) {
                log.warn("Invalid site for uuid: " + uuid);
                continue;
            }
            
            PublisherSettlementStats publisherStats = publisherStatsMap.get(s.getUserId());
            if (publisherStats == null) {
                publisherStats = stats;
                publisherStats.setUserId(s.getUserId());
                publisherStatsMap.put(s.getUserId(), publisherStats);
            } else {
                publisherStats.setCpcNo(publisherStats.getCpcNo() + stats.getCpcNo());
                publisherStats.setCpcCommission(publisherStats.getCpcCommission() + stats.getCpcCommission());
                publisherStats.setCpmNo(publisherStats.getCpmNo() + stats.getCpmNo());
                publisherStats.setCpmCommission(publisherStats.getCpmCommission().add(stats.getCpmCommission()));
                publisherStats.setCpsNo(publisherStats.getCpsNo() + stats.getCpsNo());
                publisherStats.setCpsCommission(publisherStats.getCpsCommission() + stats.getCpsCommission());
                publisherStats.setCommission(publisherStats.getCommission() + stats.getCommission());
            }
        }
        
        log.info("Number of publishers: " + publisherStatsMap.size());
        for (Map.Entry<Integer, PublisherSettlementStats> entry : publisherStatsMap.entrySet()) {
            int userId = entry.getKey();
            PublisherSettlementStats stats = entry.getValue();
            
            try {
                publisherSettlementService.insertOrUpdate(userId, stats);
            } catch (Exception e) {
                log.error("Failed update publisher settlement. " + stats.toString(), e);
            }
        }
        
        log.info("Finish update publish settlement.");
    }
    

    public void resetSuspendedCampaigns() {
        log.info("Start resetting suspended campaigns...");
        
        Map<Integer, Long> advertiserBalance = new HashMap<Integer, Long>();
        List<AdCampaign> suspendedCampaigns = adCampaignService.listCampaignsByStatus(AdStatusEnum.SUSPENDED);
        for (AdCampaign campaign : suspendedCampaigns) {
            int advertiserId = campaign.getAdvertiserId();
            AdCampBudget campBudget = adCampaignBudgetService.getLatestCampBudget(campaign.getId());
            if (campBudget == null) continue;
            
            try {
                // 计算广告主的余额并缓存
                Long balance = advertiserBalance.get(advertiserId);
                if (balance == null) {
                    balance = advertiserBalanceService.getLatestBalance(advertiserId);
                    advertiserBalance.put(advertiserId, balance);
                }
                
                // 重新启用Campaign
                if (balance > 0 && !campBudget.isExceedBudget()) {
                    log.info("Reset suspended campaign. [id=" + campaign.getId() + ", name=" + 
                                    campaign.getName() + "]");
                    adCampaignService.updateAdCampaignStatus(campaign.getId(), AdStatusEnum.ENABLED);
                }
            } catch (Exception e) {
                log.error("Failed reset suspended campaign. " + campBudget.toString(), e);
            }
        }
        log.info("Finish resetting suspended campaigns...");
    }
}
