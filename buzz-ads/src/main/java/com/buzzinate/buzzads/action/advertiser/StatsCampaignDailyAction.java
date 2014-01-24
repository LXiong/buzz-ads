package com.buzzinate.buzzads.action.advertiser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.buzzinate.buzzads.action.StatisticsBaseAction;
import com.buzzinate.buzzads.analytics.stats.AdCampaignDailyStatistic;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdEntryService;
import com.buzzinate.buzzads.core.service.AdOrderService;
import com.buzzinate.buzzads.core.service.StatsCampaignService;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdEntry;
import com.buzzinate.buzzads.domain.AdOrder;
import com.buzzinate.buzzads.domain.enums.AvailableStatsEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.buzzads.service.AdStatsServices;
import com.opensymphony.xwork2.Action;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2013-3-5
 */
@Controller
@Scope("request")
public class StatsCampaignDailyAction extends StatisticsBaseAction {

    private static final long serialVersionUID = -8454634291762163954L;

    @Autowired
    private StatsCampaignService statsCampaignService;
    @Autowired
    private AdCampaignService adCampaignService;
    @Autowired
    private AdOrderService adOrderService;
    @Autowired
    private AdEntryService adEntryService;
    @Autowired
    private AdStatsServices adStatsService;
    
    private List<AdCampaign> campaigns;
    
    private List<AdOrder> adOrders;
    
    private List<AdEntry> adEntries;
    
    private Integer campaignId;
    
    private AdCampaign campaign;
    
    private Integer availableStats = AvailableStatsEnum.CLICKS.getCode();
    
    private Map<Integer, String> viewTypeMap = AvailableStatsEnum.charOverViewType();
    
    private int activeCampaigns;
    private int activeGroups;
    private int activeEntries;
    
    /*
     * 所有活动
     * 
     */
    @Override
    public String execute() throws Exception {
        initQuickDateRange();
        if (page == null) {
            page = new Pagination();
        }
        int advertiserId = loginHelper.getUserId();
        List<AdCampaign> activeCampaignList = adCampaignService.listActiveCampaignsByAdvertiserId(advertiserId);
        activeCampaigns = activeCampaignList.size();
        if (activeCampaigns > 0) {
            List<AdOrder> activeOrderList = adOrderService.
                    listActiveAdOrdersByAdvertiserId(advertiserId, getCampIds(activeCampaignList));
            activeGroups = activeOrderList.size();
            if (activeGroups > 0) {
                activeEntries = adEntryService.
                        listActiveEntriesByAdvertiserId(advertiserId, getOrderIds(activeOrderList)).size();
            }
        }
        
        campaigns = statsCampaignService.allCampaignStats(advertiserId, dateStart, dateEnd, page);
        //图标数据
        initVisulizeChart();
        jsonDataStats = buildDailyStatisticsJsChart(
                statsCampaignService.listAllCampStats(advertiserId, dateStart, dateEnd), 
                dateStart, dateEnd,
                AvailableStatsEnum.findByValue(availableStats));
        return SUCCESS;
    }
    
    /*
     * 所有广告组
     */
    public String allGroups() {
        initQuickDateRange();
        if (page == null) {
            page = new Pagination();
        }
        int advertiserId = loginHelper.getUserId();
        adOrders = adStatsService.allGroupStats(loginHelper.getUserId(), dateStart, dateEnd, page);
        initVisulizeChart();
        jsonDataStats = buildDailyStatisticsJsChart(
                statsCampaignService.listAllCampStats(advertiserId, dateStart, dateEnd), 
                dateStart, dateEnd,
                AvailableStatsEnum.findByValue(availableStats));
        return SUCCESS;
    }
    
    /*
     * 所有广告项
     */
    public String allAdEntrys() {
        initQuickDateRange();
        if (page == null) {
            page = new Pagination();
        }
        int advertiserId = loginHelper.getUserId();
        adEntries = adStatsService.allAdEntryStats(loginHelper.getUserId(), dateStart, dateEnd, page);
        initVisulizeChart();
        jsonDataStats = buildDailyStatisticsJsChart(
                statsCampaignService.listAllCampStats(advertiserId, dateStart, dateEnd), 
                dateStart, dateEnd,
                AvailableStatsEnum.findByValue(availableStats));
        return SUCCESS;
    }
    
