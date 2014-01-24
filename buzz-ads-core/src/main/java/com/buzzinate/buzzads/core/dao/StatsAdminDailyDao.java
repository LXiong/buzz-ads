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
import com.buzzinate.buzzads.analytics.stats.AdminDailyStatistic;
import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.core.util.FormatUtil;
import com.buzzinate.buzzads.core.util.Pagination;

/**
 * Admin每日数据统计
 * 
 * @author Jeffrey Ji<jeffrey.ji@buzzinate.com>
 * 
 *         2012-12-17
 */
@Component
@Transactional(value = "buzzads", readOnly = true)
public class StatsAdminDailyDao extends AdsDaoBase<AdminDailyStatistic, Integer> {

    public StatsAdminDailyDao() {
        super(AdminDailyStatistic.class, "id");
    }
    
    @SuppressWarnings("unchecked")
    public List<AdminDailyStatistic> getAdminDailyStatistic(Date dateDay) {
        Criteria criteria = getSession().createCriteria(AdminDailyStatistic.class);
        criteria.add(Restrictions.eq("dateDay", dateDay));
        return (List<AdminDailyStatistic>) criteria.list();
    }

    public Long countQueryStats(Date startDate, Date endDate) {
        Query query = getSession().getNamedQuery("StatsAdminDailyDao.countStats");
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        return (Long) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<AdminDailyStatistic> getQueryStats(Date startDate, Date endDate, Pagination page) {
        Query query = getSession().getNamedQuery("StatsAdminDailyDao.queryStats");
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        query.setFirstResult(page.getStartRow());
        query.setMaxResults(page.getPageSize());
        List<Object[]> entries = query.list();
        page.setTotalRecords(countQueryStats(startDate, endDate).intValue());
        page.validatePageInfo();
        page.setReturnRecords(entries.size());

        List<AdminDailyStatistic> res = new ArrayList<AdminDailyStatistic>();
        Iterator<Object[]> iter = entries.iterator();
        int i = 0;
        while (iter.hasNext()) {
            Object[] obj = (Object[]) iter.next();
            i = 0;
            AdminDailyStatistic admin = new AdminDailyStatistic();
            admin.setDateDay((Date) obj[i++]);
            admin.setViews((Integer) obj[i++]);
            admin.setClicks((Integer) obj[i++]);
            admin.setCpsClickNo((Integer) obj[i++]);
            admin.setCpsViewNo((Integer) obj[i++]);
            admin.setCpsOrderNo((Integer) obj[i++]);
            admin.setCpsTotalCommission((Integer) obj[i++]);
            admin.setCpsPubCommission((Integer) obj[i++]);
            admin.setCpcViewNo((Integer) obj[i++]);
            admin.setCpcOriginalClickNo((Integer) obj[i++]);
            admin.setCpcClickNo((Integer) obj[i++]);
            admin.setCpcTotalCommission((Integer) obj[i++]);
            admin.setCpcPubCommission((Integer) obj[i++]);
            admin.setCpmViewNo((Integer) obj[i++]);
            admin.setCpmClickNo((Integer) obj[i++]);
            admin.setCpmPubCommission((BigDecimal) obj[i++]);
            admin.setCpmTotalCommission((BigDecimal) obj[i++]);
            res.add(admin);
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getQueryChartStats(Date startDate, Date endDate) {
        Query query = getSession().getNamedQuery("StatsAdminDailyDao.queryChartStats");
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        List<Object[]> entries = query.list();
        List<Object[]> datas = new ArrayList<Object[]>();
        Iterator<Object[]> iter = entries.iterator();
        while (iter.hasNext()) {
            Object[] obj = (Object[]) iter.next();
            datas.add(new Object[] {FormatUtil.getDoubleNoStr((Integer) obj[1] + (Integer) obj[2]), (Date) obj[0]});
        }
        return datas;
    }

    @SuppressWarnings("unchecked")
    public AdBasicStatistic getQuerySums(Date startDate, Date endDate) {
        Query query = getSession().getNamedQuery("StatsAdminDailyDao.querySums");
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        List<Object[]> entries = query.list();
        AdBasicStatistic admin = new AdBasicStatistic();
        Iterator<Object[]> iter = entries.iterator();
        if (iter.hasNext()) {
            Object[] obj = (Object[]) iter.next();
            admin.setCpsTotalCommission((Integer) obj[0]);
            admin.setCpsPubCommission((Integer) obj[1]);
            admin.setCpcTotalCommission((Integer) obj[2]);
            admin.setCpcPubCommission((Integer) obj[3]);
            admin.setCpsTotalConfirmedCommission((Integer) obj[4]);
            admin.setCpmTotalCommission((BigDecimal)obj[5]);
            admin.setCpmPubCommission((BigDecimal)obj[6]);
        }
        return admin;
    }

    @Transactional(value = "buzzads", readOnly = false)
    public void updateAdminDailyProportion(Date dateDay, Integer network, Integer cpcPubComm,
            Integer cpsPubComm, BigDecimal cpmPubComm) {
        Query query = getSession().getNamedQuery("StatsAdminDailyDao.updateAdminDailyProportion");
        query.setDate("dateDay", dateDay);
        query.setInteger("network", network);
        query.setInteger("cpcPubComm", cpcPubComm);
        query.setInteger("cpsPubComm", cpsPubComm);
        query.setBigDecimal("cpmPubComm", cpmPubComm);
        query.executeUpdate();
    }
}
