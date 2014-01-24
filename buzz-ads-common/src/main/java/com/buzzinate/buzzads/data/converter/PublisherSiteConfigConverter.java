package com.buzzinate.buzzads.data.converter;

import java.util.HashSet;
import java.util.Set;

import com.buzzinate.buzzads.data.thrift.TPublisherSiteConfig;
import com.buzzinate.buzzads.domain.PublisherBlackDomain;
import com.buzzinate.buzzads.domain.PublisherSiteConfig;
import com.buzzinate.buzzads.enums.PublisherSiteConfigType;

/**
 * 
 * @author Johnson
 *
 */
public class PublisherSiteConfigConverter {
    private PublisherSiteConfigConverter() {}
    
    public static TPublisherSiteConfig toThrift(PublisherSiteConfig publisherSiteConfig) {
        Set<String> blackDomains = new HashSet<String>();
        Set<String> blackEntryLinks = new HashSet<String>();
        for (PublisherBlackDomain domain : publisherSiteConfig.getBlackDomains()) {
            if (domain.getType() == PublisherSiteConfigType.DOMAIN) {
                blackDomains.add(domain.getUrl());
            } else {
                blackEntryLinks.add(domain.getUrl());
            }
        }
        return new TPublisherSiteConfig.Builder()
                .uuid(publisherSiteConfig.getUuid())
                .blackKeywords(publisherSiteConfig.getBlackKeywords())
                .blackDomains(blackDomains).blackEntryLinks(blackEntryLinks).build();
    }

}
