package com.buzzinate.buzzads.data.converter;

import com.buzzinate.buzzads.data.thrift.TCampaignBudget;
import com.buzzinate.buzzads.domain.AdCampBudget;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> 
 * Mar 12, 2013 1:26:13 PM
 * 
 */
public final class CampaignBudgetConverter {
    private CampaignBudgetConverter() {
    }

    /**
     * Convert form thrift object to AdCampBudget domain object
     * 
     * @param tCb
     * @return
     */
    public static AdCampBudget fromThrift(TCampaignBudget tCb) {
        AdCampBudget cb = new AdCampBudget();
        cb.setCampaignId(tCb.getCampaignId());
        cb.setAdvertiserId(tCb.getAdvertiserId());
        cb.setBalance(tCb.getAdvertiserBalance());
        cb.setBudgetDay(tCb.getBudgetDay());
        cb.setBudgetTotal(tCb.getBudgetTotal());
        cb.setMaxBudgetDay(tCb.getMaxBudgetDay());
        cb.setMaxBudgetTotal(tCb.getMaxBudgetTotal());
        
        return cb;
    }

    public static TCampaignBudget toThrift(AdCampBudget cb) {
        return new TCampaignBudget.Builder()
            .campaignId(cb.getCampaignId())
            .advertiserId(cb.getAdvertiserId())
            .advertiserBalance(cb.getBalance())
            .budgetDay(cb.getBudgetDay())
            .budgetTotal(cb.getBudgetTotal())
            .maxBudgetDay(cb.getMaxBudgetDay())
            .maxBudgetTotal(cb.getMaxBudgetTotal())
            .build();
    }
}
