package com.buzzinate.buzzads.common.log;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.domain.OperationLog;
/**
 * 日志记录
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-12-24
 */
@Transactional(value = "buzzads")
@Component
public class OperationLogDao extends AdsDaoBase<OperationLog, Long> {
    
    public OperationLogDao() {
        super(OperationLog.class, "id");
    }

}
