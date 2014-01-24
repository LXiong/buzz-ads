package com.buzzinate.buzzads.core.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buzzinate.buzzads.analytics.stats.AdCampaignDailyStatistic;
import com.buzzinate.buzzads.core.dao.StatsCampaignDailyDao;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.AdCampBudget;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.enums.BidTypeEnum;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2013-3-5
 */
@Service
public class StatsCampaignService {
    
    @Autowired
    private StatsCampaignDailyDao statsCampaignDao;
    @Autowired
    private AdCampaignService adCampaignService;
    @Autowired
    private AdCampaignBudgetService adCampaignBudgetService;
    /**
     * 查询广告活动消耗
     * @param advertiserId
     * @param start
     * @param end
     * @return
     */
    public List<AdCampaign> allCampaignStats(int advertiserId, Date start, Date end, Pagination page) {
        List<AdCampaign> campaigns = adCampaignService.listCampaigns(advertiserId, page);
        if (campaigns.size() > 0) {
            for (AdCampaign campaign : campaigns) {
                //补全预算信息
                AdCampBudget budget = adCampaignBudgetService.getCampBudget(campaign.getId());
                if (budget != null) {
                    campaign.setMaxDayBudget(budget.getMaxBudgetDay());
                    campaign.setMaxBudgetTotal(budget.getMaxBudgetTotal());
                }
                //补全统计信息
                Object[] objs = statsCampaignDao.getSumStatsByCampRangeTime(campaign.getId(), start, end);
                if (objs != null) {
                    campaign.setViews((Integer) objs[0]);
                    campaign.setClicks((Integer) objs[1]);
                    campaign.setCpcComm((Integer) objs[2]);
                    campaign.setCpsComm((Integer) objs[3]);
                    campaign.setCpmViews((Integer) objs[4]);
                    campaign.setCpmComm(objs[5] == null ? new BigDecimal(0) : (BigDecimal) objs[5]);
                    campaign.setCpcClicks((Integer) objs[6]);
                }
            }
        }
        return campaigns;
    }
    
    
    /*
     * 绘图
     */
    public List<AdCampaignDailyStatistic> listAllCampStats(int advertiserId, Date start, Date end) {
        if (advertiserId == 0)
            return convertToAdCampaignDailyStatisticList(statsCampaignDao.adminViewAllCampStats(start, end));
        return convertToAdCampaignDailyStatisticList(statsCampaignDao.listAllCampStats(advertiserId, start, end));
    }
    
    public List<AdCampaignDailyStatistic> pageAllCampStats(int advertiserId, Date start, Date end, Pagination page) {
        if (advertiserId == 0)
            return convertToAdCampaignDailyStatisticList(statsCampaignDao.adminPageAllCampStats(start, end, page));
        return convertToAdCampaignDailyStatisticList(statsCampaignDao.pageAllCampStats(advertiserId, start, end, page));
    }
    
    private List<AdCampaignDailyStatistic> convertToAdCampaignDailyStatisticList(List<Object[]> objs) {
        List<AdCampaignDailyStatistic> list = new ArrayList<AdCampaignDailyStatistic>();
        if (objs.size() > 0) {
            int i;
            for (Object[] obj : objs) {
                AdCampaignDailyStatistic st = new AdCampaignDailyStatistic();
                i = 0;
                st.setDateDay((Date) obj[i++]);
                st.setViews((Integer) obj[i++]);
                st.setClicks((Integer) obj[i++]);
                st.setCpcClickNo((Integer) obj[i++]);
                st.setCpmViewNo((Integer) obj[i++]);
                st.setCpmClickNo((Integer) obj[i++]);
                st.setCpsOrderNo((Integer) obj[i++]);
                st.setCpsTotalCommission((Integer) obj[i++]);
                st.setCpcTotalCommission((Integer) obj[i++]);
                Object cpmTotal = obj[i++];
                st.setCpmTotalCommission(cpmTotal == null ? new BigDecimal(0) : (BigDecimal) cpmTotal);
                list.add(st);
            }
        }
        return list;
    }
    
    public List<AdCampaignDailyStatistic> listAllAdCampaignStatsByCampaignIdDaily(Date startDate, 
            Date endDate, int campaignId) {
        return statsCampaignDao.getAdCampaignStatsByCampaignIdDaily(startDate, endDate, campaignId);
    }
    
    public List<AdCampaignDailyStatistic> pageAdCampaignStatsByCampaignIdDaily(int campId, Date start, Date end, 
            Pagination page) {
        return convertToAdCampaignDailyStatisticList(statsCampaignDao.pageCampStatsByCampId(campId, start, end, page));
    }

    /**
     * 获取截至指定日期的每日数据统计
     * @param campaignId
     * @param endDay
     * @return
     */
    public List<AdCampaignDailyStatistic> pageAdCampaignStatsByCampaignIdDailyBeforeDate(int campaignId, 
            Date endDay, Pagination page) {
        return convertToAdCampaignDailyStatisticList(statsCampaignDao.
                pageCampStatsByCampIdBeforeDate(campaignId, endDay, page));
    }
    
