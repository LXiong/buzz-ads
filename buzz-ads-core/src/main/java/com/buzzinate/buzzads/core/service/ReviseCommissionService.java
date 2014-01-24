package com.buzzinate.buzzads.core.service;


import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buzzinate.buzzads.analytics.stats.AdminDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.PublisherDailyStatistic;
import com.buzzinate.buzzads.core.bean.PublisherSettlementStats;
import com.buzzinate.buzzads.core.dao.StatsCampaignDailyDao;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.domain.AdvertiserBalance;
import com.buzzinate.buzzads.domain.AdvertiserBilling;
import com.buzzinate.buzzads.domain.PublisherSettlement;
import com.buzzinate.buzzads.domain.Site;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.buzzads.enums.AdvertiserBillingType;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.UuidUtil;

/**
 * 
 * @author johnson
 *
 */
@Service
public class ReviseCommissionService {
    
    private static Log log = LogFactory.getLog("reviseTask");
    
    @Autowired
    private StatsAdminService statsAdminService;
    @Autowired
    private StatsPublisherServices statsPublisherServices;
    @Autowired
    private SiteService siteService;
    @Autowired
    private PublisherSettlementService publisherSettlementService;
    @Autowired
    private StatsCampaignDailyDao statsCampaignDailyDao;
    @Autowired
    private AdvertiserBillingService advertiserBillingService;
    @Autowired
    private AdvertiserBalanceService advertiserBalanceService;
    @Autowired
    private PublisherContactService publisherContactService;
    
