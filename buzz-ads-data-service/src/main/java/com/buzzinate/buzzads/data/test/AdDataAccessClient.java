package com.buzzinate.buzzads.data.test;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.thrift.protocol.TBinaryProtocol;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.data.converter.AdItemConverter;
import com.buzzinate.buzzads.data.thrift.TAdCriteria;
import com.buzzinate.buzzads.data.thrift.TAdDataAccessServices;
import com.buzzinate.buzzads.data.thrift.TAdItem;
import com.buzzinate.buzzads.data.thrift.TAdNetworkEnum;
import com.buzzinate.buzzads.data.thrift.TAdStats;
import com.buzzinate.buzzads.data.thrift.TAdStatsCriteria;
import com.buzzinate.buzzads.data.thrift.TCampaignBudget;
import com.buzzinate.buzzads.data.thrift.TFrozenChannel;
import com.buzzinate.buzzads.data.thrift.TPagination;
import com.buzzinate.buzzads.data.thrift.TPublisherContactInfo;
import com.buzzinate.buzzads.domain.AdItem;
import com.buzzinate.buzzads.enums.AdEntryTypeEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ClientBuilder;
import com.twitter.finagle.thrift.ThriftClientFramedCodec;
import com.twitter.finagle.thrift.ThriftClientRequest;
import com.twitter.util.Duration;
import com.twitter.util.FutureEventListener;

/**
 * Fot test
 * 
 * @author zyeming
 * 
 */
public final class AdDataAccessClient {

    private AdDataAccessClient() {
    }

    public static void main(String[] args) {

        Service<ThriftClientRequest, byte[]> service = ClientBuilder.safeBuild(ClientBuilder.get()
                .hosts(new InetSocketAddress(ConfigurationReader.getInt("data.access.server.port")))
                .codec(ThriftClientFramedCodec.get()).
                // Retry number, only applies to recoverable errors
                retries(3).requestTimeout(Duration.fromSeconds(50)).
                // Connection pool core size
                hostConnectionCoresize(100).
                // Connection pool max size
                hostConnectionLimit(600).
                // The amount of idle time for which a connection is cached in pool.
                hostConnectionIdleTime(Duration.fromSeconds(30)).
                // The maximum number of connection requests that are queued when the
                // connection concurrency exceeds the connection limit.
                hostConnectionMaxWaiters(500));

        TAdDataAccessServices.FinagledClient client = new TAdDataAccessServices.FinagledClient(service,
                new TBinaryProtocol.Factory());

        client.findPublisherContact(1).addEventListener(new FutureEventListener<TPublisherContactInfo>() {
            @Override
            public void onFailure(Throwable t) {
                System.out.println("Exception! " + t.toString());
            }

            @Override
            public void onSuccess(TPublisherContactInfo pci) {
                System.out.println("Sucecss! Name: " + pci.toString());
            }
        });

        // test findAdItems
        TAdCriteria criteria = new TAdCriteria.Builder().build();
        TPagination pagination = new TPagination.Builder().start(Short.valueOf("0")).count(Short.valueOf("10")).build();
        client.findAdItems(criteria, pagination).addEventListener(new FutureEventListener<List<TAdItem>>() {
            @Override
            public void onFailure(Throwable arg0) {
                System.out.println("Exception! " + arg0.toString());
            }

            @Override
            public void onSuccess(List<TAdItem> arg0) {
                for (TAdItem item : arg0) {
                    System.out.println(AdItemConverter.fromThrift(item));
                }
            }
        });
        
        AdItem it = AdItemConverter.fromThrift(client.findAdItems(criteria, pagination).get().get(0));
        it.setDestination("www.test.cn");
        
        TAdItem tt = AdItemConverter.toThrift(it);
        client.updateAdItem(tt).addEventListener(new FutureEventListener<Void>() {
            @Override
            public void onFailure(Throwable arg0) {
                System.out.println("Exception! " + arg0.toString());
            }

            @Override
            public void onSuccess(Void arg0) {
                System.out.println("update success");
            }
        });
        
        // test updateAdItems
        AdItem item = new AdItem();
        item.setEntryId(1);
        item.setOrderId(1);
        item.setCampaignId(1);
        item.setAdvertiserId(1);
        item.setStatus(AdStatusEnum.ENABLED);
        item.setTitle("test");
        item.setResourceType(AdEntryTypeEnum.IMAGE);
        item.setResourceUrl("test");
        item.setKeywords("test");

        client.updateAdItem(AdItemConverter.toThrift(item)).addEventListener(new FutureEventListener<Void>() {
            @Override
            public void onFailure(Throwable arg0) {
                System.out.println("Exception! " + arg0.toString());
            }

            @Override
            public void onSuccess(Void arg0) {
                System.out.println("update success");
            }
        });

        List<Integer> campaignIds = new ArrayList<Integer>();
        campaignIds.add(1);
        campaignIds.add(2);
        client.findCampaignBudgets(campaignIds).addEventListener(new FutureEventListener<List<TCampaignBudget>>() {
            @Override
            public void onFailure(Throwable arg0) {
                System.out.println("Exception! " + arg0.toString());
            }

            @Override
            public void onSuccess(List<TCampaignBudget> arg0) {
                for (TCampaignBudget cb : arg0)
                    System.out.println(cb);
            }
        });
        Set<TAdNetworkEnum> value  = new HashSet<TAdNetworkEnum>();
        value.add(TAdNetworkEnum.LEZHI);
        value.add(TAdNetworkEnum.BSHARE);
        TAdStatsCriteria criteria1 = new TAdStatsCriteria.Builder().network(value).build();
        TAdStatsCriteria criteria2 = new TAdStatsCriteria.Builder().build();
        TPagination pagination1 = new TPagination.Builder().start(Short.valueOf("0")).count(Short.valueOf("200")).build();
        
        client.findAdStats(criteria1, pagination).addEventListener(new FutureEventListener<List<TAdStats>>() {
            @Override
            public void onFailure(Throwable arg0) {
                System.out.println("Exception! " + arg0.toString());
            }

            @Override
            public void onSuccess(List<TAdStats> arg0) {
                for (TAdStats cb : arg0)
                    System.out.println(cb);
            }
        });
        
        client.findAdStats(criteria2, pagination1).addEventListener(new FutureEventListener<List<TAdStats>>() {
            @Override
            public void onFailure(Throwable arg0) {
                System.out.println("Exception! " + arg0.toString());
            }

            @Override
            public void onSuccess(List<TAdStats> arg0) {
                for (TAdStats cb : arg0)
                    System.out.println(cb);
            }
        });
        
        client.findAllFrozenList().addEventListener(new FutureEventListener<List<TFrozenChannel>>() {
            @Override
            public void onFailure(Throwable t) {
                System.out.println("Exception! " + t.toString());
            }

            @Override
            public void onSuccess(List<TFrozenChannel> arg0) {
                System.out.println("info size" + arg0.size());
            }
        });
    }

}
