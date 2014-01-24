package com.buzzinate.buzzads.core.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.domain.PublisherSiteConfig;

/**
 * 
 * @author Johnson
 *
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class PublisherSiteConfigDao extends AdsDaoBase<PublisherSiteConfig, byte[]>{
    
    public PublisherSiteConfigDao() {
        super(PublisherSiteConfig.class, "uuid");
    }
    
    @SuppressWarnings("unchecked")
    public List<PublisherSiteConfig> listPublisherSiteConfigByUserId(int userId) {
        Query query = getSession().getNamedQuery("PublisherSiteConfigDao.listPublisherSiteConfigByUserId");
        query.setInteger("userId", userId);
        return (List<PublisherSiteConfig>) query.list();
    }

}
