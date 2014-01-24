package com.buzzinate.adx.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kun
 * Date: 13-6-29
 * Time: 下午9:51
 * 媒体信息
 */
public class MediaInfo implements Serializable {
    private static final long serialVersionUID = 979108109711318264L;
    private String url;
    private float floorPrice;
    private List<String> blockKeywords;
    private List<String> blockUrls;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(float floorPrice) {
        this.floorPrice = floorPrice;
    }

    public List<String> getBlockKeywords() {
        return blockKeywords;
    }

    public void setBlockKeywords(List<String> blockKeywords) {
        this.blockKeywords = blockKeywords;
    }

    public List<String> getBlockUrls() {
        return blockUrls;
    }

    public void setBlockUrls(List<String> blockUrls) {
        this.blockUrls = blockUrls;
    }

    @Override
    public String toString() {
        return "MediaInfo{" +
                "url='" + url + '\'' +
                ", floorPrice=" + floorPrice +
                ", blockKeywords=" + blockKeywords +
                ", blockUrls=" + blockUrls +
                '}';
    }


}
