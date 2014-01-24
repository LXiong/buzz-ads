package com.buzzinate.buzzads.action.advertiser;

import com.buzzinate.buzzads.action.StatisticsBaseAction;
import com.buzzinate.buzzads.analytics.stats.AdCampaignDailyStatistic;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdvertiserBalanceService;
import com.buzzinate.buzzads.core.service.StatsCampaignService;
import com.buzzinate.buzzads.core.util.AdsDateUtils;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.enums.AvailableStatsEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.common.util.DateTimeUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 广告主dashboard概览
 *
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *         <p/>
 *         2013-3-18
 */
@Controller
@Scope("request")
public class AdvertiserDashBoardAction extends StatisticsBaseAction {
    
    protected static DecimalFormat doubleformat = new DecimalFormat("#,###,##0.00");
    private static final long MAX_RECHARGE_PRICE = 
                    ConfigurationReader.getLong("ads.max.advertiser.recharge", 10000000L);

    private static final long serialVersionUID = -7896014862388803821L;
    @Autowired
    private StatsCampaignService statsCampaignService;
    @Autowired
    private AdCampaignService campService;
    @Autowired
    private AdvertiserBalanceService advertiserBalanceService;

    private int campaignId;
    private List<AdCampaignDailyStatistic> statlist;
    private List<AdCampaign> camps;
    private AdCampaignDailyStatistic totalStats;
    private AdCampaignDailyStatistic lastTotalStats;
    private AdCampaignDailyStatistic currentTotalStats;
    private String balance;

    private Map<Integer, String> viewTypeMap = AvailableStatsEnum.charOverViewType();
    private Integer availableStats = AvailableStatsEnum.CLICKS.getCode();

    private String poundage = ConfigurationReader.getString("buzzads.recharge.poundage.rate", "0");
    private int activeCampaigns;
    private int suspendedCampaigns;
    
    @Override
    public String execute() {
        initQuickDateRange();
        //所有活动
        camps = campService.getCampaignsByAdvertiserId(loginHelper.getUserId());
        for (AdCampaign camp : camps) {
            if (camp.getStatus().equals(AdStatusEnum.SUSPENDED)) {
                suspendedCampaigns += 1;
            } else if (camp.getStatus().equals(AdStatusEnum.ENABLED)) {
                activeCampaigns += 1;
            }
        }
        
        balance = doubleformat
                .format((double) (advertiserBalanceService
                        .getByAdvertiserId(loginHelper.getUserId())
                        .getBalance()) / 100);
        initVisulizeChart();
        //上月消耗
        lastTotalStats = statsCampaignService.sumCampStatsByAdvertiserIdWithRangeDate(loginHelper.getUserId(),
                DateTimeUtil.convertDate(AdsDateUtils.lastMonFirstDay()), 
                DateTimeUtil.convertDate(AdsDateUtils.lastMonLastDay()));
        //本月消耗
        currentTotalStats = statsCampaignService.sumCampStatsByAdvertiserIdWithRangeDate(loginHelper.getUserId(),
                DateTimeUtil.convertDate(DateTimeUtil.getFirstDayOfCurrentMonth()), DateTimeUtil.getCurrentDateDay());
        if (campaignId == 0) {
            //列表
            statlist = statsCampaignService.pageAllCampStats(loginHelper.getUserId(), dateStart, dateEnd, page);
            //总概览
            totalStats = statsCampaignService.sumCampStatsByAdvertiserIdWithRangeDate(loginHelper.getUserId(),
                    dateStart, dateEnd);
            jsonDataStats = buildDailyStatisticsJsChart(
                    statsCampaignService.listAllCampStats(loginHelper.getUserId(), dateStart, dateEnd),
                    dateStart, dateEnd,
                    AvailableStatsEnum.findByValue(availableStats));
        } else {
            statlist = statsCampaignService.pageAdCampaignStatsByCampaignIdDaily(campaignId, dateStart, dateEnd, page);
            totalStats = statsCampaignService.sumCampStatsByCampIdWithRangeDate(campaignId, dateStart, dateEnd);
            jsonDataStats = buildDailyStatisticsJsChart(
                    statsCampaignService.listAllAdCampaignStatsByCampaignIdDaily(dateStart, dateEnd, campaignId),
                    dateStart, dateEnd, AvailableStatsEnum.findByValue(availableStats));
        }
        return SUCCESS;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public List<AdCampaignDailyStatistic> getStatlist() {
        return statlist;
    }

    public List<AdCampaign> getCamps() {
        return camps;
    }

    public AdCampaignDailyStatistic getTotalStats() {
        return totalStats;
    }

    public Map<Integer, String> getViewTypeMap() {
        return viewTypeMap;
    }

    public Integer getAvailableStats() {
        return availableStats;
    }

    public void setAvailableStats(Integer availableStats) {
        this.availableStats = availableStats;
    }

    public String getPoundage() {
        return poundage;
    }

    public AdCampaignDailyStatistic getLastTotalStats() {
        return lastTotalStats;
    }

    public AdCampaignDailyStatistic getCurrentTotalStats() {
        return currentTotalStats;
    }

    public String getBalance() {
        return balance;
    }
    
    public int getSuspendedCampaigns() {
        return suspendedCampaigns;
    }

    public int getActiveCampaigns() {
        return activeCampaigns;
    }

    public long getMaxRechargePrice() {
        return MAX_RECHARGE_PRICE;
    }

}
