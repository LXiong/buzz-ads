package com.buzzinate.adx.client;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Await;
import akka.dispatch.Future;
import akka.pattern.Patterns;
import akka.util.Duration;

import com.buzzinate.adx.actors.DispatcherActor;
import com.buzzinate.adx.message.RTBMessage;
import com.buzzinate.adx.message.WinnerMessage;
import com.buzzinate.buzzads.core.util.ConfigurationReader;

/**
 * @author james.chen
 *
 */
public class AdxLocalClient implements Serializable, AdxClient {

    private static final long serialVersionUID = -4794631517186912231L;
    
    private static final Log LOG = LogFactory.getLog(AdxLocalClient.class);
    //clientName
    private static final String CLIENT_NAME = ConfigurationReader.getString("adx.client.system.name");

    private static final int TIME_OUT = ConfigurationReader.getInt("adx.client.dispatch.timeout", 400);
    
    private ActorSystem system;
    private ActorRef  dispatcherRounter;
    
    
    public AdxLocalClient() {
        system = ActorSystem.create(CLIENT_NAME);
    }
    
    /**
     * reb Result.
     * @param rtbMessage
     * @return
     */
    public WinnerMessage rtbResult(RTBMessage rtbMessage) {
        WinnerMessage winner = null;
        try {
            long start = System.currentTimeMillis();
            dispatcherRounter = system.actorOf(new Props(DispatcherActor.class));
            Future<Object> results = Patterns.ask(dispatcherRounter, rtbMessage, TIME_OUT);
            winner = (WinnerMessage) Await.result(results, Duration.apply(TIME_OUT, TimeUnit.MILLISECONDS));
            LOG.info("rtbResult back:" + winner + ",cost=" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            LOG.error("rtbResult failed detail errors:", e);
        }
        return winner;
    }
}
