package com.buzzinate.buzzads.core.dao;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.AdvertiserBilling;
import com.buzzinate.buzzads.enums.AdvertiserBillingType;
import com.buzzinate.common.util.DateTimeUtil;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 13, 2013 11:30:07 AM
 */
@Component
@Transactional(value = "buzzads", readOnly = true)
public class AdvertiserBillingDao extends AdsDaoBase<AdvertiserBilling, Integer> {
    private static final Logger LOG = LoggerFactory.getLogger(AdvertiserBillingDao.class);

    public AdvertiserBillingDao() {
        super(AdvertiserBilling.class, "id");
    }

    public List<AdvertiserBilling> listBillingsByDay(int advertiserId, Date startDate, Date endDate,
            List<AdvertiserBillingType> types, Pagination page) {
        int num = count(startDate, endDate, types, true, advertiserId, "AdvertiserBillingDao.countByDay").intValue();
        return listBillingsByDay(startDate, endDate, types, page, true, advertiserId,
                "AdvertiserBillingDao.listBillingsByDay", num);
    }

    public List<AdvertiserBilling> listBillingsByDay(Date startDate, Date endDate, List<AdvertiserBillingType> types,
            Pagination page) {
        int num = count(startDate, endDate, types, false, 0, "AdvertiserBillingDao.countAllByDay").intValue();
        return listBillingsByDay(startDate, endDate, types, page, false, 0,
                "AdvertiserBillingDao.listAllBillingsByDay", num);
    }

    @SuppressWarnings("unchecked")
    private List<AdvertiserBilling> listBillingsByDay(Date startDate, Date endDate, List<AdvertiserBillingType> types,
            Pagination page, boolean isAdvertiser, int advertiserId, String hql, int num) {
        Query query = getQuery(hql, startDate, endDate, types, isAdvertiser, advertiserId);
        query.setFirstResult(page.getStartRow());
        query.setMaxResults(page.getPageSize());

        List<AdvertiserBilling> entries = query.list();
        page.setTotalRecords(num);
        page.validatePageInfo();
        page.setReturnRecords(entries.size());

        return entries;
    }

    public List<AdvertiserBilling> listBillingsByMonth(int advertiserId, Date startDate, Date endDate,
            List<Integer> types, Pagination page) {
        int num = count(startDate, endDate, types, true, advertiserId, "AdvertiserBillingDao.countByMonth").intValue();
        return listBillingsByMonth(startDate, endDate, types, page, true, advertiserId,
                "AdvertiserBillingDao.listBillingsByMonth", "AdvertiserBillingDao.listBalancesByMonth", num);
    }

    public List<AdvertiserBilling> listBillingsByMonth(Date startDate, Date endDate, List<Integer> types,
            Pagination page) {
        int num = countAll(startDate, endDate, types, false, 0);
        return listBillingsByMonth(startDate, endDate, types, page, false, 0,
                "AdvertiserBillingDao.listAllBillingsByMonth", "AdvertiserBillingDao.listAllBalancesByMonth", num);
    }

