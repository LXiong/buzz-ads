package com.buzzinate.buzzads.core.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.analytics.stats.AdBasicStatistic;
import com.buzzinate.buzzads.analytics.stats.AdDailyStatistic;
import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.enums.AdNetworkEnum;

/**
 * 广告每日数据统计
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-11-29
 */
@Component
@Transactional(value = "buzzads", readOnly = true)
public class StatsAdDailyDao extends AdsDaoBase<AdDailyStatistic, Integer> {

    public StatsAdDailyDao() {
        super(AdDailyStatistic.class, "id");
    }

    public Long countQueryStats(Date startDate, Date endDate) {
        Query query = getSession().getNamedQuery("StatsAdDailyDao.countStats");
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        return (Long) query.uniqueResult();
    }

    public Long countQueryStats(List<Integer> entryIds, Date startDate, Date endDate) {
        Query query = getSession().getNamedQuery("StatsAdDailyDao.countStatsWithEntryIds");
        query.setParameterList("entryIds", entryIds);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        return (Long) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<AdDailyStatistic> getQueryStats(Date startDate, Date endDate, Pagination page) {
        Query query = getSession().getNamedQuery("StatsAdDailyDao.queryStats");
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        query.setFirstResult(page.getStartRow());
        query.setMaxResults(page.getPageSize());
        List<Object[]> entries = query.list();
        page.setTotalRecords(countQueryStats(startDate, endDate).intValue());
        page.validatePageInfo();
        page.setReturnRecords(entries.size());

        return getResult(entries);
    }

    @SuppressWarnings("unchecked")
    public List<AdDailyStatistic> getQueryStats(List<Integer> entryIds, Date startDate, Date endDate, Pagination page) {
        Query query = getSession().getNamedQuery("StatsAdDailyDao.queryStatsWithEntryIds");
        query.setParameterList("entryIds", entryIds);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        query.setFirstResult(page.getStartRow());
        query.setMaxResults(page.getPageSize());
        List<Object[]> entries = query.list();
        page.setTotalRecords(countQueryStats(entryIds, startDate, endDate).intValue());
        page.validatePageInfo();
        page.setReturnRecords(entries.size());

        return getResult(entries);
    }

    public Object[] getAdStats(Date startDate, Date endDate, int adEntryId) {
        Query query = getSession().getNamedQuery("StatsAdDailyDao.getAdstatsByEntryId");
        query.setLong("adEntryId", adEntryId);
        query.setDate("start", startDate);
        query.setDate("end", endDate);
        Object[] obj = (Object[]) query.uniqueResult();
        if (obj[0] == null)
            obj = null;
        return obj;
    }
    
    private List<AdDailyStatistic> getResult(List<Object[]> entries) {
        List<AdDailyStatistic> res = new ArrayList<AdDailyStatistic>();
        Iterator<Object[]> iter = entries.iterator();
        while (iter.hasNext()) {
            Object[] obj = (Object[]) iter.next();
            AdDailyStatistic ad = new AdDailyStatistic();
            ad.setAdEntryId((Integer) obj[0]);
            ad.setViews((Integer) obj[1]);
            ad.setCpsOrderNo((Integer) obj[2]);
            ad.setCpsTotalCommission((Integer) obj[3]);
            ad.setCpcClickNo((Integer) obj[4]);
            ad.setCpcTotalCommission((Integer) obj[5]);
            ad.setCpsTotalConfirmedCommission((Integer) obj[6]);
            res.add(ad);
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    public AdBasicStatistic getQuerySums(List<Integer> entryIds, Date startDate, Date endDate) {
        Query query = getSession().getNamedQuery("StatsAdDailyDao.querySumsWithEntryIds");
        query.setParameterList("entryIds", entryIds);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        List<Object[]> entries = query.list();

        AdBasicStatistic ad = new AdBasicStatistic();
        Iterator<Object[]> iter = entries.iterator();
        if (iter.hasNext()) {
            Object[] obj = (Object[]) iter.next();
            ad.setCpsTotalCommission((Integer) obj[0]);
            ad.setCpsPubCommission((Integer) obj[1]);
            ad.setCpcTotalCommission((Integer) obj[2]);
            ad.setCpcPubCommission((Integer) obj[3]);
            ad.setCpsTotalConfirmedCommission((Integer) obj[4]);
        }
        return ad;
    }
    
    @SuppressWarnings("unchecked")
    public List<AdDailyStatistic> getAdStatsByOrderIds(Date startDate, Date endDate, List<Integer> orderIds) {
        Query query = getSession().getNamedQuery("StatsAdDailyDao.getAdStatsByOrderIds");
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        query.setParameterList("orderIds", orderIds.toArray());
        return (List<AdDailyStatistic>) query.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<AdDailyStatistic> getAdStatsByEntryIds(Date startDate, Date endDate, List<Integer> entryIds) {
        if (entryIds == null || entryIds.isEmpty()) {
            return new ArrayList<AdDailyStatistic>();
        }
        Query query = getSession().getNamedQuery("StatsAdDailyDao.getAdStatsByEntryIds");
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        query.setParameterList("entryIds", entryIds.toArray());
        return (List<AdDailyStatistic>) query.list();
    }
    
    
    @SuppressWarnings("unchecked")
    public List<AdDailyStatistic> getAdStatsByCondition(int entryId, Date day, Set<AdNetworkEnum> network) {
        Criteria criteria = getSession().createCriteria(AdDailyStatistic.class);
        ProjectionList list = Projections.projectionList();
        if (entryId > 0) {
            criteria.add(Restrictions.eq("adEntryId", entryId));
        }
        list.add(Projections.groupProperty("adEntryId"));
        if (network.size() > 0) {
            criteria.add(Restrictions.in("network", network));
            list.add(Projections.groupProperty("network"));
        }
        criteria.add(Restrictions.eq("dateDay", day));
        list.add(Projections.sum("clicks"));
        list.add(Projections.sum("views"));
        list.add(Projections.sum("cpcClickNo"));
        
        criteria.setProjection(list);
        List<AdDailyStatistic> adStats = new ArrayList<AdDailyStatistic>();
        List<Object[]> stats = criteria.list();
        for (Object[] o : stats) {
            AdDailyStatistic statistic = new AdDailyStatistic();
            statistic.setAdEntryId((Integer) o[0]);
            if (network.size() > 0) {
                statistic.setNetwork((AdNetworkEnum) o[1]);
                statistic.setClicks(((Long) o[2]).intValue());
                statistic.setViews(((Long) o[3]).intValue());
                statistic.setCpcClickNo(((Long) o[4]).intValue());
            } else {
                statistic.setClicks(((Long) o[1]).intValue());
                statistic.setViews(((Long) o[2]).intValue());
                statistic.setCpcClickNo(((Long) o[3]).intValue());
            }
            adStats.add(statistic);
        }
            
        return adStats;
    }

    @Transactional(value = "buzzads", readOnly = false)
    public void updateAdDailyStatProportion(List<byte[]> uuidList,
            Date startDate, Date endDate, int proportion) {
        Query query = getSession().getNamedQuery("StatsAdDailyDao.updateAdDailyStatProportion");
        query.setParameterList("uuidList", uuidList);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        query.setDouble("proportion", proportion / 100.0);
        query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getOrderInfoByProportion(List<byte[]> uuidList, Date startDate, Date endDate) {
        Query query = getSession().getNamedQuery("StatsAdDailyDao.getOrderInfoByProportion");
        query.setParameterList("uuidList", uuidList);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        return query.list();
    }

}
    