package com.buzzinate.adx.util;

import java.io.Serializable;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.common.util.kestrel.KestrelClient;

/**
 * @author james.chen
 *
 */
public final class KestrelUtil implements Serializable {

    private static final long serialVersionUID = 6351314228803418921L;
    
    private static final String IPS = ConfigurationReader.getString("adx.data.kestrel.ips");
    private static final int POOL_SIZE = ConfigurationReader.getInt("adx.data.kestrel.pool.size");
    
    private static KestrelClient K_CLIENT;
    
    private KestrelUtil() {
    }
    
    static {
        K_CLIENT = new KestrelClient(IPS,POOL_SIZE);
    }

    /**
     * send message
     * @param queueName
     * @param message
     */
    public static void sendMessage(String queueName , Object message) {
        K_CLIENT.put(queueName, message);
    }
}