    @SuppressWarnings("unchecked")
    private List<AdvertiserBilling> listBillingsByMonth(Date startDate, Date endDate, List<Integer> types,
            Pagination page, boolean isAdvertiser, int advertiserId, String hql, String balanceHql, int num) {
        Query query = getQuery(hql, startDate, endDate, types, isAdvertiser, advertiserId);
        query.setFirstResult(page.getStartRow());
        query.setMaxResults(page.getPageSize());

        List<Object[]> entries = query.list();
        page.setTotalRecords(num);
        page.validatePageInfo();
        page.setReturnRecords(entries.size());

        List<AdvertiserBilling> res = new ArrayList<AdvertiserBilling>();
        Iterator<Object[]> iter = entries.iterator();
        while (iter.hasNext()) {
            Object[] obj = (Object[]) iter.next();
            AdvertiserBilling ab = new AdvertiserBilling();
            ab.setAdvertiserId((Integer) obj[0]);
            try {
                Date date = DateTimeUtil.getDateByString((String) obj[1], DateTimeUtil.FMT_DATE_YYYY_SLASH_MM);
                ab.setBillingDay(date);
            } catch (Exception e) {
                LOG.error("error happen in parsing advertiser_billing month:" + e.toString());
            }
            ab.setDebits((Integer) obj[2]);
            ab.setCredits((Integer) obj[3]);
            res.add(ab);
        }

        query = getQuery(balanceHql, startDate, endDate, types, isAdvertiser, advertiserId);
        iter = query.list().iterator();
        while (iter.hasNext()) {
            Object[] obj = (Object[]) iter.next();
            for (AdvertiserBilling ab : res) {
                if (ab.getAdvertiserId() == ((Integer) obj[0]).intValue() &&
                        DateTimeUtil.formatDate(ab.getBillingDay(), DateTimeUtil.FMT_DATE_YYYY_SLASH_MM).equals(
                                (String) obj[1])) {
                    ab.setBalance((Long) obj[2]);
                }
            }
        }
        return res;
    }

    @SuppressWarnings("rawtypes")
    private Query getQuery(String hql, Date startDate, Date endDate, List types, boolean isAdvertiser,
            int advertiserId) {
        Query query = getSession().getNamedQuery(hql);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        query.setParameterList("types", types);
        if (isAdvertiser && advertiserId != 0)
            query.setInteger("advertiserId", advertiserId);

        return query;
    }


    @SuppressWarnings("rawtypes")
    private Long count(Date startDate, Date endDate, List types, boolean isAdvertiser, int advertiserId, String hql) {
        Query query = getQuery(hql, startDate, endDate, types, isAdvertiser, advertiserId);
        Object res = query.uniqueResult();
        return res == null ? 0L : (Long) res;
    }


    @SuppressWarnings("unchecked")
    private int countAll(Date startDate, Date endDate, List<Integer> types, boolean isAdvertiser, int advertiserId) {
        Query query = getQuery("AdvertiserBillingDao.countAllByMonth", startDate, endDate, types, isAdvertiser,
                advertiserId);
        Long res = 0L;
        Iterator<Object> iter = query.list().iterator();
        while (iter.hasNext()) {
            res += (Long) iter.next();
        }
        return res.intValue();
    }

    @SuppressWarnings("unchecked")
    public List<AdvertiserBilling> listBillingByType(List<AdvertiserBillingType> types, Pagination page, int num) {
        Query query = getSession().getNamedQuery("AdvertiserBillingDao.listAllBillingsByType");
        query.setParameterList("types", types);
        query.setFirstResult(page.getStartRow());
        query.setMaxResults(page.getPageSize());
        List<AdvertiserBilling> entries = query.list();
        page.setTotalRecords(num);
        page.validatePageInfo();
        page.setReturnRecords(entries.size());
        return entries;
    }

    public List<AdvertiserBilling> listBillingsByType(List<AdvertiserBillingType> types, Pagination page) {
        Query query = getSession().getNamedQuery("AdvertiserBillingDao.countByType");
        query.setParameterList("types", types);
        Object result = query.uniqueResult();
        return listBillingByType(types, page, result == null ? 0 : ((Long) result).intValue());
    }
    
    public AdvertiserBilling getAdvertiserBillingByDay(int advertiserId, Date startDate, Date endDate) {
        Query query = getSession().getNamedQuery("AdvertiserBillingDao.getAdvertiserBillingByDay");
        query.setInteger("advertiserId", advertiserId);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        return (AdvertiserBilling) query.uniqueResult();
    }
    
    public AdvertiserBilling getLastAdvertiserBillingByDay(int advertiserId, Date startDate, Date endDate) {
        Query query = getSession().getNamedQuery("AdvertiserBillingDao.getLastAdvertiserBillingByDay");
        query.setInteger("advertiserId", advertiserId);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        query.setMaxResults(1);
        return (AdvertiserBilling) query.uniqueResult();
    }
}
