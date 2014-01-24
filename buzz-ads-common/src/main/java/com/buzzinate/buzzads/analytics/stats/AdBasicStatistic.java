package com.buzzinate.buzzads.analytics.stats;

import com.buzzinate.buzzads.analytics.stats.enums.AdStatisticType;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * view、click、cpc、cps统计基类
 *
 * @author Jacky Lu<jacky.lu@buzzinate.com>
 *         <p/>
 *         2012-12-11
 */
public class AdBasicStatistic {

    protected static DecimalFormat format = new DecimalFormat("#,###,##0.00%");
    protected static DecimalFormat doubleformat = new DecimalFormat("#,###,##0.00");
    protected Date dateDay;
    protected int clicks;

    protected int views;
    //cpc浏览数
    private int cpcViewNo;
    //cpc有效点击
    private int cpcClickNo;
    //cpc站长佣金，即我们发放给站长的佣金
    private int cpcPubCommission;
    //cpc佣金总额，即我们得到的佣金
    private int cpcTotalCommission;
    //产生的cps订单总数
    private int cpsOrderNo;
    //cps站长实际佣金，即已确认的cps佣金
    private int cpsConfirmedCommission;
    //cps站长预期佣金，包括已确认和未确认的cps佣金
    private int cpsPubCommission;
    //cps佣金总额，即我们得到的佣金
    private int cpsTotalCommission;
    // cps订单总额
    private int cpsTotalPrice;
    //cps收入，即广告商给我们的实际收入
    private int cpsTotalConfirmedCommission;
    //cpm展示
    private int cpmViewNo;
    //cpm有效点击
    private int cpmClickNo;
    //cpm站长佣金，即我们发放给站长的佣金
    private BigDecimal cpmPubCommission = new BigDecimal(0);
    //cpm佣金总额，即我们得到的佣金
    private BigDecimal cpmTotalCommission = new BigDecimal(0);
    // cpc原始点击数(在entry、order、campaign下同clicks，仅在admin中统计)
    private int cpcOriginalClickNo;
    //cps浏览数(仅在admin中统计)
    private int cpsViewNo;
    //cps有效点击(仅在admin中统计)
    private int cpsClickNo;
    
    public AdBasicStatistic() {

    }

    public AdBasicStatistic(Date dateDay) {
        this.dateDay = dateDay;
    }

    public AdBasicStatistic(long views, long clicks, long cpcClickNo, long cpcTotalCommission, long cpsTotalCommission) {
        this.views = ((Long) views).intValue();
        this.clicks = ((Long) clicks).intValue();
        this.cpcClickNo = ((Long) cpcClickNo).intValue();
        this.cpcTotalCommission = ((Long) cpcTotalCommission).intValue();
        this.cpsTotalCommission = ((Long) cpsTotalCommission).intValue();
    }
    
    public AdBasicStatistic(long views, long clicks, long cpcClickNo,
            long cpmViewNo, long cpcTotalCommission, long cpsTotalCommission,
            BigDecimal cpmTotalCommission) {
        this.views = ((Long) views).intValue();
        this.clicks = ((Long) clicks).intValue();
        this.cpcClickNo = ((Long) cpcClickNo).intValue();
        this.cpmViewNo = ((Long) cpmViewNo).intValue();
        this.cpcTotalCommission = ((Long) cpcTotalCommission).intValue();
        this.cpsTotalCommission = ((Long) cpsTotalCommission).intValue();
        this.cpmTotalCommission = cpmTotalCommission;
    }

    public Date getDateDay() {
        return dateDay;
    }

