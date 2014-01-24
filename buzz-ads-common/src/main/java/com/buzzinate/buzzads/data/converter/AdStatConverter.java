package com.buzzinate.buzzads.data.converter;

import com.buzzinate.buzzads.analytics.stats.AdDailyStatistic;
import com.buzzinate.buzzads.data.thrift.TAdNetworkEnum;
import com.buzzinate.buzzads.data.thrift.TAdStats;
import com.buzzinate.buzzads.data.thrift.TAdStats.Builder;

/**
 * 
 * @author Johnson
 *
 */
public final class AdStatConverter {
    private AdStatConverter() {
    }
    
    public static TAdStats toThrift(AdDailyStatistic statistic) {
        TAdNetworkEnum tNetwork = TAdNetworkEnum.LEZHI;
        long dateDay = 0;
        if (statistic.getNetwork() != null) {
            tNetwork = TAdNetworkEnum.findByValue(statistic.getNetwork().getCode());
        }
        if (statistic.getDateDay() != null) {
            dateDay = statistic.getDateDay().getTime();
        }
        Builder builder = new TAdStats.Builder()
                .entryId(statistic.getAdEntryId()).dateDay(dateDay)
                .views(statistic.getViews()).clicks(statistic.getClicks())
                .cpcClicks(statistic.getCpcClickNo()).network(tNetwork);
        return builder.build();
    }
}
