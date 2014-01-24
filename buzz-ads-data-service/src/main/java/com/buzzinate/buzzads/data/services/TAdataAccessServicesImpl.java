package com.buzzinate.buzzads.data.services;

import java.util.List;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;

import com.buzzinate.buzzads.core.service.AdCampaignBudgetService;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdEntryService;
import com.buzzinate.buzzads.core.service.AdOrderService;
import com.buzzinate.buzzads.core.service.AdvertiserBalanceService;
import com.buzzinate.buzzads.core.service.CampDayBudgetService;
import com.buzzinate.buzzads.core.service.ChannelService;
import com.buzzinate.buzzads.core.service.PublisherContactService;
import com.buzzinate.buzzads.core.service.PublisherSiteConfigService;
import com.buzzinate.buzzads.core.service.StatsAdService;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.data.operations.FindAdItemsOp;
import com.buzzinate.buzzads.data.operations.FindAdStatsOp;
import com.buzzinate.buzzads.data.operations.FindAllFrozenListOp;
import com.buzzinate.buzzads.data.operations.FindCampaignBudgets;
import com.buzzinate.buzzads.data.operations.FindPublisherContactOp;
import com.buzzinate.buzzads.data.operations.FindPublisherSiteConfigOp;
import com.buzzinate.buzzads.data.operations.SaveOrUpdatePublisherContactOp;
import com.buzzinate.buzzads.data.operations.UpdateAdItemOp;
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
import com.twitter.util.ExecutorServiceFuturePool;
import com.twitter.util.Future;

/**
 * 服务接口实现
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-12-11
 */
public class TAdataAccessServicesImpl implements FutureIface {

    @Autowired
    private PublisherContactService publisherContactService;
    @Autowired
    private AdOrderService adOrderService;
    @Autowired
    private AdEntryService adEntryService;
    @Autowired
    private AdCampaignService adCampaignService;
    @Autowired
    private AdCampaignBudgetService adCampaignBudgetService;
    @Autowired
    private CampDayBudgetService campDayBudgetService;
    @Autowired
    private AdvertiserBalanceService advertiserBalanceService;
    @Autowired
    private StatsAdService statsAdService;
    @Autowired
    private PublisherSiteConfigService publisherSiteConfigService;
    @Autowired
    private ChannelService channelService;

    private ExecutorServiceFuturePool futurePool = new ExecutorServiceFuturePool(
            Executors.newFixedThreadPool(ConfigurationReader.getInt("data.access.server.future.pool.size")));

    @Override
    public Future<TPublisherContactInfo> findPublisherContact(int userId) {
        FindPublisherContactOp op = new FindPublisherContactOp(publisherContactService, userId);
        Future<TPublisherContactInfo> future = futurePool.apply(op);
        return future;
    }

    @Override
    public Future<Void> saveOrUpdatePublisherContact(TPublisherContactInfo publisherContact) {
        SaveOrUpdatePublisherContactOp op = new SaveOrUpdatePublisherContactOp(publisherContactService,
                publisherContact);
        Future<Void> future = futurePool.apply(op);
        return future;
    }
    @Override
    public Future<List<TAdItem>> findAdItems(TAdCriteria criteria, TPagination pagination) {
        FindAdItemsOp op = new FindAdItemsOp(adEntryService, adOrderService, adCampaignService, 
                channelService, criteria, pagination);
        Future<List<TAdItem>> future = futurePool.apply(op);
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
    public Future<Void> updateAdItem(TAdItem adItem) {
        UpdateAdItemOp op = new UpdateAdItemOp(adEntryService, adOrderService, adItem);
        Future<Void> future = futurePool.apply(op);
        return future;
    }
    @Override
    public Future<List<TAdStats>> findAdStats(TAdStatsCriteria criteria, TPagination pagination) {
        FindAdStatsOp op = new FindAdStatsOp(statsAdService, criteria, pagination);
        Future<List<TAdStats>> future = futurePool.apply(op);
        return future;
    }

    @Override
    public Future<TPublisherSiteConfig> getPublisherSiteConfig(String uuid) {
        FindPublisherSiteConfigOp op = new FindPublisherSiteConfigOp(uuid, publisherSiteConfigService);
        Future<TPublisherSiteConfig> future = futurePool.apply(op);
        return future;
    }
    @Override
    public Future<List<TFrozenChannel>> findAllFrozenList() {
        FindAllFrozenListOp op = new FindAllFrozenListOp(channelService);
        Future<List<TFrozenChannel>> future = futurePool.apply(op);
        return future;
    }
}
