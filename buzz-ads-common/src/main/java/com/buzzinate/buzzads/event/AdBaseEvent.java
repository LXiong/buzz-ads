package com.buzzinate.buzzads.event;

import java.io.Serializable;

import com.buzzinate.buzzads.enums.IdType;

/**
 * 广告事件基础类
 * 
 * @author martin
 *
 */
public class AdBaseEvent implements AdEvent, Serializable {

    private static final long serialVersionUID = -2592509659053608645L;
    
    protected IdType type;
    protected int id;
    
    public IdType getType() {
        return type;
    }
    public void setType(IdType type) {
        this.type = type;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
