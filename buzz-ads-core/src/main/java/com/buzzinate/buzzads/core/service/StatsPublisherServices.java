package com.buzzinate.buzzads.core.service;

import com.buzzinate.buzzads.analytics.stats.AdBasicStatistic;
import com.buzzinate.buzzads.analytics.stats.PublisherDailyStatistic;
import com.buzzinate.buzzads.core.bean.PublisherSettlementStats;
import com.buzzinate.buzzads.core.dao.StatsPublisherDailyDao;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.Site;
import com.buzzinate.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author johnson
 */
@Service
public class StatsPublisherServices {

    @Autowired
    private StatsPublisherDailyDao statPublisherDailyDao;
    @Autowired
    private SiteService siteService;

    public void saveOrUpdate(PublisherDailyStatistic stats) {
        statPublisherDailyDao.saveOrUpdate(stats);
    }

    public List<PublisherDailyStatistic> getAllPublisherDailyStatistics(Date dateDay) {
        return statPublisherDailyDao.getAllPublisherDailyStatistics(dateDay);
    }

    public List<PublisherDailyStatistic> getAllPublisherDailyStatistics(List<byte[]> uuids, Date dateStart,
                                                                        Date dateEnd) {
        List<Object[]> objects = statPublisherDailyDao.getAllPublisherDailyStatistics(uuids, dateStart, dateEnd);
        return buildDailyStatisticsBySite(objects);
    }

    public List<PublisherDailyStatistic> getPublisherDailyStatistics(byte[] uuid, Date dateStart, Date dateEnd) {
        List<Object[]> objects = statPublisherDailyDao.getPublisherDailyStatistics(uuid, dateStart, dateEnd);
        return buildDailyStatisticsByDay(objects);
    }

    public List<PublisherDailyStatistic> buildDailyStatisticsBySite(List<Object[]> objects) {
        List<PublisherDailyStatistic> list = new ArrayList<PublisherDailyStatistic>();
        for (Object[] o : objects) {
            PublisherDailyStatistic dailyStatistic = new PublisherDailyStatistic();
            dailyStatistic.setUuid((byte[]) o[0]);
            dailyStatistic.setCpsOrderNo((Integer) o[1]);
            dailyStatistic.setCpsPubCommission((Integer) o[2]);
            dailyStatistic.setCpcClickNo((Integer) o[3]);
            dailyStatistic.setCpcPubCommission((Integer) o[4]);

            Site site = siteService.getUuidSiteByUuid(dailyStatistic.getUuidString());
            if (site != null) {
                dailyStatistic.setSiteName(site.getName());
            }

            list.add(dailyStatistic);
        }
        return list;
    }

    public List<PublisherDailyStatistic> buildDailyStatisticsByDay(List<Object[]> objects) {
        List<PublisherDailyStatistic> list = new ArrayList<PublisherDailyStatistic>();
        for (Object[] o : objects) {
            PublisherDailyStatistic dailyStatistic = new PublisherDailyStatistic();
            dailyStatistic.setDateDay((Date) o[0]);
            dailyStatistic.setViews((Integer) o[1]);
            dailyStatistic.setClicks((Integer) o[2]);
            dailyStatistic.setCpsOrderNo((Integer) o[3]);
            dailyStatistic.setCpsPubCommission((Integer) o[4]);
            dailyStatistic.setCpcClickNo((Integer) o[5]);
            dailyStatistic.setCpcPubCommission((Integer) o[6]);
            dailyStatistic.setCpmPubCommission(o[7] == null ? new BigDecimal(0) : (BigDecimal) o[7]);
            dailyStatistic.setCpmViewNo((Integer) o[8]);
            dailyStatistic.setPageview((Integer) o[9]);
            list.add(dailyStatistic);
        }
        return list;
    }

    public Long countStats(Date dateStart, Date dateEnd) {
        return statPublisherDailyDao.countStats(dateStart, dateEnd);
    }

    public List<PublisherDailyStatistic> queryStats(Date dateStart, Date dateEnd, Pagination page) {
        return statPublisherDailyDao.queryStats(dateStart, dateEnd, page);
    }

    public AdBasicStatistic getQuerySums(List<byte[]> uuids, Date startDate, Date endDate) {
        return statPublisherDailyDao.getQuerySums(uuids, startDate, endDate);
    }

    public List<PublisherSettlementStats> getPublisherDailyStatsForSettlement(Date date) {
        Date dateDay = DateTimeUtil.getDateDay(date);
        return statPublisherDailyDao.getPublisherDailyStatsForSettlement(dateDay);
    }

    public List<PublisherSettlementStats> getPublisherStatsForRevise(Date dateStart, Date dateEnd) {
        return statPublisherDailyDao.getPublisherStatsForRevise(dateStart, dateEnd);
    }

    public List<PublisherDailyStatistic> viewPublisherAllSite(List<byte[]> uuids, Date dateStart, Date dateEnd) {
        List<Object[]> objects = statPublisherDailyDao.viewPublisherAllSite(uuids, dateStart, dateEnd);
        return buildPublisherAllSiteGeneralStats(objects);
    }

    private List<PublisherDailyStatistic> buildPublisherAllSiteGeneralStats(
            List<Object[]> objects) {
        List<PublisherDailyStatistic> list = new ArrayList<PublisherDailyStatistic>();
        for (Object[] o : objects) {
            PublisherDailyStatistic dailyStatistic = new PublisherDailyStatistic();
            dailyStatistic.setDateDay((Date) o[0]);
            dailyStatistic.setViews((Integer) o[1]);
            dailyStatistic.setClicks((Integer) o[2]);
            dailyStatistic.setCpsOrderNo((Integer) o[3]);
            dailyStatistic.setCpsPubCommission((Integer) o[4]);
            dailyStatistic.setCpcPubCommission((Integer) o[5]);
            dailyStatistic.setCpmPubCommission(o[6] == null ? new BigDecimal(0) : (BigDecimal) o[6]);
            list.add(dailyStatistic);
        }
        return list;
    }

    /*
     * 获得站长某一天的佣金
     */
    public double getDayComm(List<byte[]> uuids, Date day) {
        if (uuids.size() == 0)
            return 0;
        return statPublisherDailyDao.getDayComm(uuids, day);
    }

    /*
     * 获得站长某一时段内的佣金
     */
    public int getRangeDateComm(List<byte[]> uuids, Date start, Date end) {
        if (uuids.size() == 0)
            return 0;
        return statPublisherDailyDao.getRangeDateComm(uuids, start, end);
    }
}
