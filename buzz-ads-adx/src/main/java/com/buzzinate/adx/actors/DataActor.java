package com.buzzinate.adx.actors;

import akka.actor.UntypedActor;
import com.buzzinate.adx.message.AdDisplayMessage;
import com.buzzinate.adx.message.WinnerMessage;
import com.buzzinate.adx.util.KestrelUtil;
import com.buzzinate.buzzads.analytics.AdAnalyticsConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author chris
 *         Created with IntelliJ IDEA.
 *         Date: 13-7-22
 *         Time: 下午6:56
 *         数据处理Actor.
 */
public class DataActor extends UntypedActor {
    private static Log log = LogFactory.getLog(DataActor.class);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof WinnerMessage) {
            try {
                KestrelUtil.sendMessage(AdAnalyticsConstants.ADXWINMSG_QUEUE, message);
            } catch (Exception e) {
                log.error("Error on DataActor", e);
                log.info("missing message " + message);
            }
        } else if (message instanceof AdDisplayMessage) {
            try {
                log.debug("receive adDisplay message" + message);
                KestrelUtil.sendMessage(AdAnalyticsConstants.ADXDISPLAYMSG_QUEUE, message);
            } catch (Exception e) {
                log.error("Error on DataActor", e);
                log.info("missing message " + message);
            }
        } else {
            log.warn("receive unknown message on DataActor");
        }
        getContext().stop(getSelf());
    }
}
