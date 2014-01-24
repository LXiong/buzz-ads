package com.buzzinate.buzzads.billing.test;

import java.util.Date;

import org.apache.thrift.transport.TTransportException;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.buzzinate.buzzads.analytics.AdClick;
import com.buzzinate.buzzads.core.service.CpcClickSetService;
import com.buzzinate.buzzads.enums.AdNetworkEnum;

public class SimulateAdClicks {

    private static ApplicationContext context = new ClassPathXmlApplicationContext(
                    new String[] {"application-billing.xml"});
    
    private static CpcClickSetService cpcClickSetService = (CpcClickSetService) 
                    context.getBean("cpcClickSetService");
    
    private SimulateAdClicks() { }

    
    /**
     * main function.
     * @param args args
     */
    public static void main(final String[] args) throws TTransportException {
        
        DateTime dt = new DateTime().minusMinutes(15);
        addClick(dt.toDate(), 1, "f8a4a53f-438a-4ffa-939f-7f313a7e2b05", "1234");
        

        addClick(new Date(), 1, "f8a4a53f-438a-4ffa-939f-7f313a7e2b05", "1234");
        addClick(new Date(), 1, "f8a4a53f-438a-4ffa-939f-7f313a7e2b05", "1234");
        addClick(new Date(), 1, "f8a4a53f-438a-4ffa-939f-7f313a7e2b05", "5678");
    }


    private static void addClick(Date time, int entryId, String uuid, String cookieId) {
        AdClick click = new AdClick();
        click.setAdEntryId(entryId);
        click.setNetwork(AdNetworkEnum.LEZHI);
        click.setCookieId(cookieId);
        click.setCreateAt(time);
        click.setPublisherUuid(uuid);
        click.setSourceUrl("http://www.bshare.cn/");
        cpcClickSetService.addCpcClick(click);
    }
    
    
    
}
