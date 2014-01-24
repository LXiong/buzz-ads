package com.buzzinate.buzzads.entity;

import com.buzzinate.buzzads.enums.AdNetworkEnum;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-7-23
 * Time: 下午3:07
 * 广告请求版位信息
 */
public class BannerInfo {
    // 广告框高度，参数名h
    private int height;
    // 广告框宽度，参数名w
    private int width;
    // 广告类型，参数rt
    // 0 - 任意（默认），1 - 图片，2 - 文字，3 - Flash
    private int type = 0;
    // 广告slot id
    private String id = "-1";
    private AdNetworkEnum network;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AdNetworkEnum getNetwork() {
        return network;
    }

    public void setNetwork(AdNetworkEnum network) {
        this.network = network;
    }
}
