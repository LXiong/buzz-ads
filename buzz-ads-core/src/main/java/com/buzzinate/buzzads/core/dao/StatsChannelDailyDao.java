package com.buzzinate.buzzads.core.dao;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.analytics.stats.ChannelDailyStatistic;
import com.buzzinate.buzzads.common.dao.AdsDaoBase;

/**
 * 广告每日数据统计
 * 
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 * 
 *         2013-5-13
 */
@Component
@Transactional(value = "buzzads", readOnly = true)
public class StatsChannelDailyDao extends AdsDaoBase<ChannelDailyStatistic, Integer> {

    public StatsChannelDailyDao() {
        super(ChannelDailyStatistic.class, "id");
    }
    
    public ChannelDailyStatistic getChannelDaily(int channelId, Date day) {
        Query query = getSession().getNamedQuery("StatsChannelDailyDao.getChannelDaily");
        query.setInteger("channelId", channelId);
        query.setDate("dateDay", day);
        return (ChannelDailyStatistic) query.uniqueResult();
    }

}
