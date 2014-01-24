package com.buzzinate.adx.parse;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.buzzinate.adx.CacheConstants;
import com.buzzinate.adx.entity.AdInfo;
import com.buzzinate.adx.entity.BannerInfo;
import com.buzzinate.adx.entity.MediaInfo;
import com.buzzinate.adx.entity.UserInfo;
import com.buzzinate.adx.enums.BidStatus;
import com.buzzinate.adx.enums.DisplayStatus;
import com.buzzinate.adx.message.BidDspMessage;
import com.buzzinate.adx.message.BidRequestMessage;
import com.buzzinate.adx.message.RTBMessage;
import com.buzzinate.adx.util.RedisUtils;

/**
 * @author james.chen
 * date 2013-7-30
 */
public final class BidJsonParser implements Serializable {

    private static final Log LOG = LogFactory.getLog(BidJsonParser.class);

    private static final long serialVersionUID = 9033651729582537909L;

    private BidJsonParser() {
        
    }
    /**
     * bannerInfo
     * 
     * @param banner
     * @return
     */
    public static JSONObject genBannerInfo(BannerInfo banner) {
        JSONObject jobject = new JSONObject();
        if (banner == null)
            return jobject;
        try {
            jobject.put("w", banner.getSize().getWidth());
            jobject.put("h", banner.getSize().getHeight());
            jobject.put("slotId", banner.getBannerId());
            jobject.put("pos", banner.getPosition().getCode());
            jobject.put("btype", banner.getType().getCode());
        } catch (JSONException e) {
            LOG.error("genBannerInfo error", e);
        }
        return jobject;
    }

    /**
     * @param userInfo
     * @return
     */
    public static JSONObject genUserInfo(UserInfo userInfo) {
        JSONObject jobject = new JSONObject();
        if (userInfo == null)
            return jobject;
        try {
            jobject.put("gender", userInfo.getGender().getNick());
        } catch (JSONException e) {
            LOG.error("genuserInfo error", e);
        }
        return jobject;
    }

    public static JSONObject genSiteInfo(MediaInfo mediaInfo) {
        JSONObject jobject = new JSONObject();
        if (mediaInfo == null)
            return jobject;
        try {
            jobject.put("url", mediaInfo.getUrl());
            jobject.put("bidfloor", mediaInfo.getFloorPrice());
            jobject.put("bkeywords", mediaInfo.getBlockKeywords());
            jobject.put("burl", mediaInfo.getBlockUrls());
        } catch (JSONException e) {
            LOG.error("genSiteInfo error", e);
        }
        return jobject;
    }

    public static JSONObject genBidInfo(BidRequestMessage message) {
        JSONObject jobject = new JSONObject();
        if (message == null)
            return jobject;
        RTBMessage rtbInfo = message.getRtbMessage();
        try {
            jobject.put("rid", rtbInfo.getRequestId());
            jobject.put("cid", rtbInfo.getCookieId());
            jobject.put("site", genSiteInfo(rtbInfo.getMediaInfo()));
            if (message.getUserInfo() != null) {
                jobject.put("user", genUserInfo(message.getUserInfo()));
            }
            if (rtbInfo.getBannerInfos() != null && rtbInfo.getBannerInfos().size() > 0) {
                JSONArray array = new JSONArray();
                for (BannerInfo info : rtbInfo.getBannerInfos()) {
                    array.add(genBannerInfo(info));
                }
                jobject.put("banner", array);
            }
        } catch (JSONException e) {
            LOG.error("genSiteInfo error", e);
        }
        return jobject;
    }

    /**
     * according by json back get adInfo by Dsp.
     * @param jsonback
     * @param dspMessage
     * @param cost
     * @return
     */
    public static Map<Integer, AdInfo> getResponseInfo(String jsonback , BidDspMessage dspMessage , long cost) {
        JSONObject json = JSONObject.parseObject(jsonback);
        Map<Integer, AdInfo> adInfos = new HashMap<Integer, AdInfo>();
        if (json.containsKey("bid")) {
            JSONArray array = json.getJSONArray("bid");
            for (int i = 0; i < array.size(); i++) {
                AdInfo adInfo = new AdInfo();
                JSONObject bidInfo = array.getJSONObject(i);
                adInfo.setDspId(dspMessage.getAdvertiserId());
                adInfo.setBannerId(bidInfo.getInteger("slotId"));
                adInfo.setLink(bidInfo.getString("link"));
                adInfo.setAdId(bidInfo.getInteger("adId"));
                adInfo.setNotifyUrl(dspMessage.getInfo().getWinnerNotifyUrl());
                adInfo.setStatus(BidStatus.SUCCESS);
                adInfo.setCost(cost);
                adInfo.setSegment(bidInfo.getString("segment"));
                adInfo.setPrice(bidInfo.getFloatValue("price"));
                adInfos.put(adInfo.getBannerId(), adInfo);
            }
        }
        return adInfos;
    }
    
    public static JSONObject getWinnerInfo(String requestId , List<AdInfo> adInfos) {
        JSONObject jobject = new JSONObject();
        try {
            jobject.put("requestId", requestId);
            JSONArray array = new JSONArray();
            for (AdInfo adInfo : adInfos) {
                JSONObject ad = new JSONObject();
                ad.put("id", adInfo.getAdId());
                ad.put("bidStatus", adInfo.getStatus().getCode());
                ad.put("displayStatus", getDisplayStatus(requestId, adInfo.getBannerId()));
                ad.put("price", adInfo.getPrice());
                array.add(ad);
            }
            
            jobject.put("banner", array);

        } catch (JSONException e) {
            LOG.error("genSiteInfo error", e);
        }
        return jobject;
    }
    
    private static DisplayStatus getDisplayStatus(String requestId , int bannerId) {
        String key = CacheConstants.KEY_DISPLAY_STATUS + requestId + bannerId;
        Object display = RedisUtils.getInstance().getObject(key);
        return display == null ? DisplayStatus.UNKNOW : DisplayStatus.DISPLAY;
    }

}
