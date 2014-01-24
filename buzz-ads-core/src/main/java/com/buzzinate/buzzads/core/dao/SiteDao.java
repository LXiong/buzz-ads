package com.buzzinate.buzzads.core.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.safehaus.uuid.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.MainDaoBase;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.Site;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-12-12
 */
@Component
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
public class SiteDao extends MainDaoBase<Site, byte[]> {

    public SiteDao() {
        super(Site.class, "uuidBytes");
    }

    @SuppressWarnings("unchecked")
    public List<Site> getUuidSiteByUserId(int userId) {
        Query query = getSession().getNamedQuery("SiteDao.getUuidSiteByUserId");
        query.setInteger("userId", userId);
        return query.list();
    }

    public Site getUuidSiteByUuid(String uuid) {
        Query query = getSession().getNamedQuery("SiteDao.getUuidSiteByUuid");
        query.setBinary("uuidBytes", new UUID(uuid).asByteArray());
        return (Site) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<Site> searchSiteByName(String name) {
        Query query = getSession().getNamedQuery("SiteDao.getSiteByName");
        query.setString("name", "%" + name + "%");
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Site> getAllSiteList() {
        Query query = getSession().getNamedQuery("SiteDao.getAllSiteList");
        return query.list();
    }

}
