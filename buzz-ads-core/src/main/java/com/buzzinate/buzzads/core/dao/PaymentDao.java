package com.buzzinate.buzzads.core.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.Payment;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-12-18
 */
@Component
@Transactional(value = "buzzads", readOnly = true)
public class PaymentDao extends AdsDaoBase<Payment, Integer> {

    public PaymentDao() {
        super(Payment.class, "id");
    }
    
    public List<Payment> listPayByUserIdReceiptNo(List<Integer> userIds, String receiptNo, Pagination page) {
        Criteria criteria = getSession().createCriteria(Payment.class);
        if (userIds != null)
            criteria.add(Restrictions.in("userId", userIds));
        if (StringUtils.isNotBlank(receiptNo)) 
            criteria.add(Restrictions.like("receiptNo", "%" + receiptNo + "%"));
        criteria.add(Restrictions.eq("status", 1));
        return getPaginationResult(criteria, page);
    }
    
    public Integer countByUserId(int userId) {
        Query query = getSession().getNamedQuery("PaymentDao.countByUserId");
        query.setInteger("userId", userId);
        return (Integer) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<Payment> getByUserId(int userId) {
        Query query = getSession().getNamedQuery("PaymentDao.getByUserId");
        query.setInteger("userId", userId);
        return query.list();
    }
    
    @Transactional(value = "buzzads", readOnly = false)
    public int deletePayment(int paymentId) {
        Query query = getSession().getNamedQuery("PaymentDao.deletePayment");
        query.setInteger("paymentId", paymentId);
        return query.executeUpdate();
    }
}
