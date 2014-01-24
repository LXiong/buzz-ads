package com.buzzinate.buzzads.data.operations;

import com.buzzinate.buzzads.core.service.PublisherContactService;
import com.buzzinate.buzzads.data.converter.PublisherContactConverter;
import com.buzzinate.buzzads.data.thrift.PublisherContactNotFoundException;
import com.buzzinate.buzzads.data.thrift.TPublisherContactInfo;
import com.buzzinate.buzzads.domain.PublisherContactInfo;
import com.twitter.util.ExceptionalFunction0;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-12-13
 */
public class FindPublisherContactOp extends ExceptionalFunction0<TPublisherContactInfo> {
    
    private long userId;
    private PublisherContactService publisherContactService;
    
    
    public FindPublisherContactOp(PublisherContactService publisherContactService, long userId) {
        this.publisherContactService = publisherContactService;
        this.userId = userId;
    }
    
    
    @Override
    public TPublisherContactInfo applyE() throws Exception {
        PublisherContactInfo pci = publisherContactService.getPublisherContactInfoByUserId((int) userId);
        if (pci == null) {
            throw new PublisherContactNotFoundException.Builder().build();
        }
        return PublisherContactConverter.toThrift(pci);
    }
    
}