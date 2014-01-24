package com.buzzinate.buzzads.action.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.dao.ChanetCampaignDao;
import com.buzzinate.buzzads.dao.ChanetTradeDataDao;
import com.buzzinate.buzzads.domain.ChanetDTO;
import com.buzzinate.buzzads.domain.ChanetTradeData;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author John Chen
 *
 */

@Controller
@Scope("request")
public class AdNetworkChaNetManageAction extends ActionSupport {

    private static final long serialVersionUID = 1895620105335892747L;

    private List<ChanetDTO> chanetCampaigns;
    private List<ChanetTradeData> chanetTransactions;
    
    private long totalCommission;
    private long totalPrice;
    
    @Autowired
    private transient ChanetCampaignDao chanetCampaignDao;
    @Autowired
    private transient ChanetTradeDataDao chanetTradeDataDao;
    
    public String campaigns() {
        chanetCampaigns = chanetCampaignDao.getChanetCampaigns();
        if (chanetCampaigns == null) {
            chanetCampaigns = new ArrayList<ChanetDTO>();
        }
        
        return Action.SUCCESS;
    }
    
    public String transactions() {
        totalCommission = 0;
        totalPrice = 0;
    
        chanetTransactions = chanetTradeDataDao.listChanetTransactions();
        if (chanetTransactions ==  null) {
            chanetTransactions = new ArrayList<ChanetTradeData>();
        } else {
            // get totals
            for (ChanetTradeData ct : chanetTransactions) {
                totalCommission += ct.getCommission();
                totalPrice += ct.getTotalPrice();
            }
        }
        
        return Action.SUCCESS;
    }

    public List<ChanetDTO> getChanetCampaigns() {
        return chanetCampaigns;
    }
    public List<ChanetTradeData> getChanetTransactions() {
        return chanetTransactions;
    }
    
    public long getTotalCommission() {
        return totalCommission;
    }
    public long getTotalPrice() {
        return totalPrice;
    }
}
