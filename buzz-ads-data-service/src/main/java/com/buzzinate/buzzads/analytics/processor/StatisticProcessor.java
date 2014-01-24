package com.buzzinate.buzzads.analytics.processor;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.buzzinate.buzzads.analytics.dao.StatisticsUpdateDao;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdEntryService;
import com.buzzinate.buzzads.core.service.AdOrderService;
import com.buzzinate.buzzads.core.service.ChannelService;
import com.buzzinate.buzzads.core.service.CpcClickSetService;
import com.buzzinate.buzzads.core.service.PublisherContactService;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.common.util.task.AbstractProcessor;

/**
 * statistic processor.
 * @author johnson
 *
 */
public class StatisticProcessor extends AbstractProcessor {

    private static final int CAPACITY = Integer.parseInt(ConfigurationReader.getString("analytics.queue.capacity"));
    private static final int BATCH_SIZE = Integer.parseInt(ConfigurationReader.getString("analytics.queue.batch.size"));
    private static final int INTERVAL = Integer.parseInt(ConfigurationReader.getString("analytics.queue.interval"));

    private static Log log = LogFactory.getLog(StatisticProcessor.class);

    @Autowired
    private StatisticsUpdateDao statisticsUpdateDao;

    @Autowired
    private AdEntryService adEntryService;
    
    @Autowired
    private AdOrderService adOrderService;
    
    @Autowired
    private AdCampaignService adCampaignService;
    
    @Autowired
    private CpcClickSetService cpcClickSetService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private PublisherContactService publisherContactService;

    public StatisticProcessor() {
        super(CAPACITY, BATCH_SIZE, INTERVAL);
    }

    
    /**
     * handle stats.
     * @param stats stats collection
     */
    @Override
    protected void workerFunction(List<Object> stats) {
        StatisticAggregator aggregator = new StatisticAggregator(adEntryService, adOrderService, adCampaignService,
                        cpcClickSetService, channelService, publisherContactService);

        // Process each statistic object
        for (Object o : stats) {
            try {
                aggregator.handleStats(o);
            } catch (Exception e) {
                // catch in case exception cause processor to stop
                log.error("Statistic processor exception: ", e);
            }
        }

        // update in analytics db
        try {
            statisticsUpdateDao.saveStatsAdDailyIntoDatabase(
                    aggregator.getAdDailyMap());
            statisticsUpdateDao.saveStatsAdOrderDailyIntoDatabase(
                    aggregator.getAdOrderDailyMap());
            statisticsUpdateDao.saveStatsAdCampaignDailyIntoDatabase(
                    aggregator.getAdCampaignDailyMap());
            statisticsUpdateDao.saveStatsPublisherDailyIntoDatabase(
                    aggregator.getPublishierDailyMap());
            statisticsUpdateDao.saveStatsAdminDailyIntoDatabase(
                    aggregator.getAdminDailyMap());
            statisticsUpdateDao.saveStatsChannelDailyIntoDatabase(
                    aggregator.getChannelDailyMap());
            log.info("Statistic processor: daily stats saved.");
        } catch (Exception e) {
            log.error("Processor exception when updating analytics db: ", e);
        }

    }
}
