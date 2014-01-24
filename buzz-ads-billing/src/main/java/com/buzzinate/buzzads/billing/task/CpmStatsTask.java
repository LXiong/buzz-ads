package com.buzzinate.buzzads.billing.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.buzzinate.buzzads.analytics.stats.AdCampaignDailyStatistic;
import com.buzzinate.buzzads.core.service.AdCampaignBudgetService;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdvertiserBalanceService;
import com.buzzinate.buzzads.core.service.EventServices;
import com.buzzinate.buzzads.core.service.StatsCampaignService;
import com.buzzinate.buzzads.domain.AdCampBudget;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.common.util.DateTimeUtil;


/**
 * Cpc statistic processing task class.
 * 
 * @author zyeming
 *
 */
public final class CpmStatsTask {
    
    private static Log log = LogFactory.getLog(CpmStatsTask.class);

    @Autowired
    private AdCampaignService adCampaignService;
    @Autowired
    private AdCampaignBudgetService adCampaignBudgetService;
    @Autowired
    private StatsCampaignService statsCampaignService;
    @Autowired
    private AdvertiserBalanceService advertiserBalanceService;
    @Autowired
    private EventServices eventServices;
    
    
    public void start() {
        log.info("Start cpm stats process task.");
        
        // 记录会被Suspend的Campaign
        Set<Integer> suspendCampaignIds = new TreeSet<Integer>();
        // 记录会被预警的Campaign
        Set<Integer> warningCampaignIds = new TreeSet<Integer>();
        // 记录被修改的Campaign
        List<Integer> modifiedCampaignIds = new ArrayList<Integer>();
        // 统计每个广告主的实时余额
        Map<Integer, Long> advertiserBalances = new HashMap<Integer, Long>();
        
        Date today = DateTimeUtil.getCurrentDateDay();
        Date yesterday = DateTimeUtil.subtractDays(today, 1);
        
        List<AdCampaign> campaigns = adCampaignService.listCampaignsBidType(BidTypeEnum.CPM);
        for (AdCampaign campaign : campaigns) {
            modifiedCampaignIds.add(campaign.getId());
            
            AdCampaignDailyStatistic stats = statsCampaignService.sumCampStatsByCampIdWithRangeDate(
                            campaign.getId(), today);
            AdCampBudget campBudget = adCampaignBudgetService.getDayCampBudget(campaign.getId(), today);
            int comm = (stats.getCpmTotalCommission().multiply(new BigDecimal(100))).intValue();
            campBudget.setBudgetDay(comm);
            campBudget.setBudgetTotal(campBudget.getBudgetTotal() + comm);
            if (!DateUtils.isSameDay(today, campBudget.getDateDay())) {
                // Update total budget if not the same day
                campBudget.setDateDay(today);
                AdCampaignDailyStatistic statsPrev = statsCampaignService.sumCampStatsByCampIdWithRangeDate(
                                campaign.getId(), yesterday);
                int commPrev = (statsPrev.getCpmTotalCommission().multiply(new BigDecimal(100))).intValue();
                campBudget.setBudgetTotal(campBudget.getBudgetTotal() + commPrev);
                adCampaignBudgetService.updateCampBudget(campBudget);
            } 
            
            if (campBudget.isExceedBudget()) {
                // 预算不足
                log.info("Campaign suspended. [id=" + campaign.getId() + ", name=" + campaign.getName() + "]");
                suspendCampaignIds.add(campaign.getId());
            } else if (BudgetUtils.isBudgetWarning(campBudget)) {
                // 预算预警
                log.info("Campaign budget warning. [id=" + campaign.getId() + ", name=" + campaign.getName() + "]");
                warningCampaignIds.add(campaign.getId());
            }
            
            // 更新广告主的最新余额，即余额减去当前时间段广告主所有活动的开销
            // TODO: 要准确的计算广告主余额，最好要把CPM、CPC等一起计算才准确。
            updateAdvertiserCurrentBalance(advertiserBalances, comm, campaign.getAdvertiserId());
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
        eventServices.sendCampaignBudgetModifyEvent(modifiedCampaignIds);
        
        log.info("Finish cpm stats process task.");
    }
    
    
    private void updateAdvertiserCurrentBalance(Map<Integer, Long> advertiserBalances, 
                    long comm, int advertiserId) {
        Long balance = advertiserBalances.get(advertiserId);
        if (balance == null) {
            balance = advertiserBalanceService.getByAdvertiserId(advertiserId).getBalance();
        } 
        balance -= comm;
        advertiserBalances.put(advertiserId, balance);
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
    
}
