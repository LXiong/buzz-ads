package com.buzzinate.adx.actors;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Scheduler;
import akka.actor.UntypedActor;
import akka.util.Duration;

import com.buzzinate.adx.Constants;
import com.buzzinate.adx.message.BidRequestMessage;
import com.buzzinate.adx.message.BidResponseMessage;
import com.buzzinate.adx.message.RTBMessage;
import com.buzzinate.adx.message.WinnerMessage;

/**
 * Created with IntelliJ IDEA.
 *
 * @author chris
 *         Date: 13-7-22
 *         Time: 上午10:28
 *         分发器Actor
 */
public class DispatcherActor extends UntypedActor {
    private static Log log = LogFactory.getLog(DispatcherActor.class);

    @Override
    public void onReceive(Object message) throws Exception {
        log.info("Dispatcher" + getSender().path().toString());
        final String sendPath = getSender().path().toString();
        if (message instanceof RTBMessage) {
            final RTBMessage rtbMessage = new RTBMessage((RTBMessage) message , sendPath);
            log.info(rtbMessage.getRequestId() + "start = " + System.currentTimeMillis());
            final ActorRef cookieActor = getContext().actorOf(new Props(CookieActor.class));
            cookieActor.tell(rtbMessage, getSelf());
        } else if (message instanceof BidRequestMessage) {
            log.info(" BidRequestMessage start = " + System.currentTimeMillis());
            final BidRequestMessage requestMessage = (BidRequestMessage) message;
            final ActorRef dspman = getContext().actorOf(new Props(DspManageActor.class));
            dspman.tell(requestMessage, getSelf());
        } else if (message instanceof BidResponseMessage) {
            log.info(" BidResponseMessage start = " + System.currentTimeMillis());
            final BidResponseMessage responseMessage = (BidResponseMessage) message;
            final ActorRef bidEngine = getContext().actorOf(new Props(BidEngineActor.class));
            bidEngine.tell(responseMessage, getSelf());
        } else if (message instanceof WinnerMessage) {
            log.info(" winnerMessage start = " + System.currentTimeMillis());
            final WinnerMessage winnerMessage = (WinnerMessage) message;
            String path = winnerMessage.getSendPath();
            log.info("winnerMessage Dispatcher" + path);
            ActorRef ref = getContext().actorFor(path);
            ref.tell(winnerMessage);
            final ActorRef winnerNotice = getContext().actorOf(new Props(WinnerNotificationActor.class));
            Scheduler scheduler = getContext().system().scheduler();
            scheduler.scheduleOnce(Duration.apply(Constants.WINNER_SCHEDULE_TIME, TimeUnit.MILLISECONDS), winnerNotice,
                    winnerMessage);
            getContext().stop(getSelf());
        }
    }

}