    public void setDateDay(Date dateDay) {
        this.dateDay = dateDay;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getCpcViewNo() {
        return cpcViewNo;
    }

    public void setCpcViewNo(int cpcViewNo) {
        this.cpcViewNo = cpcViewNo;
    }

    public int getCpcClickNo() {
        return cpcClickNo;
    }

    public void setCpcClickNo(int cpcClickNo) {
        this.cpcClickNo = cpcClickNo;
    }

    public int getCpcPubCommission() {
        return cpcPubCommission;
    }

    public void setCpcPubCommission(int cpcPubCommission) {
        this.cpcPubCommission = cpcPubCommission;
    }

    public int getCpcTotalCommission() {
        return cpcTotalCommission;
    }

    public void setCpcTotalCommission(int cpcTotalCommission) {
        this.cpcTotalCommission = cpcTotalCommission;
    }

    public int getCpsOrderNo() {
        return cpsOrderNo;
    }

    public void setCpsOrderNo(int cpsOrderNo) {
        this.cpsOrderNo = cpsOrderNo;
    }

    public int getCpsConfirmedCommission() {
        return cpsConfirmedCommission;
    }

    public void setCpsConfirmedCommission(int cpsConfirmedCommission) {
        this.cpsConfirmedCommission = cpsConfirmedCommission;
    }

    public int getCpsPubCommission() {
        return cpsPubCommission;
    }

    public void setCpsPubCommission(int cpsPubCommission) {
        this.cpsPubCommission = cpsPubCommission;
    }

    public int getCpsTotalCommission() {
        return cpsTotalCommission;
    }

    public void setCpsTotalCommission(int cpsTotalCommission) {
        this.cpsTotalCommission = cpsTotalCommission;
    }

    public int getCpsTotalPrice() {
        return cpsTotalPrice;
    }

    public void setCpsTotalPrice(int cpsTotalPrice) {
        this.cpsTotalPrice = cpsTotalPrice;
    }

    public int getCpsTotalConfirmedCommission() {
        return cpsTotalConfirmedCommission;
    }

    public void setCpsTotalConfirmedCommission(int cpsTotalConfirmedCommission) {
        this.cpsTotalConfirmedCommission = cpsTotalConfirmedCommission;
    }

    public int getCpmViewNo() {
        return cpmViewNo;
    }

    public void setCpmViewNo(int cpmViewNo) {
        this.cpmViewNo = cpmViewNo;
    }

    public int getCpmClickNo() {
        return cpmClickNo;
    }

    public void setCpmClickNo(int cpmClickNo) {
        this.cpmClickNo = cpmClickNo;
    }

    public BigDecimal getCpmPubCommission() {
        if (this.cpmPubCommission == null) {
            return new BigDecimal(0);
        }
        return cpmPubCommission;
    }
    
    public String getCpmPubCommissionDouble() {
        return doubleformat.format(getCpmPubCommission().doubleValue());
    }

    public void setCpmPubCommission(BigDecimal cpmPubCommission) {
        this.cpmPubCommission = cpmPubCommission;
    }

    public BigDecimal getCpmTotalCommission() {
        if (this.cpmTotalCommission == null) {
            return new BigDecimal(0);
        }
        return cpmTotalCommission;
    }
    
    public String getCpmTotalCommissionDouble() {
        return doubleformat.format(getCpmTotalCommission().doubleValue());
    }

    public void setCpmTotalCommission(BigDecimal cpmTotalCommission) {
        this.cpmTotalCommission = cpmTotalCommission;
    }

    public String getClickToView() {
        if (views == 0)
            return "-";
        return format.format((double) clicks / views);
    }

    public String getCpcClickToView() {
        if (cpcViewNo == 0)
            return "-";
        return format.format((double) cpcClickNo / cpcViewNo);
    }

    public String getCpcCTR() {
        if (views == 0) {
            return "-";
        }
        return format.format((double) cpcClickNo / views);
    }

    public String getCpsOrderToView() {
        if (views == 0)
            return "-";
        return format.format((double) cpsOrderNo / views);
    }

    public String getOrderToView() {
        if (views == 0)
            return "-";
        return format.format((double) getOrderNo() / views);
    }

    public int getCpcOriginalClickNo() {
        return cpcOriginalClickNo;
    }

    public void setCpcOriginalClickNo(int cpcOriginalClickNo) {
        this.cpcOriginalClickNo = cpcOriginalClickNo;
    }

    public int getCpsViewNo() {
        return cpsViewNo;
    }

    public void setCpsViewNo(int cpsViewNo) {
        this.cpsViewNo = cpsViewNo;
    }

    public int getCpsClickNo() {
        return cpsClickNo;
    }

    public void setCpsClickNo(int cpsClickNo) {
        this.cpsClickNo = cpsClickNo;
    }

    public void increaseStats(AdBasicStatistic adStats, AdStatisticType type) {
        switch (type) {
        case VIEW:
            views++;
            break;
        case CLICK:
            clicks++;
            break;
        case CPC_VIEW:
            views++;
            cpcViewNo++;
            break;
        case CPS_CLICK:
            clicks++;
            cpsClickNo++;
            break;
        case CPS_VIEW:
            views++;
            cpsViewNo++;
            break;
        case CPC_CLICK:
            clicks++;
            cpcOriginalClickNo++;
            // cpcClickNo handled in CpcStatsTask
            break;
        case CPM_CLICK:
            clicks++;
            cpmClickNo++;
            break;
        case CPM_VIEW:
            views++;
            cpmViewNo++;
            if (adStats != null) {
                cpmPubCommission = cpmPubCommission.add(adStats
                        .getCpmPubCommission());
                cpmTotalCommission = cpmTotalCommission.add(adStats
                        .getCpmTotalCommission());
            }
            break;
        case CPS_FINISHED:
            cpsOrderNo++;
            if (adStats != null) {
                cpsPubCommission += adStats.getCpsPubCommission();
                cpsTotalCommission += adStats.getCpsTotalCommission();
                cpsTotalPrice += adStats.getCpsTotalPrice();
            }
            break;
        case CPS_CONFIRMED:
            if (adStats != null) {
                cpsConfirmedCommission += adStats.getCpsConfirmedCommission();
                cpsTotalConfirmedCommission += adStats
                        .getCpsTotalConfirmedCommission();
            }
            break;
        default:
            break;
        }
    }

    public String getCpcPubCommissionDouble() {
        return doubleformat.format((double) cpcPubCommission / 100);
    }

    public String getCpsPubCommissionDouble() {
        return doubleformat.format((double) cpsPubCommission / 100);
    }

    public String getCpcTotalCommissionDouble() {
        return doubleformat.format((double) cpcTotalCommission / 100);
    }

    public String getCpsConfirmedCommissionDouble() {
        return doubleformat.format((double) cpsConfirmedCommission / 100);
    }

    public String getCpsTotalCommissionDouble() {
        return doubleformat.format((double) cpsTotalCommission / 100);
    }

    public String getCpsTotalConfirmedCommissionDouble() {
        return doubleformat.format((double) cpsTotalConfirmedCommission / 100);
    }

    public String getCpsTotalPriceDouble() {
        return doubleformat.format((double) cpsTotalPrice / 100);
    }

    public String getPubCommissionDouble() {
        return doubleformat.format(getPubCommission());
    }

    public double getPubCommission() {
        return ((double) cpsPubCommission + (double) cpcPubCommission) / 100 +
                cpmPubCommission.doubleValue();
    }

    public String getTotalCommissionDouble() {
        return doubleformat.format(getTotalCommission());
    }

    public double getTotalCommission() {
        return ((double) cpsTotalCommission + (double) cpcTotalCommission) / 100 +
                cpmTotalCommission.doubleValue();
    }

    public int getOrderNo() {
        return cpsOrderNo + cpcClickNo + cpmViewNo;
    }

    //每千次展示获得的收入
    public String getPubECPM() {
        if (views == 0)
            return "-";
        return "¥" + doubleformat.format(getPubCommission() * 1000 / views);
    }

    public String getTotalECPM() {
        if (views == 0)
            return "-";
        return "¥" + doubleformat.format(getTotalCommission() * 1000 / views);
    }

}