package com.buzzinate.buzzads.core.bean;

import com.buzzinate.buzzads.enums.AdEntryTypeEnum;
import com.buzzinate.buzzads.enums.AdNetworkEnum;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-7-24
 * Time: 下午12:24
 * uuid url映射
 */
public class AdBanner implements Serializable {
    private static final long serialVersionUID = 7872372313129662512L;
    private int id;
    private byte[] uuid;
    private String url;
    private int width;
    private int height;
    private AdEntryTypeEnum type;
    private AdNetworkEnum network;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getUuid() {
        return uuid;
    }

    public void setUuid(byte[] uuid) {
        this.uuid = uuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public AdEntryTypeEnum getType() {
        return type;
    }

    public void setType(AdEntryTypeEnum type) {
        this.type = type;
    }

    public AdNetworkEnum getNetwork() {
        return network;
    }

    public void setNetwork(AdNetworkEnum network) {
        this.network = network;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdBanner adBanner = (AdBanner) o;

        if (id != adBanner.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
