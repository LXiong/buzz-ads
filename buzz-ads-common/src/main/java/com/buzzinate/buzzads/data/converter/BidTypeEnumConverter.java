package com.buzzinate.buzzads.data.converter;

import java.util.EnumSet;
import java.util.Set;

import com.buzzinate.buzzads.data.thrift.TBidTypeEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 5, 2013 4:01:18 PM
 * 
 */
public final class BidTypeEnumConverter {
    private BidTypeEnumConverter() {
    }

    public static Set<TBidTypeEnum> toThrift(Set<BidTypeEnum> bidTypes) {
        if (bidTypes == null)
            return null;
        Set<TBidTypeEnum> res = EnumSet.noneOf(TBidTypeEnum.class);
        for (BidTypeEnum bidType : bidTypes) {
            res.add(TBidTypeEnum.findByValue(bidType.getCode()));
        }
        return res;
    }

    public static Set<BidTypeEnum> fromThrift(Set<TBidTypeEnum> bidTypes) {
        if (bidTypes == null)
            return null;
        Set<BidTypeEnum> res = EnumSet.noneOf(BidTypeEnum.class);
        for (TBidTypeEnum bidType : bidTypes) {
            res.add(BidTypeEnum.findByValue(bidType.getValue()));
        }
        return res;
    }
}
