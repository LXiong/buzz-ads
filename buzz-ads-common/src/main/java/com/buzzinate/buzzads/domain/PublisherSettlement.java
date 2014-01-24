package com.buzzinate.buzzads.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;

import com.buzzinate.buzzads.enums.BankEnum;
import com.buzzinate.buzzads.enums.SettlementStatusEnum;

/**
 * 站长结算表
 *
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *         <p/>
 *         2012-11-26
 */
public class PublisherSettlement implements Serializable {

    private static final long serialVersionUID = 6267194612829447415L;
    private static DecimalFormat doubleformat = new DecimalFormat("#,###,##0.00");
    private int id;
    private Date month;
    private int cpsNo;
    private int cpsCommission;
    private int cpcNo;
    private int cpcCommission;
    private int commission;
    private SettlementStatusEnum status;
    private Date payTime;
    private int userId;
    private int paymentId;
    private int cpmNo;
    private BigDecimal cpmCommission = new BigDecimal(0);
    // 记录comm更新的日期，以免重复计算comm
    private Date commUpdateDay;
    //账户
    private String email;
    //收款姓名
    private String receiveName;
    // 开户行code
    private BankEnum receiveBankCode;
    //收款账户
    private String receiveAccount;
    //支付流水号
    private String receiptNo;
    //支付备注
    private String comment;
    private String linkName;

    public String getCpsCommissionDouble() {
        return doubleformat.format((double) cpsCommission / 100);
    }

    public String getCpcCommissionDouble() {
        return doubleformat.format((double) cpcCommission / 100);
    }

    public String getCommissionDouble() {
        return doubleformat.format((double) commission / 100);
    }

    public String getCpmCommissionDouble() {
        return doubleformat.format(getCpmCommission().doubleValue());
    }

    public String getTotalCommissionDouble() {
        return doubleformat.format((double) (cpsCommission + cpcCommission) / 100 + cpmCommission.doubleValue());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
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

    public int getTotalCommission() {
        return cpsCommission + cpmCommission.multiply(new BigDecimal(100)).
                setScale(0, RoundingMode.HALF_DOWN).intValue() + cpcCommission;
    }

    public SettlementStatusEnum getStatus() {
        return status;
    }

    public void setStatus(SettlementStatusEnum status) {
        this.status = status;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {

        this.payTime = payTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceiveAccount() {
        return receiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        this.receiveAccount = receiveAccount;
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

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public Date getCommUpdateDay() {
        return commUpdateDay;
    }

    public void setCommUpdateDay(Date commUpdateDay) {
        this.commUpdateDay = commUpdateDay;
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

    public String getReceiveBankName() {
        if (receiveBankCode != null) {
            return receiveBankCode.getText();
        }
        return BankEnum.UNKNOWN.getText();
    }

    public void setReceiveBankCode(BankEnum receiveBankCode) {
        this.receiveBankCode = receiveBankCode;
    }

    @Override
    public String toString() {
        return "PublisherSettlement [id=" + id + ", month=" + month + ", cpsNo=" + cpsNo +
                ", cpsCommission=" + cpsCommission + ", cpcNo=" + cpcNo + ", cpcCommission=" + cpcCommission +
                ", commission=" + commission + ", status=" + status + ", payTime=" + payTime + "]";
    }
}
