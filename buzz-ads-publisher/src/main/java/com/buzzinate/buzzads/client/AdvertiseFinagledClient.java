package com.buzzinate.buzzads.client;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.springframework.stereotype.Component;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.thrift.AdItem;
import com.buzzinate.buzzads.thrift.AdParam;
import com.buzzinate.buzzads.thrift.AdServices;
import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ClientBuilder;
import com.twitter.finagle.thrift.ThriftClientFramedCodec;
import com.twitter.finagle.thrift.ThriftClientRequest;
import com.twitter.util.Duration;

/**
 * 
 * @author Johnson
 *
 */
@Component
public final class AdvertiseFinagledClient {
    private static Log log = LogFactory.getLog(AdvertiseFinagledClient.class);
    
    private Service<ThriftClientRequest, byte[]> service = ClientBuilder.safeBuild(ClientBuilder.get().
                hosts(ConfigurationReader.getString("buzzads.thrift.host")). 
                codec(ThriftClientFramedCodec.get()).
                // Retry number, only applies to recoverable errors
                retries(3).
                requestTimeout(Duration.fromMilliseconds(ConfigurationReader.getLong("buzzads.thrift.timeout"))).
                // Connection pool core size
                hostConnectionCoresize(ConfigurationReader.getInt("buzzads.thrift.pool.coreSize")).
                // Connection pool max size
                hostConnectionLimit(ConfigurationReader.getInt("buzzads.thrift.pool.maxActive")).
                // The amount of idle time for which a connection is cached in pool.
                hostConnectionIdleTime(Duration.fromSeconds(ConfigurationReader.getInt("buzzads.thrift.pool.maxIdle"))).
                // The maximum number of connection requests that are queued when the 
                // connection concurrency exceeds the connection limit.
                hostConnectionMaxWaiters(ConfigurationReader.getInt("buzzads.thrift.pool.maxWait")));

    private AdServices.FinagledClient client = new AdServices.FinagledClient(service,
                new TBinaryProtocol.Factory());

    
    public List<AdItem> serve(AdParam param) {
        try {
            return client.serve(param).get();
        } catch (Exception e) {
            log.error("There's an exception when getting the adItem.", e);
        }
        return null;
    }

}
