package com.buzzinate.buzzads.core.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.domain.AdxDspRtbStatistic;

/**
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 *
 *         2013-07-04
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class AdxDspRtbStatisticDao extends AdsDaoBase<AdxDspRtbStatistic, Integer> {

    public AdxDspRtbStatisticDao() {
        super(AdxDspRtbStatistic.class, "id");
    }

    @Transactional(value = "buzzads", readOnly = false)
    public int updateAdxDspStatistic(AdxDspRtbStatistic stats) {
        Query query = getSession().getNamedQuery("AdxDspRtbStatistic.updateAdxDspStatistic");
        query.setInteger("key", stats.getKey());
        query.setLong("requestCount", stats.getRequestCount());
        query.setLong("responseCount", stats.getResponseCount());
        query.setLong("bidSuccCount", stats.getBidSuccCount());
        query.setLong("validCount", stats.getValidCount());
        return query.executeUpdate();
    }

}
