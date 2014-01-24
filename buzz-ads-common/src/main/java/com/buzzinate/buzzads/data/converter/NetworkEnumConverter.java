package com.buzzinate.buzzads.data.converter;

import java.util.EnumSet;
import java.util.Set;

import com.buzzinate.buzzads.data.thrift.TAdNetworkEnum;
import com.buzzinate.buzzads.enums.AdNetworkEnum;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Feb 26, 2013 5:01:31 PM
 * 
 */
public final class NetworkEnumConverter {
    private NetworkEnumConverter() {
    }

    public static Set<TAdNetworkEnum> toThrift(Set<AdNetworkEnum> networks) {
        if (networks == null)
            return null;
        Set<TAdNetworkEnum> res = EnumSet.noneOf(TAdNetworkEnum.class);
        for (AdNetworkEnum network : networks) {
            res.add(TAdNetworkEnum.findByValue(network.getCode()));
        }
        return res;
    }

    public static Set<AdNetworkEnum> fromThrift(Set<TAdNetworkEnum> networks) {
        if (networks == null)
            return null;
        Set<AdNetworkEnum> res = EnumSet.noneOf(AdNetworkEnum.class);
        for (TAdNetworkEnum network : networks) {
            res.add(AdNetworkEnum.findByValue(network.getValue()));
        }
        return res;
    }
}
