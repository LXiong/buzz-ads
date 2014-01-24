package com.buzzinate.adx.entity;

import com.buzzinate.buzzads.data.thrift.AdEntrySizeEnum;
import com.buzzinate.buzzads.enums.AdEntryPosEnum;
import com.buzzinate.buzzads.enums.AdEntryTypeEnum;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kun
 * Date: 13-6-29
 * Time: 下午9:41
 * 版位信息
 */
public class BannerInfo implements Serializable {
    private static final long serialVersionUID = 327546472946206120L;
    private int bannerId;
    private AdEntrySizeEnum size;
    private AdEntryTypeEnum type;
    private List<Integer> adIds;
    private AdEntryPosEnum position;

    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public AdEntrySizeEnum getSize() {
        return size;
    }

    public void setSize(AdEntrySizeEnum size) {
        this.size = size;
    }

    public AdEntryTypeEnum getType() {
        return type;
    }

    public void setType(AdEntryTypeEnum type) {
        this.type = type;
    }

    public List<Integer> getAdIds() {
        return adIds;
    }

    public void setAdIds(List<Integer> adIds) {
        this.adIds = adIds;
    }

    public AdEntryPosEnum getPosition() {
        return position;
    }

    public void setPosition(AdEntryPosEnum position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "BannerInfo{" +
                "bannerId=" + bannerId +
                ", size=" + size +
                ", type=" + type +
                ", adIds=" + adIds +
                ", position=" + position +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BannerInfo that = (BannerInfo) o;

        if (bannerId != that.bannerId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return bannerId;
    }
}
