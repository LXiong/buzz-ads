package com.buzzinate.buzzads.core.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.analytics.stats.AdBasicStatistic;
import com.buzzinate.buzzads.analytics.stats.AdDailyStatistic;
import com.buzzinate.buzzads.core.dao.StatsAdDailyDao;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.enums.AdNetworkEnum;

/**
 * @ClassName: StatsAdService
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com>
 */
@Service
public class StatsAdService {
    @Autowired
    private StatsAdDailyDao statsAdDailyDao;

    @Transactional(value = "buzzads", readOnly = true)
    public List<AdDailyStatistic> getQueryStats(List<Integer> entryIds, Date startDate, Date endDate, Pagination page) {
        return statsAdDailyDao.getQueryStats(entryIds, startDate, endDate, page);
    }

    @Transactional(value = "buzzads", readOnly = true)
    public List<AdDailyStatistic> getQueryStats(Date startDate, Date endDate, Pagination page) {
        return statsAdDailyDao.getQueryStats(startDate, endDate, page);
    }

    public AdBasicStatistic getQuerySums(List<Integer> entryIds, Date startDate, Date endDate) {
        return statsAdDailyDao.getQuerySums(entryIds, startDate, endDate);
    }
    
    public List<AdDailyStatistic> getAdStatsByCondition(int entryId, Date dateDay, Set<AdNetworkEnum> network) {
        return statsAdDailyDao.getAdStatsByCondition(entryId, dateDay, network);
    }
}
