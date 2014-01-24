package com.buzzinate.buzzads.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import com.buzzinate.common.util.DateTimeUtil;

public class AppTest {

    /**
     * @param args
     * @throws UnknownHostException 
     */
    public static void main(String[] args) throws UnknownHostException {
        System.out.println(DateTimeUtil.getCurrentDate().after(DateTimeUtil.getYestoday()));
    }

}
