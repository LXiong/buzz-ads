package com.buzzinate.buzzads.core.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.PublisherContactInfo;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-12-12
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class PublisherContactInfoDao extends AdsDaoBase<PublisherContactInfo, Integer> {
    
    public PublisherContactInfoDao() {
        super(PublisherContactInfo.class, "userId");
    }
    
    public List<PublisherContactInfo> listPublisherContacts(PublisherContactInfo publisher, Pagination page) {
        
        Criteria criteria = getSession().createCriteria(PublisherContactInfo.class);
        if (StringUtils.isNotBlank(publisher.getName())) {
            criteria.add(Restrictions.like("name", "%" + publisher.getName() + "%"));
        }
        if (StringUtils.isNotBlank(publisher.getEmail())) {
            criteria.add(Restrictions.like("email", "%" + publisher.getEmail() + "%"));
        }
        return getPaginationResult(criteria, page);
    }
    
    public List<PublisherContactInfo> findAllUserByEmail(String searchEmail) {
        PublisherContactInfo info = new PublisherContactInfo();
        info.setEmail(searchEmail);
        return listPublisherContacts(info, null);
    }
    
    @Transactional(value = "buzzads", readOnly = false)
    public int updatePublisherStatus(int userId, int status) {
        Query query = getSession().getNamedQuery("PublisherContactInfoDao.updatePublisherStatus");
        query.setInteger("userId", userId);
        query.setInteger("status", status);
        return query.executeUpdate();
    }
    
    @SuppressWarnings("unchecked")
    public List<PublisherContactInfo> findByUserIds(List<Integer> userIds) {
        Query query = getSession().getNamedQuery("PublisherContactInfoDao.findByUserIds");
        query.setParameterList("userIds", userIds);
        return query.list();
    }
}
