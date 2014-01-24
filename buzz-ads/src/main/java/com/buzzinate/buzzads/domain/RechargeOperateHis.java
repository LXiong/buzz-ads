package com.buzzinate.buzzads.domain;

import com.buzzinate.buzzads.domain.enums.OperateType;

import java.io.Serializable;
import java.util.Date;

/**
 * RechargeOperateHis Record
 *
 * @author james.chen
 */
public class RechargeOperateHis implements Serializable {
    
    private static final long serialVersionUID = 5760127002984132047L;
    //primary key
    private int id;
    //operate type
    private OperateType operateType;
    //record id
    private int recordId;
    //user id
    private int advertiserId;
    //insert time
    private Date insertTime;

    public RechargeOperateHis() {

    }

    public RechargeOperateHis(OperateType type, int orderNo, int userId) {
        this.operateType = type;
        this.insertTime = new Date();
        this.recordId = orderNo;
        this.advertiserId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OperateType getOperateType() {
        return operateType;
    }

    public void setOperateType(OperateType operateType) {
        this.operateType = operateType;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(int advertiserId) {
        this.advertiserId = advertiserId;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
}
