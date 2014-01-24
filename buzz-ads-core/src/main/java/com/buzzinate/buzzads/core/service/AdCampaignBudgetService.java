package com.buzzinate.buzzads.core.service;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.StatelessSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buzzinate.buzzads.analytics.stats.AdCampaignDailyStatistic;
import com.buzzinate.buzzads.core.bean.CpcTimeSegment;
import com.buzzinate.buzzads.core.dao.AdCampaignBudgetDao;
import com.buzzinate.buzzads.core.util.RedisClient;
import com.buzzinate.buzzads.domain.AdCampBudget;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.CampaignDayBudget;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.common.util.DateTimeUtil;

/**
 * 
 * @author zyeming
 *
 */
@Service
public class AdCampaignBudgetService {
    
    private static final int CAMP_BUDGET_EXPIRE = 3600 * 4;
    
    private static final int SEGMENT_CAMP_COST_EXPIRE = 3600 * 4;
    

    @Autowired
    private AdCampaignBudgetDao budgetDao;
    @Autowired
    private AdCampaignService adCampaignService;
    @Autowired
    private CampDayBudgetService campDayBudgetService;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private StatsCampaignService statsCampaignService;
    
    /**
     * 返回到目前的活动花费和预算数据
     * @param campaignId
     * @return
     */
    public AdCampBudget getLatestCampBudget(int campaignId) {
        if (campaignId <= 0) {
            throw new IllegalArgumentException();
        }
        
        AdCampaign campaign = adCampaignService.getAdCampaignById(campaignId);
        if (campaign == null) return null;
        AdCampBudget campBudget = getDayCampBudget(campaignId, DateTimeUtil.getCurrentDateDay());
        if (campBudget == null) return null;
        
        if (campaign.getBidType().equals(BidTypeEnum.CPC)) {
            // 获取当前的AdCampBudget，然后加上缓存的SegmentCampCost的花费。
            // 因为有可能刚过一个Segment，上一个Segment的数据还未更新到DB，所以需要把两个Segment的Cost数据都加上去。
            // 在BillingTask里，处理完一个Segment的Cost后，会将其从缓存中清除掉，避免重复计算。
            CpcTimeSegment prevSegment = CpcTimeSegment.getPreviousSegment();
            int prevSegmentCost = getSegmentCampCostCache(campaignId, prevSegment);
            CpcTimeSegment currSegment = CpcTimeSegment.getNextSegment(prevSegment);
            int currSegmentCost = getSegmentCampCostCache(campaignId, currSegment);
            
            if (DateUtils.isSameDay(currSegment.getDay(), campBudget.getDateDay()) &&
                    DateUtils.isSameDay(prevSegment.getDay(), campBudget.getDateDay())) {
                campBudget.setBudgetDay(prevSegmentCost + currSegmentCost + campBudget.getBudgetDay());
            } else if (DateUtils.isSameDay(currSegment.getDay(), campBudget.getDateDay())) {
                campBudget.setBudgetDay(currSegmentCost + campBudget.getBudgetDay());
            } else if (DateUtils.isSameDay(prevSegment.getDay(), campBudget.getDateDay())) {
                campBudget.setDateDay(currSegment.getDay());
                campBudget.setBudgetDay(currSegmentCost);
            } else {
                campBudget.setDateDay(currSegment.getDay());
                campBudget.setBudgetDay(prevSegmentCost + currSegmentCost);
            }
            campBudget.setBudgetTotal(prevSegmentCost + currSegmentCost + campBudget.getBudgetTotal());
        } else {
            if (!DateUtils.isSameDay(DateTimeUtil.getCurrentDateDay(), campBudget.getDateDay())) {
                campBudget.setDateDay(DateTimeUtil.getCurrentDateDay());
                campBudget.setBudgetDay(0);  
            } else {
                if (campaign.getBidType().equals(BidTypeEnum.CPM)) {
                    AdCampaignDailyStatistic stats = statsCampaignService
                            .sumCampStatsByCampIdWithRangeDate(campaign.getId(), campBudget.getDateDay());
                    int comm = (stats.getCpmTotalCommission().multiply(new BigDecimal(100))).intValue();
                    campBudget.setBudgetDay(comm);
                    campBudget.setBudgetTotal(campBudget.getBudgetTotal() + comm);
                }
            }
        }
        return campBudget;
    }
    
