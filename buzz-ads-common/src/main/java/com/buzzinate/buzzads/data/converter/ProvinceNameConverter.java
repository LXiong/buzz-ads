package com.buzzinate.buzzads.data.converter;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import com.buzzinate.common.util.ip.ProvinceName;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 7, 2013 11:18:35 AM
 * 
 */
public final class ProvinceNameConverter {
    private ProvinceNameConverter() {
    }

    public static Set<ProvinceName> fromStringSet(String[] locations) {
        if (locations == null)
            return null;
        Set<ProvinceName> res = EnumSet.noneOf(ProvinceName.class);
        for (String location : locations) {
            ProvinceName pn = ProvinceName.findByValue(location);
            if (pn != null)
                res.add(pn);
        }
        return res;
    }

    public static Set<ProvinceName> fromStringSet(Set<String> locations) {
        if (locations == null)
            return null;
        Set<ProvinceName> res = EnumSet.noneOf(ProvinceName.class);
        for (String location : locations) {
            res.add(ProvinceName.findByValue(location));
        }
        return res;
    }

    public static Set<String> toStringSet(Set<ProvinceName> locations) {
        if (locations == null)
            return null;
        Set<String> res = new HashSet<String>();
        for (ProvinceName location : locations) {
            res.add(location.getCode());
        }
        return res;
    }

}
