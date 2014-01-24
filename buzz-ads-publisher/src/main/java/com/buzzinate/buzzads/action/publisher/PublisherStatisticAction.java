package com.buzzinate.buzzads.action.publisher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.safehaus.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.action.StatisticsBaseAction;
import com.buzzinate.buzzads.analytics.stats.PublisherDailyStatistic;
import com.buzzinate.buzzads.core.service.PaymentService;
import com.buzzinate.buzzads.core.service.PublisherSettlementService;
import com.buzzinate.buzzads.core.service.SiteService;
import com.buzzinate.buzzads.core.service.StatsPublisherServices;
import com.buzzinate.buzzads.domain.Payment;
import com.buzzinate.buzzads.domain.PublisherSettlement;
import com.buzzinate.buzzads.domain.Site;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.buzzads.util.VisualizeJsUtil;
import com.buzzinate.common.util.DateTimeUtil;
import com.opensymphony.xwork2.Action;

/**
 * 
 * @author johnson
 * 
 */
@Controller
@Scope("request")
public class PublisherStatisticAction extends StatisticsBaseAction {
    private static final long serialVersionUID = -8199338603073453733L;

    @Autowired
    protected SiteService siteService;
    @Autowired
    protected StatsPublisherServices statsPublisherServices;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PublisherSettlementService publisherSettlementService;
    
    private String jsonDataPubCommission = "";
    private String jsonDataPubCommissionPie = "";
    private List<Payment> payments;
    private List<PublisherSettlement> settlements;
    private String uuidString;
    private String commissionType;
    private double totalPubCommission;

    @Override
    /*
     * 效果概览
     */
    public String execute() {
        initQuickDateRange();
        initVisulizeChart();
        int userId = loginHelper.getUserId();
        List<Site> sites = siteService.getUuidSiteByUserId(userId);
        
        if (sites == null || sites.isEmpty()) {
            this.addActionError("你的账户没有网站。");
            return Action.ERROR;
        }
        request.setAttribute("sites", sites);
        
        List<byte[]> uuids = new ArrayList<byte[]>();
        for (Site site : sites) {
            uuids.add(site.getUuidBytes());
        }
        
        //昨日预计收入
        double yesterdayComm = statsPublisherServices.getDayComm(uuids,
                DateTimeUtil.subtractDays(DateTimeUtil.getCurrentDateDay(), 1));
        request.setAttribute("yesterdayComm", doubleformat.format(yesterdayComm));
       
        //未结算收入
        request.setAttribute("unpaidComm",
                doubleformat.format((double) publisherSettlementService.getUnPaidUserCommission(userId) / 100));
        
        //累计支付收入
        request.setAttribute("allComm",
                doubleformat.format((double) publisherSettlementService.getPaidUserCommission(userId) / 100));
        
        byte[] uuid = null;
        if (uuidString != null) {
            try {
                uuid = new UUID(uuidString).asByteArray();
            } catch (Exception e) {
                this.addActionError("uuid: 无效的站长UUID。");
                return Action.ERROR;
            }
            if (!isValidUuid()) {
                this.addActionError("uuid: 不是合法的站长UUID。");
                return Action.ERROR;
            }
        } else {
            uuidString = sites.get(0).getUuid();
            uuid = new UUID(uuidString).asByteArray();
        }
        
        List<PublisherDailyStatistic> publisherDailyStatistics = statsPublisherServices.getPublisherDailyStatistics(
                uuid, dateStart, dateEnd);
        for (PublisherDailyStatistic pb : publisherDailyStatistics) {
            totalPubCommission += pb.getPubCommission();
        }
        // 分页处理
        page.setTotalRecords(publisherDailyStatistics.size());

        int fromIndex = (page.getPageNum() > 0 ? page.getPageNum() - 1 : page.getPageNum()) * page.getPageSize();
        if (fromIndex > publisherDailyStatistics.size()) {
            publisherDailyStatistics = new ArrayList<PublisherDailyStatistic>();
        } else {
            int toIndex = Math.min((page.getPageNum()) * page.getPageSize(), publisherDailyStatistics.size());
            publisherDailyStatistics = publisherDailyStatistics.subList(fromIndex, toIndex);
        }
        request.setAttribute("publisherDailyStatistics", publisherDailyStatistics);
        request.setAttribute("totalPubCommissionDouble", doubleformat.format(totalPubCommission));
        buildJsChart(publisherDailyStatistics, dateStart, dateEnd);
        buildCommissionJsPie(publisherDailyStatistics);
        return SUCCESS;
    }
    
