package com.buzzinate.buzzads.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buzzinate.buzzads.analytics.stats.AdDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.AdOrderDailyStatistic;
import com.buzzinate.buzzads.core.dao.StatsAdDailyDao;
import com.buzzinate.buzzads.core.dao.StatsAdOrderDailyDao;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdEntryService;
import com.buzzinate.buzzads.core.service.AdOrderService;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdEntry;
import com.buzzinate.buzzads.domain.AdOrder;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;

/**
 * 
 * @author Johnson
 *
 */
@Service
public class AdStatsServices {
    @Autowired
    private StatsAdDailyDao statsAdDailyDao;
    @Autowired
    private StatsAdOrderDailyDao statsAdOrderDailyDao;
    
    @Autowired
    private AdOrderService adOrderService;
    @Autowired
    private AdCampaignService adCampaignService;
    @Autowired
    private AdEntryService adEntryService;
    
    
    public List<AdOrderDailyStatistic> getAdOrderStatsByOrderIdDaily(
                    Date startDate, Date endDate, int orderId) {
        return statsAdOrderDailyDao.getAdOrderStatsByOrderIdDaily(startDate, endDate, orderId);
    }
    
    //获取entries 在时间段内的 对应统计信息
    public List<AdEntry> getAdEntryStatsByEntries(Date startDate, Date endDate, 
            List<AdEntry> entries, AdOrder order) {
        List<Integer> entryIds = new ArrayList<Integer>(); 
        for (AdEntry entry : entries) {
            entryIds.add(entry.getId());
        }
        
        //获取 统计信息 并转为map
        List<AdDailyStatistic> dailyStatsList = statsAdDailyDao.
                getAdStatsByEntryIds(startDate, endDate, entryIds);
        
        Map<Integer, AdDailyStatistic> dailyStatsMap = convertToDailyEntryStatisticMap(dailyStatsList);
        
        //遍历entries 填入统计信息
        for (AdEntry entry : entries) {
            entry.setOrderName(order.getName());
            AdDailyStatistic dailyStats = dailyStatsMap.get(entry.getId());
            if (dailyStats != null) {
                entry.setClicks(dailyStats.getClicks());
                entry.setViews(dailyStats.getViews());
                entry.setCpcClicks(dailyStats.getCpcClickNo());
                entry.setCpcComm(dailyStats.getCpcTotalCommission());
                entry.setCpsComm(dailyStats.getCpsTotalCommission());
                entry.setCpmViews(dailyStats.getCpmViewNo());
                entry.setCpmComm(dailyStats.getCpmTotalCommission());
            }
        }
        return entries;
    }
    
    public List<AdDailyStatistic> getAdEntryStatsByAdvId(int advId, Date startDate, Date endDate) {
        List<AdOrder> orders = adOrderService.listAdOrdersByAdvertiserId(advId);
        if (orders.size() == 0)
            return new ArrayList<AdDailyStatistic>();
        List<Integer> orderIds = new ArrayList<Integer>();
        for (AdOrder order: orders) {
            orderIds.add(order.getId());
        }
        return statsAdDailyDao.getAdStatsByOrderIds(startDate, endDate,
                orderIds);
    }

    //获取orders 在时间段内的 对应统计信息
    public List<AdOrder> getAdOrderStatsByOrders(Date startDate, Date endDate, List<AdOrder> orders) {
        
        List<Integer> orderIds = new ArrayList<Integer>();
        for (AdOrder order : orders) {
            orderIds.add(order.getId());
        }
        //获取 统计信息 并转为map
        List<AdOrderDailyStatistic> dailyStatsList = statsAdOrderDailyDao.
                getAdOrderStatsSumByOrderIds(startDate, endDate, orderIds);
        
        Map<Integer, AdOrderDailyStatistic> dailyStatsMap = convertToDailyOrderStatisticMap(dailyStatsList);
        
        //遍历entries 填入统计信息
        for (AdOrder order: orders) {
            AdOrderDailyStatistic dailyStats = dailyStatsMap.get(order.getId());
            if (dailyStats != null) {
                order.setClicks(dailyStats.getClicks());
                order.setCpcClicks(dailyStats.getCpcClickNo());
                order.setViews(dailyStats.getViews());
                order.setCpcComm(dailyStats.getCpcTotalCommission());
                order.setCpsComm(dailyStats.getCpsTotalCommission());
                order.setCpmViews(dailyStats.getCpmViewNo());
                order.setCpmComm(dailyStats.getCpmTotalCommission());
            }
        }
        return orders;
    }
    
    public List<AdOrderDailyStatistic> getAdOrderStatsByAdvId(int advertiserId, Date startDate, Date endDate) {
        List<AdOrder> orders = adOrderService.listAdOrdersByAdvertiserId(advertiserId);
        if (orders.size() == 0)
            return new ArrayList<AdOrderDailyStatistic>();
        List<Integer> orderIds = new ArrayList<Integer>();
        for (AdOrder order: orders) {
            orderIds.add(order.getId());
        }
        return statsAdOrderDailyDao.getAdOrderStatsByOrderIdsDaily(startDate, endDate, orderIds);
    }
    
