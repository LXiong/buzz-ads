package com.buzzinate.buzzads.analytics.processor;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.buzzinate.buzzads.analytics.AdAnalyticsConstants;
import com.buzzinate.buzzads.core.service.AdxDspRtbInfoService;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.core.util.RedisClient;
import com.buzzinate.buzzads.domain.AdxDspRtbInfo;
import com.buzzinate.buzzads.domain.AdxDspRtbStatistic;
import com.buzzinate.data.entry.DspStatisticInfo;

/**
 * dsp分数通知并写入数据库
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 *      2013-7-24
 *      
 * */
public class DataNotifyTask implements Runnable{

    private static Log log = LogFactory.getLog(DataNotifyTask.class); 
    private Map<Integer, DspStatisticInfo> totalDataMap;
    private static int thresholdWarning = ConfigurationReader.getInt("analytics.data.threshold.warning");
//    private static int thresholdSuspend = ConfigurationReader.getInt("analytics.data.threshold.suspend");
    private static final int DSP_SCORE_CACHE_EXPIRE = 3600 * 2;
    
    @Autowired
    private AdxDspRtbInfoService adxDspRtbInfoService;
    @Autowired
    private RedisClient redisClient;
    
    public DataNotifyTask(final Map<Integer, DspStatisticInfo> totalDataMap) {
        super();
        this.totalDataMap = totalDataMap;
    }
    
    public DataNotifyTask() {
        super();
    }

    @Override
    public void run() {

        this.totalDataMap = AdxDspScoreProcess.dataMap;
        if (totalDataMap.isEmpty()) {
           log.debug("没有数据需要处理");
           return; 
        }
        int dspPercent; 
        // 发送消息订阅给任务模块, 讲dsp评分信息放入redis。
        redisClient.publish(AdAnalyticsConstants.ADX_DSP_SCORE_PUBSUB_CHANNEL,
                AdAnalyticsConstants.ADX_DSP_SCORE_PUBSUB_MESSAGE);
        redisClient.set(AdAnalyticsConstants.ADX_DSP_SCORE, DSP_SCORE_CACHE_EXPIRE, totalDataMap.values());
        for (DspStatisticInfo dspInfo : totalDataMap.values()) {
            dspPercent = dspInfo.getSuccessPercent();
            int key = dspInfo.getKey();
            try {
                AdxDspRtbStatistic rtbStats = new AdxDspRtbStatistic(key, dspInfo.getRequestCount(), 
                        dspInfo.getResponseCount(), dspInfo.getBidSuccCount(), dspInfo.getValidCount());
                adxDspRtbInfoService.updateAdxDspStatistic(rtbStats);
                if (dspPercent <= thresholdWarning) {
                    // mail通知管理员(超时或者异常都log记录)
                    AdxDspRtbInfo rtbInfo = adxDspRtbInfoService.getAdxDspRtbInfoByKey(key);
                    adxDspRtbInfoService.sendDspWarningEmail(rtbInfo, rtbStats);
                }
            } catch (Exception e) {
                log.error("dspInfo" + dspInfo + "\n exception: "  + e);
            }
        }

    }

}
