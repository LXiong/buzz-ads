package com.buzzinate.buzzads.action.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.safehaus.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.action.StatisticsBaseAction;
import com.buzzinate.buzzads.analytics.stats.AdBasicStatistic;
import com.buzzinate.buzzads.analytics.stats.AdDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.AdminDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.PublisherDailyStatistic;
import com.buzzinate.buzzads.core.service.AdEntryService;
import com.buzzinate.buzzads.core.service.ChannelService;
import com.buzzinate.buzzads.core.service.PublisherSettlementService;
import com.buzzinate.buzzads.core.service.SiteService;
import com.buzzinate.buzzads.core.service.StatsAdService;
import com.buzzinate.buzzads.core.service.StatsAdminService;
import com.buzzinate.buzzads.core.service.StatsPublisherServices;
import com.buzzinate.buzzads.domain.Site;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.buzzads.enums.SettlementStatusEnum;
import com.buzzinate.buzzads.user.LoginHelper;
import com.buzzinate.buzzads.util.VisualizeJsUtil;
import com.buzzinate.common.util.DateTimeUtil;
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
    @Autowired
    private PublisherSettlementService publisherSettlementService;
    @Autowired
    protected ChannelService channelService;
    @Autowired
    protected LoginHelper loginHelper;

    private String adsTitle;
    private String siteName;
    private List<AdminDailyStatistic> adminStats;
    private List<AdDailyStatistic> adsStats;
    private List<PublisherDailyStatistic> siteStats;
    private AdBasicStatistic sum;
    private String jsonData = "";
    private String uuidString;
    private String jsonDataPubCommission = "";
    private String jsonDataPubCommissionPie = "";
    private String commissionType;
    private double totalPubCommission;
    private String siteUrl;
    // 接收格式为format[COLUMN ASC|DESC]             排序字段和排序顺序
    private String sortColumn;
    private String sequence;

    @Override
    public String execute() {
        isAdminLogin();
        if (hasActionErrors()) {
            return Action.ERROR;
        }
        initQuickDateRange();
        initVisulizeChart();
        //昨日预计收入
        double yesterdayComm = statsAdminService.getDayComm(DateTimeUtil.subtractDays(DateTimeUtil.
                    getCurrentDateDay(), 1));
        request.setAttribute("yesterdayComm", doubleformat.format(yesterdayComm));
       
        //未结算收入
        request.setAttribute("unpaidComm", doubleformat.format((double) publisherSettlementService.
                getTotalCommByStatus(SettlementStatusEnum.UNPAID) / 100));
        
        //累计支付收入
        request.setAttribute("allComm", doubleformat.format((double) publisherSettlementService.
                getTotalCommByStatus(SettlementStatusEnum.PAID) / 100));
        
        
        List<AdminDailyStatistic> adminDailyStatistics = getAdminDailyStatistics();
        if (hasActionErrors()) {
            return Action.ERROR;
        }
        
        // 分页处理
        request.setAttribute("adminDailyStatistics", adminDailyStatistics);
        request.setAttribute("totalPubCommissionDouble", doubleformat.format(totalPubCommission));
        
        buildJsChart(adminDailyStatistics, dateStart, dateEnd);
        buildCommissionJsPie(adminDailyStatistics);
        return SUCCESS;
    }
    
    private void isAdminLogin() {
        if (!loginHelper.isLoginAsAdmin()) {
            //addActionError(textProvider.getText("buzzads.advertiser.unauthorized"));
            addActionError("您不是管理员，无权查看");
        }
    }

    private List<AdminDailyStatistic> getAdminDailyStatistics() {
        List<AdminDailyStatistic> adminDailyStatistics = new ArrayList<AdminDailyStatistic>(); 
        // 如果uuid为空的话则为ALL所有网站检索
        if (StringUtils.isNotBlank(uuidString)) {
            byte[] uuid = null;
            if (uuidString != null) {
                try {
                    uuid = new UUID(uuidString).asByteArray();
                } catch (Exception e) {
                    this.addActionError("uuid: 无效的站长UUID。");
                    return adminDailyStatistics;
                }
                if (!isValidUuid()) {
                    this.addActionError("uuid: 不是合法的站长UUID。");
                    return adminDailyStatistics;
                }
            }
            
            List<PublisherDailyStatistic> publisherDailyStatistics = statsPublisherServices.getPublisherDailyStatistics(
                    uuid, dateStart, dateEnd);
            for (PublisherDailyStatistic pb : publisherDailyStatistics) {
                AdminDailyStatistic adTmp = new AdminDailyStatistic();
                try {
                    BeanUtils.copyProperties(adTmp, pb);
                } catch (Exception e) {
                    // exception nothing
                }
                adminDailyStatistics.add(adTmp);
            }
            
            for (AdminDailyStatistic ad : adminDailyStatistics) {
                totalPubCommission += ad.getPubCommission();
            }
            
            // 分页处理
            page.setTotalRecords(adminDailyStatistics.size());
            int fromIndex = (page.getPageNum() > 0 ? page.getPageNum() - 1 : page.getPageNum()) * page.getPageSize();
            if (fromIndex > adminDailyStatistics.size()) {
                adminDailyStatistics = new ArrayList<AdminDailyStatistic>();
            } else {
                int toIndex = Math.min((page.getPageNum()) * page.getPageSize(), adminDailyStatistics.size());
                adminDailyStatistics = adminDailyStatistics.subList(fromIndex, toIndex);
            }
        } else {
            totalPubCommission = statsAdminService.getQuerySums(dateStart, dateEnd).getPubCommission();
            adminDailyStatistics = statsAdminService.getQueryStats(dateStart, dateEnd, page);
        }
        
        return adminDailyStatistics;
    }

    public String commissionViewByType() {
        isAdminLogin();
        if (hasActionErrors()) {
            return Action.ERROR;
        }
        
        initQuickDateRange();
        initVisulizeChart();
        
        List<AdminDailyStatistic> metaStatistics = getAdminDailyStatistics();
        if (hasActionErrors()) {
            return Action.ERROR;
        }
        
        // 根据展示类型过滤数据
        List<AdminDailyStatistic> adminDailyStatistics = new ArrayList<AdminDailyStatistic>();
        for (AdminDailyStatistic daily : metaStatistics) {
            switch (BidTypeEnum.valueOf(commissionType)) {
            case CPC:
                if (daily.getCpcViewNo() <= 0)
                    continue;
                break;
            case CPM:
                if (daily.getCpmViewNo() <= 0)
                    continue;
                break;
            case CPS:
                if (daily.getCpsViewNo() <= 0)
                    continue;
                break;
            default:
                break;
            }
            adminDailyStatistics.add(daily);
        }
        request.setAttribute("adminDailyStatistics", adminDailyStatistics);
        //cauculate totalPubCommission and build chart
        buildCommissionJsChartByType(adminDailyStatistics, dateStart, dateEnd);
        request.setAttribute("totalPubCommissionDouble", doubleformat.format(totalPubCommission));
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
    
    private void buildCommissionJsChartByType(List<AdminDailyStatistic> adminDailyStatistics, 
            Date start, Date end) {
        List<Object[]> pubCommission = new ArrayList<Object[]>();
        if (adminDailyStatistics != null) {
            for (AdminDailyStatistic daily : adminDailyStatistics) {
                switch (BidTypeEnum.valueOf(commissionType)) {
                case CPS:
                    double cpsPub =  ((double) daily.getCpsPubCommission()) / 100;
                    pubCommission.add(new Object[] {daily.getCpsPubCommissionDouble(), daily.getDateDay() });
                    totalPubCommission += cpsPub;
                    break;
                case CPC:
                    double cpcPub = ((double) daily.getCpcPubCommission()) / 100;
                    pubCommission.add(new Object[] {daily.getCpcPubCommissionDouble(), daily.getDateDay() });
                    totalPubCommission += cpcPub;
                    break;
                case CPM:
                    pubCommission.add(new Object[] {daily.getCpmPubCommissionDouble(), daily.getDateDay() });
                    totalPubCommission += daily.getCpmPubCommission().doubleValue();
                    break;
                default:
                    break;
                }
            }
        }
        Collections.reverse(pubCommission);
        jsonDataPubCommission = VisualizeJsUtil.getVisualizeJsData(pubCommission,
                textProvider.getText("buzzads.stats.common.pubCommission"), start, end);
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
    
    private void buildCommissionJsPie(List<AdminDailyStatistic> adminDailyStatistics) {
        List<String> dates = new ArrayList<String>();
        List<Integer> cpsDaily = new ArrayList<Integer>();
        List<Integer> cpcDaily = new ArrayList<Integer>();
        List<Integer> cpmDaily = new ArrayList<Integer>();
        Map<String, List<Integer>> commissionMap = new HashMap<String, List<Integer>>();
        if (adminDailyStatistics != null) {
            for (AdminDailyStatistic daily: adminDailyStatistics) {
                dates.add(daily.getDay());
                cpsDaily.add(daily.getCpsPubCommission());
                cpcDaily.add(daily.getCpcPubCommission());
                cpmDaily.add(daily.getCpmPubCommission().multiply(new BigDecimal(100)).intValue());
            }
        }
        commissionMap.put("CPS", cpsDaily);
        commissionMap.put("CPC", cpcDaily);
        commissionMap.put("CPM", cpmDaily);
        jsonDataPubCommissionPie = VisualizeJsUtil.getVisualizeJsPieData(dates, commissionMap, "佣金比例");
    }
    
    private void buildJsChart(List<AdminDailyStatistic> adminDailyStatistics, Date start, Date end) {
        List<Object[]> pubCommission = new ArrayList<Object[]>();
        if (adminDailyStatistics != null) {
            for (AdminDailyStatistic daily : adminDailyStatistics) {
                pubCommission.add(new Object[] {daily.getPubCommissionDouble(), daily.getDateDay() });
            }
        }
        Collections.reverse(pubCommission);
        jsonDataPubCommission = VisualizeJsUtil.getVisualizeJsData(pubCommission,
                textProvider.getText("buzzads.stats.common.pubCommission"), start, end);
    }
    
    /**
     * 通过siteName进行模糊查询
     * */
    public String findSiteList() {
        isAdminLogin();
        if (hasActionErrors()) {
            return Action.ERROR;
        }
        initQuickDateRange();
        request.setAttribute("siteList", channelService.getSiteListBySiteName(siteUrl, dateStart, dateEnd, page, 
                sortColumn, sequence));
        return Action.SUCCESS;
    }
    
    protected boolean isValidUuid() {
        Site site = siteService.getUuidSiteByUuid(uuidString);
        return site != null;
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

    public String getJsonDataPubCommission() {
        return jsonDataPubCommission;
    }

    public void setJsonDataPubCommission(String jsonDataPubCommission) {
        this.jsonDataPubCommission = jsonDataPubCommission;
    }

    public String getJsonDataPubCommissionPie() {
        return jsonDataPubCommissionPie;
    }

    public void setJsonDataPubCommissionPie(String jsonDataPubCommissionPie) {
        this.jsonDataPubCommissionPie = jsonDataPubCommissionPie;
    }

    public String getUuidString() {
        return uuidString;
    }

    public void setUuidString(String uuidString) {
        this.uuidString = uuidString;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public double getTotalPubCommission() {
        return totalPubCommission;
    }

    public void setTotalPubCommission(double totalPubCommission) {
        this.totalPubCommission = totalPubCommission;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
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