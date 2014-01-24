package com.buzzinate.buzzads.core.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.domain.AdDetailCps;
import com.buzzinate.buzzads.enums.OrderSourceEnum;
import com.buzzinate.common.util.UuidUtil;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-11-26
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class AdDetailCpsDao extends AdsDaoBase<AdDetailCps, Integer> {

    public AdDetailCpsDao() {
        super(AdDetailCps.class, "id");
    }

    @SuppressWarnings("unchecked")
    public List<AdDetailCps> getUuidDetail(String uuid) {
        Query query = getSession().getNamedQuery("AdDetailCpsDao.getUuidDetail");
        query.setBinary("uuid", UuidUtil.uuidToByteArray(uuid));
        return query.list();
    }
    
    public AdDetailCps getAdDetailCpsByOcdId(String ocd, OrderSourceEnum source) {
        Query query = getSession().getNamedQuery("AdDetailCpsDao.getAdDetailCpsByOcdId");
        query.setString("cpsOid", ocd);
        query.setInteger("source", source.getCode());
        return (AdDetailCps) query.uniqueResult();
    }
    
    @Transactional(value = "buzzads", readOnly = false)
    public Integer updateForConfirmation(int id, int pubComm, int comm, int status, Date confirmTime) {
        Query query = getSession().getNamedQuery("AdDetailCpsDao.updateForConfirmation");
        query.setInteger("id", id);
        query.setInteger("pubComm", pubComm);
        query.setInteger("comm", comm);
        query.setInteger("status", status);
        query.setDate("confirmTime", confirmTime);
        return query.executeUpdate();
    }
}
