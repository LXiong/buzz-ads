package com.buzzinate.buzzads.domain;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2013-2-27
 */
public class AdCampBudget implements Serializable {

    private static final long serialVersionUID = 1579194916543452536L;

    private int campaignId;
    private int advertiserId;
    private Date dateDay;
    private long budgetDay;
    private long budgetTotal;
    private long maxBudgetDay;
    private long maxBudgetTotal;

    //extra field
    private long balance;
    
    
    public AdCampBudget() {
        super();
    }

    public AdCampBudget(int campaignId, int advertiserId) {
        super();
        this.campaignId = campaignId;
        this.advertiserId = advertiserId;
    }

    /**
     * 判断是否超出预算
     * @return
     */
    public boolean isExceedBudget() {
        return (getMaxBudgetDay() > 0 && getBudgetDay() >= getMaxBudgetDay()) || 
                        (getMaxBudgetTotal() > 0 && getBudgetTotal() >= getMaxBudgetTotal());
    }
    
    /**
     * 更新活动开销
     * @param day
     * @param cost
     */
    public void addCost(Date day, int cost) {
        if (getDateDay() == null || !DateUtils.isSameDay(day, getDateDay())) {
            setDateDay(day);
            setBudgetDay(cost);
        } else {
            setBudgetDay(cost + getBudgetDay());
        }
        setBudgetTotal(cost + getBudgetTotal());
    }
    
    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public int getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(int advertiserId) {
        this.advertiserId = advertiserId;
    }

    public Date getDateDay() {
        return dateDay;
    }

    public void setDateDay(Date dateDay) {
        this.dateDay = dateDay;
    }

    public long getBudgetDay() {
        return budgetDay;
    }

    public void setBudgetDay(long budgetDay) {
        this.budgetDay = budgetDay;
    }

    public long getBudgetTotal() {
        return budgetTotal;
    }

    public void setBudgetTotal(long budgetTotal) {
        this.budgetTotal = budgetTotal;
    }

    public long getMaxBudgetDay() {
        return maxBudgetDay;
    }

    public void setMaxBudgetDay(long maxBudgetDay) {
        this.maxBudgetDay = maxBudgetDay;
    }

    public long getMaxBudgetTotal() {
        return maxBudgetTotal;
    }

    public void setMaxBudgetTotal(long maxBudgetTotal) {
        this.maxBudgetTotal = maxBudgetTotal;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AdCampBudget [campaignId=" + campaignId + ", advertiserId=" + advertiserId + 
                        ", dateDay=" + dateDay + ", budgetDay=" + budgetDay + ", budgetTotal=" + 
                        budgetTotal + ", maxBudgetDay=" + maxBudgetDay + ", maxBudgetTotal=" +
                        maxBudgetTotal + ", balance=" + balance + "]";
    }
    
}
