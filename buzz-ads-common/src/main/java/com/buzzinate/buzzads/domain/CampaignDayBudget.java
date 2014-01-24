package com.buzzinate.buzzads.domain;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * 广告预算的每日设定
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2013-5-16
 */
public class CampaignDayBudget implements Serializable {

    protected static DecimalFormat doubleformat = new DecimalFormat("#,###,##0.00");
    private static final long serialVersionUID = 8277756925569189822L;
    private int id;
    private int campId;
    private Date budgetDay;
    private int budget;
    
    public CampaignDayBudget() {
        
    }
    
    public CampaignDayBudget(Date budgetDay, int budget) {
        this.budget = budget;
        this.budgetDay = budgetDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCampId() {
        return campId;
    }

    public void setCampId(int campId) {
        this.campId = campId;
    }

    public Date getBudgetDay() {
        return budgetDay;
    }

    public void setBudgetDay(Date budgetDay) {
        this.budgetDay = budgetDay;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }
    
    public String getBudgetDouble() {
        return doubleformat.format((double) budget / 100);
    }
}
