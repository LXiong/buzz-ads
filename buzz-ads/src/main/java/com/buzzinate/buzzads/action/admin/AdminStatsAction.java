package com.buzzinate.buzzads.action.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.action.StatisticsBaseAction;
import com.buzzinate.buzzads.analytics.stats.AdBasicStatistic;
import com.buzzinate.buzzads.analytics.stats.AdDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.AdminDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.PublisherDailyStatistic;
import com.buzzinate.buzzads.core.service.AdEntryService;
import com.buzzinate.buzzads.core.service.SiteService;
import com.buzzinate.buzzads.core.service.StatsAdService;
import com.buzzinate.buzzads.core.service.StatsAdminService;
import com.buzzinate.buzzads.core.service.StatsPublisherServices;
import com.buzzinate.buzzads.domain.Site;
import com.buzzinate.buzzads.util.VisualizeJsUtil;
import com.opensymphony.xwork2.Action;

/**
 * 
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com>
 * 
 */
@Controller
@Scope("request")
public class AdminStatsAction extends StatisticsBaseAction {
    private static final long serialVersionUID = -77780564207575L;
    
    @Autowired
    protected SiteService siteService;
    @Autowired
    protected StatsPublisherServices statsPublisherServices;
    @Autowired
    private StatsAdminService statsAdminService;
    @Autowired
    private StatsAdService statsAdService;
    @Autowired
    private AdEntryService adEntryService;

    
    private String adsTitle;
    private String siteName;
    private List<AdminDailyStatistic> adminStats;
    private List<AdDailyStatistic> adsStats;
    private List<PublisherDailyStatistic> siteStats;
    private AdBasicStatistic sum;
    private String jsonData = "";

    
    @Override
    public String execute() {
        initDateTimePicker();
        adminStats = statsAdminService.getQueryStats(dateStart, dateEnd, page);
        sum = statsAdminService.getQuerySums(dateStart, dateEnd);
        return Action.SUCCESS;
    }

    public String getByTime() {
        initDateTimePicker();
        initVisulizeChart();
        sum = statsAdminService.getQuerySums(dateStart, dateEnd);
        adminStats = statsAdminService.getQueryStats(dateStart, dateEnd, page);
        jsonData = VisualizeJsUtil.getVisualizeJsData(statsAdminService.getQueryChartStats(dateStart, dateEnd),
                "管理员收入统计报表", dateStart, dateEnd);
        return Action.SUCCESS;
    }

    public String getByAds() {
        initDateTimePicker();
        // 存取id和title
        Map<Integer, String> idTitle = new HashMap<Integer, String>();
        // 按广告title模糊查询
        if (StringUtils.isNotBlank(adsTitle)) {
            // 一次性把title取出来，方便后面组合广告title
            idTitle.putAll(adEntryService.searchAdsByTitle(adsTitle.trim()));
            if (idTitle.isEmpty()) {
                sum = new AdBasicStatistic();
                return Action.SUCCESS;
            }
        }
        List<Integer> entryIds = new ArrayList<Integer>();
        entryIds.addAll(idTitle.keySet());
        // 按广告查询
        if (entryIds.size() > 0) {
            adsStats = statsAdService.getQueryStats(entryIds, dateStart, dateEnd, page);
            sum = statsAdService.getQuerySums(entryIds, dateStart, dateEnd);
            for (AdDailyStatistic ad : adsStats) {
                ad.setAdTitle(idTitle.get(ad.getAdEntryId()));
            }
        } else {
            sum = statsAdminService.getQuerySums(dateStart, dateEnd);
            adsStats = statsAdService.getQueryStats(dateStart, dateEnd, page);
            for (AdDailyStatistic ad : adsStats) {
                entryIds.add(ad.getAdEntryId());
            }
            if (entryIds.size() > 0) {
                idTitle.putAll(adEntryService.getAdsByIds(entryIds));
                for (AdDailyStatistic ad : adsStats) {
                    ad.setAdTitle(idTitle.get(ad.getAdEntryId()));
                }
            }
        }
        return Action.SUCCESS;
    }

    public String getByWebsite() {
        initDateTimePicker();
        // 存取uuid和name
        List<Site> sites = new ArrayList<Site>();
        if (StringUtils.isNotBlank(siteName)) {
            sites.addAll(siteService.searchSiteByName(siteName));
            if (sites.isEmpty()) {
                sum = new AdBasicStatistic();
                return Action.SUCCESS;
            }
        }
        // 按网站名查询
        if (sites.size() > 0) {
            List<byte[]> uuids = new ArrayList<byte[]>();
            for (Site site : sites) {
                uuids.add(site.getUuidBytes());
            }
            sum = statsPublisherServices.getQuerySums(uuids, dateStart, dateEnd);
            siteStats = statsPublisherServices.getAllPublisherDailyStatistics(uuids, dateStart, dateEnd);
            page.setTotalRecords(siteStats.size());
            page.validatePageInfo();
            page.setReturnRecords(siteStats.size());
        } else {
            sum = statsAdminService.getQuerySums(dateStart, dateEnd);
            siteStats = statsPublisherServices.queryStats(dateStart, dateEnd, page);
            for (PublisherDailyStatistic stat : siteStats) {
                Site site = siteService.getUuidSiteByUuid(stat.getUuidString());
                if (site != null) {
                    stat.setSiteName(site.getName());
                }
            }
        }
        return Action.SUCCESS;
    }

    public List<AdminDailyStatistic> getAdminStats() {
        return adminStats;
    }

    public void setAdminStats(List<AdminDailyStatistic> adminStats) {
        this.adminStats = adminStats;
    }

    public List<AdDailyStatistic> getAdsStats() {
        return adsStats;
    }

    public void setAdsStats(List<AdDailyStatistic> adsStats) {
        this.adsStats = adsStats;
    }

    public AdBasicStatistic getSum() {
        return sum;
    }

    public void setSum(AdBasicStatistic sum) {
        this.sum = sum;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getAdsTitle() {
        return adsTitle;
    }

    public void setAdsTitle(String adsTitle) {
        this.adsTitle = adsTitle;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public List<PublisherDailyStatistic> getSiteStats() {
        return siteStats;
    }

    public void setSiteStats(List<PublisherDailyStatistic> siteStats) {
        this.siteStats = siteStats;
    }
}