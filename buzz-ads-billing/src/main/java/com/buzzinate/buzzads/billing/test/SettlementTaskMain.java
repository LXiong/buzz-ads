package com.buzzinate.buzzads.billing.test;

import org.apache.thrift.transport.TTransportException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.buzzinate.buzzads.billing.task.SettlementTask;

/**
 * 
 * @author zyeming
 *
 */
public final class SettlementTaskMain {

    private static ApplicationContext context = new ClassPathXmlApplicationContext(
                    new String[] {"application-billing.xml"});

    private SettlementTaskMain() { }

    
    /**
     * main function.
     * @param args args
     */
    public static void main(final String[] args) throws TTransportException {
        SettlementTask settlementTask = (SettlementTask) context.getBean("settlementTask");
        settlementTask.start();
    }
    
}
