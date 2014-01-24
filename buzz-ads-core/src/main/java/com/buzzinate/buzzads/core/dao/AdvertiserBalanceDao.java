package com.buzzinate.buzzads.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.domain.AdvertiserBalance;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 13, 2013 5:31:25 PM
 * 
 */
@Component
@Transactional(value = "buzzads", readOnly = true)
public class AdvertiserBalanceDao extends AdsDaoBase<AdvertiserBalance, Integer> {

    public AdvertiserBalanceDao() {
        super(AdvertiserBalance.class, "id");
    }

    @Transactional(value = "buzzads", readOnly = false)
    public int insertOrUpdateForCredits(int advertiserId, long credits) {
        Query query = getSession().getNamedQuery("AdvertiserBalanceDao.insertOrUpdateForCredits");
        query.setInteger("advertiserId", advertiserId);
        query.setLong("credits", credits);
        return query.executeUpdate();
    }

    @Transactional(value = "buzzads", readOnly = false)
    public int insertOrUpdateForDebits(int advertiserId, long debits) {
        Query query = getSession().getNamedQuery("AdvertiserBalanceDao.insertOrUpdateForDebits");
        query.setInteger("advertiserId", advertiserId);
        query.setLong("debits", debits);
        return query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, Long> getBalances(List<Integer> advertiserIds) {
        Query query = getSession().getNamedQuery("AdvertiserBalanceDao.listByAdvertiserIds");
        query.setParameterList("advertiserIds", advertiserIds);
        List<Object[]> list = query.list();
        Map<Integer, Long> res = new HashMap<Integer, Long>();
        for (Object[] i : list) {
            res.put((Integer) i[0], (Long) i[1]);
        }
        return res;
    }

    public long getByAdvertiserId(int advertiserId) {
        Query query = getSession().getNamedQuery("AdvertiserBalanceDao.getByAdvertiserId");
        query.setParameter("advertiserId", advertiserId);
        return (Long) query.uniqueResult();
    }

    public long getBalanceSum() {
        Query query = getSession().getNamedQuery("AdvertiserBalanceDao.getBalanceSum");
        return (Long) query.uniqueResult();
    }
}
