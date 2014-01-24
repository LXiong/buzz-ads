/**
 *
 */
package com.buzzinate.buzzads.payment.dao;

import com.buzzinate.buzzads.payment.bean.PaymentOrderInfo;
import com.buzzinate.common.util.dao.GenericDaoHibernateImpl;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Jerry.Ma
 */
@Transactional(value = "buzzads", readOnly = true)
@Repository
public class PaymentOrderDao extends GenericDaoHibernateImpl<PaymentOrderInfo, Integer> {

    public PaymentOrderDao() {
        super(PaymentOrderInfo.class, "id");
    }

    /**
     * Gets all payment order info
     *
     * @return
     * @status @see com.buzzinate.payment.PayConfig
     * @searchOrderNo matches the orderNo.
     */
    @SuppressWarnings("unchecked")
    public List<PaymentOrderInfo> getPaymentOrderInfoByStatusAll(Integer status, String searchOrderNo) {
        Query query = getSession().getNamedQuery("paymentOrderInfo.getPaymentOrderInfoByStatus");
        query.setInteger("orderStatus", status);
        query.setString("orderNo", "%" + searchOrderNo + "%");
        return (List<PaymentOrderInfo>) query.list();
    }

    /**
     * Gets all payment order info
     *
     * @return
     * @searchOrderNo matches the orderNo.
     */
    @SuppressWarnings("unchecked")
    public List<PaymentOrderInfo> getPaymentOrderInfoByStatusAll(String searchOrderNo) {
        Query query = getSession().getNamedQuery("paymentOrderInfo.getPaymentOrderInfoByStatusAll");
        query.setString("orderNo", "%" + searchOrderNo + "%");
        return (List<PaymentOrderInfo>) query.list();
    }
}
