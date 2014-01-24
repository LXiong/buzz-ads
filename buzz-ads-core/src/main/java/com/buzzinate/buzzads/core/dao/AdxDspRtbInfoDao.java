package com.buzzinate.buzzads.core.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.domain.AdxDspRtbInfo;

/**
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 *
 *         2013-07-04
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class AdxDspRtbInfoDao extends AdsDaoBase<AdxDspRtbInfo, Integer> {

    public AdxDspRtbInfoDao() {
        super(AdxDspRtbInfo.class, "id");
    }

    @Transactional(value = "buzzads", readOnly = false)
    public int updateRtbInfoByKey(int key, String winnerNotifyUrl, String bidRequestUrl) {
        Query query = getSession().getNamedQuery("AdxDspRtbInfoDao.updateAdxDspRtbInfo");
        query.setInteger("key", key);
        query.setString("winnerNotifyUrl", winnerNotifyUrl);
        query.setString("bidRequestUrl", bidRequestUrl);
        return query.executeUpdate();
    }
    
    public AdxDspRtbInfo getRtbInfoByKey(int key) {
        Query query = getSession().getNamedQuery("AdxDspRtbInfoDao.getRtbInfoByKey");
        query.setInteger("key", key);
        Object obj = query.uniqueResult();
        if (obj == null) {
            obj = new AdxDspRtbInfo();
        }
        return (AdxDspRtbInfo) obj;
    }

//    public AdxDspRtbInfo getAdxDspRtbInfoById(Integer dspId) {
//        Query query = getSession().getNamedQuery("AdxDspRtbInfoDao.getAdxDspRtbInfoById");
//        query.setInteger("dspId", dspId);
//        Object[] obj = (Object[]) query.uniqueResult();
//        AdxDspRtbInfo dspInfo = new AdxDspRtbInfo();
//        if (obj == null) {
//            return dspInfo;
//        }
//        int i = 0;
//        dspInfo.setKey((Integer) obj[i]);
//        i ++;
//        dspInfo.setBidRequestUrl(obj[i] == null ? "" : obj[i].toString());
//        i ++;
//        dspInfo.setWinnerNotifyUrl(obj[i] == null ? "" : obj[i].toString());
//        i ++;
//        // dsp statistic
//        dspInfo.setRequestCount((Long) obj[i]);
//        i ++;
//        dspInfo.setResponseCount((Long) obj[i]);
//        i ++;
//        dspInfo.setBidSuccCount((Long) obj[i]);
//        i ++;
//        dspInfo.setValidCount((Long) obj[i]);
//        i ++;
//        
//        return dspInfo;
//    }

}
