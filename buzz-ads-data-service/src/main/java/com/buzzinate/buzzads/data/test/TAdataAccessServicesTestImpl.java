package com.buzzinate.buzzads.data.test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;

import com.buzzinate.buzzads.core.service.AdCampaignBudgetService;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdEntryService;
import com.buzzinate.buzzads.core.service.AdOrderService;
import com.buzzinate.buzzads.core.service.AdvertiserBalanceService;
import com.buzzinate.buzzads.core.service.CampDayBudgetService;
import com.buzzinate.buzzads.core.service.ChannelService;
import com.buzzinate.buzzads.data.operations.FindAdItemsOp;
import com.buzzinate.buzzads.data.operations.FindCampaignBudgets;
import com.buzzinate.buzzads.data.operations.UpdateAdItemOp;
import com.buzzinate.buzzads.data.thrift.PublisherContactNotFoundException;
import com.buzzinate.buzzads.data.thrift.TAdCriteria;
import com.buzzinate.buzzads.data.thrift.TAdDataAccessServices.FutureIface;
import com.buzzinate.buzzads.data.thrift.TAdItem;
import com.buzzinate.buzzads.data.thrift.TAdStats;
import com.buzzinate.buzzads.data.thrift.TAdStatsCriteria;
import com.buzzinate.buzzads.data.thrift.TCampaignBudget;
import com.buzzinate.buzzads.data.thrift.TFrozenChannel;
import com.buzzinate.buzzads.data.thrift.TPagination;
import com.buzzinate.buzzads.data.thrift.TPublisherContactInfo;
import com.buzzinate.buzzads.data.thrift.TPublisherSiteConfig;
import com.twitter.util.ExceptionalFunction0;
import com.twitter.util.ExecutorServiceFuturePool;
import com.twitter.util.Future;

/**
 * 服务接口测试实现
 * 
 * @author zyeming
 * 
 */
public class TAdataAccessServicesTestImpl implements FutureIface {

    private ExecutorService pool = Executors.newFixedThreadPool(4);
    private ExecutorServiceFuturePool futurePool = new ExecutorServiceFuturePool(pool);

    @Autowired
    private AdEntryService adEntryService;
    @Autowired
    private AdOrderService adOrderService;
    @Autowired
    private AdCampaignService adCampaignService;
    @Autowired
    private AdCampaignBudgetService adCampaignBudgetService;
    @Autowired
    private CampDayBudgetService campDayBudgetService;
    @Autowired
    private AdvertiserBalanceService advertiserBalanceService;
    @Autowired
    private ChannelService channelService;

    @Override
    public Future<TPublisherContactInfo> findPublisherContact(int userId) {
        FindPublisherContactOp op = new FindPublisherContactOp(userId);
        Future<TPublisherContactInfo> future = futurePool.apply(op);
        return future;
    }

    @Override
    public Future<Void> saveOrUpdatePublisherContact(TPublisherContactInfo publisherContact) {
        return null;
    }

    /**
     * 
     * @author zyeming
     * 
     */
    public static class FindPublisherContactOp extends ExceptionalFunction0<TPublisherContactInfo> {
        private int userId;
        public FindPublisherContactOp(int userId) {
            this.userId = userId;
        }
        @Override
        public TPublisherContactInfo applyE() throws Exception {
            if (userId > 0) {
                return new TPublisherContactInfo.Builder().userId(userId).name("Yeming").build();
            } else {
                throw new PublisherContactNotFoundException.Builder().build();
            }
        }
    }
    @Override
    public Future<List<TAdItem>> findAdItems(TAdCriteria criteria, TPagination pagination) {
        FindAdItemsOp op = new FindAdItemsOp(adEntryService, adOrderService, adCampaignService, channelService, 
                criteria, pagination);
        Future<List<TAdItem>> future = futurePool.apply(op);
        return future;
    }

    @Override
    public Future<List<TAdStats>> findAdStats(TAdStatsCriteria criteria, TPagination pagination) {
        return null;
    }
    @Override
    public Future<Void> updateAdItem(TAdItem adItem) {
        UpdateAdItemOp op = new UpdateAdItemOp(adEntryService, adOrderService, adItem);
        Future<Void> future = futurePool.apply(op);
        return future;
    }
    @Override
    public Future<List<TCampaignBudget>> findCampaignBudgets(List<Integer> campaignIds) {
        FindCampaignBudgets op = new FindCampaignBudgets(
                adCampaignBudgetService, campDayBudgetService,
                advertiserBalanceService, campaignIds);
        Future<List<TCampaignBudget>> future = futurePool.apply(op);
        return future;
    }

    @Override
    public Future<TPublisherSiteConfig> getPublisherSiteConfig(String uuid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<List<TFrozenChannel>> findAllFrozenList() {
        // TODO Auto-generated method stub
        return null;
    }
}
