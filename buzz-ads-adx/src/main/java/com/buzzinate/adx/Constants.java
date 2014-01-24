package com.buzzinate.adx;

import com.buzzinate.buzzads.core.util.ConfigurationReader;

/**
 * @author james.chen
 * date 2013-7-31
 */
public interface Constants {

    int DSP_TIME_OUT = ConfigurationReader.getInt("adx.dsp.timeout");
    
    int BID_ENGINE_TIMEOUT = ConfigurationReader.getInt("adx.bidengine.timeout");
    
    int COOKIE_TIME_OUT = ConfigurationReader.getInt("adx.cookie.timeout");
    
    int WINNER_SCHEDULE_TIME = ConfigurationReader.getInt("adx.winner.schedule.time");
    
    String COMMA = ",";
}
