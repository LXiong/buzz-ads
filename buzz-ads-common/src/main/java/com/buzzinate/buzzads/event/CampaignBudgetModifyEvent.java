package com.buzzinate.buzzads.event;

import java.io.Serializable;
import java.util.List;

/**
 * 广告预算变更事件
 * 
 * @author yeming
 *
 */
public class CampaignBudgetModifyEvent implements AdEvent, Serializable {

    private static final long serialVersionUID = -7871274728603281385L;

    private List<Integer> campaignIds;

    public List<Integer> getCampaignIds() {
        return campaignIds;
    }

    public void setCampaignIds(List<Integer> campaignIds) {
        this.campaignIds = campaignIds;
    }
    
}
