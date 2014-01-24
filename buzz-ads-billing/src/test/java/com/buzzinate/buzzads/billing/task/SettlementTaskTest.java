package com.buzzinate.buzzads.billing.task;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;
import org.unitils.inject.annotation.InjectInto;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import com.buzzinate.buzzads.core.bean.PublisherSettlementStats;
import com.buzzinate.buzzads.core.service.AdCampaignBudgetService;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdvertiserBalanceService;
import com.buzzinate.buzzads.core.service.PublisherSettlementService;
import com.buzzinate.buzzads.core.service.SiteService;
import com.buzzinate.buzzads.core.service.StatsPublisherServices;
import com.buzzinate.buzzads.domain.AdCampBudget;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdvertiserBalance;
import com.buzzinate.buzzads.domain.Site;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.UuidUtil;

/**
 * 
 * @author zyeming
 *
 */
public class SettlementTaskTest extends UnitilsTestNG {

    @TestedObject
    private SettlementTask settlementTask;
    
    
    @InjectInto(property = "adCampaignService") 
    private Mock<AdCampaignService> adCampaignService; 
    
    @InjectInto(property = "adCampaignBudgetService") 
    private Mock<AdCampaignBudgetService> adCampaignBudgetService; 
    
    @InjectInto(property = "advertiserBalanceService") 
    private Mock<AdvertiserBalanceService> advertiserBalanceService;
    
    @InjectInto(property = "statsPublisherServices") 
    private Mock<StatsPublisherServices> statsPublisherServices;
    
    @InjectInto(property = "publisherSettlementService") 
    private Mock<PublisherSettlementService> publisherSettlementService; 
    
    @InjectInto(property = "siteService") 
    private Mock<SiteService> siteService; 
    
    
    @BeforeMethod
    public void initialize() { 
        prepareAdsMock();
    } 
    
    
    @Test
    public void testUpdatePublisherSettlement() { 
        List<PublisherSettlementStats> statsList = new ArrayList<PublisherSettlementStats>();
        PublisherSettlementStats s1 = createPublisherSettlementStats(11, 1100, 2, 800);
        statsList.add(s1);
        statsPublisherServices.returns(statsList).getPublisherDailyStatsForSettlement(null);
        
        settlementTask.updatePublisherSettlement(DateTimeUtil.getCurrentDateDay());
        
        publisherSettlementService.assertInvoked().insertOrUpdate(2, s1);
    }
    
    
    @Test
    public void testResetSuspendedCampaigns() { 
        AdCampBudget cb1 = new AdCampBudget(1, 2);
        cb1.setDateDay(DateTimeUtil.getCurrentDateDay());
        cb1.setBudgetDay(300);
        cb1.setBudgetTotal(500);
        cb1.setMaxBudgetDay(500);
        cb1.setMaxBudgetTotal(1000);
        adCampaignBudgetService.returns(cb1).getLatestCampBudget(1);
        
        AdCampBudget cb2 = new AdCampBudget(2, 2);
        cb2.setDateDay(DateTimeUtil.getCurrentDateDay());
        cb2.setBudgetDay(800);
        cb2.setBudgetTotal(1500);
        cb2.setMaxBudgetDay(500);
        cb2.setMaxBudgetTotal(1000);
        adCampaignBudgetService.returns(cb2).getLatestCampBudget(2);
        
        settlementTask.resetSuspendedCampaigns();
        
        adCampaignService.assertInvoked().updateAdCampaignStatus(1, AdStatusEnum.ENABLED);
        adCampaignService.assertNotInvoked().updateAdCampaignStatus(2, AdStatusEnum.ENABLED);
    }
    
    
    private PublisherSettlementStats createPublisherSettlementStats(
                    int cpcNo, int cpcComm, int cpsNo, int cpsComm) {
        PublisherSettlementStats s = new PublisherSettlementStats(
                        UuidUtil.uuidToByteArray("f8a4a53f-438a-4ffa-939f-7f313a7e2b05"), 
                        DateTimeUtil.getCurrentDateDay());
        s.setCpcNo(cpcNo);
        s.setCpcCommission(cpcComm);
        s.setCpsNo(cpsNo);
        s.setCpsCommission(cpsComm);
        s.setCommission(s.getCpcCommission() + s.getCpsCommission());
        return s;
    }
    
    
    private void prepareAdsMock() {
        AdCampaign c1 = new AdCampaign();
        c1.setId(1);
        c1.setAdvertiserId(2);
        adCampaignService.returns(c1).getAdCampaignById(1);
        
        AdCampaign c2 = new AdCampaign();
        c2.setId(2);
        c2.setAdvertiserId(2);
        adCampaignService.returns(c1).getAdCampaignById(2);
        
        AdvertiserBalance advertiserBalance = new AdvertiserBalance();
        advertiserBalance.setAdvertiserId(2);
        advertiserBalance.setBalance(1000);
        advertiserBalance.setCreditsTotal(1000);
        advertiserBalanceService.returns(advertiserBalance).getByAdvertiserId(2);
        advertiserBalanceService.returns(1000L).getLatestBalance(2);
        
        List<AdCampaign> camps = new ArrayList<AdCampaign>();
        camps.add(c1);
        camps.add(c2);
        adCampaignService.returns(camps).listCampaignsByStatus(null);
        
        Site s = new Site();
        s.setUserId(2);
        siteService.returns(s).getUuidSiteByUuid(null);
    }
    
}
