package com.buzzinate.advertise.test.finagle;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.protocol.TBinaryProtocol;

import com.buzzinate.buzzads.data.thrift.TAdCriteria;
import com.buzzinate.buzzads.data.thrift.TAdDataAccessServices;
import com.buzzinate.buzzads.data.thrift.TAdItem;
import com.buzzinate.buzzads.data.thrift.TAdStats;
import com.buzzinate.buzzads.data.thrift.TAdStatsCriteria;
import com.buzzinate.buzzads.data.thrift.TCampaignBudget;
import com.buzzinate.buzzads.data.thrift.TPagination;
import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ClientBuilder;
import com.twitter.finagle.thrift.ThriftClientFramedCodec;
import com.twitter.finagle.thrift.ThriftClientRequest;
import com.twitter.util.Duration;
import com.twitter.util.FutureEventListener;

public final class AdDataAccessClient {
    public static void main(String[] args) {

        Service<ThriftClientRequest, byte[]> service = ClientBuilder.safeBuild(
                        ClientBuilder.get().
                        hosts(new InetSocketAddress(32125)).
                        codec(ThriftClientFramedCodec.get()).
                        // Retry number, only applies to recoverable errors
                        retries(3).
                        requestTimeout(Duration.fromSeconds(5)).
                        // Connection pool core size
                        hostConnectionCoresize(100).
                        // Connection pool max size
                        hostConnectionLimit(600).
                        // The amount of idle time for which a connection is cached in pool.
                        hostConnectionIdleTime(Duration.fromSeconds(30)).
                        // The maximum number of connection requests that are queued when the 
                        // connection concurrency exceeds the connection limit.
                        hostConnectionMaxWaiters(500));

        TAdDataAccessServices.FinagledClient client = new TAdDataAccessServices.FinagledClient(
                        service, new TBinaryProtocol.Factory());
        TAdCriteria.Builder cBuilder = new TAdCriteria.Builder();
        TAdCriteria criteria = cBuilder.build();
        TPagination.Builder pBuilder = new TPagination.Builder();
        pBuilder.start((short)(0));
        pBuilder.count((short)200);
        TPagination pagination = pBuilder.build();
        //Duration duration = Duration.apply(10, TimeUnit.SECONDS);
        
        List<TAdItem> items = client.findAdItems(criteria, pagination).get();//.get(duration);
        for(TAdItem item : items){
    		System.out.println("entry id => " + item.getEntryId());
    	}
        System.out.println("find " + items.size() +" items");
        
        
        List<Integer> campaignIds = new ArrayList();
        for(int i = 1; i < 1000; i++){
        	campaignIds.add(i);
        }
        List<TCampaignBudget> budgets = client.findCampaignBudgets(campaignIds).get();
        for(TCampaignBudget budget : budgets){
        	System.out.println("budget campaign id => " + budget.getCampaignId());
        }
        System.out.println("find " + budgets.size() + " budgets");
        
        TAdStatsCriteria.Builder statBuilder = new TAdStatsCriteria.Builder();
        TAdStatsCriteria statCriteria = statBuilder.build();
        List<TAdStats> stats = client.findAdStats(statCriteria, pagination).get();
        for(TAdStats stat : stats){
        	System.out.println("ad stat id => " + stat.getEntryId());
        }
        System.out.println("find " + stats.size() + " stats");
      //  client.
//        
//        client.findAdItems(criteria, pagination).addEventListener(new FutureEventListener<List<TAdItem>>() {
//            @Override
//            public void onFailure(Throwable t) {
//                System.out.println("Exception! " + t.toString());
//            }
//
//            @Override
//            public void onSuccess(List<TAdItem> items) {
//            	for(TAdItem item : items){
//            		System.out.println(item.getEntryId());
//            	}
//                System.out.println("Sucecss! Name: " + items.size());
//            }
//        });
     
     
    }
    
}
