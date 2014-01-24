package com.buzzinate.adx;

/**
 * key Constant for cache
 * @author james.chen
 *
 */
public interface CacheConstants {
    // display status
    String KEY_DISPLAY_STATUS = "DISPLAY:STATUS";

    // dsp cache Info
    String KEY_DSP_INFO_CACHE = "DSP:INFO";
    
    int EXPIRE_HALF_MINUTE = 30;
    // one minute
    int EXPIRE_ONE_MINUTE = 60;
    // half one hour
    int EXPIRE_HALF_ONE_HOUR = EXPIRE_ONE_MINUTE * 30;
    // one hour
    int EXPIRE_ONE_HOUR = EXPIRE_ONE_MINUTE * 60;
    // one day
    int EXPIRE_ONE_DAY = EXPIRE_ONE_HOUR * 24;
}
