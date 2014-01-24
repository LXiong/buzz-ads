package com.buzzinate.adx.util;

import org.apache.commons.lang3.StringUtils;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorContext;
import akka.routing.DefaultResizer;
import akka.routing.RoundRobinRouter;
import akka.routing.RouterConfig;

import com.buzzinate.adx.actors.BidEngineActor;
import com.buzzinate.adx.actors.CookieActor;
import com.buzzinate.adx.actors.DataActor;
import com.buzzinate.adx.actors.DspBidActor;
import com.buzzinate.adx.actors.DspManageActor;
import com.buzzinate.adx.actors.WinnerNotificationActor;
import com.buzzinate.buzzads.core.util.ConfigurationReader;

/**
 * @author james.chen
 *
 */
public final class ActorRouterUtil {
    
    private static final int DEFAUL_LOW = ConfigurationReader.getInt("adx.rounter.low.default", 10);
    private static final int DEFAUL_UP = ConfigurationReader.getInt("adx.rounter.up.default", 100);
    private static final int DSP_BID_LOW = ConfigurationReader.getInt("adx.rounter.low.dspbid", 10);
    private static final int DSP_BID_UP = ConfigurationReader.getInt("adx.rounter.up.dspbid", 100);
    
    private static ActorRef cookieRouter;
    private static ActorRef dspMangerRouter;
    private static ActorRef dspBidRouter;
    private static ActorRef dataRouter;
    private static ActorRef bidEngineRouter;
    private static ActorRef winnerNotifyRouter;
    
    private ActorRouterUtil() {
    }
    
    private static RouterConfig getDefaultRounConfig() {
        return new RoundRobinRouter(new DefaultResizer(DEFAUL_LOW , DEFAUL_UP));
    }
    
    /**
     * @param contex
     * @param clazz
     * @return
     */
    private static ActorRef createRouterActor(UntypedActorContext contex , Class<? extends Actor> clazz ,
            RouterConfig router , String dispather) {
        Props props = new Props(clazz).withRouter(router);
        if (StringUtils.isNotBlank(dispather)) {
            props = props.withDispatcher(dispather);
        }
        return contex.actorOf(props);
    }

    private static ActorRef createRounterActorDefault(UntypedActorContext contex , Class<? extends Actor> clazz ,
            String dispatcher) {
        return createRouterActor(contex, clazz, getDefaultRounConfig(), dispatcher);
    }

    public static ActorRef getCookieRouter(UntypedActorContext contex) {
        return cookieRouter == null ? createRounterActorDefault(contex, CookieActor.class, "") : cookieRouter;
    }

    public static ActorRef getDspMangerRouter(UntypedActorContext contex) {
        if (dspMangerRouter == null) {
            return createRounterActorDefault(contex, DspManageActor.class, "dspManageDispatcher");
        }
        return dspMangerRouter;
    }
    
    public static ActorRef getDataRouter(UntypedActorContext contex) {
        return dataRouter == null ? createRounterActorDefault(contex, DataActor.class, "") : dataRouter;
    }
    
    public static ActorRef getBidEngineRouter(UntypedActorContext contex) {
        if (bidEngineRouter == null) {
            return createRounterActorDefault(contex, BidEngineActor.class, "BidEngineDispatcher");
        }
        return bidEngineRouter;
    }
    
    public static ActorRef getWinnerRouter(UntypedActorContext contex) {
        Class<? extends Actor> clazz = WinnerNotificationActor.class;
        return winnerNotifyRouter == null ? createRounterActorDefault(contex, clazz, "") : winnerNotifyRouter;
    }
    
    public static ActorRef getDspBidRouter(UntypedActorContext contex) {
        RouterConfig bidConfig = new RoundRobinRouter(new DefaultResizer(DSP_BID_LOW , DSP_BID_UP));
        if (dspBidRouter == null) {
            return createRouterActor(contex, DspBidActor.class, bidConfig, "dspbidDispatcher");
        }
        return dspBidRouter;
    }

}
