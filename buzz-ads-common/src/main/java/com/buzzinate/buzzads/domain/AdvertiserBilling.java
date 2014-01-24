package com.buzzinate.buzzads.domain;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

import com.buzzinate.buzzads.enums.AdvertiserBillingType;
import com.buzzinate.common.util.DateTimeUtil;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 13, 2013 11:31:11 AM
 * 
 */
public class AdvertiserBilling implements Serializable {

    private static final long serialVersionUID = -72333025157333308L;
    private static DecimalFormat format = new DecimalFormat("#,###,##0.00");

    private int id;
    private int advertiserId;
    private AdvertiserBillingType type;
    private Date billingDay;
    private String comment;
    private int debits;
    private int credits;
    private long balance;
    private Date updateAt = new Date();

    // extra field
    private String companyName;
    
    public AdvertiserBilling() {
    }
    
    public AdvertiserBilling(int advertiserId, long debits) {
        this.advertiserId = advertiserId;
        this.debits = ((Long) debits).intValue();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getAdvertiserId() {
        return advertiserId;
    }
    public void setAdvertiserId(int advertiserId) {
        this.advertiserId = advertiserId;
    }
    public AdvertiserBillingType getType() {
        return type;
    }
    public void setType(AdvertiserBillingType type) {
        this.type = type;
    }
    public Date getBillingDay() {
        return billingDay;
    }
    public void setBillingDay(Date billingDay) {
        this.billingDay = billingDay;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public int getDebits() {
        return debits;
    }
    public String getDebitsDouble() {
        return format.format((double) debits / 100);
    }
    public void setDebits(int debits) {
        this.debits = debits;
    }
    public int getCredits() {
        return credits;
    }
    public String getCreditsDouble() {
        return format.format((double) credits / 100);
    }
    public void setCredits(int credits) {
        this.credits = credits;
    }
    public long getBalance() {
        return balance;
    }
    public String getBalanceDouble() {
        return format.format((double) balance / 100);
    }
    public void setBalance(long balance) {
        this.balance = balance;
    }
    public Date getUpdateAt() {
        return updateAt;
    }
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
    public String getUpdateAtDayStr() {
        return DateTimeUtil.formatDate(updateAt);
    }
    public String getUpdateAtMonthStr() {
        return DateTimeUtil.formatDate(updateAt, DateTimeUtil.FMT_DATE_YYYY_SLASH_MM);
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
