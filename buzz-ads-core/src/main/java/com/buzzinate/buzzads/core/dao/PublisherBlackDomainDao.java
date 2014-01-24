package com.buzzinate.buzzads.core.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.domain.PublisherBlackDomain;

/**
 * 
 * @author Johnson
 *
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class PublisherBlackDomainDao extends AdsDaoBase<PublisherBlackDomain, Integer> {
    public PublisherBlackDomainDao() {
        super(PublisherBlackDomain.class, "id");
    }
    
    
    @SuppressWarnings("unchecked")
    public List<PublisherBlackDomain> listByUuid(byte[] uuid) {
        Query query = getSession().getNamedQuery("PublisherBlackDomain.listByUuid");
        query.setBinary("uuid", uuid);
        return (List<PublisherBlackDomain>) query.list();
    }

}
