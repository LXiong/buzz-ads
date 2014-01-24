package com.buzzinate.buzzads.core.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.analytics.stats.AdBasicStatistic;
import com.buzzinate.buzzads.analytics.stats.PublisherDailyStatistic;
import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.core.bean.PublisherSettlementStats;
import com.buzzinate.buzzads.core.util.Pagination;
/**
 *
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 *         2012-11-30
 */
@Component
@Transactional(value = "buzzads", readOnly = true)
public class StatsPublisherDailyDao extends AdsDaoBase<PublisherDailyStatistic, Integer> {

    public StatsPublisherDailyDao() {
        super(PublisherDailyStatistic.class, "id");
    }

    @SuppressWarnings("unchecked")
    public List<PublisherDailyStatistic> getAllPublisherDailyStatistics(Date dateDay) {
        Criteria criteria = getSession().createCriteria(PublisherDailyStatistic.class);
        criteria.add(Restrictions.eq("dateDay", dateDay));
        return (List<PublisherDailyStatistic>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getAllPublisherDailyStatistics(List<byte[]> uuids, Date dateStart, Date dateEnd) {
        Query query = getSession().getNamedQuery("StatsPublisherDailyDao.getAllPublisherDailyStatistics");
        query.setParameterList("uuids", uuids);
        query.setDate("dateStart", dateStart);
        query.setDate("dateEnd", dateEnd);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getPublisherDailyStatistics(byte[] uuid, Date dateStart, Date dateEnd) {
        Query query = getSession().getNamedQuery("StatsPublisherDailyDao.getPublisherDailyStatistics");
        query.setBinary("uuid", uuid);
        query.setDate("dateStart", dateStart);
        query.setDate("dateEnd", dateEnd);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Transactional(value = "buzzads", readOnly = true)
    public List<PublisherSettlementStats> getPublisherDailyStatsForSettlement(Date day) {
        Query query = getSession().getNamedQuery("StatsPublisherDailyDao.getPublisherDailyStatsForSettlement");
        query.setDate("date", day);
        List<Object[]> list = query.list();

        List<PublisherSettlementStats> res = new ArrayList<PublisherSettlementStats>();
        for (Object[] o : list) {
            PublisherSettlementStats s = new PublisherSettlementStats((byte[]) o[0], day);
            s.setCpcNo((Integer) o[1]);
            s.setCpcCommission((Integer) o[2]);
            s.setCpsNo((Integer) o[3]);
            s.setCpsCommission((Integer) o[4]);
            s.setCpmNo((Integer) o[5]);
            s.setCpmCommission(o[6] == null ? new BigDecimal(0) : (BigDecimal) o[6]);
            s.setCommission(s.getCpcCommission() + s.getCpsCommission() + s.getCpmCommission().multiply(
                    new BigDecimal(100)).intValue());
            res.add(s);
        }
        return res;
    }
    
    @SuppressWarnings("unchecked")
    public List<PublisherSettlementStats> getPublisherStatsForRevise(Date dateStart, Date dateEnd) {
        Query query = getSession().getNamedQuery("StatsPublisherDailyDao.getPublisherStatsForRevise");
        query.setDate("dateStart", dateStart);
        query.setDate("dateEnd", dateEnd);
        List<PublisherDailyStatistic> list = (List<PublisherDailyStatistic>) query.list();
        List<PublisherSettlementStats> res = new ArrayList<PublisherSettlementStats>();
        for (PublisherDailyStatistic statistic : list) {
            PublisherSettlementStats s = new PublisherSettlementStats();
            s.setUuid(statistic.getUuid());
            s.setCpcNo(statistic.getCpcClickNo());
            s.setCpcCommission(statistic.getCpcPubCommission());
            s.setCpsNo(statistic.getCpsOrderNo());
            s.setCpsCommission(statistic.getCpsConfirmedCommission());
            s.setCpmNo(statistic.getCpmViewNo());
            s.setCpmCommission(statistic.getCpmPubCommission());
            res.add(s);
        }
        return res;
    }

    public Long countStats(Date dateStart, Date dateEnd) {
        Query query = getSession().getNamedQuery("StatsPublisherDailyDao.countStats");
        query.setDate("dateStart", dateStart);
        query.setDate("dateEnd", dateEnd);
        return (Long) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Transactional(value = "buzzads", readOnly = true)
    public List<PublisherDailyStatistic> queryStats(Date dateStart, Date dateEnd, Pagination page) {
        Query query = getSession().getNamedQuery("StatsPublisherDailyDao.queryStats");
        query.setDate("dateStart", dateStart);
        query.setDate("dateEnd", dateEnd);
        List<Object[]> entries = query.list();

        page.setTotalRecords(countStats(dateStart, dateEnd).intValue());
        page.validatePageInfo();
        page.setReturnRecords(entries.size());

        List<PublisherDailyStatistic> list = new ArrayList<PublisherDailyStatistic>();
        for (Object[] o : entries) {
            PublisherDailyStatistic stat = new PublisherDailyStatistic();
            stat.setUuid((byte[]) o[0]);
            stat.setCpsOrderNo((Integer) o[1]);
            stat.setCpsPubCommission((Integer) o[2]);
            stat.setCpcClickNo((Integer) o[3]);
            stat.setCpcPubCommission((Integer) o[4]);

            list.add(stat);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public AdBasicStatistic getQuerySums(List<byte[]> uuids, Date startDate, Date endDate) {
        Query query = getSession().getNamedQuery("StatsPublisherDailyDao.querySumsWithUuids");
        query.setDate("dateStart", startDate);
        query.setDate("dateEnd", endDate);
        query.setParameterList("uuids", uuids);
        List<Object[]> entries = query.list();
        AdBasicStatistic publisher = new AdBasicStatistic();
        Iterator<Object[]> iter = entries.iterator();
        if (iter.hasNext()) {
            Object[] obj = (Object[]) iter.next();
            publisher.setCpsTotalCommission((Integer) obj[0]);
            publisher.setCpsPubCommission((Integer) obj[1]);
            publisher.setCpcTotalCommission((Integer) obj[2]);
            publisher.setCpcPubCommission((Integer) obj[3]);
        }
        return publisher;
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> viewPublisherAllSite(List<byte[]> uuids, Date dateStart, Date dateEnd) {
        Query query = getSession().getNamedQuery("StatsPublisherDailyDao.viewPublisherAllSite");
        query.setParameterList("uuids", uuids);
        query.setDate("dateStart", dateStart);
        query.setDate("dateEnd", dateEnd);
        return query.list();
    }

    public double getDayComm(List<byte[]> uuids, Date day) {
        Query query = getSession().getNamedQuery("StatsPublisherDailyDao.getDayComm");
        query.setParameterList("uuids", uuids);
        query.setDate("day", day);
        Object[] o = (Object[]) query.uniqueResult();
        if (o != null) {
            Integer cpsComm = (Integer) o[0];
            Integer cpcComm = (Integer) o[1];
            BigDecimal cpmComm = o[2] == null ? new BigDecimal(0) : (BigDecimal) o[2];
            return ((double) cpsComm + (double) cpcComm) / 100 +
                    cpmComm.doubleValue();
        }
        return 0.0;
    }

    public int getRangeDateComm(List<byte[]> uuids, Date start, Date end) {
        Query query = getSession().getNamedQuery("StatsPublisherDailyDao.getRangeDateComm");
        query.setParameterList("uuids", uuids);
        query.setDate("start", start);
        query.setDate("end", end);
        return query.uniqueResult() == null ? 0 : (Integer) query.uniqueResult();
    }
    
    @Transactional(value = "buzzads", readOnly = false)
    public void updateAllPublisherDailyStatistics(List<byte[]> uuids, Date dateStart, Date dateEnd, int proportion) {
        Query query = getSession().getNamedQuery("StatsPublisherDailyDao.updateAllPublisherDailyStatistics");
        query.setDouble("proportion", proportion / 100.0);
        query.setParameterList("uuids", uuids);
        query.setDate("dateStart", dateStart);
        query.setDate("dateEnd", dateEnd);
        query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getAdminInfoByProportion(List<byte[]> uuids, Date dateStart, Date dateEnd) {
        Query query = getSession().getNamedQuery("StatsPublisherDailyDao.getAdminInfoByProportion");
        query.setParameterList("uuids", uuids);
        query.setDate("dateStart", dateStart);
        query.setDate("dateEnd", dateEnd);
        return query.list();
    }

}
