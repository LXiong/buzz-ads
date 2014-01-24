package com.buzzinate.buzzads.analytics.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.buzzinate.adx.entity.AdInfo;
import com.buzzinate.adx.enums.BidStatus;
import com.buzzinate.adx.message.WinnerMessage;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.common.util.task.AbstractProcessor;
import com.buzzinate.data.entry.DspStatisticInfo;


/**
 * 数据处理
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 *      2013-7-24
 *      
 * */
public class AdxDspScoreProcess extends AbstractProcessor {
    
//    private static Log log = LogFactory.getLog(AdxDspScoreProcess.class);
    public static Map<Integer, DspStatisticInfo> dataMap = new HashMap<Integer, DspStatisticInfo>();
    
    public AdxDspScoreProcess() {
        super(1000, ConfigurationReader.getInt("adx.report.buffer.size"), ConfigurationReader.getInt("adx.report.max.waiting.interval"));
    }

    @Override
    protected void workerFunction(List<Object> stats) {
     // 数据位一个长度为两位的数组，第一位记录（第一位记录dspId，第二位记录消息类型）
        for (Object obj : stats) {
            if (obj instanceof WinnerMessage) {
                WinnerMessage winMsg = (WinnerMessage) obj;
                // 取得成功信息和失败信息
                List<AdInfo> winners = winMsg.getWinners();
                List<AdInfo> segmentlosers = winMsg.getSegmentlosers();
                winners.addAll(segmentlosers);
                for (AdInfo adInfo : winners) {
                    Integer key = adInfo.getDspId();
                    BidStatus status = adInfo.getStatus();
                    processData(key, status);
                }
            }
        }
    }
    
    /**
     * 统计dsp各项指标数据
     * */
    private void processData(Integer key, BidStatus status) {
        if (dataMap.containsKey(key)) {
            DspStatisticInfo dspInfo = dataMap.get(key);
            dspInfo.increaseMsgCount(status);
        } else {
            DspStatisticInfo dspInfo = new DspStatisticInfo(key);
            dspInfo.increaseMsgCount(status);
            dataMap.put(key, dspInfo);
        }
    }
        
    /**
     * 取得当前时刻所有线程处理完成的各项指标数据
     * 
     * */
//    public Map<Integer, DspInfo> getTotalDataMap() {
//        Map<Integer, DspInfo> totalDataMap = new HashMap<Integer, DspInfo>();
//        for (Map<Integer, DspInfo> tmpMap : dataMapList) {
//            for (Integer dspId : tmpMap.keySet()) {
//                if (totalDataMap.containsKey(dspId)) {
//                    DspInfo dspInfo = totalDataMap.get(dspId);
//                    dspInfo.mergeCountInfo(tmpMap.get(dspId));
//                } else {
//                    totalDataMap.put(dspId, tmpMap.get(dspId));
//                }
//                // clear data
//                tmpMap.remove(dspId);
//            }
//        }
//        return totalDataMap;
//    }

}
