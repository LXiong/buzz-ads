package com.buzzinate.buzzads.service;

import com.buzzinate.buzzads.dao.RechargeOperateHisDao;
import com.buzzinate.buzzads.domain.RechargeOperateHis;
import com.buzzinate.buzzads.domain.enums.OperateType;
import com.buzzinate.buzzads.user.LoginHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * RechargeOperateHis Service, handle
 *
 * @author chris.xue 2013-3-19
 * @since 2012-6-25
 */
@Service
public class RechargeOperateHisService {

    private static Log log = LogFactory.getLog(RechargeOperateHisService.class);

    @Autowired
    private RechargeOperateHisDao pointsOperateHisDao;

    @Autowired
    private LoginHelper loginHelper;

    private void createHis(OperateType operateType, int recordId) {
        int userId = 0;
        try {
            userId = loginHelper.getUserId();
            if (recordId == 0 || userId == 0) {
                return;
            }
            RechargeOperateHis his = new RechargeOperateHis(operateType, recordId, userId);
            pointsOperateHisDao.create(his);
        } catch (Exception e) {
            log.error(new StringBuilder("Create His operateType=")
                    .append(operateType).append(",recordId=").append(recordId).append(",userId=").append(userId), e);
        }
    }

    //create view record info
    public void createView(int recordId) {
        createHis(OperateType.VIEW, recordId);
    }

    //create delete record log
    public void createDelete(int recordId) {
        createHis(OperateType.DELETE, recordId);
    }

    // create add record log
    public void createAdd(int recordId) {
        createHis(OperateType.ADD, recordId);
    }

    // create modify record log
    public void createModify(int recordId) {
        createHis(OperateType.MODIFY, recordId);
    }

    public List<RechargeOperateHis> listAll() {
        return pointsOperateHisDao.getOperateHis();
    }
}
