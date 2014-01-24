package com.buzzinate.buzzads.data.operations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.buzzinate.buzzads.core.service.PublisherContactService;
import com.buzzinate.buzzads.data.converter.PublisherContactConverter;
import com.buzzinate.buzzads.data.thrift.MissingRequiredFieldException;
import com.buzzinate.buzzads.data.thrift.TPublisherContactInfo;
import com.buzzinate.buzzads.domain.PublisherContactInfo;
import com.twitter.util.ExceptionalFunction0;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-12-13
 */
public class SaveOrUpdatePublisherContactOp extends ExceptionalFunction0<Void> {
    
    private static final Log LOG = LogFactory.getLog(SaveOrUpdatePublisherContactOp.class);
    
    private TPublisherContactInfo publisherContact;
    private PublisherContactService publisherContactService;
    
    
    public SaveOrUpdatePublisherContactOp(PublisherContactService publisherContactService, 
                    TPublisherContactInfo publisherContact) {
        this.publisherContactService = publisherContactService;
        this.publisherContact = publisherContact;
    }
    
    
    @Override
    public Void applyE() throws Exception {  
        LOG.info("SaveOrUpdatePublisherContact: " + publisherContact);
        PublisherContactInfo pci;
        if (publisherContact.getUserId() <= 0) {
            pci = PublisherContactConverter.fromThrift(publisherContact);
        } else {
            pci = publisherContactService.getPublisherContactInfoByUserId((int) publisherContact.getUserId());
            if (pci == null) {
                LOG.warn("Saving a new publisher.");
                pci = new PublisherContactInfo();
                pci.setUserId((int) publisherContact.getUserId());
            }
            PublisherContactConverter.mergeThrift(pci, publisherContact);
        }
        
        try {
            publisherContactService.saveOrUpdatePublisherContact(pci);
        } catch (Exception e) {
            LOG.warn("Failed to SaveOrUpdatePublisherContact: " + pci, e);
            throw new MissingRequiredFieldException.Builder().build();
        }
        return null;
    }
    

}       