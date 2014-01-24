package com.buzzinate.buzzads.core.bean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

/**
 * Stats object for settlement
 * @author zyeming
 *
 */
public class PublisherSettlementStats {

    private byte[] uuid;
    private Date day;
    private int cpsNo;
    private int cpsCommission;
    private int cpcNo;
    private int cpcCommission;
    private int commission;
    private int userId;
    private int cpmNo;
    private BigDecimal cpmCommission = new BigDecimal(0);   // 给站长的cpm佣金
 
    public PublisherSettlementStats() {
        super();
    }   
    
    public PublisherSettlementStats(byte[] uuid, Date day) {
        super();
        this.uuid = uuid;
        this.day = day;
    }
    
    
    public byte[] getUuid() {
        return uuid;
    }
    public void setUuid(byte[] uuid) {
        this.uuid = uuid;
    }
    public Date getDay() {
        return day;
    }
    public void setDay(Date day) {
        this.day = day;
    }
    public int getCpsNo() {
        return cpsNo;
    }
    public void setCpsNo(int cpsNo) {
        this.cpsNo = cpsNo;
    }
    public int getCpsCommission() {
        return cpsCommission;
    }
    public void setCpsCommission(int cpsCommission) {
        this.cpsCommission = cpsCommission;
    }
    public int getCpcNo() {
        return cpcNo;
    }
    public void setCpcNo(int cpcNo) {
        this.cpcNo = cpcNo;
    }
    public int getCpcCommission() {
        return cpcCommission;
    }
    public void setCpcCommission(int cpcCommission) {
        this.cpcCommission = cpcCommission;
    }
    public int getCommission() {
        return commission;
    }
    public void setCommission(int commission) {
        this.commission = commission;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCpmNo() {
        return cpmNo;
    }

    public void setCpmNo(int cpmNo) {
        this.cpmNo = cpmNo;
    }

    public BigDecimal getCpmCommission() {
        if (this.cpmCommission == null) {
            return new BigDecimal(0);
        }
        return cpmCommission;
    }

    public void setCpmCommission(BigDecimal cpmCommission) {
        this.cpmCommission = cpmCommission;
    }

    @Override
    public String toString() {
        return "PublisherSettlementStats [uuid=" + Arrays.toString(uuid) + ", day=" + day + 
                        ", cpsNo=" + cpsNo + ", cpsCommission=" + cpsCommission + 
                        ", cpcNo=" + cpcNo + ", cpcCommission=" + cpcCommission + 
                        ", cpmNo=" + cpmNo + ", cpmCommission=" + cpmCommission + 
                        ", commission=" + commission + ", userId=" + userId + "]";
    }
      
}
