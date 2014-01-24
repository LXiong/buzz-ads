package com.buzzinate.buzzads.billing.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.transport.TTransportException;
import org.quartz.Trigger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;


/**
 * Billing server class.
 * @author zyeming
 *
 */
public final class Server {

    private static ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {"application-billing.xml"});

    
    private static Log log = LogFactory.getLog(Server.class);

    private Server() { }

    
    /**
     * main function.
     * @param args args
     * @throws TTransportException TTransportException
     */
    public static void main(final String[] args) {
        
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.debug("Billing server interrupted: "  + e.getMessage());
            } 
        }
        
    }

}
