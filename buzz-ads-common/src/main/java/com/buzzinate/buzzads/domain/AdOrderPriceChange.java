package com.buzzinate.buzzads.domain;

import java.io.Serializable;
import java.util.Date;
/**
 * 广告组价格变动记录表
 * 
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 * 
 *         2012-06-14
 */
public class AdOrderPriceChange implements Serializable {

    private static final long serialVersionUID = 4067688886568616905L;
    
    private int id;
    private Integer orderId;
    private Date dateTime;
    private Integer bidPriceOld;
    private Integer bidPriceNew;
    
    public AdOrderPriceChange(Integer orderId, Integer bidPriceOld,
            Integer bidPriceNew) {
        super();
        this.orderId = orderId;
        this.dateTime = new Date();
        this.bidPriceOld = bidPriceOld;
        this.bidPriceNew = bidPriceNew;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Integer getOrderId() {
        return orderId;
    }
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    public Date getDateTime() {
        return dateTime;
    }
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
    public Integer getBidPriceOld() {
        return bidPriceOld;
    }
    public void setBidPriceOld(Integer bidPriceOld) {
        this.bidPriceOld = bidPriceOld;
    }
    public Integer getBidPriceNew() {
        return bidPriceNew;
    }
    public void setBidPriceNew(Integer bidPriceNew) {
        this.bidPriceNew = bidPriceNew;
    }

}
