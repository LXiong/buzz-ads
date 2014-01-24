package com.buzzinate.buzzads.analytics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;
import org.unitils.database.annotations.Transactional;

import com.buzzinate.buzzads.analytics.processor.StatisticProcessor;
import com.buzzinate.buzzads.domain.AdDetailCps;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.buzzads.enums.TradeConfirmEnum;
import com.buzzinate.common.util.DateTimeUtil;

@Test(groups = { "dao", "mysql" })
//@SpringApplicationContext("test_applicationAnalytics.xml")
@Transactional
public class StatisticsProcessorTest extends UnitilsTestNG {
    
//    @SpringBeanByName
//    private StatisticProcessor statisticProcessor;
    
    @Test
    public void testShowUp() throws InterruptedException {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                        new String[]{"application-analytics.xml"});
        StatisticProcessor statisticProcessor = (StatisticProcessor) context.getBean("statisticProcessor");
                
        AdShowUps showUps = new AdShowUps();
        showUps.setNetwork(AdNetworkEnum.LEZHI);
        List<Long> entryIds = new ArrayList<Long>();
        entryIds.add(1L);
        entryIds.add(2L);
        showUps.setAdEntryIds(entryIds);
        showUps.setCookieId("ABCDEFG");
        showUps.setPublisherUuid("f8a4a53f-438a-4ffa-939f-7f313a7e2b05");
        showUps.setSourceUrl("http://www.bshare.cn/");
        showUps.setIp("8.8.8.8");
        showUps.setCreateAt(new Date());
        statisticProcessor.addStats(showUps);
        statisticProcessor.addStats(showUps);
        
        AdClick click = new AdClick();
        click.setNetwork(AdNetworkEnum.LEZHI);
        click.setAdEntryId(1);
        click.setCookieId("ABCDEFG");
        click.setPublisherUuid("f8a4a53f-438a-4ffa-939f-7f313a7e2b06");
        click.setSourceUrl("http://www.bshare.cn/");
        click.setIp("8.8.8.8");
        click.setCreateAt(new Date());
        statisticProcessor.addStats(click);
        
        click = new AdClick();
        click.setNetwork(AdNetworkEnum.LEZHI);
        click.setAdEntryId(2);
        click.setCookieId("ABCDEFG");
        click.setPublisherUuid("f8a4a53f-438a-4ffa-939f-7f313a7e2b06");
        click.setSourceUrl("http://www.bshare.cn/");
        click.setIp("8.8.8.8");
        click.setCreateAt(new Date());
        statisticProcessor.addStats(click);
        
//        AdDetailCps cps = new AdDetailCps();
//        cps.setUuid("f8a4a53f-438a-4ffa-939f-7f313a7e2b07");
//        cps.setAdEntryId(2);
//        cps.setAdOrderId(2);
//        cps.setComm(111);
//        cps.setPubComm(89);
//        cps.setTradeTime(DateTimeUtil.convertDate("2013-2-1"));
//        cps.setNetwork(AdNetworkEnum.LEZHI);
//        cps.setStatus(TradeConfirmEnum.NO_CONFIRM);
//        statisticProcessor.addStats(cps);
//        
//        cps = new AdDetailCps();
//        cps.setUuid("f8a4a53f-438a-4ffa-939f-7f313a7e2b07");
//        cps.setAdEntryId(2);
//        cps.setAdOrderId(2);
//        cps.setComm(65);
//        cps.setPubComm(51);
//        cps.setTradeTime(DateTimeUtil.convertDate("2013-2-1"));
//        cps.setConfirmTime(DateTimeUtil.convertDate("2013-3-10"));
//        cps.setNetwork(AdNetworkEnum.LEZHI);
//        cps.setStatus(TradeConfirmEnum.CONFIRM_OK);
//        statisticProcessor.addStats(cps);
        
        while (true) {
            Thread.sleep(100);
        }
    }

}
