package com.buzzinate.buzzads.domain;

import java.io.Serializable;
import java.util.Date;

import org.safehaus.uuid.UUID;

import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.buzzads.enums.OrderSourceEnum;
import com.buzzinate.buzzads.enums.TradeConfirmEnum;

/**
 * 佣金明细
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-11-26
 */
public class AdDetailCps implements Serializable {

    private static final long serialVersionUID = 731918289550019242L;

    private int id;
    private String uuid;
    private int adOrderId;
    private int adEntryId;
    private int pubComm;
    private int comm;
    private int totalPrice;
    private Date tradeTime;
    private OrderSourceEnum source;
    private String cpsOid;
    private TradeConfirmEnum status;
    private Date confirmTime;
    private AdNetworkEnum network;
    
    public AdDetailCps() { }
    
    public AdDetailCps(byte[] uuidBytes, AdNetworkEnum network, long pubComm, long comm, long totalPrice) {
        this.uuid = new UUID(uuidBytes).toString();
        this.network = network;
        this.pubComm = (int) pubComm;
        this.comm = (int) comm;
        this.totalPrice = (int) totalPrice;
    }
    
    public AdDetailCps(int adOrderId, int adEntryId, AdNetworkEnum network,
            long pubComm, long comm, long totalPrice) {
        this.adEntryId = adEntryId;
        this.adOrderId = adOrderId;
        this.network = network;
        this.pubComm = (int) pubComm;
        this.comm = (int) comm;
        this.totalPrice = (int) totalPrice;
    }
    
    public AdDetailCps(AdNetworkEnum network, long pubComm, long comm, long totalPrice) {
        this.network = network;
        this.pubComm = (int) pubComm;
        this.comm = (int) comm;
        this.totalPrice = (int) totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Used by Hibernate to get UUID from database.
     * @return
     */
    public byte[] getUuidBytes() {
        if (this.uuid != null) {
            UUID uuidx = new UUID(this.uuid);
            return uuidx.asByteArray();
        } else {
            return null;
        }
    }
    
    /**
     * Used by Hibernate to set UUID in database.
     * @param uuidByte
     */
    public void setUuidBytes(byte[] uuidByte) {
        if (uuidByte != null) {
            UUID uuidx = new UUID(uuidByte);
            this.uuid = uuidx.toString();
        } else {
            this.uuid = null;
        }
    }
    
    public int getAdOrderId() {
        return adOrderId;
    }

    public void setAdOrderId(int adOrderId) {
        this.adOrderId = adOrderId;
    }

    public int getAdEntryId() {
        return adEntryId;
    }

    public void setAdEntryId(int adEntryId) {
        this.adEntryId = adEntryId;
    }

    public int getPubComm() {
        return pubComm;
    }

    public void setPubComm(int pubComm) {
        this.pubComm = pubComm;
    }

    public int getComm() {
        return comm;
    }

    public void setComm(int comm) {
        this.comm = comm;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public OrderSourceEnum getSource() {
        return source;
    }

    public void setSource(OrderSourceEnum source) {
        this.source = source;
    }

    public String getCpsOid() {
        return cpsOid;
    }

    public void setCpsOid(String cpsOid) {
        this.cpsOid = cpsOid;
    }

    public TradeConfirmEnum getStatus() {
        return status;
    }

    public void setStatus(TradeConfirmEnum status) {
        this.status = status;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public AdNetworkEnum getNetwork() {
        return network;
    }

    public void setNetwork(AdNetworkEnum network) {
        this.network = network;
    }
    
    
}
