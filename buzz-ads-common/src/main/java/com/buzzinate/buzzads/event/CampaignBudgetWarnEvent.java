package com.buzzinate.buzzads.event;

import java.io.Serializable;
import java.util.List;

/**
 * 广告预算预警事件
 * 
 * @author martin
 *
 */
public class CampaignBudgetWarnEvent implements AdEvent, Serializable {
    
    private static final long serialVersionUID = -4862215095254203707L;
    
    private List<Integer> campaignIds;

    public List<Integer> getCampaignIds() {
        return campaignIds;
    }

    public void setCampaignIds(List<Integer> campaignIds) {
        this.campaignIds = campaignIds;
    }
    
}