    /**
     * 订正前一天站长佣金。因为是按分成计算，可能会有精度误差，每天调整一次。
     * @param day
     */
    public void revisePublisherCommission(Date day) {
        
        //用于统计并更新Admin总的站长佣金数
        Map<AdNetworkEnum, Integer> totalCpcPubCommMap = new HashMap<AdNetworkEnum, Integer>();
        Map<AdNetworkEnum, Integer> totalCpsPubCommMap = new HashMap<AdNetworkEnum, Integer>();
        Map<AdNetworkEnum, Integer> totalCpsConfirmedPubCommMap = new HashMap<AdNetworkEnum, Integer>();
        Map<AdNetworkEnum, BigDecimal> totalCpmPubCommMap = new HashMap<AdNetworkEnum, BigDecimal>();
        
        //订正站长数据中的站长佣金，并计算总站长佣金
        List<PublisherDailyStatistic> publisherStatsList = 
                        statsPublisherServices.getAllPublisherDailyStatistics(day);
        for (PublisherDailyStatistic s : publisherStatsList) {
            AdNetworkEnum network = s.getNetwork();
            int proportion = publisherContactService.getPublisherProportionByUUID(s.getUuidString());
            int cpcPubComm = s.getCpcTotalCommission() * proportion / 100;
            int cpsPubComm = s.getCpsTotalCommission() * proportion / 100;
            int cpsConfirmedPubComm = s.getCpsTotalConfirmedCommission() * proportion / 100;

            BigDecimal cpmPubComm = s.getCpmTotalCommission().multiply(
                            new BigDecimal(proportion / 100.00));
            
            String logMessage = "PublisherDailyStatistic[UUID=" + UuidUtil.byteArrayToUuid(s.getUuid()) + 
                            ", dateDay=" + DateTimeUtil.formatDate(s.getDateDay()) + 
                            ", network=" + s.getNetwork() + "]" +
                            ", cpcPubCommission: " + s.getCpcPubCommission() + "=>" + cpcPubComm + 
                            ", cpmPubCommission: " + s.getCpmPubCommission() + "=>" + cpmPubComm + 
                            ", cpsPubCommission: " + s.getCpsPubCommission() + "=>" + cpsPubComm + 
                            ", cpsConfirmedCommission: " + s.getCpsConfirmedCommission() + "=>" + cpsConfirmedPubComm; 
                    
            boolean isChanged = false;
            if (s.getCpcPubCommission() != cpcPubComm) {
                s.setCpcPubCommission(cpcPubComm);
                isChanged = true;
            }
            if (s.getCpmPubCommission().compareTo(cpmPubComm) != 0) {
                s.setCpmPubCommission(cpmPubComm);
                isChanged = true;
            }
            if (s.getCpsPubCommission() != cpsPubComm) {
                s.setCpsPubCommission(cpsPubComm);
                isChanged = true;
            }
            if (s.getCpsConfirmedCommission() != cpsConfirmedPubComm) {
                s.setCpsConfirmedCommission(cpsConfirmedPubComm);
                isChanged = true;
            }
            
            if (isChanged) {
                statsPublisherServices.saveOrUpdate(s);
                log.info(logMessage);
            }
            
            // 统计各种网络分类中的站长总佣金
            BigDecimal totalCpmPubComm = totalCpmPubCommMap.get(network);
            if (totalCpmPubComm == null) {
                totalCpmPubCommMap.put(network, new BigDecimal(0));
            } else {
                totalCpmPubCommMap.put(network, totalCpmPubComm.add(cpmPubComm));
            }
            updateTotalCommMap(network, totalCpcPubCommMap, cpcPubComm);
            updateTotalCommMap(network, totalCpsPubCommMap, cpsPubComm);
            updateTotalCommMap(network, totalCpsConfirmedPubCommMap, cpsConfirmedPubComm);
        }
        
        //订正Admin数据中的站长佣金
        List<AdminDailyStatistic> adminStatsList = statsAdminService.getAdminDailyStatistic(day);
        for (AdminDailyStatistic s : adminStatsList) {
            AdNetworkEnum network = s.getNetwork();
            BigDecimal totalCpmPubComm = totalCpmPubCommMap.get(network) == null ? 
                    new BigDecimal(0.00) : totalCpmPubCommMap.get(network);
            int totalCpcPubComm = getTotalComm(network, totalCpcPubCommMap);
            int totalCpsPubComm = getTotalComm(network, totalCpsPubCommMap);
            int totalCpsConfirmedPubComm = getTotalComm(network, totalCpsConfirmedPubCommMap);
            
            String logMessage = "AdminDailyStatistic[dateDay=" + DateTimeUtil.formatDate(s.getDateDay()) + 
                            ", network=" + s.getNetwork() + "]" +
                            ", cpcPubCommission: " + s.getCpcPubCommission() + "=>" + totalCpcPubComm +
                            ", cpmPubCommission: " + s.getCpmPubCommission() + "=>" + totalCpmPubComm +
                            ", cpsPubCommission: " + s.getCpsPubCommission() + "=>" + totalCpsPubComm + 
                            ", cpsConfirmedCommission: " + s.getCpsConfirmedCommission() + "=>" + 
                            totalCpsConfirmedPubComm; 
                    
            boolean isChanged = false;
            if (s.getCpcPubCommission() != totalCpcPubComm) {
                s.setCpcPubCommission(totalCpcPubComm);
                isChanged = true;
            }
            if (s.getCpmPubCommission().compareTo(totalCpmPubComm) != 0) {
                s.setCpmPubCommission(totalCpmPubComm);
                isChanged = true;
            }
            if (s.getCpsPubCommission() != totalCpsPubComm) {
                s.setCpsPubCommission(totalCpsPubComm);
                isChanged = true;
            }
            if (s.getCpsConfirmedCommission() != totalCpsConfirmedPubComm) {
                s.setCpsConfirmedCommission(totalCpsConfirmedPubComm);
                isChanged = true;
            }
            
            if (isChanged) {
                statsAdminService.saveOrUpdate(s);
                log.info(logMessage);
            }
        }
        
    }
    
    private int getTotalComm(AdNetworkEnum network, Map<AdNetworkEnum, Integer> totalCommMap) {
        Integer comm = totalCommMap.get(network);
        return comm == null ? 0 : comm;
    }

