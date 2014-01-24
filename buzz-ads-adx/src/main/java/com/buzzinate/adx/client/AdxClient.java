package com.buzzinate.adx.client;

import com.buzzinate.adx.message.RTBMessage;
import com.buzzinate.adx.message.WinnerMessage;

/**
 * @author james.chen
 *
 */
public interface AdxClient {
    
    WinnerMessage rtbResult(RTBMessage rtbMessage);
}
