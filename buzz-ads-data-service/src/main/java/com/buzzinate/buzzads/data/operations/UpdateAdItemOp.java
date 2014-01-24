package com.buzzinate.buzzads.data.operations;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.buzzinate.buzzads.core.service.AdEntryService;
import com.buzzinate.buzzads.core.service.AdOrderService;
import com.buzzinate.buzzads.data.converter.AdItemConverter;
import com.buzzinate.buzzads.data.thrift.TAdItem;
import com.buzzinate.buzzads.domain.AdEntry;
import com.buzzinate.buzzads.domain.AdItem;
import com.buzzinate.buzzads.domain.AdOrder;
import com.twitter.util.ExceptionalFunction0;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 5, 2013 5:13:50 PM
 * 
 */
public class UpdateAdItemOp extends ExceptionalFunction0<Void> {
    private static final Log LOG = LogFactory.getLog(UpdateAdItemOp.class);
    private AdEntryService adEntryService;
    private AdOrderService adOrderService;

    private AdItem adItem;

    public UpdateAdItemOp(AdEntryService adEntryService, AdOrderService adOrderService, TAdItem tItem) {
        this.adEntryService = adEntryService;
        this.adOrderService = adOrderService;

        this.adItem = AdItemConverter.fromThrift(tItem);
    }

    @Override
    public Void applyE() {
        AdEntry entry = adEntryService.getEntryById(adItem.getEntryId());
        if (adItem.getResourceType() != null) {
            entry.setResourceType(adItem.getResourceType());
        }
        if (StringUtils.isNotBlank(adItem.getResourceUrl()))
            entry.setResourceUrl(adItem.getResourceUrl());
        if (StringUtils.isNotBlank(adItem.getTitle()))
            entry.setTitle(adItem.getTitle());
        if (StringUtils.isNotBlank(adItem.getDestination()))
            entry.setDestination(adItem.getDestination());
        LOG.info("Updating adItem: EntryId:" + adItem.getEntryId() + 
                  ", ResourceUrl:" + adItem.getResourceUrl() + ", Title:" + 
                  adItem.getTitle() + ", Destination:" + 
                  adItem.getDestination());
        adEntryService.saveOrUpdate(entry);

        if (StringUtils.isNotBlank(adItem.getKeywords())) {
            AdOrder order = adOrderService.getOrderById(adItem.getOrderId());
            order.setKeywords(adItem.getKeywords());
            adOrderService.saveOrUpdate(order);
        }
        return null;
    }
}
