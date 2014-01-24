package com.buzzinate.buzzads.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buzzinate.buzzads.core.dao.SiteDao;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.core.util.RedisClient;
import com.buzzinate.buzzads.domain.Site;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-12-12
 */
@Service
public class SiteService {

    private static final String USER_SITES_CACHE_KEY = "USER_SITES:";
    private static final int USER_SITES_CACHE_EXPIRE = 3600 * 4;
    private static final String SITE_CACHE_KEY = "SITE:";
    private static final String ALL_SITES_CACHE_KEY = "ALL_SITES";
    private static final int SITE_CACHE_EXPIRE = 3600 * 4;
    
    @Autowired
    private SiteDao siteDao;
    @Autowired
    private RedisClient redisClient;
    


    @SuppressWarnings("unchecked")
    public List<Site> getUuidSiteByUserId(int userId) {
        String sitesKey = USER_SITES_CACHE_KEY + userId;
        if (redisClient.getObject(sitesKey) != null) {
            return (List<Site>) redisClient.getObject(sitesKey);
        } else {
            List<Site> sites = siteDao.getUuidSiteByUserId(userId);
            if (sites != null)
                redisClient.set(sitesKey, USER_SITES_CACHE_EXPIRE, sites);
            return sites;
        }
    }

    public Site getUuidSiteByUuid(String uuid) {
        String siteKey = SITE_CACHE_KEY + uuid;
        if (redisClient.getObject(siteKey) != null) {
            return (Site) redisClient.getObject(siteKey);
        } else {
            Site site = siteDao.getUuidSiteByUuid(uuid);
            if (site != null)
                redisClient.set(siteKey, SITE_CACHE_EXPIRE, site);
            return site;
        }
    }

    public List<Site> searchSiteByName(String name) {
        return siteDao.searchSiteByName(name);
    }
    
    public List<String> getUuids(int userId) {
        List<String> uuids = new ArrayList<String>();
        List<Site> sites = getUuidSiteByUserId(userId);
        for (Site site : sites) {
            uuids.add(site.getUuid());
        }
        return uuids;
    }
    
    @SuppressWarnings("unchecked")
    public List<Site> getAllSiteList() {
        String siteKey = ALL_SITES_CACHE_KEY;
        if (redisClient.getObject(siteKey) != null) {
            return (List<Site>) redisClient.getObject(siteKey);
        } else {
            List<Site> sites = siteDao.getAllSiteList();
            if (sites != null)
                redisClient.set(siteKey, SITE_CACHE_EXPIRE, sites);
            return sites;
        }
    }

}
