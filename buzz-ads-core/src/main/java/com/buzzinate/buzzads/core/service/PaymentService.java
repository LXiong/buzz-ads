package com.buzzinate.buzzads.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.exception.ServiceException;
import com.buzzinate.buzzads.common.service.AdsBaseService;
import com.buzzinate.buzzads.core.dao.PaymentDao;
import com.buzzinate.buzzads.core.dao.PublisherContactInfoDao;
import com.buzzinate.buzzads.core.dao.PublisherSettlementDao;
import com.buzzinate.buzzads.core.util.LogConstants;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.Payment;
import com.buzzinate.buzzads.domain.PublisherContactInfo;
import com.buzzinate.buzzads.domain.PublisherSettlement;
import com.buzzinate.common.util.DateTimeUtil;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-12-18
 */
@Service
public class PaymentService extends AdsBaseService {
    
    @Autowired
    private PaymentDao paymentDao;
    @Autowired
    private PublisherSettlementDao publisherSettlementSettleDao;
    @Autowired
    private PublisherContactInfoDao publisherContactInfoDao;
    
    public Payment getPaymentById(int paymentId) {
        return paymentDao.read(paymentId);
    }
    
    /**
     * 支付历史检索
     * @param searchEmail
     * @param receiptNo
     * @return
     */
    @Transactional(value = "buzzads", readOnly = true)
    public List<Payment> listPaymentHistory(String searchEmail, String receiptNo, Pagination page) {
        List<Payment> payments = null;
        List<Integer> userIds = null;
        Map<Integer, PublisherContactInfo> pMap = null;
        if (StringUtils.isNotBlank(searchEmail)) {
            List<PublisherContactInfo> publishers = publisherContactInfoDao.findAllUserByEmail(searchEmail);
            if (publishers.size() == 0) 
                return new ArrayList<Payment>();
            userIds = new ArrayList<Integer>(publishers.size());
            pMap = new HashMap<Integer, PublisherContactInfo>(publishers.size());
            for (PublisherContactInfo p : publishers) {
                userIds.add(p.getUserId());
                pMap.put(p.getUserId(), p);
            }
        } 
        payments =  paymentDao.listPayByUserIdReceiptNo(userIds, receiptNo, page);
        if (payments.size() > 0) {
            //补充站长信息
            if (pMap == null && userIds == null) {
                userIds = new ArrayList<Integer>();
                pMap = new HashMap<Integer, PublisherContactInfo>();
                for (Payment p : payments) {
                    userIds.add(p.getUserId());
                }
                List<PublisherContactInfo> infos = publisherContactInfoDao.findByUserIds(userIds);
                for (PublisherContactInfo p : infos) {
                    pMap.put((int) p.getUserId(), p);
                }
            } 
            for (Payment p : payments) {
                p.setInfo(pMap.get(p.getUserId()));
            }
        }
       
        return payments;
    }
    
    /**
     * 支付佣金
     * 1、查出需要支付的佣金
     * 2、检验是否可以支付
     * 3、保存支付信息，更新支付状态
     * @param payment
     */
    @Transactional(value = "buzzads", readOnly = false)
    public void pay(Payment payment, Date settleMonth) {
        //获取结算数据
        List<PublisherSettlement> settles = publisherSettlementSettleDao.findCanSettlesByUserId(payment.getUserId(), 
                settleMonth);
        if (settles.size() == 0)
            throw new ServiceException("此用户没有需要支付的佣金");
        List<Integer> settleIds = new ArrayList<Integer>();
        //总佣金
        int totalComm = 0;
        for (PublisherSettlement ps : settles) {
            totalComm += ps.getCommission();
            settleIds.add(ps.getId());
        }
        if (totalComm < 10000)
            throw new ServiceException("用户佣金不足100元,禁止支付");
        if ((payment.getPaid() + payment.getFee() + payment.getTax()) != totalComm)
            throw new ServiceException("支付金额错误");
        //结算周期
        String period = DateTimeUtil.formatDate(settles.get(0).getMonth(), DateTimeUtil.FMT_DATE_YYYY_SLASH_MM) + 
                "-" + DateTimeUtil.formatDate(settles.get(settles.size() - 1).getMonth(), 
                        DateTimeUtil.FMT_DATE_YYYY_SLASH_MM);
        payment.setPeriod(period);
        payment.setPaymentTime(new Date());
        payment.setAmount(totalComm);
        payment.setUpdateTime(payment.getPaymentTime());
        //可以支付
        Integer payId = paymentDao.create(payment);
        Integer count = publisherSettlementSettleDao.updatePublisherSettleStatus(settleIds, payId, 
                payment.getPaymentTime());
        if (count.intValue() != settleIds.size())
            throw new ServiceException("数据异常");
        //记录日志
        Map<String, String> param = new HashMap<String, String>();
        param.put("pubId", String.valueOf(payment.getUserId()));
        param.put("month", period);
        param.put("commCount", String.valueOf(payment.getPaid()));
        this.addOperationLog(LogConstants.OPTYPE_MONTHSETTLE_PAY, LogConstants.OBJNAME_MONTHSETTLE, 
                String.valueOf(payment.getUserId()), String.valueOf(payId), param);
    }
    
    /**
     * 撤销支付
     * 1、更新settle为未支付状态
     * 2、删除支付信息
     * @param payment
     */
    @Transactional(value = "buzzads", readOnly = false)
    public void rollbackPay(Integer paymentId) {
        publisherSettlementSettleDao.rollbackPay(paymentId);
        paymentDao.deletePayment(paymentId);
        
        //记录操作日志
        Map<String, String> param = new HashMap<String, String>();
        param.put("payId", String.valueOf(paymentId));
        this.addOperationLog(LogConstants.OPTYPE_MONTHSETTLE_CANCELPAY, LogConstants.OBJNAME_MONTHSETTLE, 
                null, String.valueOf(paymentId), param);
    }

    public List<Payment> getByUserId(int userId) {
        return paymentDao.getByUserId(userId);
    }
}
