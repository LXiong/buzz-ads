package com.buzzinate.buzzads.core.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.domain.CampaignDayBudget;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2013-5-16
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class CampDayBudgetDao extends AdsDaoBase<CampaignDayBudget, Integer> {
    
    public CampDayBudgetDao() {
        super(CampaignDayBudget.class, "id");
    }
    
    @SuppressWarnings("unchecked")
    public List<CampaignDayBudget> findCampaignDayBudgetByCampId(int campId) {
        Query query = getSession().getNamedQuery("CampDayBudgetDao.findCampaignDayBudgetByCampId");
        query.setInteger("campId", campId);
        return query.list();
    }
    
    public int deleteByCampaignId(int campId) {
        Query query = getSession().getNamedQuery("CampDayBudgetDao.deleteByCampaignId");
        query.setInteger("campId", campId);
        return query.executeUpdate();
    }
    
    public CampaignDayBudget findDayBudgetByCampIdAndDate(int campId, Date date) {
        Query query = getSession().getNamedQuery("CampDayBudgetDao.findDayBudgetByCampIdAndDate");
        query.setInteger("campId", campId);
        query.setDate("date", date);
        return (CampaignDayBudget) query.uniqueResult();
    }

}
