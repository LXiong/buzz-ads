package com.buzzinate.adx.server;

import akka.actor.ActorSystem;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.typesafe.config.ConfigFactory;

/**
 * the Adx Server install server.
 * @author james.chen
 *
 */
public class AdxServer {
    private static String serverName = ConfigurationReader.getString("adx.server.system.name");

    public static void main() {
        ActorSystem.create(serverName, ConfigFactory.load().getConfig("remote_server"));
    }
}
