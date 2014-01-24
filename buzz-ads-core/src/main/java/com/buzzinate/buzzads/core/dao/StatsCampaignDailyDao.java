package com.buzzinate.buzzads.core.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.analytics.stats.AdCampaignDailyStatistic;
import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.core.util.Pagination;

/**
 *
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 *         2013-3-5
 */
@Component
@Transactional(value = "buzzads", readOnly = true)
public class StatsCampaignDailyDao extends
        AdsDaoBase<AdCampaignDailyStatistic, Integer> {

    public StatsCampaignDailyDao() {
        super(AdCampaignDailyStatistic.class, "id");
    }

    @Transactional(value = "buzzads", readOnly = false)
    public Object[] getSumStatsByCampRangeTime(int campId, Date start, Date end) {
        Query query = getSession().getNamedQuery("StatsCampaignDailyDao.getSumStatsByCampRangeTime");
        query.setInteger("campaignId", campId);
        query.setDate("start", start);
        query.setDate("end", end);
        Object[] obj = (Object[]) query.uniqueResult();
        if (obj[0] == null)
            obj = null;
        return obj;
    }

    @Transactional(value = "buzzads", readOnly = false)
    public Object[] sumCampStatsByAdvertiserIdWithRangeDate(int advId, Date start, Date end) {
        Query query = getSession().getNamedQuery("StatsCampaignDailyDao.sumCampStatsByAdvertiserIdWithRangeDate");
        query.setInteger("advId", advId);
        query.setDate("start", start);
        query.setDate("end", end);
        Object[] obj = (Object[]) query.uniqueResult();
        if (obj[0] == null)
            obj = null;
        return obj;
    }

    @Transactional(value = "buzzads", readOnly = false)
    public Object[] sumCampStatsWithRangeDate(Date start, Date end) {
        Query query = getSession().getNamedQuery("StatsCampaignDailyDao.sumCampStatsWithRangeDate");
        query.setDate("start", start);
        query.setDate("end", end);
        Object[] obj = (Object[]) query.uniqueResult();
        if (obj[0] == null)
            obj = null;
        return obj;
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> listAllCampStats(int advertiserId, Date start, Date end) {
        Query query = getSession().getNamedQuery("StatsCampaignDailyDao.pageAllCampStats");
        query.setInteger("advId", advertiserId);
        query.setDate("start", start);
        query.setDate("end", end);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> adminViewAllCampStats(Date start, Date end) {
        //StatsCampaignDailyDao.adminViewAllCampStats");
        Query query = getSession().getNamedQuery("StatsCampaignDailyDao.adminPageAllCampStats");
        query.setDate("start", start);
        query.setDate("end", end);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> pageAllCampStats(int advertiserId, Date start, Date end, Pagination page) {
        Query query = getSession().getNamedQuery("StatsCampaignDailyDao.pageAllCampStats");
        query.setInteger("advId", advertiserId);
        query.setDate("start", start);
        query.setDate("end", end);
        query.setFirstResult(page.getStartRow());
        query.setMaxResults(page.getPageSize());
        //处理分页
        int totalRecords = pageAllStatsCount(advertiserId, start, end);
        page.setTotalRecords(totalRecords);
        page.validatePageInfo();
        List<Object[]> objs = query.list();
        page.setReturnRecords(objs.size());
        return objs;
    }

    public int pageAllStatsCount(int advertiserId, Date start, Date end) {
        Query query = getSession().getNamedQuery("StatsCampaignDailyDao.pageAllCampStats.count");
        query.setInteger("advId", advertiserId);
        query.setDate("start", start);
        query.setDate("end", end);
        return (Integer) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> adminPageAllCampStats(Date start, Date end, Pagination page) {
        Query query = getSession().getNamedQuery("StatsCampaignDailyDao.adminPageAllCampStats");
        query.setDate("start", start);
        query.setDate("end", end);
        query.setFirstResult(page.getStartRow());
        query.setMaxResults(page.getPageSize());
        //处理分页
        int totalRecords = adminViewAllStatsCount(start, end);
        page.setTotalRecords(totalRecords);
        page.validatePageInfo();
        List<Object[]> objs = query.list();
        page.setReturnRecords(objs.size());
        return objs;
    }

    public int adminViewAllStatsCount(Date start, Date end) {
        Query query = getSession().getNamedQuery("StatsCampaignDailyDao.adminPageAllCampStats.count");
        query.setDate("start", start);
        query.setDate("end", end);
        return (Integer) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> pageCampStatsByCampId(int campId, Date start, Date end, Pagination page) {
        Query query = getSession().getNamedQuery("StatsCampaignDailyDao.pageCampStatsByCampId");
        query.setInteger("campId", campId);
        query.setDate("start", start);
        query.setDate("end", end);
        query.setFirstResult(page.getStartRow());
        query.setMaxResults(page.getPageSize());
        //处理分页                         4
        int totalRecords = pageCampStatsByCampIdCount(campId, start, end);
        page.setTotalRecords(totalRecords);
        page.validatePageInfo();
        List<Object[]> objs = query.list();
        page.setReturnRecords(objs.size());
        return objs;
    }

    public List<Object[]> pageCampStatsByCampIdBeforeDate(int campaignId, Date endDay, Pagination page) {
        Query query = getSession().getNamedQuery("StatsCampaignDailyDao.pageCampStatsByCampIdBeforeDate");
        query.setInteger("campId", campaignId);
        query.setDate("end", endDay);
        query.setFirstResult(page.getStartRow());
        query.setMaxResults(page.getPageSize());
        //处理分页                         4
        int totalRecords = pageCampStatsByCampIdBeforeDateCount(campaignId, endDay);
        page.setTotalRecords(totalRecords);
        page.validatePageInfo();
        List<Object[]> objs = query.list();
        page.setReturnRecords(objs.size());
        return objs;
    }

    private int pageCampStatsByCampIdBeforeDateCount(int campaignId, Date endDay) {
        Query query = getSession().getNamedQuery("StatsCampaignDailyDao.pageCampStatsByCampIdBeforeDate.count");
        query.setInteger("campId", campaignId);
        query.setDate("end", endDay);
        return (Integer) query.uniqueResult();
    }

    public int pageCampStatsByCampIdCount(int campId, Date start, Date end) {
        Query query = getSession().getNamedQuery("StatsCampaignDailyDao.pageCampStatsByCampId.count");
        query.setInteger("campId", campId);
        query.setDate("start", start);
        query.setDate("end", end);
        return (Integer) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<AdCampaignDailyStatistic> getAdCampaignStatsByCampaignIdDaily(Date startDate,
            Date endDate, int campaignId) {
        Query query = getSession().getNamedQuery("StatsCampaignDailyDao.getAdCampaignStatsByCampaignIdDaily");
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        query.setInteger("campaignId", campaignId);
        return (List<AdCampaignDailyStatistic>) query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getAdvertiserPayByDay(Date dateDay) {
        Query query = getSession().getNamedQuery("StatsCampaignDailyDao.getAdvertiserPayByDay");
        query.setDate("dateDay", dateDay);
        return query.list();
    }

    /**
     * 获取截止指定日期前的数据统计
     * @param campaignId
     * @param endDay
     * @return
     */
    public Object[] getSumStatsByCampIdBeforeTime(int campaignId, Date endDay) {
        Query query = getSession().getNamedQuery("StatsCampaignDailyDao.sumCampStatsByCampIdBeforeTime");
        query.setInteger("campaignId", campaignId);
        query.setDate("end", endDay);
        Object[] obj = (Object[]) query.uniqueResult();
        if (obj[0] == null)
            obj = null;
        return obj;
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> listStatsViewByCampType(List<Integer> campIds, Date startDate, 
            Date endDate, String orderInfo) {
        Query query = getSession().createSQLQuery(getSession().getNamedQuery(
                "StatsCampaignDailyDao.listStatsViewByCampType").getQueryString() + "\t\t\n ORDER BY " + orderInfo);
        query.setParameterList("campIds", campIds);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        return query.list();
    }

    @Transactional(value = "buzzads", readOnly = false)
    public void updateCampDailyProportion(Integer campId, Date dateDay, Integer network, Integer cpcPubComm,
            Integer cpsPubComm, BigDecimal cpmPubComm) {
        Query query = getSession().getNamedQuery("StatsCampaignDailyDao.updateCampDailyProportion");
        query.setInteger("campId", campId);
        query.setDate("dateDay", dateDay);
        query.setInteger("network", network);
        query.setInteger("cpcPubComm", cpcPubComm);
        query.setInteger("cpsPubComm", cpsPubComm);
        query.setBigDecimal("cpmPubComm", cpmPubComm);
        query.executeUpdate();
    }

}