    private void updateTotalCommMap(AdNetworkEnum network, Map<AdNetworkEnum, Integer> totalCommMap,
                    int cpcPubComm) {
        Integer totalCpcPubComm = totalCommMap.get(network);
        if (totalCpcPubComm == null) {
            totalCommMap.put(network, cpcPubComm);
        } else {
            totalCommMap.put(network, totalCpcPubComm + cpcPubComm);
        }
    }
    
    /**
     * 订正广告主的前一天的支出
     * @param day
     */
    public void reviseAdvertiserBilling(Date day) {
        Date reviseDay = DateTimeUtil.getDateDay(day);
        
        //获取广告商在day的广告支出数组列表，
        List<Object[]> advertiserPayList = statsCampaignDailyDao.getAdvertiserPayByDay(reviseDay);
        Date startDate = reviseDay;
        Date endDate = DateTimeUtil.plusDays(reviseDay, 1);
        for (Object[] advertiserPay : advertiserPayList) {
            int advertiserId = (Integer) advertiserPay[0];
            int cpcComm = (Integer) advertiserPay[1];
            int cpmComm = (advertiserPay[2] == null ? new BigDecimal(0) : (BigDecimal) advertiserPay[2]).multiply(
                    new BigDecimal(100)).intValue();
            
            AdvertiserBilling billing = advertiserBillingService
                    .getAdvertiserBillingByDay(advertiserId, startDate, endDate);
            //如果有差价则插入一条新的记录  类型为adjustment 并设置comment
            if (billing != null && billing.getDebits() != cpcComm + cpmComm) {
                
                String logMessage = DateTimeUtil.formatDate(day) + ", AdvertiserBilling[AdvertiserId=" + 
                            advertiserId + "]" +
                            ", debits: " + billing.getDebits() + "=>" + (cpcComm + cpmComm);
                
                AdvertiserBalance balance = advertiserBalanceService.getByAdvertiserId(advertiserId);
                //balance 不应为空  但还是判断一下
                if (balance == null) {
                    log.info("There's no balance for afvertiserId: " + advertiserId + "found, abort revise");
                    continue;
                }
                
                int imbalance = cpmComm + cpcComm - billing.getDebits();
                
                logMessage += "\n AdvertiserBalance[AdvertiserId=" + advertiserId + "]" +
                        ", debitsTotal: " + balance.getDebitsTotal() + "=>" + (balance.getDebitsTotal() + imbalance) + 
                        ",balance: " + balance.getBalance() + "=>" + (balance.getBalance() - imbalance);
                
                
                AdvertiserBilling reviseBilling = new AdvertiserBilling(advertiserId, imbalance);
                reviseBilling.setBalance(balance.getBalance() - imbalance);
                reviseBilling.setType(AdvertiserBillingType.DEBIT_DAY);
                reviseBilling.setComment("Created by revise task for the imbalance");
                reviseBilling.setBillingDay(reviseDay);
                
                try {
                    log.info(logMessage);
                    advertiserBalanceService.reviseBillingAndbalance(advertiserId, reviseBilling, imbalance);
                    log.info("revise the last advertiser billing and balance");
                } catch (Exception e) {
                    log.error("Failed revise advertiser billing and balance. ");
                }
            }
        }
        
    }
    
