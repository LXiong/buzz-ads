package com.buzzinate.buzzads.action.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.action.StatisticsBaseAction;
import com.buzzinate.buzzads.analytics.stats.AdCampaignDailyStatistic;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdvertiserAccountService;
import com.buzzinate.buzzads.core.service.StatsCampaignService;
import com.buzzinate.buzzads.core.util.AdsDateUtils;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdvertiserAccount;
import com.buzzinate.buzzads.domain.enums.AvailableStatsEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.common.util.DateTimeUtil;
import com.opensymphony.xwork2.Action;

/**
 * 广告概览页面
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2013-3-20
 */
@Controller
@Scope("request")
public class AdminCampViewAction extends StatisticsBaseAction {

    private static final long serialVersionUID = -5086284100830889186L;
    @Autowired
    private transient AdCampaignService campService;
    @Autowired
    private transient StatsCampaignService statsCampService;
    @Autowired
    private transient AdvertiserAccountService advService;

    private int advId;
    private int campaignId;
    private Integer availableStats = AvailableStatsEnum.VIEWS.getCode();

    private Map<Integer, String> viewTypeMap = AvailableStatsEnum
            .adminCharViewType();
    
    private List<AdvertiserAccount> advertisers;
    private List<AdCampaign> campaigns;
    private List<AdCampaignDailyStatistic> stats;
    private AdCampaignDailyStatistic totalStats;
    private AdCampaignDailyStatistic lastTotalStats;
    private AdCampaignDailyStatistic currentTotalStats;
    
    private int cpmCount;
    private int cpcCount;
    private int cpsCount;
    private String sortColumn;
    private String sequence;

    @Override
    public String execute() {
        if (!loginHelper.hasRole("ROLE_AD_ADMIN")) {
            //addActionError(textProvider.getText("buzzads.advertiser.unauthorized"));
            addActionError("您不是管理员，无权查看");
            return Action.ERROR;
        }
        advertisers = advService.listAllAdvertisersByAdmin();
        if (advId == 0) {
            campaigns = new ArrayList<AdCampaign>();
        } else {
            campaigns = campService.findCampsByAdvType(advId, null);
        }
        getLastAndCurrentComm();
        cpsCount = campService.countCampsByBidType(BidTypeEnum.CPS, advId).intValue();
        cpcCount = campService.countCampsByBidType(BidTypeEnum.CPC, advId).intValue();
        cpmCount = campService.countCampsByBidType(BidTypeEnum.CPM, advId).intValue();
        initQuickDateRange();
        initVisulizeChart();
        if (campaignId == 0) {
            stats = statsCampService.pageAllCampStats(advId, dateStart, dateEnd, page);
            totalStats = statsCampService.sumCampStatsByAdvertiserIdWithRangeDate(advId, 
                    dateStart, dateEnd);
            jsonDataStats = buildDailyStatisticsJsChart(
                    statsCampService.listAllCampStats(advId, dateStart, dateEnd),
                    dateStart, dateEnd,
                    AvailableStatsEnum.findByValue(availableStats));
        } else {
            stats = statsCampService.pageAdCampaignStatsByCampaignIdDaily(campaignId, dateStart, dateEnd, page);
            totalStats = statsCampService.sumCampStatsByCampIdWithRangeDate(campaignId, dateStart, dateEnd);
            jsonDataStats = buildDailyStatisticsJsChart(
                    statsCampService.listAllAdCampaignStatsByCampaignIdDaily(dateStart, dateEnd, campaignId), 
                    dateStart, dateEnd, AvailableStatsEnum.findByValue(availableStats));
        }
        return SUCCESS;
    }

    private void getLastAndCurrentComm() {
      //上月收入
        lastTotalStats = statsCampService.sumCampStatsByAdvertiserIdWithRangeDate(advId, 
                DateTimeUtil.convertDate(AdsDateUtils.lastMonFirstDay()), 
                DateTimeUtil.convertDate(AdsDateUtils.lastMonLastDay()));
        //本月收入
        currentTotalStats = statsCampService.sumCampStatsByAdvertiserIdWithRangeDate(advId, 
                DateTimeUtil.convertDate(DateTimeUtil.getFirstDayOfCurrentMonth()), DateTimeUtil.getCurrentDateDay());
    }

