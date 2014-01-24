package com.buzzinate.buzzads.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.domain.ChanetTradeData;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-11-23
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class ChanetTradeDataDao extends AdsDaoBase<ChanetTradeData, Long> {
    
    public ChanetTradeDataDao() {
        super(ChanetTradeData.class, "id");
    }
    
    @Transactional(value = "buzzads", readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Long createChanetTradeData(ChanetTradeData tradeData) {
        return this.create(tradeData);
    }

    @SuppressWarnings("unchecked")
    public List<ChanetTradeData> listChanetTransactions() {
        Query query = getSession().getNamedQuery("ChanetTradeDataDao.getChanetTransactions");
        return (List<ChanetTradeData>) query.list();
    }
    
    public ChanetTradeData getChanetDataByOcd(String ocd) {
        Query query = getSession().getNamedQuery("ChanetTradeDataDao.getChanetDataByOcd");
        query.setString("ocd", ocd);
        return (ChanetTradeData) query.uniqueResult();
    }
}
