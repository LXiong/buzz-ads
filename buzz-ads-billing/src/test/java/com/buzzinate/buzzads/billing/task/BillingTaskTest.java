package com.buzzinate.buzzads.billing.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;
import org.unitils.inject.annotation.InjectInto;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import com.buzzinate.buzzads.billing.dao.CpcStatsUpdateDao;
import com.buzzinate.buzzads.core.bean.CpcTimeSegment;
import com.buzzinate.buzzads.core.service.AdCampaignBudgetService;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdEntryService;
import com.buzzinate.buzzads.core.service.AdOrderService;
import com.buzzinate.buzzads.core.service.AdvertiserBalanceService;
import com.buzzinate.buzzads.core.service.CpcClickSetService;
import com.buzzinate.buzzads.core.service.CpcClickSetService.CpcMetaTuple;
import com.buzzinate.buzzads.core.service.EventServices;
import com.buzzinate.buzzads.core.service.PublisherContactService;
import com.buzzinate.buzzads.core.service.SiteService;
import com.buzzinate.buzzads.domain.AdCampBudget;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdEntry;
import com.buzzinate.buzzads.domain.AdOrder;
import com.buzzinate.buzzads.domain.AdvertiserBalance;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.common.util.DateTimeUtil;

/**
 * 
 * @author zyeming
 *
 */
public class BillingTaskTest extends UnitilsTestNG {

    @TestedObject
    private CpcStatsTask cpcStatsTask;
    

    @InjectInto(property = "adEntryService") 
    private Mock<AdEntryService> adEntryService; 
    
    @InjectInto(property = "adOrderService") 
    private Mock<AdOrderService> adOrderService; 
    
    @InjectInto(property = "adCampaignService") 
    private Mock<AdCampaignService> adCampaignService; 
    
    @InjectInto(property = "adCampaignBudgetService") 
    private Mock<AdCampaignBudgetService> adCampaignBudgetService; 
    
    @InjectInto(property = "advertiserBalanceService") 
    private Mock<AdvertiserBalanceService> advertiserBalanceService;
    
    @InjectInto(property = "eventServices") 
    private Mock<EventServices> eventServices;
    
    @InjectInto(property = "cpcStatsUpdateDao") 
    private Mock<CpcStatsUpdateDao> cpcStatsUpdateDao; 
    
    @InjectInto(property = "cpcClickSetService") 
    private Mock<CpcClickSetService> cpcClickSetService;
    
    @InjectInto(property = "publisherContactService") 
    private Mock<PublisherContactService> publisherContactService;
    
    @BeforeMethod
    public void initialize() { 
        prepareAdsMock();
    } 
    
    
    @Test
    public void testProcessFinishedCpcSegment() { 
        CpcMetaTuple t1 = new CpcMetaTuple(1, "f8a4a53f-438a-4ffa-939f-7f313a7e2b05", AdNetworkEnum.LEZHI);
        CpcMetaTuple t2 = new CpcMetaTuple(2, "f8a4a53f-438a-4ffa-939f-7f313a7e2b05", AdNetworkEnum.LEZHI);
        
        List<CpcMetaTuple> metaList = new ArrayList<CpcMetaTuple>();
        metaList.add(t1);
        metaList.add(t2);
        cpcClickSetService.returns(metaList).getMetaList(null);
        
        cpcClickSetService.returns(1).getCpcClicks(null, t1);
        cpcClickSetService.returns(2).getCpcClicks(null, t2);
        
        CpcTimeSegment prevSegment = CpcTimeSegment.getPreviousSegment();
        cpcStatsTask.processFinishedCpcSegment(prevSegment);
    }
    
    
    
    public void testProcessCurrentCpcSegment() { 
        CpcMetaTuple t1 = new CpcMetaTuple(1, "f8a4a53f-438a-4ffa-939f-7f313a7e2b05", AdNetworkEnum.LEZHI);
        CpcMetaTuple t2 = new CpcMetaTuple(2, "f8a4a53f-438a-4ffa-939f-7f313a7e2b05", AdNetworkEnum.LEZHI);
        
        List<CpcMetaTuple> metaList = new ArrayList<CpcMetaTuple>();
        metaList.add(t1);
        metaList.add(t2);
        cpcClickSetService.returns(metaList).getMetaList(null);
        
        cpcClickSetService.returns(1).getCpcClicks(null, t1);
        cpcClickSetService.returns(2).getCpcClicks(null, t2);
        
        
        AdCampBudget campBudget = new AdCampBudget();
        campBudget.setAdvertiserId(1);
        campBudget.setMaxBudgetDay(200);
        campBudget.setMaxBudgetTotal(500);
        adCampaignBudgetService.returns(campBudget).getCampBudget(1);
        adCampaignBudgetService.returns(campBudget).getCampBudget(2);
        adCampaignBudgetService.returns(campBudget).getDayCampBudget(2, DateTimeUtil.getCurrentDate());
        
        CpcTimeSegment prevSegment = CpcTimeSegment.getPreviousSegment();
        cpcStatsTask.processCurrentCpcSegment(prevSegment, new HashSet<Integer>());
    }
    
    
    private void prepareAdsMock() {
        AdEntry e1 = new AdEntry();
        e1.setAdvertiserId(2);
        e1.setOrderId(1);
        e1.setCampaignId(1);
        adEntryService.returns(e1).getEntryById(1);
        
        AdOrder o1 = new AdOrder();
        o1.setAdvertiserId(2);
        o1.setCampaignId(1);
        o1.setBidPrice(80);
        adOrderService.returns(o1).getOrderById(1);
        
        AdCampaign c1 = new AdCampaign();
        c1.setId(1);
        c1.setAdvertiserId(2);
        adCampaignService.returns(c1).getAdCampaignById(1);
        
        
        AdEntry e2 = new AdEntry();
        e2.setAdvertiserId(2);
        e2.setOrderId(2);
        e2.setCampaignId(2);
        adEntryService.returns(e2).getEntryById(2);
        
        AdOrder o2 = new AdOrder();
        o2.setAdvertiserId(2);
        o2.setCampaignId(2);
        o2.setBidPrice(150);
        adOrderService.returns(o1).getOrderById(2);
        
        AdCampaign c2 = new AdCampaign();
        c2.setId(2);
        c2.setAdvertiserId(2);
        adCampaignService.returns(c1).getAdCampaignById(2);
        
        AdvertiserBalance advertiserBalance = new AdvertiserBalance();
        advertiserBalance.setAdvertiserId(2);
        advertiserBalance.setBalance(1000);
        advertiserBalance.setCreditsTotal(1000);
        advertiserBalanceService.returns(advertiserBalance).getByAdvertiserId(2);
    }
    
}
