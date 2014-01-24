package com.buzzinate.buzzads.billing.test;

import org.apache.thrift.transport.TTransportException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.buzzinate.buzzads.billing.task.CpmStatsTask;

/**
 * 
 * @author zyeming
 *
 */
public final class CpmStatsTaskMain {

    private static ApplicationContext context = new ClassPathXmlApplicationContext(
                    new String[] {"application-billing.xml"});
    
    private CpmStatsTaskMain() { }

    
    /**
     * main function.
     * @param args args
     */
    public static void main(final String[] args) throws TTransportException {
        CpmStatsTask cpmStatsTask = (CpmStatsTask) context.getBean("cpmStatsTask");
        cpmStatsTask.start();
    }
    
}
