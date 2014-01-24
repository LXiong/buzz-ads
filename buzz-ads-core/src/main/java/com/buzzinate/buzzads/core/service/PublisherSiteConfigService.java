package com.buzzinate.buzzads.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buzzinate.buzzads.core.dao.PublisherBlackDomainDao;
import com.buzzinate.buzzads.core.dao.PublisherSiteConfigDao;
import com.buzzinate.buzzads.domain.PublisherBlackDomain;
import com.buzzinate.buzzads.domain.PublisherSiteConfig;
import com.buzzinate.buzzads.enums.PublisherSiteConfigType;
import com.buzzinate.common.util.string.StringUtil;

/**
 * 
 * @author JOhnson
 *
 */
@Service
public class PublisherSiteConfigService {
    @Autowired
    private PublisherSiteConfigDao publisherSiteConfigDao;
    @Autowired
    private PublisherBlackDomainDao publisherBlackDomainDao;
    @Autowired
    private EventServices eventServices;
    
    public PublisherSiteConfig getByUuid(String uuid) {
        byte[] uuidBytes = StringUtil.uuidToByteArray(uuid);
        PublisherSiteConfig publisherSiteConfig = publisherSiteConfigDao.read(uuidBytes);
        if (publisherSiteConfig == null) {
            publisherSiteConfig = new PublisherSiteConfig(uuid);
        }
        List<PublisherBlackDomain> blackDomainList = publisherBlackDomainDao.listByUuid(uuidBytes);
        publisherSiteConfig.setBlackDomains(blackDomainList);
        return publisherSiteConfig;
    }
    
    public void setBlackKeyWords(PublisherSiteConfig publisherSiteConfig) {
        publisherSiteConfigDao.saveOrUpdate(publisherSiteConfig);
        //发送关键字设置事件
        eventServices.sendSiteConfigModifyEvent(publisherSiteConfig.getUuid(), PublisherSiteConfigType.KEYWORDS);
    }
    
    public void addBlackDomain(PublisherBlackDomain blackDomain) {
        publisherBlackDomainDao.create(blackDomain);
        eventServices.sendSiteConfigModifyEvent(blackDomain.getUuid(), blackDomain.getType());
    }
    
    public void deleteBlackDomain(PublisherBlackDomain blackDomain) {
        publisherBlackDomainDao.delete(blackDomain.getId());
        eventServices.sendSiteConfigModifyEvent(blackDomain.getUuid(), blackDomain.getType());
    }
    
    public PublisherBlackDomain getPublisherBlackDomain(int id) {
        return publisherBlackDomainDao.read(id);
    }

}