    public AdCampaignDailyStatistic sumCampStatsByAdvertiserIdWithRangeDate(int advId, Date start, Date end) {
        if (advId == 0) {
            return convertToAdCampaignDailyStatistic(statsCampaignDao
                    .sumCampStatsWithRangeDate(start, end));
        }
        return convertToAdCampaignDailyStatistic(statsCampaignDao
                .sumCampStatsByAdvertiserIdWithRangeDate(advId, start, end));
    }
    
    public AdCampaignDailyStatistic sumCampStatsByCampIdWithRangeDate(int campId, Date start, Date end) {
        return convertToAdCampaignDailyStatistic(statsCampaignDao
                .getSumStatsByCampRangeTime(campId, start, end));
    }
    
    public AdCampaignDailyStatistic sumCampStatsByCampIdWithRangeDate(int campId, Date day) {
        Date end = DateUtils.addSeconds(day, 1);
        return convertToAdCampaignDailyStatistic(statsCampaignDao
                .getSumStatsByCampRangeTime(campId, day, end));
    }

    /**
     * 获取截至指定日期前的数据统计
     * @param campaignId
     * @param endDay
     * @return
     */
    public AdCampaignDailyStatistic sumCampStatsByCampIdBeforeDate(int campaignId, Date endDay) {
        return convertToAdCampaignDailyStatistic(statsCampaignDao.getSumStatsByCampIdBeforeTime(campaignId, endDay));
    }
    
    private AdCampaignDailyStatistic convertToAdCampaignDailyStatistic(Object[] objs) {
        AdCampaignDailyStatistic stat = new AdCampaignDailyStatistic();
        if (objs != null) {
            stat.setViews((Integer) objs[0]);
            stat.setClicks((Integer) objs[1]);
            stat.setCpcTotalCommission((Integer) objs[2]);
            stat.setCpsTotalCommission((Integer) objs[3]);
            stat.setCpmViewNo((Integer) objs[4]);
            stat.setCpmTotalCommission(objs[5] == null ? new BigDecimal(0) : (BigDecimal) objs[5]);
        }
        return stat;
    }
    
    public List<AdCampaignDailyStatistic> listStatsViewByCampType(int advId, int campId, BidTypeEnum bidType, 
            Date startDate, Date endDate, String sortColumn, String sequence) {
        List<AdCampaign> camps = null;
        List<Integer> campIds = new ArrayList<Integer>();
        if (campId == 0) {
            if (advId == 0) {
                camps = adCampaignService.listCampaignsBidType(bidType);
            } else {
                camps = adCampaignService.findCampsByAdvType(advId, bidType);
            }
            for (AdCampaign camp : camps) {
                campIds.add(Integer.valueOf(camp.getId()));
            }
        } else {
            campIds.add(Integer.valueOf(campId));
        }
        if (campIds.size() == 0) 
            return new ArrayList<AdCampaignDailyStatistic>();
        
        // 默认按照各自的降序排列
        if (StringUtils.isEmpty(sortColumn)) {
            if (BidTypeEnum.CPC.equals(bidType)) {
                sortColumn = "cpcCommission";
            } else if (BidTypeEnum.CPM.equals(bidType)) {
                sortColumn = "cpmCommission";
            } else if (BidTypeEnum.CPS.equals(bidType)) {
                sortColumn = "cpsCommission";
            }
            sequence = "DESC";
        }
        
        return convertToStatistic(statsCampaignDao.listStatsViewByCampType(campIds, startDate, endDate, sortColumn + " " + sequence));
    }
    
    private List<AdCampaignDailyStatistic> convertToStatistic(List<Object[]> statsList) {
        List<AdCampaignDailyStatistic> stats = new ArrayList<AdCampaignDailyStatistic>();
        for (Object[] obj : statsList) {
            AdCampaignDailyStatistic stat = new AdCampaignDailyStatistic();
            stat.setCampaignId((Integer) obj[0]);
            stat.setViews(((BigDecimal) obj[1]).intValue());
            stat.setClicks(((BigDecimal) obj[2]).intValue());
            stat.setCpcTotalCommission(((BigDecimal) obj[3]).intValue());
            stat.setCpsTotalCommission(((BigDecimal) obj[4]).intValue());
            stat.setCpmViewNo(((BigDecimal) obj[5]).intValue());
            stat.setCpmTotalCommission(obj[6] == null ? new BigDecimal(0) : (BigDecimal) obj[6]);
            stat.setCpcClickNo(((BigDecimal) obj[7]).intValue());
            stat.setCpsOrderNo(((BigDecimal) obj[8]).intValue());
            stats.add(stat);
        }
        return stats;
    }

}
