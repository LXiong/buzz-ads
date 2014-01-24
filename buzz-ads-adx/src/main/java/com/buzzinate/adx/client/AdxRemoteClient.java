package com.buzzinate.adx.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.AddressFromURIString;
import akka.actor.Props;
import akka.dispatch.Await;
import akka.dispatch.Future;
import akka.pattern.Patterns;
import akka.routing.DefaultResizer;
import akka.routing.RemoteRouterConfig;
import akka.routing.RoundRobinRouter;
import akka.util.Duration;

import com.buzzinate.adx.actors.DispatcherActor;
import com.buzzinate.adx.message.RTBMessage;
import com.buzzinate.adx.message.WinnerMessage;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.typesafe.config.ConfigFactory;

/**
 * adx rtb process client.
 * @author james.chen
 *
 */
public class AdxRemoteClient implements Serializable, AdxClient {

    private static final long serialVersionUID = -4794631517186912231L;
    
    private static final Log LOG = LogFactory.getLog(AdxRemoteClient.class);
    //clientName
    private static final String CLIENT_NAME = ConfigurationReader.getString("adx.client.system.name");
    //related server address
    private static final String SERVER_ADDRESS = ConfigurationReader.getString("adx.client.remote.address");
    //instance no
    private static final int INTANCENO_LOW = ConfigurationReader.getInt("adx.client.dispatcher.instance.no.low",
            10);
    // up
    private static final int INTANCENO_UP = ConfigurationReader.getInt("adx.client.dispatcher.instance.no.up",
            100);

    private static final int TIME_OUT = ConfigurationReader.getInt("adx.client.dispatch.timeout", 400);
    
    private ActorSystem system;
    private ActorRef  dispatcherRounter;
    
    
    public AdxRemoteClient() {
        system = ActorSystem.create(CLIENT_NAME, ConfigFactory.load().getConfig("remote_client"));
        List<Address> adds = new ArrayList<Address>();
        String[] addresss = StringUtils.split(SERVER_ADDRESS, ",");
        for (String address : addresss) {
            adds.add(AddressFromURIString.parse(address));
        }
        DefaultResizer resize = new DefaultResizer(INTANCENO_LOW , INTANCENO_UP);
        dispatcherRounter =
                system.actorOf(new Props(DispatcherActor.class).withRouter(new RemoteRouterConfig(new RoundRobinRouter(
                        resize) , adds.toArray(new Address[adds.size()]))));
    }
    
    /**
     * reb Result.
     * @param rtbMessage
     * @return
     */
    public WinnerMessage rtbResult(RTBMessage rtbMessage) {
        WinnerMessage winner = null;
        try {
            Future<Object> results = Patterns.ask(dispatcherRounter, rtbMessage, TIME_OUT);
            winner = (WinnerMessage) Await.result(results, Duration.apply(TIME_OUT, TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            LOG.error("rtbResult failed detail errors:", e);
        }
        return winner;
    }
}
