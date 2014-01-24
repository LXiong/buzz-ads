package com.buzzinate.buzzads.core.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.AdvertiserAccount;
import com.buzzinate.buzzads.enums.AdvertiserStatusEnum;

/**
 * 
 * @author Johnson
 * 
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class AdvertiserAccountDao extends AdsDaoBase<AdvertiserAccount, Integer> {

    public AdvertiserAccountDao() {
        super(AdvertiserAccount.class, "advertiserId");
    }

    @SuppressWarnings("unchecked")
    public List<AdvertiserAccount> listAllAdvertisers() {
        Query query = getSession().getNamedQuery("AdvertiserAccountDao.listAllAdvertisers");
        query.setInteger("status", AdvertiserStatusEnum.NORMAL.getCode());
        return query.list();
    }

    public List<AdvertiserAccount> getAdvertisers(String companyName, List<Integer> advertiserIds, Pagination page) {
        Criteria criteria = getSession().createCriteria(AdvertiserAccount.class);
        if (StringUtils.isNotBlank(companyName)) {
            criteria.add(Restrictions.like("companyName", "%" + companyName + "%"));
        }
        if (advertiserIds != null && !advertiserIds.isEmpty()) {
            criteria.add(Restrictions.in("advertiserId", advertiserIds));
        }
        Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        page.setTotalRecords(count.intValue());
        page.validatePageInfo();

        // Reset projection and get records for the requested page.
        criteria.setProjection(null);
        criteria.setFirstResult(page.getStartRow());
        criteria.setMaxResults(page.getPageSize());

        @SuppressWarnings("unchecked")
        List<AdvertiserAccount> list = criteria.list();
        page.setReturnRecords(list.size());

        return list;
    }

    @Transactional(value = "buzzads", readOnly = false)
    public int updateAdvertiserStatus(int advertiserId, int status) {
        Query query = getSession().getNamedQuery("AdvertiserAccountDao.updateAdvertiserStatus");
        query.setInteger("advertiserId", advertiserId);
        query.setInteger("status", status);
        return query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, String> getAllAdvertiserName() {
        Query query = getSession().getNamedQuery("AdvertiserAccountDao.listAllAdvertiserName");
        Map<Integer, String> res = new HashMap<Integer, String>();
        Iterator<Object[]> iter = query.list().iterator();
        while (iter.hasNext()) {
            Object[] obj = (Object[]) iter.next();
            res.put((Integer) obj[0], (String) obj[1]);
        }
        return res;
    }
}
