package com.buzzinate.buzzads.billing.dao;


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
import com.buzzinate.buzzads.analytics.stats.PublisherDailyStatistic;

/**
 * statistics update dao.
 * @author jacky lu
 */
@Component
@Repository
@Transactional(value = "buzzads")
public class CpcStatsUpdateDao {

    private static Log log = LogFactory.getLog(CpcStatsUpdateDao.class);

    @Autowired
    private SessionFactory sessionFactory;


    /**
     * insert or update ad daily info.
     * @param statAdDailyMap ad daily static
     */
    public void saveAdDailyCpc(final AdDailyStatistic stats) {
        this.executeUpdate(new Command() {
            @Override
            public Object execute(StatelessSession session) {
                doSaveAdDailyCpc(session, stats);
                return null;
            }
        });
    }

    /**
     * do ad daily info insert or update.
     * @param session dao session
     * @param statAdDailyMap ad daily static
     */
    public void doSaveAdDailyCpc(StatelessSession session,
            AdDailyStatistic stats) {
        Query query = session.getNamedQuery("statAdDaily.insertOrUpdateCpc");
        query.setBinary("uuid", stats.getUuid());
        query.setLong("adentryId", stats.getAdEntryId());
        query.setDate("date", stats.getDateDay());
        query.setInteger("network", stats.getNetwork().getCode());
        query.setLong("adorderId", stats.getAdOrderId());
        this.bindStateFiled(query, stats);
        query.executeUpdate();
    }
    
    
    /**
     * insert or update ad campaign daily info.
     * @param stats ad daily static
     */
    public void saveCampaignDailyCpc(final AdCampaignDailyStatistic stats) {
        this.executeUpdate(new Command() {
            @Override
            public Object execute(StatelessSession session) {
                doSaveCampaignDailyCpc(session, stats);
                return null;
            }
        });
    }

    /**
     * do ad campaign daily info insert or update.
     * @param session dao session
     * @param stats ad daily static
     */
    public void doSaveCampaignDailyCpc(StatelessSession session,
                    AdCampaignDailyStatistic stats) {
        Query query = session.getNamedQuery("statCampaignDaily.insertOrUpdateCpc");
        query.setLong("campaignId", stats.getCampaignId());
        query.setDate("date", stats.getDateDay());
        query.setInteger("network", stats.getNetwork().getCode());
        query.setLong("advertiserId", stats.getAdvertiserId());
        this.bindStateFiled(query, stats);
        this.bindStateCpmFiled(query, stats);
        query.executeUpdate();
    }
    
    
    /**
     * insert or update ad campaign daily info.
     * @param stats ad daily static
     */
    public void saveOrderDailyCpc(final AdOrderDailyStatistic stats) {
        this.executeUpdate(new Command() {
            @Override
            public Object execute(StatelessSession session) {
                doSaveOrderDailyCpc(session, stats);
                return null;
            }
        });
    }

    /**
     * do ad campaign daily info insert or update.
     * @param session dao session
     * @param stats ad daily static
     */
    public void doSaveOrderDailyCpc(StatelessSession session,
                    AdOrderDailyStatistic stats) {
        Query query = session.getNamedQuery("statOrderDaily.insertOrUpdateCpc");
        query.setLong("orderId", stats.getOrderId());
        query.setDate("date", stats.getDateDay());
        query.setInteger("network", stats.getNetwork().getCode());
        this.bindStateFiled(query, stats);
        query.executeUpdate();
    }
    

    /**
     * insert or update publisher daily info.
     * @param statsDailyMap site publisher daily info
     */
    public void savePublisherDailyCpc(final PublisherDailyStatistic stats) {
        this.executeUpdate(new Command() {
            @Override
            public Object execute(StatelessSession session) {
                doSavePublisherDailyCpc(session, stats);
                return null;
            }
        });
    }


    /**
     * do publisher daily info insert or update.
     * @param session dao session
     * @param statDailyMap publisher daily static
     */
    public void doSavePublisherDailyCpc(StatelessSession session, PublisherDailyStatistic stats) {
        Query query = session.getNamedQuery("statsPublisherDaily.insertOrUpdateCpc");
        query.setBinary("uuid", stats.getUuid());
        query.setDate("date", stats.getDateDay());
        query.setInteger("network", stats.getNetwork().getCode());
        this.bindStateFiled(query, stats);
        this.bindStateCpmFiled(query, stats);
        query.executeUpdate();
    }


    /**
     * insert or update amin daily.
     * @param statsAdminDailyMap admin daily info
     */
    public void saveAdminDailyCpc(final AdminDailyStatistic stats) {
        this.executeUpdate(new Command() {
            @Override
            public Object execute(StatelessSession session) {
                doSaveAdminDailyCpc(session, stats);
                return null;
            }
        });
    }

    /**
     * insert or update admin daily.
     * @param session session
     * @param statsAdminDailyMap admin daily statistic info
     */
    public void doSaveAdminDailyCpc(StatelessSession session, AdminDailyStatistic stats) {
        Query query = session.getNamedQuery("statsAdminDaily.insertOrUpdateCpc");
        query.setDate("date", stats.getDateDay());
        query.setInteger("network", stats.getNetwork().getCode());
        this.bindStateFiled(query, stats);
        this.bindStateCpmFiled(query, stats);
        query.executeUpdate();
    }

    
    /**
     * bind common filed in base class.
     * @param query hibernate query object
     * @param vo statistic object
     */
    private void bindStateFiled(Query query, AdBasicStatistic vo) {
        query.setInteger("cpcClickNo", vo.getCpcClickNo());
        query.setLong("cpcPubCommission", vo.getCpcPubCommission());
        query.setLong("cpcTotalCommission", vo.getCpcTotalCommission());
    }
    
    /**
     * bind CPM filed in base class.
     * @param query hibernate query object
     * @param vo statistic object
     */
    private void bindStateCpmFiled(Query query, AdBasicStatistic vo) {
        query.setInteger("cpmViewNo", vo.getCpmViewNo());
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
    public void executeUpdate(Command command) {
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
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    /**
     * Interface to wrap the transaction code.
     * @author zyeming
     */
    public interface Command {
        /**
         * execute command.
         * @param session session
         * @return Object
         */
        Object execute(StatelessSession session);
    }
}