    /**
     * 创建Campaign Budget数据
     * @param campBudget
     * @return
     */
    public int createCampBudget(AdCampBudget campBudget) {
        if (campBudget == null || campBudget.getCampaignId() <= 0) {
            throw new IllegalArgumentException();
        }
        budgetDao.create(campBudget);
        redisClient.set(getCampBudgetCacheKey(campBudget.getCampaignId()), CAMP_BUDGET_EXPIRE, campBudget);
        return campBudget.getCampaignId();
    }
    
    /**
     * 获取Campaign Budget数据
     * @param campaignId
     * @return
     */
    public AdCampBudget getCampBudget(int campaignId) {
        if (campaignId <= 0) {
            throw new IllegalArgumentException();
        }
        AdCampBudget campBudget = (AdCampBudget) redisClient.getObject(getCampBudgetCacheKey(campaignId));
        if (campBudget == null) {
            campBudget = budgetDao.read(campaignId);
            if (campBudget != null) {
                redisClient.set(getCampBudgetCacheKey(campaignId), CAMP_BUDGET_EXPIRE, campBudget);
            }
        }
        
        return campBudget;
    }
    
    /**
     * 获取Campaign Budget数据， 如果有特定日预算，则设置为特定日预算
     * @param campaignId
     * @return
     */
    public AdCampBudget getDayCampBudget(int campaignId, Date day) {
        AdCampBudget campBudget = getCampBudget(campaignId);
        // check daily max budget
        CampaignDayBudget campDayBudget = campDayBudgetService.findDayBudgetByCampIdAndDate(
                        campaignId, day);
        if (campDayBudget != null) {
            campBudget.setMaxBudgetDay(campDayBudget.getBudget());
        }
        return campBudget;
    }
    
    /**
     * 更新Campaign Budget数据
     * @param campBudget
     */
    public void updateCampBudget(AdCampBudget campBudget) {
        if (campBudget == null || campBudget.getCampaignId() <= 0) {
            throw new IllegalArgumentException();
        }
        
        budgetDao.update(campBudget);
        redisClient.set(getCampBudgetCacheKey(campBudget.getCampaignId()), CAMP_BUDGET_EXPIRE, campBudget);
    }
    
    /**
     * 更新Campaign Budget数据
     * @param campBudget
     */
    public void updateCampBudget(StatelessSession session, AdCampBudget campBudget) {
        if (campBudget == null || campBudget.getCampaignId() <= 0) {
            throw new IllegalArgumentException();
        }
        
        session.update(campBudget);
        redisClient.set(getCampBudgetCacheKey(campBudget.getCampaignId()), CAMP_BUDGET_EXPIRE, campBudget);
    }
    
    /**
     * 获取广告活动在时间段内开销缓存
     * @param campaignId
     * @param seg
     * @return
     */
    public int getSegmentCampCostCache(int campaignId, CpcTimeSegment seg) {
        String key = getSegmentCampCostCacheKey(campaignId, seg);
        try {
            return Integer.parseInt(redisClient.get(key));
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * 设置广告活动指定时间段内的开销缓存
     * @param campaignId
     * @param seg
     * @param cost
     */
    public void setSegmentCampCostCache(int campaignId, CpcTimeSegment seg, int cost) {
        String key = getSegmentCampCostCacheKey(campaignId, seg);
        redisClient.set(key, SEGMENT_CAMP_COST_EXPIRE, Integer.toString(cost));
    }
    
    /**
     * 删除广告活动在时间段内的开销缓存
     * @param campaignId
     * @param seg
     */
    public void delSegmentCampCostCache(int campaignId, CpcTimeSegment seg) {
        String key = getSegmentCampCostCacheKey(campaignId, seg);
        redisClient.delete(key);
    }
    
    
    private String getCampBudgetCacheKey(int campaignId) {
        return "CampBudget:" + campaignId;
    }
    
    private String getSegmentCampCostCacheKey(int campaignId, CpcTimeSegment seg) {
        return "SegCampCost:" + seg.toCacheKey() + ":" + campaignId;
    }

}
