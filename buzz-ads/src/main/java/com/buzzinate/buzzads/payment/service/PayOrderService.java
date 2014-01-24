/**
 *
 */
package com.buzzinate.buzzads.payment.service;

import com.buzzinate.buzzads.payment.bean.PaymentOrderInfo;
import com.buzzinate.buzzads.payment.dao.PaymentOrderDao;
import com.buzzinate.common.util.service.GenericServiceSupport;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jerry.Ma
 */
@Service
public class PayOrderService extends
        GenericServiceSupport<PaymentOrderInfo, Integer, PaymentOrderDao>
        implements java.io.Serializable {

    private static final long serialVersionUID = 6265372687509988347L;

    public void save(PaymentOrderInfo orderInfo) {
        super.getDao().create(orderInfo);
    }

    public PaymentOrderInfo getPaymentOrderInfo(String orderNo) {

        Map<String, Object> matchers = new HashMap<String, Object>();
        matchers.put("orderNo", orderNo);
        List<PaymentOrderInfo> list = super.getDao().query(matchers);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public void update(PaymentOrderInfo value) {
        super.getDao().update(value);
    }

    /**
     * Gets all payment order info associated to the given userId
     *
     * @param userId
     * @return
     * @status @see com.buzzinate.payment.PayConfig
     */
    public List<PaymentOrderInfo> getPaymentOrderInfoByUserId(int userId, int status) {

        Map<String, Object> matchers = new HashMap<String, Object>();
        matchers.put("userId", userId);
        matchers.put("orderStatus", status);
        List<PaymentOrderInfo> list = super.getDao().query(matchers);
        if (list.size() == 0) {
            return null;
        }
        return list;
    }

    /**
     * Gets all payment order info
     *
     * @return
     * @status @see com.buzzinate.payment.PayConfig
     * Pass in null to get all.
     * @searchOrderNo matches the orderNo.
     */
    public List<PaymentOrderInfo> getPaymentOrderInfoByStatusAll(Integer status, String searchOrderNo) {
        if (searchOrderNo != null && !searchOrderNo.trim().isEmpty()) {
            if (status != null) {
                return getDao().getPaymentOrderInfoByStatusAll(status, searchOrderNo);
            }
            return getDao().getPaymentOrderInfoByStatusAll(searchOrderNo);
        }
        Map<String, Object> matchers = new HashMap<String, Object>();
        if (status != null) {
            matchers.put("orderStatus", status);
        }
        return super.getDao().query(matchers);
    }
}
