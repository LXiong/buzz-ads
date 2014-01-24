package com.buzzinate.buzzads.domain;

import com.buzzinate.buzzads.domain.enums.TradeStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Alipay trade record info
 *
 * @author martin
 */
public class AlipayRecord implements Serializable {
	private static final long serialVersionUID = -9148279323145496831L;

	private static DecimalFormat format = new DecimalFormat("###,###,##0.00");

    private int id;
    private int recordId;
    private int advertiserId;
    private int amount;
    private String tradeNo;
    private long aliTradeNo;
    private TradeStatus tradeStatus;
    private Date createTime;
    private Date paymentTime;

    private String userName;

    public int getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(int advertiserId) {
        this.advertiserId = advertiserId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public long getAliTradeNo() {
        return aliTradeNo;
    }

    public void setAliTradeNo(long aliTradeNo) {
        this.aliTradeNo = aliTradeNo;
    }

    public TradeStatus getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAmountValue() {
        return format.format(amount / 100.0);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "AlipayRecord{" +
                "id=" + id +
                ", recordId=" + recordId +
                ", advertiserId=" + advertiserId +
                ", amount=" + amount +
                ", tradeNo='" + tradeNo + '\'' +
                ", aliTradeNo=" + aliTradeNo +
                ", tradeStatus=" + tradeStatus +
                ", createTime=" + createTime +
                ", paymentTime=" + paymentTime +
                ", userName='" + userName + '\'' +
                '}';
    }
}
