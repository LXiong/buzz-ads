package com.buzzinate.buzzads.action.publisher;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.core.service.PublisherSiteConfigService;
import com.buzzinate.buzzads.core.service.SiteService;
import com.buzzinate.buzzads.domain.PublisherBlackDomain;
import com.buzzinate.buzzads.domain.PublisherSiteConfig;
import com.buzzinate.buzzads.domain.Site;
import com.buzzinate.buzzads.enums.PublisherSiteConfigType;
import com.buzzinate.buzzads.user.LoginHelper;
import com.buzzinate.common.util.string.UrlUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Johnson
 *
 */
@Controller
@Scope("request")
public class PublisherSiteConfigAction extends ActionSupport {

    private static final long serialVersionUID = -5221322807424809518L;
    
    private String uuid;
    private int blackId;
    private String blackKeywords;
    private String domain;
    private String entryLink;
    
    private List<Site> sites;
    
    private PublisherSiteConfig publisherSiteConfig;
    
    @Autowired
    private PublisherSiteConfigService publisherSiteConfigService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private LoginHelper loginHelper;
    
    public String execute() {
        int userId = loginHelper.getUserId();
        sites = siteService.getUuidSiteByUserId(userId);
        if (StringUtils.isEmpty(uuid) && sites.size() > 0) {
            uuid = sites.get(0).getUuid();
        } else {
            Site site = siteService.getUuidSiteByUuid(uuid);
            if (site == null || site.getUserId() != userId) {
                addActionError("不是合法的uuid");
                return Action.ERROR;
            }
        }
        publisherSiteConfig = publisherSiteConfigService.getByUuid(uuid);

        return Action.SUCCESS;
    }
    
    public String setBlackKeyWords() {
        int userId = loginHelper.getUserId();
        if (StringUtils.isEmpty(uuid)) {
            addActionError("uuid不能为空");
            return Action.INPUT;
        }
        
        Site site = siteService.getUuidSiteByUuid(uuid);
        if (site == null || site.getUserId() != userId) {
            addActionError("不是合法的uuid");
            return Action.INPUT;
        }
        publisherSiteConfig = publisherSiteConfigService.getByUuid(uuid);
        
        publisherSiteConfig.setBlackKeywords(blackKeywords);
        
        try {
            publisherSiteConfigService.setBlackKeyWords(publisherSiteConfig);
        } catch (Exception e) {
            LOG.info("发生未知错误", e);
            return Action.ERROR;
        }
        return Action.SUCCESS;
    }
    
    public String removeBlackDomain() {
        if (blackId <= 0) {
            return Action.INPUT;
        }
        int userId = loginHelper.getUserId();

        PublisherBlackDomain blackDomain = publisherSiteConfigService.getPublisherBlackDomain(blackId);
        uuid = blackDomain.getUuid();
        Site site = siteService.getUuidSiteByUuid(uuid);
        if (site == null || site.getUserId() != userId) {
            addActionError("不是合法的链接id");
            return Action.INPUT;
        }
        
        publisherSiteConfigService.deleteBlackDomain(blackDomain);
        
        return Action.SUCCESS;
        
    }
    
    public String addBlackDomain() {
        int userId = loginHelper.getUserId();
        if (StringUtils.isEmpty(uuid)) {
            addActionError("uuid不能为空");
            return Action.INPUT;
        }
        
        Site site = siteService.getUuidSiteByUuid(uuid);
        if (site == null || site.getUserId() != userId) {
            addActionError("不是合法的uuid");
            return Action.INPUT;
        }
        
        if (StringUtils.isEmpty(domain) && StringUtils.isEmpty(entryLink)) {
            addActionError("广告域名和广告链接不能都为空");
            return Action.INPUT;
        }
        
        
        PublisherBlackDomain blackDomain = new PublisherBlackDomain();
        blackDomain.setUuid(uuid);
        if (!StringUtils.isEmpty(domain)) {
            blackDomain.setUrl(domain);
            blackDomain.setType(PublisherSiteConfigType.DOMAIN);
        } else {
            blackDomain.setUrl(entryLink);
            blackDomain.setType(PublisherSiteConfigType.ADENTRYLINK);
        }
        publisherSiteConfigService.addBlackDomain(blackDomain);
        
        return Action.SUCCESS;
    }
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }
    
    public String getBlackKeywords() {
        return blackKeywords;
    }
    
    public void setBlackKeywords(String blackKeywords) {
        this.blackKeywords = blackKeywords;
    }
    
    public String getDomain() {
        return domain;
    }
    
    public void setDomain(String domain) {
        this.domain = UrlUtil.getFullUrlWithPrefix(domain);
    }
    
    public String getEntryLink() {
        return entryLink;
    }

    public void setEntryLink(String entryLink) {
        this.entryLink = UrlUtil.getFullUrlWithPrefix(entryLink);
    }
    
    public PublisherSiteConfig getPublisherSiteConfig() {
        return publisherSiteConfig;
    }

    public void setPublisherSiteConfig(PublisherSiteConfig publisherSiteConfig) {
        this.publisherSiteConfig = publisherSiteConfig;
    }
    
    public int getBlackId() {
        return blackId;
    }

    public void setBlackId(int blackId) {
        this.blackId = blackId;
    }

}
