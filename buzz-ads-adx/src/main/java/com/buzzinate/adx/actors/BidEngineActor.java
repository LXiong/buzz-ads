package com.buzzinate.adx.actors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.buzzinate.adx.entity.AdInfo;
import com.buzzinate.adx.entity.BannerInfo;
import com.buzzinate.adx.enums.BidStatus;
import com.buzzinate.adx.message.BidResponseMessage;
import com.buzzinate.adx.message.WinnerMessage;
import com.buzzinate.adx.util.HtmlUtil;

/**
 * @author chris
 * Created with IntelliJ IDEA.
 * Date: 13-7-22
 * Time: 下午6:43 竞价引擎
 */
public class BidEngineActor extends UntypedActor {
    private static Log log = LogFactory.getLog(BidEngineActor.class);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof BidResponseMessage) {
            long start = System.currentTimeMillis();
            BidResponseMessage bidResponseMessage = (BidResponseMessage) message;
            final WinnerMessage winnerMessage = getWinner(bidResponseMessage);
            final ActorRef dataActor = getContext().actorOf(new Props(DataActor.class));
            long end = System.currentTimeMillis();
            log.info("cost=" + (end - start) + ",end time=" + end + ",start=" + start);
            getSender().tell(winnerMessage);
            dataActor.tell(winnerMessage);
            getContext().stop(getSelf());
        } else {
            log.info("receive unknown message on bidEngineActor");
        }
    }
    
    public WinnerMessage getWinner(BidResponseMessage bidResponseMessage) {
        AdInfo[] adInfos = bidResponseMessage.getAdInfos();
        List<BannerInfo> banners = bidResponseMessage.getBidRequestMessage().getRtbMessage().getBannerInfos();
        List<AdInfo> winners = new ArrayList<AdInfo>();
        List<AdInfo> losers = new ArrayList<AdInfo>();
        Set<Integer> bannerId = new HashSet<Integer>();
        for (BannerInfo banner : banners) {
            bannerId.add(banner.getBannerId());
        }
        if (adInfos == null || adInfos.length == 0 || bannerId.isEmpty()) {
            return new WinnerMessage(winners , losers , bidResponseMessage);
        }
        // prepare floor price
        AdInfo[] adInfosCopies = Arrays.copyOf(adInfos, adInfos.length);
        float floorPrice = bidResponseMessage.getBidRequestMessage().getFloorPrice();
        SortedSet<AdInfo> sortedSet = new TreeSet<AdInfo>();
        for (AdInfo adInfo : adInfosCopies) {
            if (!bannerId.contains(adInfo.getBannerId())) {
                continue;
            }
            if (BidStatus.SUCCESS.equals(adInfo.getStatus())) {
                if (adInfo.getPrice() >= floorPrice) {
                    adInfo.setSecprice(floorPrice);
                    sortedSet.add(adInfo);
                } else if (adInfo.getPrice() == 0) {
                    adInfo.setStatus(BidStatus.NO_BID);
                    losers.add(adInfo);
                } else {
                    adInfo.setStatus(BidStatus.FAILURE);
                    losers.add(adInfo);
                }
            } else {
                losers.add(adInfo);
            }
        }
        //compare bidprice
        getRealWinner(winners, losers, sortedSet);
        return new WinnerMessage(winners , losers , bidResponseMessage);
    }

    private void getRealWinner(List<AdInfo> winners , List<AdInfo> losers , SortedSet<AdInfo> sortedSet) {
        int bannerId = 0;
        boolean success = false;
        for (AdInfo adInfo : sortedSet) {
            // first AdInfo
            if (bannerId != adInfo.getBannerId()) {
                success = false;
                bannerId = adInfo.getBannerId();
            } else if (bannerId == adInfo.getBannerId() && success) {
                adInfo.setStatus(BidStatus.FAILURE);
                losers.add(adInfo);
                continue;
            }
            //segment check
            if (HtmlUtil.isLegalHtml(adInfo.getSegment(), adInfo.getLink())) {
                winners.add(adInfo);
                success = true;
                continue;
            }
            adInfo.setStatus(BidStatus.SEGEMNT_FAIL);
            losers.add(adInfo);
        }
    }
}
