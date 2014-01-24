package com.buzzinate.adx.actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import akka.actor.UntypedActor;

import com.buzzinate.adx.entity.AdInfo;
import com.buzzinate.adx.message.WinnerMessage;
import com.buzzinate.adx.parse.BidJsonParser;
import com.buzzinate.adx.util.SendUtils;

/**
 * Created with IntelliJ IDEA.
 * @author : kun
 * Date: 13-7-28
 * Time: 下午10:32
 * 通知Winner
 */
public class WinnerNotificationActor extends UntypedActor {
    private static Log log = LogFactory.getLog(WinnerNotificationActor.class);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof WinnerMessage) {
            log.info("WinnerNotificationActor start" + System.currentTimeMillis());
            WinnerMessage winner = (WinnerMessage) message;
            Map<String, List<AdInfo>> winnerUrls = mergeAdInfos(winner.getWinners(), winner.getSegmentlosers());
            for (String url : winnerUrls.keySet()) {
                List<AdInfo> adInfos = winnerUrls.get(url);
                //send winner url.
                if (adInfos.size() > 0) {
                    String requestId = adInfos.get(0).getRequestId();
                    SendUtils.winnerRequest(url, BidJsonParser.getWinnerInfo(requestId, adInfos).toJSONString());
                }
            }
        }
        getContext().stop(getSelf());
    }
    
    
    public Map<String, List<AdInfo>> mergeAdInfos(List<AdInfo> winers , List<AdInfo> segmentslosser) {
        Map<String, List<AdInfo>> results = new HashMap<String, List<AdInfo>>();
        if (winers == null || winers.size() == 0) {
            return results;
        }
        winers.addAll(segmentslosser);
        for (AdInfo adInfo : winers) {
            if (results.containsKey(adInfo.getNotifyUrl())) {
                List<AdInfo> values = results.get(adInfo.getNotifyUrl());
                values.add(adInfo);
            } else {
                List<AdInfo> values = new ArrayList<AdInfo>();
                values.add(adInfo);
                results.put(adInfo.getNotifyUrl(), values);
            }
        }
        return results;
    }
}
