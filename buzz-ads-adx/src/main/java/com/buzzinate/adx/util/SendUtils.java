package com.buzzinate.adx.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.buzzinate.buzzads.core.util.ConfigurationReader;

/**
 * @author james.chen
 * date 2013-7-30
 */
public final class SendUtils implements Serializable {

    private static final long serialVersionUID = -3817579940536137232L;
    private static final Log LOG = LogFactory.getLog(SendUtils.class);
    private static final int PRR_ROUTER = ConfigurationReader.getInt("adx.http.max.rounter", 20);
    private static final int MAX_VALUE = ConfigurationReader.getInt("adx.http.total.thread.count", 100);
    private static final int TIME_OUT = ConfigurationReader.getInt("adx.dsp.timeout", 120);

    private static BidHttpClient httpClient = new BidHttpClient(PRR_ROUTER , MAX_VALUE , TIME_OUT , TIME_OUT);
    
    private SendUtils() {
    }

    public static String bidRequest(String url , String bidInfo) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("bid", bidInfo);
        if (StringUtils.isBlank(url)) {
            LOG.error("bidRequest URl is null" + bidInfo);
            return "";
        }
        return httpClient.post(url, params);
    }
    
    public static String winnerRequest(String url , String bidInfo) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("win", bidInfo);
        if (StringUtils.isBlank(url)) {
            LOG.error("winnerRequest URl is null" + bidInfo);
            return "";
        }
        return httpClient.post(url, params);
    }
}
