package com.buzzinate.buzzads.analytics.dao;


import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.analytics.stats.AdBasicStatistic;
import com.buzzinate.buzzads.analytics.stats.AdCampaignDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.AdDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.AdOrderDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.AdminDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.ChannelDailyStatistic;
import com.buzzinate.buzzads.analytics.stats.PublisherDailyStatistic;

/**
 * statistics update dao.
 * @author jacky lu
 */
@Component
@Repository
@Transactional(value = "buzzads")
public class StatisticsUpdateDao {

    private static Log log = LogFactory.getLog(StatisticsUpdateDao.class);

    @Autowired
    private SessionFactory sessionFactory;


    /**
     * insert or update ad daily info.
     * @param stats ad daily static
     */
    public void saveStatsAdDailyIntoDatabase(
           final Map<String, AdDailyStatistic> stats) {
        this.executeUpdate(new Command() {
            @Override
            public Object execute(StatelessSession session) {
                doSaveStatsAdDailyIntoDatabase(session, stats);
                return null;
            }
        });
    }

    /**
     * do ad daily info insert or update.
     * @param session dao session
     * @param stats ad daily static
     */
    private void doSaveStatsAdDailyIntoDatabase(StatelessSession session,
            Map<String, AdDailyStatistic> stats) {
        Query query = session.getNamedQuery("statAdDaily.insertOrUpdate");
        for (AdDailyStatistic mt : stats.values()) {
            try {
                query.setLong("adentryId", mt.getAdEntryId());
                query.setDate("date", mt.getDateDay());
                query.setInteger("network", mt.getNetwork().getCode());
                query.setBinary("uuid", mt.getUuid());
                query.setLong("adorderId", mt.getAdOrderId());
                this.bindStateFiled(query, mt);
                this.bindCpmStateFiled(query, mt);

                query.setInteger("cpmClickNo", mt.getCpmClickNo());
                query.executeUpdate();
            } catch (Exception e) {
                log.error("Exception while inserting stat ad daily data.", e);
            }
        }
    }

    /**
     * insert or update ad order daily info.
     * @param stats ad daily static
     */
    public void saveStatsAdOrderDailyIntoDatabase(
           final Map<String, AdOrderDailyStatistic> stats) {
        this.executeUpdate(new Command() {
            @Override
            public Object execute(StatelessSession session) {
                doSaveStatsAdOrderDailyIntoDatabase(session, stats);
                return null;
            }
        });
    }

    /**
     * do ad order daily info insert or update.
     * @param session dao session
     * @param stats ad order daily static
     */
    private void doSaveStatsAdOrderDailyIntoDatabase(StatelessSession session,
            Map<String, AdOrderDailyStatistic> stats) {
        Query query = session.getNamedQuery("statAdOrderDaily.insertOrUpdate");
        for (AdOrderDailyStatistic mt : stats.values()) {
            try {
                query.setLong("orderId", mt.getOrderId());
                query.setDate("date", mt.getDateDay());
                query.setInteger("network", mt.getNetwork().getCode());
                this.bindStateFiled(query, mt);
                
                this.bindCpmStateFiled(query, mt);
                
                query.setInteger("cpmClickNo", mt.getCpmClickNo());
                
                query.executeUpdate();
            } catch (Exception e) {
                log.error("Exception while inserting stat ad daily data.", e);
            }
        }
    }
    
    /**
     * insert or update ad campaign daily info.
     * @param stats ad campaign static
     */
    public void saveStatsAdCampaignDailyIntoDatabase(
           final Map<String, AdCampaignDailyStatistic> stats) {
        this.executeUpdate(new Command() {
            @Override
            public Object execute(StatelessSession session) {
                doSaveStatsAdCampaignDailyIntoDatabase(session, stats);
                return null;
            }
        });
    }

    /**
     * do ad campaign daily info insert or update.
     * @param session dao session
     * @param stats ad campaign daily static
     */
    private void doSaveStatsAdCampaignDailyIntoDatabase(StatelessSession session,
            Map<String, AdCampaignDailyStatistic> stats) {
        Query query = session.getNamedQuery("statAdCampaignDaily.insertOrUpdate");
        for (AdCampaignDailyStatistic mt : stats.values()) {
            try {
                query.setLong("campaignId", mt.getCampaignId());
                query.setDate("date", mt.getDateDay());
                query.setInteger("network", mt.getNetwork().getCode());
                query.setInteger("advertiserId", mt.getAdvertiserId());
                bindStateFiled(query, mt);
                this.bindCpmStateFiled(query, mt);
                
                query.setInteger("cpmClickNo", mt.getCpmClickNo());
                query.executeUpdate();
            } catch (Exception e) {
                log.error("Exception while inserting stat ad daily data.", e);
            }
        }
    }
    
    
    /**
     * insert or update publisher daily info.
     * @param statsDailyMap site publisher daily info
     */
    public void saveStatsPublisherDailyIntoDatabase(
           final Map<String, PublisherDailyStatistic> statsDailyMap) {
        this.executeUpdate(new Command() {
            @Override
            public Object execute(StatelessSession session) {
                doSaveStatsPublisherDailyIntoDatabase(session, statsDailyMap);
                return null;
            }
        });
    }


