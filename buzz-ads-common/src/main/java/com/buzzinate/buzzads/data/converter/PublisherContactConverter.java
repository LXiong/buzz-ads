package com.buzzinate.buzzads.data.converter;

import com.buzzinate.buzzads.data.thrift.TPublisherContactInfo;
import com.buzzinate.buzzads.data.thrift.TPublisherContactRevMethod;
import com.buzzinate.buzzads.data.thrift.TPublisherContactStatus;
import com.buzzinate.buzzads.domain.PublisherContactInfo;
import com.buzzinate.buzzads.enums.PublisherContactRevMethod;
import com.buzzinate.buzzads.enums.PublisherContactStausEnum;

/**
 * 
 * @author zyeming
 *
 */
public final class PublisherContactConverter {
    
    private PublisherContactConverter() { }
    
    
    /**
     * Convert from PublisherContactInfo domain object to thrift object
     * @param info
     * @return
     */
    public static TPublisherContactInfo toThrift(PublisherContactInfo info) {
        return new TPublisherContactInfo.Builder().
                        userId(info.getUserId()).
                        name(info.getName()).
                        email(info.getEmail()).
                        mobile(info.getMobile()).
                        qq(info.getQq()).
                        revMethod(TPublisherContactRevMethod.findByValue(info.getReceiveMethod().getCode())).
                        revAccount(info.getReceiveAccount()).
                        revName(info.getReceiveName()).
                        revBank(info.getReceiveBank()).
                        status(TPublisherContactStatus.findByValue(info.getStatus().getCode())).
                        build();
    }
    
    
    /**
     * Convert form thrift object to PublisherContactInfo domain object
     * @param tInfo
     * @return
     */
    public static PublisherContactInfo fromThrift(TPublisherContactInfo tInfo) {
        PublisherContactInfo info = new PublisherContactInfo();
        info.setUserId((int) tInfo.getUserId());
        mergeThrift(info, tInfo);
        return info;
    }
    
    
    /**
     * Merge thrift object into existing PublisherContactInfo
     * @param info
     * @param tInfo
     * @return
     */
    public static void mergeThrift(PublisherContactInfo info, TPublisherContactInfo tInfo) {
        if (tInfo.getEmailOption().isDefined())
            info.setEmail(tInfo.getEmail());
        if (tInfo.getNameOption().isDefined())
            info.setName(tInfo.getName());
        if (tInfo.getMobileOption().isDefined())
            info.setMobile(tInfo.getMobile());
        if (tInfo.getQqOption().isDefined())
            info.setQq(tInfo.getQq());
        if (tInfo.getRevMethodOption().isDefined())
            info.setReceiveMethod(PublisherContactRevMethod.findByValue(tInfo.getRevMethod().getValue()));
        if (tInfo.getRevAccountOption().isDefined())
            info.setReceiveAccount(tInfo.getRevAccount());
        if (tInfo.getRevBankOption().isDefined())
            info.setReceiveBank(tInfo.getRevBank());
        if (tInfo.getRevNameOption().isDefined())
            info.setReceiveName(tInfo.getRevName());
        if (tInfo.getStatusoption().isDefined())
            info.setStatus(PublisherContactStausEnum.findByValue(tInfo.getStatus().getValue()));
    }

}
