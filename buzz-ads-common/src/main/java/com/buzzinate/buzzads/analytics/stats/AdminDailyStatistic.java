package com.buzzinate.buzzads.analytics.stats;


import com.buzzinate.buzzads.analytics.stats.enums.AdStatisticType;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.common.util.DateTimeUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理员每日数据
 *
 * @author Jacky Lu<jacky.lu@buzzinate.com>
 *         <p/>
 *         2012-12-11
 */
public class AdminDailyStatistic extends AdBasicStatistic implements Serializable {


    private static final long serialVersionUID = -1214393287535369836L;
    private int id;
    private AdNetworkEnum network;
    private int pageview;

    public AdminDailyStatistic() { }
    
    public AdminDailyStatistic(Date dateDay, AdNetworkEnum network) {
        this.network = network;
        this.dateDay = dateDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AdNetworkEnum getNetwork() {
        return network;
    }

    public void setNetwork(AdNetworkEnum network) {
        this.network = network;
    }

    public String getDay() {
        return DateTimeUtil.formatDate(dateDay);
    }

    public int getPageview() {
        return pageview;
    }

    public void setPageview(int pageview) {
        this.pageview = pageview;
    }

}