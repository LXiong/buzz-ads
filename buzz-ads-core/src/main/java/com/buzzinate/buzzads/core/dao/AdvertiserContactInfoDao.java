package com.buzzinate.buzzads.core.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.domain.AdvertiserContactInfo;

/**
 * 
 * @author Johnson
 * 
 */

@Repository
@Transactional(value = "buzzads", readOnly = true)
public class AdvertiserContactInfoDao extends AdsDaoBase<AdvertiserContactInfo, Integer> {

    public AdvertiserContactInfoDao() {
        super(AdvertiserContactInfo.class, "id");
    }

    @SuppressWarnings("unchecked")
    public List<AdvertiserContactInfo> getAdvertiserContactInfo(int advertiserId) {
        Query query = getSession().getNamedQuery("AdvertiserContactInfoDao.getAdvertiserContactInfo");
        query.setInteger("advertiserId", advertiserId);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Integer> getAdvertiserIdsByNameAndEmail(String name, String email) {
        Criteria criteria = getSession().createCriteria(AdvertiserContactInfo.class);
        if (StringUtils.isNotBlank(name)) {
            criteria.add(Restrictions.like("name", "%" + name + "%"));
        }
        if (StringUtils.isNotBlank(email)) {
            criteria.add(Restrictions.like("email", "%" + email + "%"));
        }
        criteria.setProjection(Projections.projectionList().add(Projections.property("advertiserId")));
        List<Integer> res = new ArrayList<Integer>();
        Iterator<Object> iter = criteria.list().iterator();
        while (iter.hasNext()) {
            res.add((Integer) iter.next());
        }

        return res;
    }
}
