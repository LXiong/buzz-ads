package com.buzzinate.buzzads.core.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buzzinate.buzzads.core.dao.CampDayBudgetDao;
import com.buzzinate.buzzads.domain.CampaignDayBudget;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2013-5-21
 */
@Service
public class CampDayBudgetService {
    
    @Autowired
    private CampDayBudgetDao campDayBudgetDao;
    
    public List<CampaignDayBudget> findCampaignDayBudgetByCampId(int campId) {
        return campDayBudgetDao.findCampaignDayBudgetByCampId(campId);
    }
    
    public CampaignDayBudget findDayBudgetByCampIdAndDate(int campId, Date date) {
        return campDayBudgetDao.findDayBudgetByCampIdAndDate(campId, date);
    }

}
