package com.buzzinate.buzzads.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.core.bean.CpcTimeSegment;
import com.buzzinate.buzzads.core.dao.AdvertiserBalanceDao;
import com.buzzinate.buzzads.domain.AdvertiserBalance;
import com.buzzinate.buzzads.domain.AdvertiserBilling;
import com.buzzinate.buzzads.enums.AdvertiserBillingType;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 13, 2013 5:35:47 PM
 */
@Service
public class AdvertiserBalanceService {

    @Autowired
    private AdvertiserBalanceDao advertiserBalanceDao;
    @Autowired
    private AdvertiserBillingService advertiserBillingService;
    @Autowired
    private AdCampaignService adCampaignService;
    @Autowired
    private AdCampaignBudgetService adCampaignBudgetService;


    /**
     * Update advertiser balance for credits
     *
     * @param advertiserId
     * @param credits
     * @return
     */
    public int insertOrUpdateForCredits(int advertiserId, long credits) {
        return advertiserBalanceDao.insertOrUpdateForCredits(advertiserId, credits);
    }

    /**
     * Update advertiser balance for debits
     *
     * @param advertiserId
     * @param debits
     * @return
     */
    public int insertOrUpdateForDebits(int advertiserId, long debits) {
        return advertiserBalanceDao.insertOrUpdateForDebits(advertiserId, debits);
    }
    
    /**
     * Revise the billing and balance for the advertiser
     * This function is used by revise task.
     * @param advertiserId
     * @param reviseBilling
     * @param imbalance
     */
    @Transactional(value = "buzzads", readOnly = false)
    public void reviseBillingAndbalance(int advertiserId, AdvertiserBilling reviseBilling, int imbalance) {
        advertiserBillingService.create(reviseBilling);
        insertOrUpdateForDebits(reviseBilling.getAdvertiserId(), imbalance);
    }

    /**
     * Add a billing record for debits and update balance
     *
     * @param advertiserId
     * @param debits
     */
    @Transactional(value = "buzzads", readOnly = false)
    public void updateBillingAndBalanceForDebits(int advertiserId, long debits, Date billingDay) {
        insertOrUpdateForDebits(advertiserId, debits);
        AdvertiserBalance balance = getByAdvertiserId(advertiserId);

        AdvertiserBilling billing = new AdvertiserBilling();
        billing.setAdvertiserId(advertiserId);
        billing.setBillingDay(billingDay);
        billing.setDebits((int) debits);
        billing.setBalance(balance.getBalance());
        billing.setType(AdvertiserBillingType.DEBIT_DAY);
        advertiserBillingService.create(billing);
    }

    /**
     * Add a billing record for credits and update balance
     *
     * @param advertiserId
     * @param credits
     * @param type
     * @return billing id
     */
    @Transactional(value = "buzzads", readOnly = false)
    public int updateBillingAndBalanceForCredits(int advertiserId, long credits, String comment, Date billingDay,
                    AdvertiserBillingType type) {
        insertOrUpdateForCredits(advertiserId, credits);
        AdvertiserBalance balance = getByAdvertiserId(advertiserId);

        AdvertiserBilling billing = new AdvertiserBilling();
        billing.setAdvertiserId(advertiserId);
        billing.setBillingDay(billingDay);
        billing.setCredits((int) credits);
        billing.setBalance(balance.getBalance());
        billing.setType(type);
        billing.setComment(comment);
        return advertiserBillingService.create(billing);
    }

    /**
     * Get balance of advertiser, create if not exists
     *
     * @param advertiserId
     * @return
     */
    public AdvertiserBalance getByAdvertiserId(int advertiserId) {
        AdvertiserBalance ab = advertiserBalanceDao.read(advertiserId);
        if (ab == null) {
            // create if not exists
            ab = new AdvertiserBalance(advertiserId);
            advertiserBalanceDao.create(ab);
        }
        return ab;
    }

    /**
     * Get balance of advertisers
     *
     * @param advertiserIds
     * @return
     */
    public Map<Integer, Long> getBalances(List<Integer> advertiserIds) {
        return advertiserBalanceDao.getBalances(advertiserIds);
    }

    /**
     * 获取广告主最新的余额，包括未更新到DB的Cost
     *
     * @param advertiserId
     * @return
     */
    public long getLatestBalance(int advertiserId) {
        // 计算所有Campaign在时间段的Cost
        List<Integer> campIds = adCampaignService.listCampaignIdsByAdvertiserId(advertiserId);
        CpcTimeSegment currSegment = CpcTimeSegment.getCurrentSegment();
        int cost = 0;
        for (int campId : campIds) {
            cost += adCampaignBudgetService.getSegmentCampCostCache(campId, currSegment);
        }

        // 当前的Balance减去缓存的时间段内的Cost即为最新的Balance
        AdvertiserBalance ab = getByAdvertiserId(advertiserId);
        return ab.getBalance() - cost;
    }

    public long getBalanceSum() {
        return advertiserBalanceDao.getBalanceSum();
    }
}
