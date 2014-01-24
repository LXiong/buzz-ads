package com.buzzinate.buzzads.data.operations;

import com.buzzinate.buzzads.core.service.PublisherSiteConfigService;
import com.buzzinate.buzzads.data.converter.PublisherSiteConfigConverter;
import com.buzzinate.buzzads.data.thrift.TPublisherSiteConfig;
import com.buzzinate.buzzads.domain.PublisherSiteConfig;
import com.twitter.util.ExceptionalFunction0;

public class FindPublisherSiteConfigOp extends ExceptionalFunction0<TPublisherSiteConfig> {
    
    public String uuid;
    public PublisherSiteConfigService publisherSiteConfigService;
    
    public FindPublisherSiteConfigOp(String uuid, PublisherSiteConfigService publisherSiteConfigService) {
        this.uuid = uuid;
        this.publisherSiteConfigService = publisherSiteConfigService;
    }

    @Override
    public TPublisherSiteConfig applyE() throws Exception {
        PublisherSiteConfig publisherSiteConfig = publisherSiteConfigService.getByUuid(uuid);
        if (publisherSiteConfig == null)
            publisherSiteConfig = new PublisherSiteConfig();
        publisherSiteConfig.setUuid(uuid);
        return PublisherSiteConfigConverter.toThrift(publisherSiteConfig);
    }

}
