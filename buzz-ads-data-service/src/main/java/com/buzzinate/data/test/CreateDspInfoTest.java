package com.buzzinate.data.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.buzzinate.adx.entity.AdInfo;
import com.buzzinate.adx.enums.BidStatus;
import com.buzzinate.adx.message.WinnerMessage;
import com.buzzinate.buzzads.analytics.AdAnalyticsConstants;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.common.util.kestrel.KestrelClient;


public class CreateDspInfoTest {
    private static ThreadLocal<KestrelClient> threadLocal = new ThreadLocal<KestrelClient>();
    private static String queueName = null;
    
    private static void init() {
        if (threadLocal.get() == null) {
            KestrelClient kestrelClient = new KestrelClient(
                    ConfigurationReader.getString("analytics.kestrel.ips"),
                    ConfigurationReader.getInt("analytics.kestrel.pool.size"));
            threadLocal.set(kestrelClient);
        }

        if (queueName == null) {
//            queueName = ConfigurationReader.getString("adx.kestrel.data.queue.name");
            queueName = AdAnalyticsConstants.ADXWINMSG_QUEUE;
        }
    }
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        init();
        ExecutorService excutorService = Executors.newFixedThreadPool(20);
        KestrelClient kestrelClient  = threadLocal.get();
        
        CreateDataThread thread1 = new CreateDataThread(kestrelClient, queueName, BidStatus.SUCCESS);
        CreateDataThread thread2 = new CreateDataThread(kestrelClient, queueName, BidStatus.SUCCESS);
        CreateDataThread thread3 = new CreateDataThread(kestrelClient, queueName, BidStatus.SUCCESS);
        CreateDataThread thread4 = new CreateDataThread(kestrelClient, queueName, BidStatus.SUCCESS);
        excutorService.execute(thread1);
        excutorService.execute(thread2);
        excutorService.execute(thread3);
        excutorService.execute(thread4);
        
    }

}

class CreateDataThread implements Runnable {
    private KestrelClient kestrelClient;
    private String queueName;
    private BidStatus status;
    
    public CreateDataThread(KestrelClient kestrelClient, String queueName, BidStatus status) {
        super();
        this.kestrelClient = kestrelClient;
        this.queueName = queueName;
        this.status = status;
    }
    
    static List<AdInfo> winners = new ArrayList<AdInfo>();
    static List<AdInfo> losers = new ArrayList<AdInfo>();
    WinnerMessage winMsg = new WinnerMessage(winners, losers, null);
    static {
        
        AdInfo adInfo1 = new AdInfo();
        adInfo1.setDspId(1);
        adInfo1.setStatus(BidStatus.SUCCESS);
        winners.add(adInfo1);
        AdInfo adInfo2 = new AdInfo();
        adInfo2.setDspId(2);
        adInfo2.setStatus(BidStatus.FAILURE);
        winners.add(adInfo2);
        AdInfo adInfo3 = new AdInfo();
        adInfo3.setDspId(3);
        adInfo3.setStatus(BidStatus.SEGEMNT_FAIL);
        winners.add(adInfo3);
        AdInfo adInfo4 = new AdInfo();
        adInfo4.setDspId(4);
        adInfo4.setStatus(BidStatus.NO_BID);
        winners.add(adInfo4);
        AdInfo adInfo5 = new AdInfo();
        adInfo5.setDspId(5);
        adInfo5.setStatus(BidStatus.TIMEOUT);
        winners.add(adInfo5);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            kestrelClient.put(queueName, winMsg);
//            System.out.println(kestrelClient.get(queueName));
        }
    }
    
}
