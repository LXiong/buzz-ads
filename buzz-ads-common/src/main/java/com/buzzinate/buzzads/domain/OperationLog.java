package com.buzzinate.buzzads.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-12-24
 */
public class OperationLog implements Serializable {

    private static final long serialVersionUID = 6236690045934118978L;

    private long id;
    private long opUserId;
    private int opType;
    private String description;
    private long targetUserId;
    private long outId;
    private Date createTime;
    private String opName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOpUserId() {
        return opUserId;
    }

    public void setOpUserId(long opUserId) {
        this.opUserId = opUserId;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public long getOutId() {
        return outId;
    }

    public void setOutId(long outId) {
        this.outId = outId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }
}
