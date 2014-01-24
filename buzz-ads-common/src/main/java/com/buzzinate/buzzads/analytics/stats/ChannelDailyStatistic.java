package com.buzzinate.buzzads.analytics.stats;

import com.buzzinate.buzzads.analytics.stats.enums.AdStatisticType;

import java.io.Serializable;
import java.util.Date;

/**
 * 广告每日数据
 *
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 *         <p/>
 *         2013-5-13
 */
public class ChannelDailyStatistic implements Serializable {
    private static final long serialVersionUID = 1767864094356163051L;

    private int id;
    private int channelId;
    private Date dateDay;
    private int views;
    private int clicks;
    private int pageview;

    public ChannelDailyStatistic() {
    }

    public ChannelDailyStatistic(int channelId, Date dateDay) {
        this.channelId = channelId;
        this.dateDay = dateDay;
    }

    public void increaseStats(AdStatisticType type) {
        switch (type) {
            case VIEW:
                views++;
                break;
            case CLICK:
                clicks++;
                break;
            case PAGEVIEW:
                pageview++;
                break;
            default:
                break;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
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

    public Date getDateDay() {
        return dateDay;
    }

    public void setDateDay(Date dateDay) {
        this.dateDay = dateDay;
    }

    public int getPageview() {
        return pageview;
    }

    public void setPageview(int pageview) {
        this.pageview = pageview;
    }
}