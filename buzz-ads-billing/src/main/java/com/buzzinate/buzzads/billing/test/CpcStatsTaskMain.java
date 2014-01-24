package com.buzzinate.buzzads.billing.test;

import org.apache.thrift.transport.TTransportException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.buzzinate.buzzads.billing.task.CpcStatsTask;

/**
 * 
 * @author zyeming
 *
 */
public final class CpcStatsTaskMain {

    private static ApplicationContext context = new ClassPathXmlApplicationContext(
                    new String[] {"application-billing.xml"});
    
    private CpcStatsTaskMain() { }

    
    /**
     * main function.
     * @param args args
     */
    public static void main(final String[] args) throws TTransportException {
        CpcStatsTask cpcStatsTask = (CpcStatsTask) context.getBean("cpcStatsTask");
        cpcStatsTask.start();
    }
    
}