    /**
     * 订正站长的佣金, 时间从前天所在月的月初到前天
     * @param month
     * @param dateEnd
     */
    public void revisePublisherSettlement(Date dateEnd) {
        Date month = DateUtils.truncate(dateEnd, Calendar.MONTH);
        Date reviseEndDay = DateTimeUtil.getDateDay(dateEnd);
        List<PublisherSettlementStats> publisherStatsList = statsPublisherServices.
                getPublisherStatsForRevise(month, reviseEndDay);
        
        Map<Integer, PublisherSettlementStats> publisherStatsMap = new HashMap<Integer, PublisherSettlementStats>(); 
        for (PublisherSettlementStats stats : publisherStatsList) {
            String uuid = UuidUtil.byteArrayToUuid(stats.getUuid());
            Site s = siteService.getUuidSiteByUuid(uuid);
            if (s == null) {
                log.warn("Invalid site for uuid: " + uuid);
                continue;
            }
            
            PublisherSettlementStats publisherStats = publisherStatsMap.get(s.getUserId());
            if (publisherStats == null) {
                publisherStats = stats;
                publisherStats.setUserId(s.getUserId());
                publisherStatsMap.put(s.getUserId(), publisherStats);
            } else {
                publisherStats.setCpcNo(publisherStats.getCpcNo() + stats.getCpcNo());
                publisherStats.setCpcCommission(publisherStats.getCpcCommission() + stats.getCpcCommission());
                publisherStats.setCpsNo(publisherStats.getCpsNo() + stats.getCpsNo());
                publisherStats.setCpsCommission(publisherStats.getCpsCommission() + stats.getCpsCommission());
                publisherStats.setCommission(publisherStats.getCommission() + stats.getCommission());
                publisherStats.setCpmNo(publisherStats.getCpmNo() + stats.getCpmNo());
                publisherStats.setCpmCommission(publisherStats.getCpmCommission().add(stats.getCpmCommission()));
            }
        }
        
        for (Map.Entry<Integer, PublisherSettlementStats> entry : publisherStatsMap.entrySet()) {
            int userId = entry.getKey();
            PublisherSettlementStats stats = entry.getValue();
            
            try {
                //获取这个月的佣金
                PublisherSettlement settlement = publisherSettlementService.getByUserIdAndMonth(userId, month);
                if (settlement != null && !validateAndResetPublisherCommission(stats, settlement, month)) {
                    publisherSettlementService.saveOrUpdate(settlement);
                    log.info("Revise the publisher settlement.");
                }
            } catch (Exception e) {
                log.error("Failed revise publisher settlement. " + stats.toString(), e);
            }
        }
    }
    
    private boolean validateAndResetPublisherCommission(PublisherSettlementStats stats, 
            PublisherSettlement settlement, Date month) {
        boolean valid = stats.getCommission() == settlement.getCommission() && 
                stats.getCpcCommission() == settlement.getCpcCommission() && 
                stats.getCpsCommission() == settlement.getCpsCommission() &&
                stats.getCpmCommission().compareTo(settlement.getCpmCommission()) == 0 &&
                stats.getCpmNo() == settlement.getCpmNo() &&
                stats.getCpcNo() == settlement.getCpcNo() &&
                stats.getCpsNo() == settlement.getCpsNo();
        if (!valid) {
            String logMessage = "PublisherSettlement[UserId=" + settlement.getUserId() + 
                    ", month=" + DateTimeUtil.formatDate(month) + "]" +
                    ", commission: " + settlement.getCommission() + "=>" + stats.getCommission() + 
                    ", cpcCommission: " + settlement.getCpcCommission() + "=>" + stats.getCpcCommission() + 
                    ", cpsCommission: " + settlement.getCpsCommission() + "=>" + stats.getCpsCommission() + 
                    ", cpmCommission: " + settlement.getCpmCommission() + "=>" + stats.getCpmCommission() + 
                    ", cpcNo: " + settlement.getCpmNo() + "=>" + stats.getCpmNo() + 
                    ", cpcNo: " + settlement.getCpcNo() + "=>" + stats.getCpcNo() + 
                    ", cpsNo: " + settlement.getCpsNo() + "=>" + stats.getCpsNo();
            log.info(logMessage);
            settlement.setCommission(stats.getCommission());
            settlement.setCpcCommission(stats.getCpcCommission());
            settlement.setCpsCommission(stats.getCpsCommission());
            settlement.setCpmCommission(stats.getCpmCommission());
            settlement.setCpmNo(stats.getCpmNo());
            settlement.setCpcNo(stats.getCpcNo());
            settlement.setCpsNo(stats.getCpsNo());
        }
        
        return valid;
    }
    
}