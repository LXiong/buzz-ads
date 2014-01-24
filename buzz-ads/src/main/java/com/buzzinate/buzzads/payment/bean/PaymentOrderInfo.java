package com.buzzinate.buzzads.payment.bean;

import com.buzzinate.common.util.DateTimeUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author Jerry.Ma
 *
 */
public class PaymentOrderInfo implements Serializable {

    private static final long serialVersionUID = -3535037130072626480L;
    
    private int id;
    private int userId;
    private String orderNo;
    private int productType;
    private int unitPrice;
    private int amountMonth;
    private int totalFee;
    private Date orderTime;
    private int orderStatus;
    private int paymentPlatform;
    private String outTradeNo;
    private Date paidTime;
    private int sendEmail;
    private Date dateStart;
    private Date dateEnd;
    private String remark;

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getAmountMonth() {
        return amountMonth;
    }

    public void setAmountMonth(int amountMonth) {
        this.amountMonth = amountMonth;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getPaymentPlatform() {
        return paymentPlatform;
    }

    public void setPaymentPlatform(int paymentPlatform) {
        this.paymentPlatform = paymentPlatform;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Date getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Date paidTime) {
        this.paidTime = paidTime;
    }

    public String getPaidTimeString() {
        return DateTimeUtil.formatDate(paidTime);
    }
    
    /**
     * @return the sendEmail
     */
    public int getSendEmail() {
        return sendEmail;
    }

    /**
     * @param sendEmail
     *            the sendEmail to set
     */
    public void setSendEmail(int sendEmail) {
        this.sendEmail = sendEmail;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }
    
    public String getDateStartString() {
        return DateTimeUtil.formatDate(dateStart);
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }
    
    public String getDateEndString() {
        return DateTimeUtil.formatDate(dateEnd);
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     *            the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

}
