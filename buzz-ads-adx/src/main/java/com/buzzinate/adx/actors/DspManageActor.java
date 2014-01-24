package com.buzzinate.adx.actors;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.buzzinate.adx.cache.DspInfoCache;
import com.buzzinate.adx.entity.AdInfo;
import com.buzzinate.adx.message.BidDspMessage;
import com.buzzinate.adx.message.BidRequestMessage;
import com.buzzinate.adx.message.BidResponseMessage;
import com.buzzinate.adx.parse.BidJsonParser;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.domain.AdxDspRtbInfo;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : chris
 *         Date: 13-7-22
 *         Time: 下午7:03
 *         dsp发送服务mock
 */
@SuppressWarnings("unchecked")
public class DspManageActor extends UntypedActor {
    private static Log log = LogFactory.getLog(DspManageActor.class);
    private static boolean test = ConfigurationReader.getBoolean("adx.test");
    private static DspInfoCache cache = new DspInfoCache();
    private int dspNum;
    private List<AdInfo> adInfos = new ArrayList<AdInfo>();
    private String sendPath;
    private BidRequestMessage temBidRequest;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof BidRequestMessage) {
            log.info("dsp manager start=" + System.currentTimeMillis());
            final BidRequestMessage bidRequest = (BidRequestMessage) message;
            List<AdInfo> dspbids = bidRequest.getRtbMessage().getDspBids();
            if (test && dspbids != null) {
                getSender().tell(new BidResponseMessage(bidRequest , dspbids.toArray(new AdInfo[dspbids.size()])));
            }
            List<AdxDspRtbInfo> dsps = getDsps();
            temBidRequest = bidRequest;
            String params = getBidParams(bidRequest);
            dspNum = dsps.size();
            sendPath = getSender().path().toString();
            // 1.getDspInfos
            for (AdxDspRtbInfo dsp : dsps) {
                final BidDspMessage dspMessage =
                        new BidDspMessage(params , dsp , bidRequest.getRtbMessage().getRequestId() , bidRequest
                                .getRtbMessage().getBannerInfos());
                ActorRef dspActor = getContext().actorOf(new Props(DspBidActor.class));
                dspActor.tell(dspMessage, getSelf());
            }
        } else if (message instanceof List) {
            adInfos.addAll((List<AdInfo>) message);
            dspNum--;
            if (dspNum == 0) {
                ActorRef ref = getContext().actorFor(sendPath);
                ref.tell(new BidResponseMessage(temBidRequest , adInfos.toArray(new AdInfo[adInfos.size()])));
            }
        }
    }

    public List<AdxDspRtbInfo> getDsps() {
        return cache.getDspFromDb();
    }

    public String getBidParams(BidRequestMessage bidMessage) {
        return BidJsonParser.genBidInfo(bidMessage).toString();
    }
}
