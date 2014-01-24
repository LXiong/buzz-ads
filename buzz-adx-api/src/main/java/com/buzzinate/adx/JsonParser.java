package com.buzzinate.adx;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.buzzinate.adx.entity.AdInfo;
import com.buzzinate.adx.entity.BannerInfo;
import com.buzzinate.adx.entity.MediaInfo;
import com.buzzinate.adx.enums.BidStatus;
import com.buzzinate.adx.message.RTBMessage;
import com.buzzinate.buzzads.data.thrift.AdEntrySizeEnum;
import com.buzzinate.buzzads.enums.AdEntryPosEnum;
import com.buzzinate.buzzads.enums.AdEntryTypeEnum;

/**
 * @author james.chen
 * 
 */
public final class JsonParser implements Serializable {

    private static final long serialVersionUID = -8232652901077769871L;

    private JsonParser() {
    }

    public static MediaInfo getMediaInfo(JSONObject mediaInfo) {
        MediaInfo info = new MediaInfo();
        info.setUrl(mediaInfo.getString("url"));
        info.setFloorPrice(mediaInfo.getFloatValue("price"));
        Object value = mediaInfo.get("blockUrl");
        info.setBlockUrls(convertArrayToList((JSONArray) value));
        info.setBlockKeywords(convertArrayToList((JSONArray) mediaInfo.get("keyWords")));
        return info;
    }

    public static List<BannerInfo> getBannerInfos(JSONArray array) {
        List<BannerInfo> bannerInfos = new ArrayList<BannerInfo>();
        if (array == null) {
            return bannerInfos;
        }
        for (int i = 0; i < array.size(); i++) {
            JSONObject banner = array.getJSONObject(i);
            BannerInfo info = new BannerInfo();
            info.setBannerId(banner.getIntValue("id"));
            info.setPosition(AdEntryPosEnum.findAdEntryPosEnumByCode(banner.getIntValue("pos")));
            info.setSize(AdEntrySizeEnum.findByValue(banner.getIntValue("size")));
            info.setType(AdEntryTypeEnum.findByValue(banner.getIntValue("btype")));
            bannerInfos.add(info);
        }
        return bannerInfos;
    }
    
    public static List<AdInfo> getAdInfos(JSONArray array) {
        List<AdInfo> badInfos = new ArrayList<AdInfo>();
        if (array == null) {
            return badInfos;
        }
        for (int i = 0; i < array.size(); i++) {
            JSONObject info = array.getJSONObject(i);
            AdInfo adInfo = new AdInfo();
            adInfo.setAdId(info.getIntValue("adId"));
            adInfo.setBannerId(info.getIntValue("bannerId"));
            adInfo.setLink(info.getString("link"));
            adInfo.setSegment(info.getString("segment"));
            adInfo.setPrice(info.getFloat("price"));
            adInfo.setStatus(BidStatus.SUCCESS);
            badInfos.add(adInfo);
        }
        return badInfos;
    }

    public static RTBMessage getRTBMessage(JSONObject rtbInfo , String requestId) {
        String cookieId = rtbInfo.getString("cId");
        String ip = rtbInfo.getString("ip");
        String agent = rtbInfo.getString("agent");
        MediaInfo media = getMediaInfo(rtbInfo.getJSONObject("media"));
        List<BannerInfo> banners = getBannerInfos(rtbInfo.getJSONArray("banner"));
        List<AdInfo> adInfos = null;
        if (rtbInfo.containsKey("dspbid")) {
            adInfos = getAdInfos(rtbInfo.getJSONArray("dspbid"));
        }
        return new RTBMessage(requestId , cookieId , banners , media , ip , agent , "" , adInfos);
    }

    private static List<String> convertArrayToList(JSONArray values) {
        List<String> result = new ArrayList<String>();
        if (values == null) {
            return result;
        }
        for (int i = 0; i < values.size(); i++) {
            result.add(values.getString(i));
        }
        return result;
    }
}
