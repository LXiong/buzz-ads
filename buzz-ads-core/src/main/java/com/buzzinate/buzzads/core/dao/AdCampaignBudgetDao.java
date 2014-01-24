package com.buzzinate.buzzads.core.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.domain.AdCampBudget;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2013-2-27
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class AdCampaignBudgetDao extends AdsDaoBase<AdCampBudget, Integer> {

    public AdCampaignBudgetDao() {
        super(AdCampBudget.class, "campaignId");
    }
    
}