    /**
     * do publisher daily info insert or update.
     * @param session dao session
     * @param statDailyMap publisher daily static
     */
    private void doSaveStatsPublisherDailyIntoDatabase(StatelessSession session,
                    Map<String, PublisherDailyStatistic> statDailyMap) {
        Query query = session.getNamedQuery("statsPublishDaily.insertOrUpdate");
        for (PublisherDailyStatistic mt : statDailyMap.values()) {
            try {
                query.setBinary("uuid", mt.getUuid());
                query.setDate("date", mt.getDateDay());
                query.setInteger("network", mt.getNetwork().getCode());
                query.setLong("pageView", mt.getPageview());
                bindStateFiled(query, mt);
                bindCpmStateFiled(query, mt);
                query.executeUpdate();
            } catch (Exception e) {
                log.error("Exception while inserting stat ad daily data.", e);
            }
        }
    }


    /**
     * insert or update amin daily.
     * @param statsAdminDailyMap admin daily info
     */
    public void saveStatsAdminDailyIntoDatabase(
           final Map<String, AdminDailyStatistic> statsAdminDailyMap) {
        this.executeUpdate(new Command() {
            @Override
            public Object execute(StatelessSession session) {
                doSaveStatsAdminDailyIntoDatabase(session, statsAdminDailyMap);
                return null;
            }
        });
    }
    
    public void saveStatsChannelDailyIntoDatabase(
           final Map<String, ChannelDailyStatistic> statsChannelDailyMap) {
        this.executeUpdate(new Command() {
            @Override
            public Object execute(StatelessSession session) {
                doSaveChannelDailyIntoDataBase(session, statsChannelDailyMap);
                return null;
            } 
        });
    }

    /**
     * insert or update admin daily.
     * @param session session
     * @param statsAdminDailyMap admin daily statistic info
     */
    private void doSaveStatsAdminDailyIntoDatabase(StatelessSession session,
                    Map<String, AdminDailyStatistic> statsAdminDailyMap) {
        Query query = session.getNamedQuery("statsAdminDaily.insertOrUpdate");
        for (AdminDailyStatistic mt : statsAdminDailyMap.values()) {
            try {
                query.setDate("date", mt.getDateDay());
                query.setInteger("network", mt.getNetwork().getCode());
                bindStateFiled(query, mt);
                this.bindCpmStateFiled(query, mt);
                
                query.setInteger("cpcViewNo", mt.getCpcViewNo());
                query.setInteger("cpmClickNo", mt.getCpmClickNo());
                query.setInteger("cpcOrigclickNo", mt.getCpcOriginalClickNo());
                query.setInteger("cpsViewNo", mt.getCpsViewNo());
                query.setInteger("cpsClickNo", mt.getCpsClickNo());
                query.executeUpdate();
            } catch (Exception e) {
                log.error("Exception while inserting stat ad daily data.", e);
            }
        }
    }
    
    /**
     * insert or update channel daily.
     * @param session session
     * @param channelDailyMap channel daily statistic info
     */
    private void doSaveChannelDailyIntoDataBase(StatelessSession session,
                    Map<String, ChannelDailyStatistic> channelDailyMap) {
        Query query = session.getNamedQuery("statsChannelDaily.insertOrUpdate");
        for (ChannelDailyStatistic stats : channelDailyMap.values()) {
            try {
                query.setDate("date", stats.getDateDay());
                query.setInteger("channelId", stats.getChannelId());
                query.setInteger("viewCount", stats.getViews());
                query.setInteger("clickCount", stats.getClicks());
                query.setInteger("pageView", stats.getPageview());
                query.executeUpdate();
            } catch (Exception e) {
                log.error("Exception while inserting stat channel daily data.", e);
            }
            
        }
    }

    /**
     * bind common filed in base class.
     * @param query hibernate query object
     * @param vo statistic object
     */
    private void bindStateFiled(Query query, AdBasicStatistic vo) {
        query.setLong("viewCount", vo.getViews());
        query.setLong("clickCount", vo.getClicks());
        query.setInteger("cpsOrderNo", vo.getCpsOrderNo());
        query.setLong("cpsConfirmedCommission", vo.getCpsConfirmedCommission());
        query.setLong("cpsPubCommission", vo.getCpsPubCommission());
        query.setLong("cpsTotalConfirmedCommission", vo.getCpsTotalConfirmedCommission());
        query.setLong("cpsTotalCommission", vo.getCpsTotalCommission());
        query.setLong("cpsTotalPrice", vo.getCpsTotalPrice());
    }

    /**
     * bind common filed in base class.
     * @param query hibernate query object
     * @param vo statistic object
     */
    private void bindCpmStateFiled(Query query, AdBasicStatistic vo) {
        // update cpm related info into db(admin\publisher\compaign)
        query.setLong("cpmViewNo", vo.getCpmViewNo());
        query.setBigDecimal("cpmPubCommission", vo.getCpmPubCommission());
        query.setBigDecimal("cpmTotalCommission", vo.getCpmTotalCommission());
    }

    /**
     * set sessionFactory.
     * @param sessionFac data session factory
     */
    public void setSessionFactory(SessionFactory sessionFac) {
        this.sessionFactory = sessionFac;
    }


    /**
     * execute update or insert command.
     * @param command update or insert command
     */
    private void executeUpdate(Command command) {
        StatelessSession session  = sessionFactory.openStatelessSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            command.execute(session);
            transaction.commit();
        } catch (Exception e) {
            log.error("Exception while update statistics: " + e.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    /**
     * Interface to wrap the transaction code.
     * @author zyeming
     */
    private interface Command {
        /**
         * execute command.
         * @param session session
         * @return Object
         */
        Object execute(StatelessSession session);
    }
}
