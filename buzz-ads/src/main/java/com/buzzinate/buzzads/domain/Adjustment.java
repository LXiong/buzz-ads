package com.buzzinate.buzzads.domain;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author chris
 * @since Date: 13-3-28 Time: 下午3:46
 */
public class Adjustment implements Serializable {
    
    private static final long serialVersionUID = -3375510059722131482L;

    private static DecimalFormat format = new DecimalFormat("###,###,##0.00");

    private int id;
    private int adminId;
    private int receiverId;
    private String receiveAmount;
    private String detail;
    private long amount;
    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiveAmount() {
        if (StringUtils.isEmpty(receiveAmount)) {
            return format.format(amount / 100.0);
        }
        return receiveAmount;
    }

    public void setReceiveAmount(String receiveAmount) {
        BigDecimal base = new BigDecimal(receiveAmount);
        amount = base.multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP).longValue();
        this.receiveAmount = receiveAmount;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Adjustment)) return false;

        Adjustment that = (Adjustment) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Adjustment{" +
                "id=" + id +
                ", adminId=" + adminId +
                ", receiverId=" + receiverId +
                ", receiveAmount='" + receiveAmount + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}