    private void addAdvAndCampInfo() {
        // 补充计划名称、广告主信息
        for (AdCampaignDailyStatistic stat : stats) {
            int campId = stat.getCampaignId();
            AdCampaign camp = campService.getAdCampaignById(campId);
            if (camp != null) {
                stat.setCampName(camp.getName());
                AdvertiserAccount adv = advService.getAdvertiserAccount(camp.getAdvertiserId());
                if (adv != null) {
                    stat.setAdvName(adv.getCompanyName());
                }
            }
        }

    }
    /**
     * cpc明细
     * @return
     */
    public String adminCpcCampView() {
        if (!loginHelper.hasRole("ROLE_AD_ADMIN")) {
            //addActionError(textProvider.getText("buzzads.advertiser.unauthorized"));
            addActionError("您不是管理员，无权查看");
            return Action.ERROR;
        }
        advertisers = advService.listAllAdvertisersByAdmin();
        if (advId == 0) {
            campaigns = new ArrayList<AdCampaign>();
        } else {
            campaigns = campService.findCampsByAdvType(advId, BidTypeEnum.CPC);
        }
        getLastAndCurrentComm();
        cpcCount = campService.countCampsByBidType(BidTypeEnum.CPC, advId).intValue();
        initQuickDateRange();
        stats = statsCampService.listStatsViewByCampType(advId, campaignId, BidTypeEnum.CPC, dateStart, dateEnd,  
                sortColumn, sequence);
        addAdvAndCampInfo();
        return SUCCESS;
    }
    
    /**
     * cpc明细
     * @return
     */
    public String adminCpmCampView() {
        if (!loginHelper.hasRole("ROLE_AD_ADMIN")) {
            //addActionError(textProvider.getText("buzzads.advertiser.unauthorized"));
            addActionError("您不是管理员，无权查看");
            return Action.ERROR;
        }
        advertisers = advService.listAllAdvertisersByAdmin();
        if (advId == 0) {
            campaigns = new ArrayList<AdCampaign>();
        } else {
            campaigns = campService.findCampsByAdvType(advId, BidTypeEnum.CPM);
        }
        getLastAndCurrentComm();
        cpmCount = campService.countCampsByBidType(BidTypeEnum.CPM, advId).intValue();
        initQuickDateRange();
        stats = statsCampService.listStatsViewByCampType(advId, campaignId, BidTypeEnum.CPM, dateStart, dateEnd,  
                sortColumn, sequence);
        addAdvAndCampInfo();
        return SUCCESS;
    }
    
    /**
     * cpc明细
     * @return
     */
    public String adminCpsCampView() {
        if (!loginHelper.hasRole("ROLE_AD_ADMIN")) {
            //addActionError(textProvider.getText("buzzads.advertiser.unauthorized"));
            addActionError("您不是管理员，无权查看");
            return Action.ERROR;
        }
        advertisers = advService.listAllAdvertisersByAdmin();
        if (advId == 0) {
            campaigns = new ArrayList<AdCampaign>();
        } else {
            campaigns = campService.findCampsByAdvType(advId, BidTypeEnum.CPS);
        }
        getLastAndCurrentComm();
        cpsCount = campService.countCampsByBidType(BidTypeEnum.CPS, advId).intValue();
        initQuickDateRange();
        stats = statsCampService.listStatsViewByCampType(advId, campaignId, BidTypeEnum.CPS, dateStart, dateEnd, 
                sortColumn, sequence);
        addAdvAndCampInfo();
        return SUCCESS;
    }
    
    public int getAdvId() {
        return advId;
    }

    public void setAdvId(int advId) {
        this.advId = advId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getAvailableStats() {
        return availableStats;
    }

    public void setAvailableStats(Integer availableStats) {
        this.availableStats = availableStats;
    }

    public Map<Integer, String> getViewTypeMap() {
        return viewTypeMap;
    }

    public List<AdvertiserAccount> getAdvertisers() {
        return advertisers;
    }

    public List<AdCampaign> getCampaigns() {
        return campaigns;
    }

    public List<AdCampaignDailyStatistic> getStats() {
        return stats;
    }

    public AdCampaignDailyStatistic getTotalStats() {
        return totalStats;
    }

    public AdCampaignDailyStatistic getLastTotalStats() {
        return lastTotalStats;
    }

    public AdCampaignDailyStatistic getCurrentTotalStats() {
        return currentTotalStats;
    }

    public int getCpmCount() {
        return cpmCount;
    }

    public int getCpcCount() {
        return cpcCount;
    }

    public int getCpsCount() {
        return cpsCount;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

}
