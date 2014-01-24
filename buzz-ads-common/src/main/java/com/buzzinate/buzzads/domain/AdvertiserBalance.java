package com.buzzinate.buzzads.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 13, 2013 5:26:15 PM
 * 
 */
public class AdvertiserBalance implements Serializable {
    private static final long serialVersionUID = 15791946543452536L;
    
    private int advertiserId;
    private long debitsTotal;
    private long creditsTotal;
    private long balance;
    private Date updateAt = new Date();

    
    public AdvertiserBalance() { }

    public AdvertiserBalance(int advertiserId) {
        this.advertiserId = advertiserId;
    }
    
    public int getAdvertiserId() {
        return advertiserId;
    }
    public void setAdvertiserId(int advertiserId) {
        this.advertiserId = advertiserId;
    }
    public long getDebitsTotal() {
        return debitsTotal;
    }
    public void setDebitsTotal(long debitsTotal) {
        this.debitsTotal = debitsTotal;
    }
    public long getCreditsTotal() {
        return creditsTotal;
    }
    public void setCreditsTotal(long creditsTotal) {
        this.creditsTotal = creditsTotal;
    }
    public long getBalance() {
        return balance;
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

}
