package com.buzzinate.buzzads.core.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buzzinate.buzzads.analytics.stats.AdBasicStatistic;
import com.buzzinate.buzzads.analytics.stats.AdminDailyStatistic;
import com.buzzinate.buzzads.core.dao.StatsAdminDailyDao;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.common.util.DateTimeUtil;

/**
 * @ClassName: StatsAdminService
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com>
 * 
 */
@Service
public class StatsAdminService {
    @Autowired
    private StatsAdminDailyDao statsAdminDailyDao;

    public void saveOrUpdate(AdminDailyStatistic stats) {
        statsAdminDailyDao.saveOrUpdate(stats);
    }
    
    public List<AdminDailyStatistic> getAdminDailyStatistic(Date dateDay) {
        return statsAdminDailyDao.getAdminDailyStatistic(dateDay);
    }
    
    public List<AdminDailyStatistic> getQueryStats(Date startDate, Date endDate, Pagination page) {
        return statsAdminDailyDao.getQueryStats(startDate, endDate, page);
    }

    public List<Object[]> getQueryChartStats(Date startDate, Date endDate) {
        return statsAdminDailyDao.getQueryChartStats(startDate, endDate);
    }

    public AdBasicStatistic getQuerySums(Date startDate, Date endDate) {
        return statsAdminDailyDao.getQuerySums(startDate, endDate);
    }
    
    public double getDayComm(Date day) {
        AdBasicStatistic adStat = statsAdminDailyDao.getQuerySums(day, day);
        return ((double) adStat.getCpcPubCommission() + (double) adStat.getCpsPubCommission()) / 100 +
                adStat.getCpmPubCommission().doubleValue();
    }

}