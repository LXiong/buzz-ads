package com.buzzinate.buzzads.domain;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
/**
 * 支付信息
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-12-18
 */
public class Payment implements Serializable {

    private static final long serialVersionUID = 2533514603983620877L;
    
    private static DecimalFormat doubleformat = new DecimalFormat("#,###,##0.00");

    private int id;
    private int userId;
    //支付周期
    private String period;
    //应付金额
    private int amount;
    //手续费
    private int fee;
    //税
    private int tax;
    //实付金额
    private int paid;
    //支付流水号
    private String receiptNo;
    private String comment;
    private Date paymentTime;
    
    private int status = 1;
    private Date updateTime;
    
    //站长信息
    private PublisherContactInfo info;
    
    public String getAmountDouble() {
        return doubleformat.format((double) amount / 100);
    }
    
    public String getFeeDouble() {
        return doubleformat.format((double) fee / 100);
    }
    
    public String getTaxDouble() {
        return doubleformat.format((double) tax / 100);
    }
    
    public String getPaidDouble() {
        return doubleformat.format((double) paid / 100);
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getPeriod() {
        return period;
    }
    public void setPeriod(String period) {
        this.period = period;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public int getFee() {
        return fee;
    }
    public void setFee(int fee) {
        this.fee = fee;
    }
    public int getTax() {
        return tax;
    }
    public void setTax(int tax) {
        this.tax = tax;
    }
    public int getPaid() {
        return paid;
    }
    public void setPaid(int paid) {
        this.paid = paid;
    }
    public String getReceiptNo() {
        return receiptNo;
    }
    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Date getPaymentTime() {
        return paymentTime;
    }
    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }
    public PublisherContactInfo getInfo() {
        return info;
    }
    public void setInfo(PublisherContactInfo info) {
        this.info = info;
    }
    
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
