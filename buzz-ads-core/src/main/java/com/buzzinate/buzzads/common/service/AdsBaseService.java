package com.buzzinate.buzzads.common.service;

import java.util.HashMap;
import java.util.Map;

import com.buzzinate.buzzads.common.log.LocalLogBuffer;
import com.buzzinate.buzzads.common.log.LogBaseConstants;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-12-24
 */
public class AdsBaseService implements LogBaseConstants {
    
    /**
     * 记录操作日志
     * @param opType
     * @param objectID
     * @param objectName
     * @param params
     */
    public void addOperationLog(String opType, String opName, String targetUserId, 
            String objectID, Map<String, String> params) {
        Map<String, Object> log = new HashMap<String, Object>();
        log.put(LOG_OP_TYPE, opType);
        log.put(LOG_OP_NAME, opName);
        log.put(LOG_OP_TARGET_USERID, targetUserId);
        log.put(LOG_OBJECT_ID, objectID);
        log.put(LOG_PARAMS, params);
        LocalLogBuffer.add(log);
    }

}
