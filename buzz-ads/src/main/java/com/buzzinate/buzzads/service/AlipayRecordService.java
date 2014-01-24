package com.buzzinate.buzzads.service;

import com.buzzinate.buzzads.core.service.AdvertiserAccountService;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.dao.AlipayRecordDao;
import com.buzzinate.buzzads.domain.AdvertiserAccount;
import com.buzzinate.buzzads.domain.AlipayRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Alipay record manager service
 *
 * @author chris.xue 2013-3-19
 */
@Service
public class AlipayRecordService {

    @Autowired
    private AlipayRecordDao alipayRecordDao;
    @Autowired
    private RechargeOperateHisService rechargeOperateHisService;
    @Autowired
    private AdvertiserAccountService advertiserAccountService;


    @Transactional(value = "buzzads", readOnly = false)
    public void save(AlipayRecord alipayRecord) {
        alipayRecordDao.saveOrUpdate(alipayRecord);
        //create alipay log record
        rechargeOperateHisService.createModify(alipayRecord.getId());
    }

    /**
     * 根据ID号获取一条支付宝支付记录
     *
     * @param paymentOrderInfoId
     * @return
     */
    public AlipayRecord read(int paymentOrderInfoId) {
        return alipayRecordDao.read(paymentOrderInfoId);
    }

    /**
     * 根据订单号获取支付宝支付记录
     *
     * @param orderNo
     * @return
     */
    public AlipayRecord getOrderInfo(String orderNo) {
        Map<String, Object> matchers = new HashMap<String, Object>();
        matchers.put("tradeNo", orderNo);
        List<AlipayRecord> records = alipayRecordDao.query(matchers);
        if (records != null && records.size() > 0) {
            return records.get(0);
        }
        return null;
    }

    /**
     * 根据条件获取支付宝支付记录
     *
     * @param conditions
     * @param pagination
     * @return
     */
    public List<AlipayRecord> getAlipayRecords(Map<String, Object> conditions, Pagination pagination) {
        List<AlipayRecord> alipayRecords = alipayRecordDao.getAlipayRecords(conditions, pagination);

        if (alipayRecords != null && !alipayRecords.isEmpty()) {
            List<Integer> userIds = new ArrayList<Integer>();
            for (AlipayRecord alipayRecord : alipayRecords) {
                userIds.add(alipayRecord.getAdvertiserId());
            }


            for (AlipayRecord alipayRecord : alipayRecords) {
                AdvertiserAccount advertiserAccount = advertiserAccountService
                        .getAdvertiserAccount(alipayRecord.getAdvertiserId());
                if (advertiserAccount != null) {
                    alipayRecord.setUserName(advertiserAccount.getCompanyName());
                }
            }
        }
        return alipayRecords;
    }

    public Long getAlipayRecordsCount(Map<String, Object> conditions) {
        return alipayRecordDao.getAlipayRecordsCount(conditions);
    }

    public Long getTotalAmount(Map<String, Object> conditions) {
        return alipayRecordDao.getTotalAmount(conditions);
    }
}
