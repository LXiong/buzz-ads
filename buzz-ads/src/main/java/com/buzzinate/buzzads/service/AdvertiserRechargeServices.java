package com.buzzinate.buzzads.service;

import com.buzzinate.buzzads.common.service.AdsBaseService;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdvertiserBalanceService;
import com.buzzinate.buzzads.core.util.LogConstants;
import com.buzzinate.buzzads.domain.AlipayRecord;
import com.buzzinate.buzzads.enums.AdvertiserBillingType;
import com.buzzinate.common.util.DateTimeUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chris
 */
@Service
public class AdvertiserRechargeServices extends AdsBaseService {


    @Autowired
    private AdvertiserBalanceService advertiserBalanceService;
    @Autowired
    private AdCampaignService adCampaignService;

    public void updateBillingAndBalanceForRecharge(AlipayRecord alipayRecord) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("advertierId", String.valueOf(alipayRecord.getAdvertiserId()));
        param.put("tradeNo", alipayRecord.getTradeNo());
        param.put("rechargeAmount", String.valueOf(alipayRecord.getAmountValue()));
        advertiserBalanceService.updateBillingAndBalanceForCredits(alipayRecord.getAdvertiserId(), 
                        alipayRecord.getAmount(), alipayRecord.getTradeNo(), 
                        DateTimeUtil.getCurrentDateDay(), AdvertiserBillingType.REFILL_RECHARGE);

        //log operation
        addOperationLog(LogConstants.OPTYPE_ADVERTISER_RECHARGE, LogConstants.OBJNAME_ADVERTISER_RECHARGE,
                String.valueOf(alipayRecord.getAdvertiserId()), String.valueOf(alipayRecord.getId()), param);

        adCampaignService.checkAndResetSuspendedCamp(alipayRecord.getAdvertiserId());
    }


}
