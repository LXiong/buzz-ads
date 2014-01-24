package com.buzzinate.buzzads.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.buzzinate.buzzads.core.service.AdCampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.service.AdsBaseService;
import com.buzzinate.buzzads.core.service.AdvertiserBalanceService;
import com.buzzinate.buzzads.core.service.AdvertiserBillingService;
import com.buzzinate.buzzads.core.util.LogConstants;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.Adjustment;
import com.buzzinate.buzzads.domain.AdvertiserBilling;
import com.buzzinate.buzzads.enums.AdvertiserBillingType;
import com.buzzinate.common.util.DateTimeUtil;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : chris
 * @since Date: 13-3-28 Time: 下午4:43
 */
@Service
public class AdjustmentServices extends AdsBaseService {
    @Autowired
    private AdvertiserBalanceService advertiserBalanceService;
    @Autowired
    private AdvertiserBillingService advertiserBillingService;
    @Autowired
    private AdCampaignService adCampaignService;

    @Transactional(value = "buzzads", readOnly = false)
    public void addAdjustment(Adjustment adjustment) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("adminId", String.valueOf(adjustment.getAdminId()));
        param.put("receiverId", String.valueOf(adjustment.getReceiverId()));
        param.put("receiveAmount", String.valueOf(adjustment.getReceiveAmount()));
        param.put("comment", adjustment.getDetail());
        int billingId = advertiserBalanceService.updateBillingAndBalanceForCredits(
                        adjustment.getReceiverId(), adjustment.getAmount(), adjustment.getDetail(), 
                        DateTimeUtil.getCurrentDateDay(), AdvertiserBillingType.ADJUSTMENT);
        addOperationLog(LogConstants.OPTYPE_ADJUSTMENT_CREATE, 
                        LogConstants.OPNAME_ADJUSTMENT_CREATE, 
                        String.valueOf(adjustment.getReceiverId()), String.valueOf(billingId), param);
        adCampaignService.checkAndResetSuspendedCamp(adjustment.getReceiverId());
    }

    public List<Adjustment> listAdjustment(Pagination pagination) {
        List<AdvertiserBilling> billings = advertiserBillingService.listBillingByType(
                        Arrays.asList(AdvertiserBillingType.ADJUSTMENT), pagination);
        List<Adjustment> adjustments = new ArrayList<Adjustment>(billings.size());
        for (AdvertiserBilling billing : billings) {
            Adjustment adjustment = new Adjustment();
            adjustment.setReceiverId(billing.getAdvertiserId());
            adjustment.setAmount(billing.getCredits());
            adjustment.setUpdateTime(billing.getUpdateAt());
            adjustment.setDetail(billing.getComment());
            adjustments.add(adjustment);
        }
        return adjustments;
    }

}
