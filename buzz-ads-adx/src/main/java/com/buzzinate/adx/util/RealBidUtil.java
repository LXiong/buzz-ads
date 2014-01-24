package com.buzzinate.adx.util;

import java.io.Serializable;

import com.buzzinate.adx.client.AdxClient;
import com.buzzinate.adx.client.AdxLocalClient;
import com.buzzinate.adx.client.AdxRemoteClient;
import com.buzzinate.adx.message.RTBMessage;
import com.buzzinate.adx.message.WinnerMessage;
import com.buzzinate.buzzads.core.util.ConfigurationReader;

/**
 * @author james.chen
 *
 */
public final class RealBidUtil implements Serializable {
    private static final long serialVersionUID = 6041617996588312931L;
    private static final boolean SERVER_BUTTON = ConfigurationReader.getBoolean("adx.rtb.user.server");
    private static final AdxClient ADX_CLIENT;
    
    private RealBidUtil() {
    }

    static {
        ADX_CLIENT = SERVER_BUTTON ? new AdxRemoteClient() : new AdxLocalClient();
    }

    public static WinnerMessage rtb(RTBMessage message) {
        return ADX_CLIENT.rtbResult(message);
    }
}