    /*
     * 效果概览
     */
    public String performanceOverview() {
        initQuickDateRange();
        initVisulizeChart();
        int userId = loginHelper.getUserId();
        List<Site> sites = siteService.getUuidSiteByUserId(userId);
        
        if (sites == null || sites.isEmpty()) {
            this.addActionError("你的账户没有网站。");
            return Action.ERROR;
        }
        request.setAttribute("sites", sites);
        
        byte[] uuid = null;
        if (uuidString != null) {
            try {
                uuid = new UUID(uuidString).asByteArray();
            } catch (Exception e) {
                this.addActionError("uuid: 无效的站长UUID。");
                return Action.ERROR;
            }
            if (!isValidUuid()) {
                this.addActionError("uuid: 不是合法的站长UUID。");
                return Action.ERROR;
            }
        } else {
            uuidString = sites.get(0).getUuid();
            uuid = new UUID(uuidString).asByteArray();
        }
        
        List<PublisherDailyStatistic> publisherDailyStatistics = statsPublisherServices.getPublisherDailyStatistics(
                uuid, dateStart, dateEnd);
        for (PublisherDailyStatistic pb : publisherDailyStatistics) {
            totalPubCommission += pb.getPubCommission();
        }
        // 分页处理
        page.setTotalRecords(publisherDailyStatistics.size());

        int fromIndex = (page.getPageNum() > 0 ? page.getPageNum() - 1 : page.getPageNum()) * page.getPageSize();
        if (fromIndex > publisherDailyStatistics.size()) {
            publisherDailyStatistics = new ArrayList<PublisherDailyStatistic>();
        } else {
            int toIndex = Math.min((page.getPageNum()) * page.getPageSize(), publisherDailyStatistics.size());
            publisherDailyStatistics = publisherDailyStatistics.subList(fromIndex, toIndex);
        }
        request.setAttribute("publisherDailyStatistics", publisherDailyStatistics);
        request.setAttribute("totalPubCommissionDouble", doubleformat.format(totalPubCommission));
        buildJsChart(publisherDailyStatistics, dateStart, dateEnd);
        buildCommissionJsPie(publisherDailyStatistics);
        return SUCCESS;
    }

    public String commissionViewByType() {
        initQuickDateRange();
        initVisulizeChart();
        int userId = loginHelper.getUserId();
        List<Site> sites = siteService.getUuidSiteByUserId(userId);
        
        if (sites == null || sites.isEmpty()) {
            this.addActionError("你的账户没有网站。");
            return Action.ERROR;
        }
        request.setAttribute("sites", sites);
        byte[] uuid = null;
        if (uuidString != null) {
            try {
                uuid = new UUID(uuidString).asByteArray();
            } catch (Exception e) {
                this.addActionError("uuid: 无效的站长UUID。");
                return Action.ERROR;
            }
            if (!isValidUuid()) {
                this.addActionError("uuid: 不是合法的站长UUID。");
                return Action.ERROR;
            }
        } else {
            uuidString = sites.get(0).getUuid();
            uuid = new UUID(uuidString).asByteArray();
        }
        
        List<PublisherDailyStatistic> publisherDailyStatistics = statsPublisherServices.getPublisherDailyStatistics(
                uuid, dateStart, dateEnd);
        // 分页处理
        page.setTotalRecords(publisherDailyStatistics.size());

        int fromIndex = (page.getPageNum() > 0 ? page.getPageNum() - 1 : page.getPageNum()) * page.getPageSize();
        if (fromIndex > publisherDailyStatistics.size()) {
            publisherDailyStatistics = new ArrayList<PublisherDailyStatistic>();
        } else {
            int toIndex = Math.min((page.getPageNum()) * page.getPageSize(), publisherDailyStatistics.size());
            publisherDailyStatistics = publisherDailyStatistics.subList(fromIndex, toIndex);
        }
        request.setAttribute("publisherDailyStatistics", publisherDailyStatistics);
        //cauculate totalPubCommission and build chart
        buildCommissionJsChartByType(publisherDailyStatistics, dateStart, dateEnd);
        request.setAttribute("totalPubCommissionDouble", doubleformat.format(totalPubCommission));
        return Action.SUCCESS;
    }

