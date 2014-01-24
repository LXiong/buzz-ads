package com.buzzinate.adx.actors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import akka.actor.UntypedActor;

import com.buzzinate.adx.entity.AdInfo;
import com.buzzinate.adx.entity.BannerInfo;
import com.buzzinate.adx.enums.BidStatus;
import com.buzzinate.adx.message.BidDspMessage;
import com.buzzinate.adx.parse.BidJsonParser;
import com.buzzinate.adx.util.BidHttpClient;
import com.buzzinate.adx.util.SendUtils;

/**
 * @author james.chen
 * date 2013-7-30
 */
public class DspBidActor extends UntypedActor {
    private static final Log LOG = LogFactory.getLog(DspBidActor.class);

    @Override
    public void onReceive(Object arg0) throws Exception {
        if (arg0 instanceof BidDspMessage) {
            // 1. send bid request. stat info and time stat
            BidDspMessage bidDspInfo = (BidDspMessage) arg0;
            long start = System.currentTimeMillis();
            String bidResponse =
                    SendUtils.bidRequest(bidDspInfo.getInfo().getBidRequestUrl(), bidDspInfo.getBidParam());
            long end = System.currentTimeMillis();
            // 2. send stat info to stat Utils.
            List<AdInfo> adInfos = new ArrayList<AdInfo>();
            LOG.info("cost=" + (end - start) + ",end=" + end + ",url=" + bidDspInfo.getInfo().getBidRequestUrl() +
                    ",params" + bidDspInfo.getBidParam());
            if (BidHttpClient.TIMEOUT.equals(bidResponse)) {
                adInfos = getConnectTimeoutAdInfos(bidDspInfo);
            } else {
                Map<Integer, AdInfo> bidInfos = BidJsonParser.getResponseInfo(bidResponse, bidDspInfo, end - start);
                adInfos = getAdInfosWithBid(bidDspInfo, bidInfos);
            }
            LOG.info("DspBidActor time=" + System.currentTimeMillis() + "sender's path" +
                    getSender().path().toString());
            getSender().tell(adInfos);
            getContext().stop(getSelf());
        }
    }
    
    /**
     * get connect TimeoutAdInfos.
     * @param bidDspInfo
     * @return
     */
    public List<AdInfo> getConnectTimeoutAdInfos(BidDspMessage bidDspInfo) {
        List<AdInfo> adInfos = new ArrayList<AdInfo>();
        List<BannerInfo> bannerInfos = bidDspInfo.getBannerInfos();
        for (BannerInfo bannerInfo : bannerInfos) {
            adInfos.add(createAdInfo(bidDspInfo, bannerInfo, BidStatus.TIMEOUT));
        }
        return adInfos;
    }

    /**get bidAdInfos if nobid add it.
     * @param bidDspInfo
     * @param bidAdInfos
     * @return
     */
    public List<AdInfo> getAdInfosWithBid(BidDspMessage bidDspInfo , Map<Integer, AdInfo> bidAdInfos) {
        List<AdInfo> adInfos = new ArrayList<AdInfo>();
        List<BannerInfo> bannerInfos = bidDspInfo.getBannerInfos();
        for (BannerInfo bannerInfo : bannerInfos) {
            if (bidAdInfos.containsKey(bannerInfo.getBannerId())) {
                adInfos.add(bidAdInfos.get(bannerInfo.getBannerId()));
            } else {
                adInfos.add(createAdInfo(bidDspInfo, bannerInfo, BidStatus.NO_BID));
            }
        }
        return adInfos;
    }

    private AdInfo createAdInfo(BidDspMessage bidDspInfo , BannerInfo bannerInfo , BidStatus status) {
        AdInfo adInfo = new AdInfo();
        adInfo.setDspId(bidDspInfo.getAdvertiserId());
        adInfo.setBannerId(bannerInfo.getBannerId());
        adInfo.setRequestId(bidDspInfo.getRequestId());
        adInfo.setStatus(status);
        return adInfo;
    }
}
