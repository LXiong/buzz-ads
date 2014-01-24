package com.buzzinate.adx.message;

import java.io.Serializable;
import java.util.List;

import com.buzzinate.adx.entity.BannerInfo;
import com.buzzinate.buzzads.domain.AdxDspRtbInfo;

/**
 * bid DspInfomation.
 * @author james.chen
 * date 2013-7-30
 */
public final class BidDspMessage implements Serializable {

    private static final long serialVersionUID = -796142699377425530L;
    private final AdxDspRtbInfo info;
    private final String bidParam;
    private final String requestId;
    private final List<BannerInfo> bannerInfos;
    
    public BidDspMessage(String bidParam, AdxDspRtbInfo dsp, String requestId, List<BannerInfo> bannerInfos) {
        this.bidParam = bidParam;
        this.info = dsp;
        this.requestId = requestId;
        this.bannerInfos = bannerInfos;
    }

    public String getBidParam() {
        return bidParam;
    }
    
    public AdxDspRtbInfo getInfo() {
        return info;
    }

    public int getAdvertiserId() {
        return info.getKey();
    }

    public String getRequestId() {
        return requestId;
    }

    public List<BannerInfo> getBannerInfos() {
        return bannerInfos;
    }
    
}