    public String viewPayments() {
        int userId = loginHelper.getUserId();
        payments = paymentService.getByUserId(userId);
        settlements = publisherSettlementService.getByUserId(userId);
        return Action.SUCCESS;
    }


    private void buildJsChart(List<PublisherDailyStatistic> publisherDailyStatistics, Date start, Date end) {
        List<Object[]> pubCommission = new ArrayList<Object[]>();
        if (publisherDailyStatistics != null) {
            for (PublisherDailyStatistic daily : publisherDailyStatistics) {
                pubCommission.add(new Object[] {daily.getPubCommissionDouble(), daily.getDateDay() });
            }
        }
        Collections.reverse(pubCommission);
        jsonDataPubCommission = VisualizeJsUtil.getVisualizeJsData(pubCommission,
                textProvider.getText("buzzads.stats.common.pubCommission"), start, end);
    }
    
    private void buildCommissionJsChartByType(List<PublisherDailyStatistic> publisherDailyStatistics, 
            Date start, Date end) {
        List<Object[]> pubCommission = new ArrayList<Object[]>();
        if (publisherDailyStatistics != null) {
            for (PublisherDailyStatistic daily : publisherDailyStatistics) {
                switch (BidTypeEnum.valueOf(commissionType)) {
                case CPS:
                    double cpsPub = ((double) daily.getCpsPubCommission()) / 100;
                    pubCommission.add(new Object[] {cpsPub, daily.getDateDay() });
                    totalPubCommission += cpsPub;
                    break;
                case CPC:
                    double cpcPub = ((double) daily.getCpcPubCommission()) / 100;
                    pubCommission.add(new Object[] {cpcPub, daily.getDateDay() });
                    totalPubCommission += cpcPub;
                    break;
                case CPM:
                    pubCommission.add(new Object[] {daily.getCpmPubCommission().doubleValue(), daily.getDateDay() });
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
    
    private void buildCommissionJsPie(List<PublisherDailyStatistic> publisherDailyStatistics) {
        List<String> dates = new ArrayList<String>();
        List<Integer> cpsDaily = new ArrayList<Integer>();
        List<Integer> cpcDaily = new ArrayList<Integer>();
        List<Integer> cpmDaily = new ArrayList<Integer>();
        Map<String, List<Integer>> commissionMap = new HashMap<String, List<Integer>>();
        if (publisherDailyStatistics != null) {
            for (PublisherDailyStatistic daily: publisherDailyStatistics) {
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


    public String getJsonDataPubCommission() {
        return jsonDataPubCommission;
    }

    public void setJsonDataPubCommission(String jsonDataPubCommission) {
        this.jsonDataPubCommission = jsonDataPubCommission;
    }
    
    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<PublisherSettlement> getSettlements() {
        return settlements;
    }

    public void setSettlements(List<PublisherSettlement> settlements) {
        this.settlements = settlements;
    }

    public String getUuidString() {
        return uuidString;
    }

    public void setUuidString(String uuidString) {
        this.uuidString = uuidString;
    }

    protected boolean isValidUuid() {
        Site site = siteService.getUuidSiteByUuid(uuidString);
        return site != null && site.getUserId() == loginHelper.getUserId();
    }
    
    public String getJsonDataPubCommissionPie() {
        return jsonDataPubCommissionPie;
    }
    
    public void setJsonDataPubCommissionPie(String jsonDataPubCommissionPie) {
        this.jsonDataPubCommissionPie = jsonDataPubCommissionPie;
    }
    
    public String getCommissionType() {
        return commissionType;
    }
    
    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }
}