    public List<AdOrder> allGroupStats(int advertiserId, Date start, Date end, Pagination page) {
        AdOrder query = new AdOrder();
        query.setAdvertiserId(advertiserId);
        query.setStatus(null);
        List<AdOrder> orders = adOrderService.listAdOrders(query, page);
        if (orders.size() > 0) {
            for (AdOrder order : orders) {
                //补全活动信息
                AdCampaign camp = adCampaignService.getAdCampaignById(order.getCampaignId());
                if (camp != null) {
                    order.setCampName(camp.getName());
                    if (!AdStatusEnum.ENABLED.equals(camp.getStatus())) {
                    	if (!AdStatusEnum.DISABLED.equals(order.getStatus())) {
                    		order.setStatus(camp.getStatus());
                    	}
                        order.setStatusDesc("广告活动已");
                    }
                }
                //TODO：补全统计信息，可以使用batch操作减少数据库请求
                Object[] daily = statsAdOrderDailyDao.getAdOrderStatsSum(start, end, order.getId());
                if (daily != null) {
                    order.setViews((Integer) daily[0]);
                    order.setClicks((Integer) daily[1]);
                    order.setCpcComm((Integer) daily[2]);
                    order.setCpsComm((Integer) daily[3]);
                    order.setCpcClicks((Integer) daily[4]);
                    order.setCpmComm((BigDecimal) daily[5]);
                }
            }
        }
        return orders;
    }
    
    public List<AdEntry> allAdEntryStats(int advertiserId, Date start, Date end, Pagination page) {
        AdEntry query = new AdEntry();
        query.setAdvertiserId(advertiserId);
        query.setStatus(null);
        List<AdEntry> adEntries = adEntryService.listAdEntries(query, page);
        if (adEntries.size() > 0) {
            for (AdEntry entry : adEntries) {
                AdCampaign camp = adCampaignService.getAdCampaignById(entry.getCampaignId());
                if (camp != null) {
                    entry.setCampName(camp.getName());
                    if (!AdStatusEnum.ENABLED.equals(camp.getStatus())) {
                    	if (!AdStatusEnum.DISABLED.equals(entry.getStatus())) {
                    		entry.setStatus(camp.getStatus());
                    	}
                        entry.setStatusDesc("广告活动已");
                    }
                }
                AdOrder order = adOrderService.getOrderById(entry.getOrderId());
                if (order != null) {
                    entry.setOrderName(order.getName());
                    if (camp != null && AdStatusEnum.ENABLED.equals(camp.getStatus()) && 
                            !AdStatusEnum.ENABLED.equals(order.getStatus())) {
                        entry.setStatus(order.getStatus());
                        entry.setStatusDesc("广告组已");
                    }
                }
                //TODO：统计信息，，可以使用batch操作减少数据库请求
                Object[] daily = statsAdDailyDao.getAdStats(start, end, entry.getId());
                if (daily != null) {
                    entry.setViews((Integer) daily[0]);
                    entry.setClicks((Integer) daily[1]);
                    entry.setCpcComm((Integer) daily[2]);
                    entry.setCpsComm((Integer) daily[3]);
                    entry.setCpmComm((BigDecimal) daily[4]);
                }
            }
        }
        return adEntries;
    }
    
    //获取entries 在时间段内的 对应统计信息
    public List<AdEntry> getAdEntryStatsByEntries(Date startDate, Date endDate, 
            List<AdEntry> entries, List<AdOrder> orders) {
        Map<Integer, AdOrder> adOrderMap = convertToAdOrderMap(orders);
        
        List<Integer> entryIds = new ArrayList<Integer>(); 
        for (AdEntry entry : entries) {
            entryIds.add(entry.getId());
        }
        
        //获取 统计信息 并转为map
        List<AdDailyStatistic> dailyStatsList = statsAdDailyDao.
                getAdStatsByEntryIds(startDate, endDate, entryIds);
        
        Map<Integer, AdDailyStatistic> dailyStatsMap = convertToDailyEntryStatisticMap(dailyStatsList);
        
        //遍历entries 填入统计信息 以及对应的order信息
        for (AdEntry entry : entries) {
            AdDailyStatistic dailyStats = dailyStatsMap.get(entry.getId());
            if (dailyStats != null) {
                entry.setClicks(dailyStats.getClicks());
                entry.setCpcClicks(dailyStats.getCpcClickNo());
                entry.setViews(dailyStats.getViews());
                entry.setCpcComm(dailyStats.getCpcTotalCommission());
                entry.setCpsComm(dailyStats.getCpsTotalCommission());
                entry.setCpmViews(dailyStats.getCpmViewNo());
                entry.setCpmComm(dailyStats.getCpmTotalCommission());
            }
            AdOrder order = adOrderMap.get(entry.getOrderId());
            if (order != null) {
                entry.setOrderName(order.getName());
                if (!AdStatusEnum.ENABLED.equals(order.getStatus())) {
                    entry.setStatus(order.getStatus());
                    entry.setStatusDesc("广告组已");
                }
            }
        }
        return entries;
    }
    
    private Map<Integer, AdOrderDailyStatistic> convertToDailyOrderStatisticMap(
            List<AdOrderDailyStatistic> dailyStatsList) {
        Map<Integer, AdOrderDailyStatistic> dailyStatsMap = new HashMap<Integer, AdOrderDailyStatistic>();
        for (AdOrderDailyStatistic dailyStats : dailyStatsList) {
            dailyStatsMap.put(dailyStats.getOrderId(), dailyStats);
        }
        return dailyStatsMap;
    }
    
    private Map<Integer, AdDailyStatistic> convertToDailyEntryStatisticMap(List<AdDailyStatistic> dailyStatsList) {
        Map<Integer, AdDailyStatistic> dailyStatsMap = new HashMap<Integer, AdDailyStatistic>();
        for (AdDailyStatistic dailyStats : dailyStatsList) {
            dailyStatsMap.put(dailyStats.getAdEntryId(), dailyStats);
        }
        return dailyStatsMap;
    }
    
    private Map<Integer, AdOrder> convertToAdOrderMap(List<AdOrder> orders) {
        Map<Integer, AdOrder> adOrderMap = new HashMap<Integer, AdOrder>();
        for (AdOrder order : orders) {
            adOrderMap.put(order.getId(), order);
        }
        return adOrderMap;
    }
}
