package com.buzzinate.buzzads.core.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.analytics.stats.AdOrderDailyStatistic;
import com.buzzinate.buzzads.common.dao.AdsDaoBase;

/**
 * 
 * @author Johnson
 *
 */
@Component
@Transactional(value = "buzzads", readOnly = true)
public class StatsAdOrderDailyDao extends AdsDaoBase<AdOrderDailyStatistic, Integer> {
    public StatsAdOrderDailyDao() {
        super(AdOrderDailyStatistic.class, "id");
    }
    
    public Object[] getAdOrderStatsSum(Date startDate, Date endDate, int orderId) {
        Query query = getSession().getNamedQuery("StatsAdOrderDao.getAdOrderStatsSum");
        query.setInteger("orderId", orderId);
        query.setDate("start", startDate);
        query.setDate("end", endDate);
        Object[] obj = (Object[]) query.uniqueResult();
        if (obj[0] == null)
            obj = null;
        return obj;
    }
    
    @SuppressWarnings("unchecked")
    public List<AdOrderDailyStatistic> getAdOrderStatsSumByOrderIds(Date startDate, Date endDate, 
                    List<Integer> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return new ArrayList<AdOrderDailyStatistic>();
        }
        Query query = getSession().getNamedQuery("StatsAdOrderDao.getAdOrderStatsSumByOrderIds");
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        query.setParameterList("orderIds", orderIds.toArray());
        return (List<AdOrderDailyStatistic>) query.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<AdOrderDailyStatistic> getAdOrderStatsByOrderIdsDaily(Date startDate, 
            Date endDate, List<Integer> orderIds) {
        Query query = getSession().getNamedQuery("StatsAdOrderDao.getAdOrderStatsByOrderIdsDaily");
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        query.setParameterList("orderIds", orderIds);
        return (List<AdOrderDailyStatistic>) query.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<AdOrderDailyStatistic> getAdOrderStatsByOrderIdDaily(Date startDate, Date endDate, int orderId) {
        Query query = getSession().getNamedQuery("StatsAdOrderDao.getAdOrderStatsByOrderIdDaily");
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        query.setInteger("orderId", orderId);
        return (List<AdOrderDailyStatistic>) query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getCampInfoByProportion(List<Integer> orderIdList, Date startDate, Date endDate) {
        Query query = getSession().getNamedQuery("StatsAdOrderDao.getCampInfoByProportion");
        query.setParameterList("orderIdList", orderIdList);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        return query.list();
    }

    @Transactional(value = "buzzads", readOnly = false)
    public void updateOrderDailyProportion(Integer orderid, Date dateDay, Integer network, Integer cpcPubComm,
            Integer cpsPubComm, BigDecimal cpmPubComm) {
        Query query = getSession().getNamedQuery("StatsAdOrderDao.updateOrderDailyProportion");
        query.setInteger("orderId", orderid);
        query.setDate("dateDay", dateDay);
        query.setInteger("network", network);
        query.setInteger("cpcPubComm", cpcPubComm);
        query.setInteger("cpsPubComm", cpsPubComm);
        query.setBigDecimal("cpmPubComm", cpmPubComm);
        query.executeUpdate();
    }

}
