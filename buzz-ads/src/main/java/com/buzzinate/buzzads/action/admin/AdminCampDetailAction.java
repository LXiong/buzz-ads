package com.buzzinate.buzzads.action.admin;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.action.StatisticsBaseAction;
import com.buzzinate.buzzads.analytics.stats.AdCampaignDailyStatistic;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdvertiserAccountService;
import com.buzzinate.buzzads.core.service.StatsCampaignService;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdvertiserAccount;
import com.buzzinate.common.util.DateTimeUtil;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-5-29
 * Time: 下午8:53
 * 广告组详情统计页面
 */
@Controller
@Scope("request")
public class AdminCampDetailAction extends StatisticsBaseAction {
    private int campaignId;
    private Pagination page;
    private AdCampaign campaign;
    private String advertiserName;
    private AdCampaignDailyStatistic totalStatistic;
    private List<AdCampaignDailyStatistic> dailyStatistics;
    @Autowired
    private AdCampaignService adCampaignService;
    @Autowired
    private AdvertiserAccountService advertiserAccountService;
    @Autowired
    private StatsCampaignService statsCampaignService;

    public String execute() {
        initDateTimePicker();
        
        if (page == null) {
            page = new Pagination();
        }
        AdvertiserAccount account = advertiserAccountService.getAdvertiserAccount(campaign.getAdvertiserId());
        advertiserName = account.getCompanyName();
        if (dateStart != null) {
            totalStatistic = statsCampaignService.sumCampStatsByCampIdWithRangeDate(campaignId, dateStart, dateEnd);
            dailyStatistics = statsCampaignService.pageAdCampaignStatsByCampaignIdDaily(campaignId, dateStart, dateEnd, page);
        } else {
            totalStatistic = statsCampaignService.sumCampStatsByCampIdBeforeDate(campaignId, dateEnd);
            dailyStatistics = statsCampaignService.pageAdCampaignStatsByCampaignIdDailyBeforeDate(campaignId, dateEnd, page);

        }
        return SUCCESS;
    }
    
    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }

    public String getAdvertiserName() {
        return advertiserName;
    }

    public List<AdCampaignDailyStatistic> getDailyStatistics() {
        return dailyStatistics;
    }

    public AdCampaignDailyStatistic getTotalStatistic() {
        return totalStatistic;
    }

    public AdCampaign getCampaign() {
        return campaign;
    }

    @Override
    public void validate() {
        if (campaignId <= 0) {
            addActionError("campaign id无效");
            return;
        }
        campaign = adCampaignService.getAdCampaignById(campaignId);
        Date today = DateTimeUtil.getCurrentDateDay();
        if (campaign == null) {
            addActionError("campaign id无效,找不到对应的campaign");
            return;
        }
        if (dateStart != null && dateEnd != null && dateStart.after(dateEnd)) {
            addActionError("开始时间必须在结束时间之前");
            return;
        }
        if (dateStart != null && dateStart.after(today)) {
            addActionError("开始时间不能大于今天");
            return;
        }
        if (dateEnd != null && dateEnd.after(today)) {
            addActionError("结束时间不能大于今天");
            return;
        }
    }
}
