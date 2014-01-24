package com.buzzinate.buzzads.domain;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

import org.safehaus.uuid.UUID;

import com.buzzinate.buzzads.enums.AdvertiserBusinessType;
import com.buzzinate.buzzads.enums.AdvertiserStatusEnum;

/**
 * 
 * @author Johnson
 *
 */
public class AdvertiserAccount implements Serializable {
    
    protected static DecimalFormat doubleformat = new DecimalFormat("#,###,##0.00");
    
    private static final long serialVersionUID = 1755913950683715736L;

    private int advertiserId;
    
    private String companyName;
    
    private String websiteName;
    
    private String websiteUrl;
    
    private AdvertiserBusinessType businessType = AdvertiserBusinessType.UNKNOWN;
    
    private AdvertiserStatusEnum status = AdvertiserStatusEnum.NORMAL;
    
    private String secret;
    
    //正在投放活动的数量
    private int liveCampCount;
    //账户余额
    private long balance;
    //总投放金额
    private long debitsTotal;
    
    public AdvertiserAccount() {
        this.secret = java.util.UUID.randomUUID().toString();
    }
    
    public String getBalanceDouble() {
        return doubleformat.format((double) balance / 100);
    }
    
    public String getDebitsTotalDouble() {
        return doubleformat.format((double) debitsTotal / 100);
    }
    
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
    
    public byte[] getSecretBytes() {
        if (this.secret != null) {
            UUID uuidx = new UUID(this.secret);
            return uuidx.asByteArray();
        } else {
            return null;
        }
    }
    
    public void setSecretBytes(byte[] uid) {
        if (uid != null) {
            UUID uuidx = new UUID(uid);
            this.secret = uuidx.toString();
        } else {
            this.secret = null;
        }
    }

    public int getBusinessTypeValue() {
        return businessType.getCode();
    }

    public void setBusinessTypeValue(int businessTypeValue) {
        this.businessType = AdvertiserBusinessType.findByValue(businessTypeValue);
    }

    public int getStatusValue() {
        return status.getCode();
    }

    public void setStatusValue(int statusValue) {
        this.status = AdvertiserStatusEnum.findByValue(statusValue);
    }

    public int getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(int advertiserId) {
        this.advertiserId = advertiserId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public AdvertiserBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(AdvertiserBusinessType businessType) {
        this.businessType = businessType;
    }

    public AdvertiserStatusEnum getStatus() {
        return status;
    }

    public void setStatus(AdvertiserStatusEnum status) {
        this.status = status;
    }

    public int getLiveCampCount() {
        return liveCampCount;
    }

    public void setLiveCampCount(int liveCampCount) {
        this.liveCampCount = liveCampCount;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getDebitsTotal() {
        return debitsTotal;
    }

    public void setDebitsTotal(long debitsTotal) {
        this.debitsTotal = debitsTotal;
    }
}