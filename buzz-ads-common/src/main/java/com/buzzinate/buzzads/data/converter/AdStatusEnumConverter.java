package com.buzzinate.buzzads.data.converter;

import java.util.EnumSet;
import java.util.Set;

import com.buzzinate.buzzads.data.thrift.TAdStatusEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 5, 2013 3:58:27 PM
 * 
 */
public final class AdStatusEnumConverter {
    private AdStatusEnumConverter() {
    }

    public static Set<TAdStatusEnum> toThrift(Set<AdStatusEnum> status) {
        if (status == null)
            return null;
        Set<TAdStatusEnum> res = EnumSet.noneOf(TAdStatusEnum.class);
        for (AdStatusEnum network : status) {
            res.add(TAdStatusEnum.findByValue(network.getCode()));
        }
        return res;
    }

    public static Set<AdStatusEnum> fromThrift(Set<TAdStatusEnum> status) {
        if (status == null)
            return null;
        Set<AdStatusEnum> res = EnumSet.noneOf(AdStatusEnum.class);
        for (TAdStatusEnum network : status) {
            res.add(AdStatusEnum.findByValue(network.getValue()));
        }
        return res;
    }
}
