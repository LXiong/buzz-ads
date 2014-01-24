package com.buzzinate.buzzads.action.advertiser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.action.StatisticsBaseAction;
import com.buzzinate.buzzads.analytics.stats.AdOrderDailyStatistic;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdEntryService;
import com.buzzinate.buzzads.core.service.AdOrderService;
import com.buzzinate.buzzads.core.service.ChannelService;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdEntry;
import com.buzzinate.buzzads.domain.AdOrder;
import com.buzzinate.buzzads.domain.Channel;
import com.buzzinate.buzzads.domain.enums.AvailableStatsEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.buzzads.service.AdStatsServices;
import com.opensymphony.xwork2.Action;

/**
 * 
 * @author Johnson
 *
 */
@Controller
@Scope("request")
public class StatsOrderDailyAction extends StatisticsBaseAction {

    private static final long serialVersionUID = -6005321251626896274L;
    
    @Autowired
    private transient AdOrderService adOrderService;
    @Autowired
    private transient AdEntryService adEntryService;
    @Autowired
    private transient AdStatsServices adStatsService;
    @Autowired
    private transient AdCampaignService adCampaignService;
    @Autowired
    private transient ChannelService channelService;
    
    private Integer orderId;
    
    private AdOrder adOrder;
    
    private Integer availableStats = AvailableStatsEnum.CLICKS.getCode();
    
    private List<AdEntry> adEntries;
    
    private Map<Integer, String> viewTypeMap = AvailableStatsEnum.charOverViewType();
    
    private AdCampaign campaign;
    
    //list the ads of a campaign
    public String listEntriesByOrderId() {
        initQuickDateRange();
        initVisulizeChart();
        
        adOrder = adOrderService.getOrderById(orderId);
        if (adOrder != null && adOrder.getAdvertiserId() == loginHelper.getUserId()) {
            String idStr = adOrder.getChannelsTarget();
            if (StringUtils.isNotBlank(idStr)) {
                String[] ids = idStr.split(",");
                List<String> channelDomains = new ArrayList<String>();
                for (String channelId : ids) {
                    Channel channel = channelService.getChannelById(Integer.valueOf(channelId).intValue());
                    if (channel != null) {
                        channelDomains.add(channel.getDomain());
                    }
                }
                adOrder.setChannelDomains(channelDomains);
            }
            campaign = adCampaignService.getAdCampaignById(adOrder.getCampaignId());
            if (campaign.getBidType() == BidTypeEnum.CPC) {
                viewTypeMap = AvailableStatsEnum.charCpcType();
            } else if (campaign.getBidType() == BidTypeEnum.CPM) {
                viewTypeMap = AvailableStatsEnum.charCpmType();
            }
            adOrder.setCampBidType(campaign.getBidType());
            List<AdOrderDailyStatistic> dailyStatsList = adStatsService.
                    getAdOrderStatsByOrderIdDaily(dateStart, dateEnd, orderId);
            jsonDataStats = buildDailyStatisticsJsChart(dailyStatsList, dateStart, dateEnd, 
                    AvailableStatsEnum.findByValue(availableStats));
            adEntries = adEntryService.listEntriesByOrderId(orderId);
            page.setTotalRecords(adEntries.size());
            int fromIndex = (page.getPageNum() > 0 ? page.getPageNum() - 1 : page.getPageNum()) * page.getPageSize();
            if (fromIndex > adEntries.size()) {
                adEntries = new ArrayList<AdEntry>();
            } else {
                int toIndex = Math.min((page.getPageNum()) * page.getPageSize(), adEntries.size());
                adEntries = adEntries.subList(fromIndex, toIndex);
            }
            adEntries = adStatsService.getAdEntryStatsByEntries(dateStart, dateEnd, adEntries, adOrder);
        } else {
            addActionError("广告组不存在，或者您无权查看该广告组下的广告");
            return Action.ERROR;
        }
        
        return Action.SUCCESS;
    }
    
    public Integer getAvailableStats() {
        return availableStats;
    }

    public void setAvailableStats(Integer availableStats) {
        this.availableStats = availableStats;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    
    public AdOrder getAdOrder() {
        return adOrder;
    }

    public void setAdOrder(AdOrder adOrder) {
        this.adOrder = adOrder;
    }
    
    public Map<Integer, String> getViewTypeMap() {
        return viewTypeMap;
    }
    
    public List<AdEntry> getAdEntries() {
        return adEntries;
    }

    public void setAdEntries(List<AdEntry> adEntries) {
        this.adEntries = adEntries;
    }

    public AdCampaign getCampaign() {
        return campaign;
    }
}
