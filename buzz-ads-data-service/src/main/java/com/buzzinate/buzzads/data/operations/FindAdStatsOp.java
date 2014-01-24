package com.buzzinate.buzzads.data.operations;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.buzzinate.buzzads.analytics.stats.AdDailyStatistic;
import com.buzzinate.buzzads.core.service.StatsAdService;
import com.buzzinate.buzzads.data.converter.AdStatConverter;
import com.buzzinate.buzzads.data.converter.NetworkEnumConverter;
import com.buzzinate.buzzads.data.thrift.TAdStats;
import com.buzzinate.buzzads.data.thrift.TAdStatsCriteria;
import com.buzzinate.buzzads.data.thrift.TPagination;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.common.util.DateTimeUtil;
import com.twitter.util.ExceptionalFunction0;

/**
 * 
 * @author Johnson
 *
 */
public class FindAdStatsOp extends ExceptionalFunction0<List<TAdStats>> {
    private int entryId;
    private Set<AdNetworkEnum> network = new HashSet<AdNetworkEnum>();
    private Date dateDay;
    private int start;
    private int count;
    private StatsAdService statsAdService;
    
    
    public FindAdStatsOp(StatsAdService statsAdService, TAdStatsCriteria criteria, TPagination pagination) {
        this.statsAdService = statsAdService;
        if (criteria.getEntryIdOption().isDefined()) {
            this.entryId = criteria.getEntryId();
        }
        if (criteria.getDateDayOption().isDefined()) {
            this.dateDay = DateTimeUtil.getDateDay(new Date(criteria.getDateDay()));
        } else {
            this.dateDay = DateTimeUtil.getDateDay(DateTimeUtil.getYestoday());
        }
        if (criteria.getNetworkOption().isDefined()) {
            network.addAll(NetworkEnumConverter.fromThrift(criteria.getNetwork()));
        }
        this.start = pagination.getStart();
        this.count = pagination.getCount();
    }

    @Override
    public List<TAdStats> applyE() throws Exception {
        List<TAdStats> tAdStats = new ArrayList<TAdStats>();
        
        List<AdDailyStatistic> stats = statsAdService.getAdStatsByCondition(entryId, dateDay, network);
        for (AdDailyStatistic statistic : stats) {
            statistic.setDateDay(dateDay);
            tAdStats.add(AdStatConverter.toThrift(statistic));
        }
        if (tAdStats.size() < start || tAdStats.isEmpty())
            return new ArrayList<TAdStats>();
        return tAdStats.subList(start, Math.min(tAdStats.size(), start + count));
    }
    
}