    //list the groups of a campaign
    public String listGroupsByCampaignId() {
        initQuickDateRange();
        initVisulizeChart();
        
        getAndBuildCampaignChart(campaignId);

        if (campaign != null && campaign.getAdvertiserId() == loginHelper.getUserId()) {
            if (campaign.getBidType() == BidTypeEnum.CPC) {
                viewTypeMap = AvailableStatsEnum.charCpcType();
            } else if (campaign.getBidType() == BidTypeEnum.CPM) {
                viewTypeMap = AvailableStatsEnum.charCpmType();
            }
            adOrders = adOrderService.getAdOrdersByCampaignId(campaignId);
            if (page == null) {
                page = new Pagination();
            }
            page.setTotalRecords(adOrders.size());
            int fromIndex = (page.getPageNum() > 0 ? page.getPageNum() - 1 : page.getPageNum()) * page.getPageSize();
            if (fromIndex > adOrders.size()) {
                adOrders = new ArrayList<AdOrder>();
            } else {
                int toIndex = Math.min((page.getPageNum()) * page.getPageSize(), adOrders.size());
                adOrders = adOrders.subList(fromIndex, toIndex);
            }
            adOrders = adStatsService.getAdOrderStatsByOrders(dateStart, dateEnd, adOrders);
        } else {
            addActionError("广告活动不存在，或者您无权查看该活动下的广告组");
            return ERROR;
        }
        return Action.SUCCESS;
    }
    
    //list the entries of a campaign
    public String listEntriesByCampaignId() {
        initQuickDateRange();
        initVisulizeChart();
        
        getAndBuildCampaignChart(campaignId);
        if (campaign != null && campaign.getAdvertiserId() == loginHelper.getUserId()) {
            if (campaign.getBidType() == BidTypeEnum.CPC) {
                viewTypeMap = AvailableStatsEnum.charCpcType();
            } else if (campaign.getBidType() == BidTypeEnum.CPM) {
                viewTypeMap = AvailableStatsEnum.charCpmType();
            }
            
            adEntries = adEntryService.listEntriesByCampaignId(campaignId);
            
            List<AdOrder> orders = adOrderService.getAdOrdersByCampaignId(campaignId);
            page.setTotalRecords(adEntries.size());
            
            int fromIndex = (page.getPageNum() > 0 ? page.getPageNum() - 1 : page.getPageNum()) * page.getPageSize();
            if (fromIndex > adEntries.size()) {
                adEntries = new ArrayList<AdEntry>();
            } else {
                int toIndex = Math.min((page.getPageNum()) * page.getPageSize(), adEntries.size());
                adEntries = adEntries.subList(fromIndex, toIndex);
            }
            adEntries = adStatsService.getAdEntryStatsByEntries(dateStart, dateEnd, adEntries, orders);
        } else {
            addActionError("广告活动不存在，或者您无权查看该活动下的广告");
            return ERROR;
        }
        return Action.SUCCESS;
    }
    
    private void getAndBuildCampaignChart(int campId) {
        campaign = adCampaignService.getAdCampaignById(campId);
        //get the data for daily chart statistic
        List<AdCampaignDailyStatistic> dailyStatsList = statsCampaignService.
                listAllAdCampaignStatsByCampaignIdDaily(dateStart, dateEnd, campaignId);
        jsonDataStats = buildDailyStatisticsJsChart(dailyStatsList, dateStart, dateEnd, 
                AvailableStatsEnum.findByValue(availableStats));
    }
    
    private List<Integer> getCampIds(List<AdCampaign> camps) {
        List<Integer> adCampIds = new ArrayList<Integer>();
        for (AdCampaign camp : camps) {
            adCampIds.add(camp.getId());
        }
        return adCampIds;
    }
    
    private List<Integer> getOrderIds(List<AdOrder> orders) {
        List<Integer> orderIds = new ArrayList<Integer>();
        for (AdOrder order : orders) {
            orderIds.add(order.getId());
        }
        return orderIds;
    }
    
    public List<AdCampaign> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<AdCampaign> campaigns) {
        this.campaigns = campaigns;
    }
    
    public List<AdOrder> getAdOrders() {
        return adOrders;
    }

    public List<AdEntry> getAdEntries() {
        return adEntries;
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }
    
    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }
    
    public Integer getAvailableStats() {
        return availableStats;
    }

    public void setAvailableStats(Integer availableStats) {
        this.availableStats = availableStats;
    }
    
    public AdCampaign getCampaign() {
        return campaign;
    }
    
    public void setCampaign(AdCampaign campaign) {
        this.campaign = campaign;
    }

    public Map<Integer, String> getViewTypeMap() {
        return viewTypeMap;
    }
    
    public int getActiveCampaigns() {
        return activeCampaigns;
    }

    public void setActiveCampaigns(int activeCampaigns) {
        this.activeCampaigns = activeCampaigns;
    }

    public int getActiveGroups() {
        return activeGroups;
    }

    public void setActiveGroups(int activeGroups) {
        this.activeGroups = activeGroups;
    }

    public int getActiveEntries() {
        return activeEntries;
    }

    public void setActiveEntries(int activeEntries) {
        this.activeEntries = activeEntries;
    }
}
