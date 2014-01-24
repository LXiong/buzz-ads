package com.buzzinate.buzzads.data.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.buzzinate.buzzads.core.service.AdCampaignBudgetService;
import com.buzzinate.buzzads.core.service.AdvertiserBalanceService;
import com.buzzinate.buzzads.core.service.CampDayBudgetService;
import com.buzzinate.buzzads.data.converter.CampaignBudgetConverter;
import com.buzzinate.buzzads.data.thrift.TCampaignBudget;
import com.buzzinate.buzzads.domain.AdCampBudget;
import com.buzzinate.buzzads.domain.CampaignDayBudget;
import com.buzzinate.common.util.DateTimeUtil;
import com.twitter.util.ExceptionalFunction0;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 12, 2013 12:58:15 PM
 * 
 */
public class FindCampaignBudgets extends ExceptionalFunction0<List<TCampaignBudget>> {

    private AdCampaignBudgetService adCampaignBudgetService;
    private AdvertiserBalanceService advertiserBalanceService;
    private CampDayBudgetService campDayBudgetService;
    private List<Integer> campaignIds;

    public FindCampaignBudgets(AdCampaignBudgetService adCampaignBudgetService,
            CampDayBudgetService campDayBudgetService,
            AdvertiserBalanceService advertiserBalanceService,
            List<Integer> campaignIds) {
        this.adCampaignBudgetService = adCampaignBudgetService;
        this.advertiserBalanceService = advertiserBalanceService;
        this.campaignIds = campaignIds;
        this.campDayBudgetService = campDayBudgetService;
    }

    @Override
    public List<TCampaignBudget> applyE() {
        if (campaignIds == null || campaignIds.isEmpty())
            return null;

        List<TCampaignBudget> res = new ArrayList<TCampaignBudget>();
        List<AdCampBudget> cbs = new ArrayList<AdCampBudget>();
        for (int campaignId : campaignIds) {
            AdCampBudget cb = adCampaignBudgetService.getLatestCampBudget(campaignId);
            CampaignDayBudget dayBudget = campDayBudgetService
                    .findDayBudgetByCampIdAndDate(campaignId,
                            DateTimeUtil.getCurrentDateDay());
            if (cb != null) {
                if (dayBudget != null) {
                    cb.setMaxBudgetDay(dayBudget.getBudget());
                } 
                cbs.add(cb);
            }
                
        }

        if (!cbs.isEmpty()) {
            Map<Integer, Long> advertiserBalance = new HashMap<Integer, Long>();
            for (AdCampBudget cb : cbs) {
                Long balance = advertiserBalance.get(cb.getAdvertiserId());
                if (balance == null) {
                    balance = advertiserBalanceService.getLatestBalance(cb.getAdvertiserId());
                    advertiserBalance.put(cb.getAdvertiserId(), balance);
                }
                cb.setBalance(balance);
                res.add(CampaignBudgetConverter.toThrift(cb));
            }
        }
        return res;
    }
}
