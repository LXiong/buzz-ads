package com.buzzinate.buzzads.analytics.stats;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.buzzinate.buzzads.enums.AdNetworkEnum;

/**
 * 
 * @author Johnson
 *
 */
public class AdOrderDailyStatistic extends AdBasicStatistic implements Serializable {

    private static final long serialVersionUID = 871640943770018509L;
    
    private int id;
    
    private int orderId;
    
    private AdNetworkEnum network;
    
    
    public AdOrderDailyStatistic() {
    }
    
    public AdOrderDailyStatistic(Date dateDay, long views, long clicks, long cpcClickNo,
            long cpcTotalCommission, long cpsTotalCommission) {
        super(views, clicks, cpcClickNo, cpcTotalCommission, cpsTotalCommission);
        this.dateDay = dateDay;
    }
    
    public AdOrderDailyStatistic(Date dateDay, long views, long clicks, long cpcClickNo, long cpmViewNo,
            long cpcTotalCommission, long cpsTotalCommission, BigDecimal cpmTotalCommission) {
        super(views, clicks, cpcClickNo, cpmViewNo, cpcTotalCommission, cpsTotalCommission, cpmTotalCommission);
        this.dateDay = dateDay;
    } 
    
    public AdOrderDailyStatistic(int orderId, long views, long clicks,
            long cpcClickNo, long cpmViewNo, long cpcTotalCommission,
            long cpsTotalCommission, BigDecimal cpmTotalCommission) {
        super(views, clicks, cpcClickNo, cpmViewNo, cpcTotalCommission, cpsTotalCommission, cpmTotalCommission);
        this.orderId = orderId;
    }

    public AdOrderDailyStatistic(int orderId, long views, long clicks, long cpcClickNo,
            long cpcTotalCommission, long cpsTotalCommission) {
        super(views, clicks, cpcClickNo, cpcTotalCommission, cpsTotalCommission);
        this.orderId = orderId;
    }
    
    public AdOrderDailyStatistic(Date dateDay, AdNetworkEnum network, int orderId) {
        this.dateDay = dateDay;
        this.network = network;
        this.orderId = orderId;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    
    public AdNetworkEnum getNetwork() {
        return network;
    }

    public void setNetwork(AdNetworkEnum network) {
        this.network = network;
    }

}
