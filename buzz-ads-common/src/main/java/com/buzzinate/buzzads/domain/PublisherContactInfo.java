package com.buzzinate.buzzads.domain;

import java.io.Serializable;
import java.util.List;

import com.buzzinate.buzzads.enums.BankEnum;
import com.buzzinate.buzzads.enums.FinanceTypeEnum;
import com.buzzinate.buzzads.enums.PublisherContactRevMethod;
import com.buzzinate.buzzads.enums.PublisherContactStausEnum;

/**
 * 站长联系人
 *
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *         <p/>
 *         2012-12-11
 */
public class PublisherContactInfo implements Serializable {
    
    private static final long serialVersionUID = -2562777340028156329L;
    private int userId;
    private String name;
    private String mobile;
    private String qq = "";
    private PublisherContactRevMethod receiveMethod = PublisherContactRevMethod.ALIPAY;
    private String receiveAccount;
    private String receiveName;
    private BankEnum receiveBankCode = BankEnum.UNKNOWN;
    private String receiveBank = "";
    private FinanceTypeEnum financeType = FinanceTypeEnum.COMPANY;
    private PublisherContactStausEnum status = PublisherContactStausEnum.NORMAL;
    private String email;
    // 站长分成比例 
    private int proportion;

    private List<Site> sites;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public PublisherContactRevMethod getReceiveMethod() {
        return receiveMethod;
    }

    public void setReceiveMethod(PublisherContactRevMethod receiveMethod) {
        this.receiveMethod = receiveMethod;
    }

    public String getReceiveAccount() {
        return receiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        this.receiveAccount = receiveAccount;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceiveBank() {
        return receiveBank;
    }

    public void setReceiveBank(String receiveBank) {
        this.receiveBank = receiveBank;
    }

    public PublisherContactStausEnum getStatus() {
        return status;
    }

    public void setStatus(PublisherContactStausEnum status) {
        this.status = status;
    }

    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public FinanceTypeEnum getFinanceType() {
        return financeType;
    }

    public void setFinanceType(FinanceTypeEnum financeType) {
        this.financeType = financeType;
    }

    public BankEnum getReceiveBankCode() {
        return receiveBankCode;
    }

    public void setReceiveBankCode(BankEnum receiveBankCode) {
        this.receiveBankCode = receiveBankCode;
    }

    public int getProportion() {
        return proportion;
    }

    public void setProportion(int proportion) {
        this.proportion = proportion;
    }
    
    public void resetProportion(int defaultProportion) {
        if (this.proportion <= 0) {
            this.proportion = defaultProportion;
        }
    }

    @Override
    public String toString() {
        return "PublisherContactInfo{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", qq='" + qq + '\'' +
                ", receiveMethod=" + receiveMethod +
                ", receiveAccount='" + receiveAccount + '\'' +
                ", receiveName='" + receiveName + '\'' +
                ", receiveBank='" + receiveBank + '\'' +
                ", financeType=" + financeType +
                ", status=" + status +
                ", email='" + email + '\'' +
                ", proportion=" + proportion + 
                ", sites=" + sites +
                '}';
    }
}