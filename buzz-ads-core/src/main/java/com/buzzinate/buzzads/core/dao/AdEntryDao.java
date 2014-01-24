package com.buzzinate.buzzads.core.dao;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.AdEntry;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *         <p/>
 *         2012-11-23
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class AdEntryDao extends AdsDaoBase<AdEntry, Integer> {

    public AdEntryDao() {
        super(AdEntry.class, "id");
    }

    @SuppressWarnings("unchecked")
    public List<AdEntry> listEntriesByOrderId(int adOrderId) {
        Query query = getSession().getNamedQuery("AdEntryDao.listEntriesByOrderId");
        query.setInteger("adOrderId", adOrderId);
        return (List<AdEntry>) query.list();
    }

    @SuppressWarnings("unchecked")
    public List<AdEntry> listEntriesByOrderIds(List<Integer> adOrderIds) {
        Query query = getSession().getNamedQuery("AdEntryDao.listEntriesByOrderIds");
        query.setParameterList("orderIds", adOrderIds);
        return query.list();
    }

    public Integer getOrderIdOfEntry(int adEntryId) {
        Query query = getSession().getNamedQuery("AdEntryDao.getOrderId");
        query.setInteger("id", adEntryId);
        return (Integer) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<AdEntry> listAdEntries(AdEntry adEntry, Pagination page) {
        Criteria criteria = getSession().createCriteria(AdEntry.class);
        if (adEntry.getAdvertiserId() != 0) {
            criteria.add(Restrictions.eq("advertiserId", adEntry.getAdvertiserId()));
        }
        if (adEntry.getCampaignId() > 0) {
            criteria.add(Restrictions.eq("campaignId", adEntry.getCampaignId()));
        }
        if (adEntry.getOrderId() > 0) {
            criteria.add(Restrictions.eq("orderId", adEntry.getOrderId()));
        }
        if (!StringUtils.isEmpty(adEntry.getTitle())) {
            criteria.add(Restrictions.like("title", "%" + adEntry.getTitle() + "%"));
        }
        if (adEntry.getStatus() != null) {
            criteria.add(Restrictions.eq("status", adEntry.getStatus()));
        }
        criteria.addOrder(Order.desc("updateAt"));
        // first get the total records in the database for this search...
        Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        page.setTotalRecords(count.intValue());
        page.validatePageInfo();
        // Reset projection and get records for the requested page.
        criteria.setProjection(null);
        criteria.setFirstResult(page.getStartRow());
        criteria.setMaxResults(page.getPageSize());
        List<AdEntry> entries = criteria.list();
        page.setReturnRecords(entries.size());
        return entries;
    }

    @SuppressWarnings("unchecked")
    public List<AdEntry> listAdEntries(AdEntry adEntry) {
        Criteria criteria = getSession().createCriteria(AdEntry.class);
        if (adEntry.getAdvertiserId() != 0) {
            criteria.add(Restrictions.eq("advertiserId", adEntry.getAdvertiserId()));
        }
        if (adEntry.getCampaignId() > 0) {
            criteria.add(Restrictions.eq("campaignId", adEntry.getCampaignId()));
        }
        if (adEntry.getOrderId() > 0) {
            criteria.add(Restrictions.eq("orderId", adEntry.getOrderId()));
        }
        if (!StringUtils.isEmpty(adEntry.getTitle())) {
            criteria.add(Restrictions.like("title", "%" + adEntry.getTitle() + "%"));
        }
        if (adEntry.getStatus() != null) {
            criteria.add(Restrictions.eq("status", adEntry.getStatus()));
        }
        criteria.addOrder(Order.desc("updateAt"));
        List<AdEntry> entries = criteria.list();
        return entries;
    }

    public int getAllAdEntriesCount() {
        Criteria criteria = getSession().createCriteria(AdEntry.class);
        criteria.add(Restrictions.ne("status", AdStatusEnum.DELETED));
        Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        return count.intValue();
    }

    @SuppressWarnings("unchecked")
    public List<AdEntry> listEntriesByStatus(AdStatusEnum status) {
        Criteria criteria = getSession().createCriteria(AdEntry.class);
        criteria.add(Restrictions.eq("status", status));
        List<AdEntry> entries = criteria.list();
        return entries;
    }

    @SuppressWarnings("unchecked")
    public List<AdEntry> getAdEntriesByStatus(int advertiserId, AdStatusEnum status, Pagination page) {
        Criteria criteria = getSession().createCriteria(AdEntry.class);
        if (advertiserId > 0) {
            criteria.add(Restrictions.eq("advertiserId", advertiserId));
        }
        criteria.add(Restrictions.eq("status", status));

        // first get the total records in the database for this search...
        Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        page.setTotalRecords(count.intValue());
        page.validatePageInfo();
        // Reset projection and get records for the requested page.
        criteria.setProjection(null);
        criteria.setFirstResult(page.getStartRow());
        criteria.setMaxResults(page.getPageSize());
        List<AdEntry> entries = criteria.list();
        page.setReturnRecords(entries.size());
        return entries;
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, String> searchAdsByTitle(String title) {
        Query query = getSession().getNamedQuery("AdEntryDao.getAdsByTitle");
        query.setString("title", "%" + title + "%");
        List<Object[]> lists = query.list();
        // 转换
        Map<Integer, String> res = new HashMap<Integer, String>();
        for (Object[] objs : lists) {
            res.put((Integer) objs[0], (String) objs[1]);
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, String> getAdsByIds(List<Integer> entryIds) {
        Query query = getSession().getNamedQuery("AdEntryDao.getAdsByIds");
        query.setParameterList("entryIds", entryIds);
        List<Object[]> lists = query.list();
        // 转换
        Map<Integer, String> res = new HashMap<Integer, String>();
        for (Object[] objs : lists) {
            res.put((Integer) objs[0], (String) objs[1]);
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    public List<AdEntry> listEntriesByCampaignId(int campaignId) {
        Query query = getSession().getNamedQuery("AdEntryDao.listEntriesByCampaignId");
        query.setInteger("campaignId", campaignId);
        return (List<AdEntry>) query.list();
    }

    @SuppressWarnings("unchecked")
    public List<AdEntry> listActiveEntriesByAdvertiserId(int advertiserId, List<Integer> activeOrderIds) {
        Query query = getSession().getNamedQuery("AdEntryDao.listActiveEntriesByAdvertiserIdWithActiveOrderIds");
        query.setInteger("advertiserId", advertiserId);
        query.setParameterList("activeOrderIds", activeOrderIds);
        return (List<AdEntry>) query.list();
    }

    @SuppressWarnings("unchecked")
    public List<AdEntry> listAdEntryByAdvertiserId(int advertiserId) {
        Query query = getSession().getNamedQuery("AdEntryDao.listAdEntryByAdvertiserId");
        query.setInteger("advertiserId", advertiserId);
        return (List<AdEntry>) query.list();
    }

    @Transactional(value = "buzzads", readOnly = false)
    public int updateEntryStatus(int entryId, AdStatusEnum status) {
        Query query = getSession().getNamedQuery("AdEntryDao.updateEntryStatus");
        query.setInteger("entryId", entryId);
        query.setInteger("status", status.getCode());
        return query.executeUpdate();
    }

    public long getAdEntryCountByCampaignId(int id) {
        Criteria criteria = getSession().createCriteria(AdEntry.class);
        criteria.add(Restrictions.eq("campaignId", id));
        criteria.setProjection(Projections.rowCount());
        Long result = (Long) criteria.uniqueResult();
        return result == null ? 0 : result.longValue();
    }
}
