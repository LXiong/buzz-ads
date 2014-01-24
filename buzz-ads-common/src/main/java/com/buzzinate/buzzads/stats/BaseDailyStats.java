package com.buzzinate.buzzads.stats;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2013-3-8
 */
public class BaseDailyStats implements Serializable {
    
    protected static DecimalFormat doubleformat = new DecimalFormat("#,###,##0.00");
    protected static DecimalFormat doubleNoCommaFormat = new DecimalFormat("######0.00");
    protected static DecimalFormat format = new DecimalFormat("#,###,##0.00%");
    private static final long serialVersionUID = -2058505115492692375L;
    // 统计信息
    private int views;
    private int clicks;
    private int cpcClicks;
    private int cpcComm;
    private int cpmViews;
    private int cpsComm;
    private BigDecimal cpmComm = new BigDecimal(0);
    
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
    public int getCpcComm() {
        return cpcComm;
    }
    public void setCpcComm(int cpcComm) {
        this.cpcComm = cpcComm;
    }
    public int getCpsComm() {
        return cpsComm;
    }
    public void setCpsComm(int cpsComm) {
        this.cpsComm = cpsComm;
    }
    
    public int getCpcClicks() {
        return cpcClicks;
    }
    public void setCpcClicks(int cpcClicks) {
        this.cpcClicks = cpcClicks;
    }
    public int getCpmViews() {
        return cpmViews;
    }
    
    public void setCpmViews(int cpmViews) {
        this.cpmViews = cpmViews;
    }
    
    public BigDecimal getCpmComm() {
        if (this.cpmComm == null) {
            return new BigDecimal(0);
        }
        return cpmComm;
    }
    public void setCpmComm(BigDecimal cpmComm) {
        this.cpmComm = cpmComm;
    }
    
    
    public double getCostDouble() {
        return ((double) cpcComm + (double) cpsComm) / 100 + cpmComm.doubleValue();
    }
    
    /*
     * 总费用
     */
    public String getCost() {
        return doubleformat.format(getCostDouble());
    }
    
    /*
     * 平均千次展示费用(单位为￥)
     */
    public String getAvgViewCost() {
        if (views == 0)
            return "-";
        return doubleformat.format(getCostDouble() * 1000 / views);
    }
    
    /*
     * 平均点击费用
     */
    public String getAvgClickCost() {
        if (clicks == 0)
            return "-";
        return doubleformat.format(getCostDouble()  / clicks);
    }
    
    /*
     * 点击/展示
     */
    public String getClickToView() {
        if (views == 0)
            return "-";
        return format.format((double) clicks / views);
    }
    
    /*
     * 有效点击/展示
     */
    public String getCpcClickToView() {
        if (views == 0)
            return "-";
        return format.format((double) cpcClicks / views);
    }

}
