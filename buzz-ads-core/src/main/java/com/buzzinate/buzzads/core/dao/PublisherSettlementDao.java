package com.buzzinate.buzzads.core.dao;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.PublisherSettlement;
import com.buzzinate.buzzads.enums.SettlementStatusEnum;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *         <p/>
 *         2012-11-26
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class PublisherSettlementDao extends AdsDaoBase<PublisherSettlement, Integer> {

    public PublisherSettlementDao() {
        super(PublisherSettlement.class, "id");
    }

    @SuppressWarnings("unchecked")
    public List<PublisherSettlement> getByUserId(int userId) {
        Query query = getSession().getNamedQuery("PublisherSettlementDao.getByUserId");
        query.setInteger("userId", userId);
        return (List<PublisherSettlement>) query.list();
    }

    public Long countByUserId(int userId) {
        Query query = getSession().getNamedQuery("PublisherSettlementDao.countByUserId");
        query.setInteger("userId", userId);
        return (Long) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<PublisherSettlement> getByUserId(int userId, Pagination page) {
        Query query = getSession().getNamedQuery("PublisherSettlementDao.getByUserId");
        query.setInteger("userId", userId);
        query.setFirstResult(page.getStartRow());
        query.setMaxResults(page.getPageSize());
        List<PublisherSettlement> entries = query.list();
        page.setTotalRecords(countByUserId(userId).intValue());
        page.validatePageInfo();
        page.setReturnRecords(entries.size());
        return entries;
    }

    @SuppressWarnings("unchecked")
    public List<PublisherSettlement> listByMonth(Date month, Pagination page) {
        // 处理分页
        Query query = getSession().getNamedQuery("PublisherSettlementDao.listByMonth.count");
        query.setDate("month", month);
        page.setTotalRecords((Integer) query.uniqueResult());
        page.validatePageInfo();

        Query q = getSession().getNamedQuery("PublisherSettlementDao.listByMonth");
        q.setDate("month", month);
        q.setInteger("start", page.getStartRow());
        q.setInteger("max", page.getPageSize());
        List<Object[]> lists = q.list();
        page.setReturnRecords(lists.size());
        // 转换
        List<PublisherSettlement> pubs = new ArrayList<PublisherSettlement>();
        for (Object[] objs : lists) {
            PublisherSettlement ps = new PublisherSettlement();
            ps.setUserId((Integer) objs[0]);
            ps.setCommission((Integer) objs[1]);
            pubs.add(ps);
        }
        return pubs;
    }

    @Transactional(value = "buzzads", readOnly = false)
    public void insertOrUpdate(int userId, Date month, int cpsNo, int cpsCommission, int cpcNo, int cpcCommission, int cpmNo, BigDecimal cpmCommission,
                               Date updateDay) {
        Query query = getSession().getNamedQuery("PublisherSettlementDao.insertOrUpdate");
        query.setInteger("userId", userId);
        query.setDate("month", month);
        query.setInteger("cpsNo", cpsNo);
        query.setInteger("cpsCommission", cpsCommission);
        query.setInteger("cpcNo", cpcNo);
        query.setInteger("cpcCommission", cpcCommission);
        query.setInteger("cpmNo", cpmNo);
        query.setBigDecimal("cpmCommission", cpmCommission);
        query.setInteger("commission", cpsCommission + cpcCommission + cpmCommission.multiply(new BigDecimal(100))
                .setScale(0, RoundingMode.HALF_DOWN).intValue());
        query.setDate("updateDay", updateDay);
        query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<PublisherSettlement> listByUserAmountMonth(List<Integer> userIds, Integer amount, Date month,
                                                           Pagination page) {
        // 处理分页
        Query query = getSession().getNamedQuery("PublisherSettlementDao.listByUserAmountMonth.count");
        query.setParameterList("userIds", userIds);
        query.setInteger("amount", amount);
        query.setDate("month", month);
        page.setTotalRecords((Integer) query.uniqueResult());
        page.validatePageInfo();

        Query q = getSession().getNamedQuery("PublisherSettlementDao.listByUserAmountMonth");
        q.setParameterList("userIds", userIds);
        q.setInteger("amount", amount);
        q.setDate("month", month);
        q.setInteger("start", page.getStartRow());
        q.setInteger("max", page.getPageSize());
        List<Object[]> lists = q.list();
        page.setReturnRecords(lists.size());
        // 转换
        List<PublisherSettlement> pubs = new ArrayList<PublisherSettlement>();
        for (Object[] objs : lists) {
            PublisherSettlement ps = new PublisherSettlement();
            ps.setUserId((Integer) objs[0]);
            ps.setCommission((Integer) objs[1]);
            pubs.add(ps);
        }

        return pubs;
    }

    @SuppressWarnings("unchecked")
    public List<PublisherSettlement> listByUserMonth(List<Integer> userIds, Date month, Pagination page) {
        // 处理分页
        Query query = getSession().getNamedQuery("PublisherSettlementDao.listByUserMonth.count");
        query.setParameterList("userIds", userIds);
        query.setDate("month", month);
        page.setTotalRecords(((Long) query.uniqueResult()).intValue());
        page.validatePageInfo();

        Query q = getSession().getNamedQuery("PublisherSettlementDao.listByUserMonth");
        q.setParameterList("userIds", userIds);
        q.setDate("month", month);
        q.setInteger("start", page.getStartRow());
        q.setInteger("max", page.getPageSize());
        List<Object[]> lists = q.list();
        page.setReturnRecords(lists.size());
        // 转换
        List<PublisherSettlement> pubs = new ArrayList<PublisherSettlement>();
        for (Object[] objs : lists) {
            PublisherSettlement ps = new PublisherSettlement();
            ps.setUserId((Integer) objs[0]);
            ps.setCommission((Integer) objs[1]);
            pubs.add(ps);
        }
        return pubs;
    }

    @SuppressWarnings("unchecked")
    public List<PublisherSettlement> listByAmountMonth(Integer amount, Date month, Pagination page) {
        // 处理分页
        Query query = getSession().getNamedQuery("PublisherSettlementDao.listByAmountMonth.count");
        query.setDate("month", month);
        query.setInteger("amount", amount);
        page.setTotalRecords((Integer) query.uniqueResult());
        page.validatePageInfo();

        Query q = getSession().getNamedQuery("PublisherSettlementDao.listByAmountMonth");
        q.setDate("month", month);
        q.setInteger("amount", amount);
        q.setInteger("start", page.getStartRow());
        q.setInteger("max", page.getPageSize());
        List<Object[]> lists = q.list();
        page.setReturnRecords(lists.size());
        // 转换
        List<PublisherSettlement> pubs = new ArrayList<PublisherSettlement>();
        for (Object[] objs : lists) {
            PublisherSettlement ps = new PublisherSettlement();
            ps.setUserId((Integer) objs[0]);
            ps.setCommission((Integer) objs[1]);
            pubs.add(ps);
        }
        return pubs;
    }

    @SuppressWarnings("unchecked")
    public List<PublisherSettlement> findCanSettlesByUserId(int userId, Date settleMonth) {
        Query query = getSession().getNamedQuery("PublisherSettlementDao.findCanSettlesByUserId");
        query.setInteger("userId", userId);
        query.setDate("settleMonth", settleMonth);
        return query.list();
    }

    @Transactional(value = "buzzads", readOnly = false)
    public Integer updatePublisherSettleStatus(List<Integer> ids, Integer paymentId, Date payTime) {
        Query query = getSession().getNamedQuery("PublisherSettlementDao.updatePublisherSettleStatus");
        query.setParameterList("ids", ids);
        query.setInteger("payId", paymentId);
        query.setTimestamp("payTime", payTime);
        return query.executeUpdate();
    }

    @Transactional(value = "buzzads", readOnly = false)
    public Integer rollbackPay(Integer paymentId) {
        Query query = getSession().getNamedQuery("PublisherSettlementDao.rollbackPay");
        query.setInteger("payId", paymentId);
        return query.executeUpdate();
    }

    public PublisherSettlement getByUserIdAndMonth(int userId, Date month) {
        Query query = getSession().getNamedQuery("PublisherSettlementDao.getByUserIdAndMonth");
        query.setInteger("userId", userId);
        query.setDate("month", month);
        return (PublisherSettlement) query.uniqueResult();
    }

    public int getTotalCommissionByUserMonth(List<Integer> userIds, Date month) {
        Query query = getSession().getNamedQuery("PublisherSettlementDao.getTotalCommissionByUserMonth");
        query.setParameterList("userIds", userIds);
        query.setDate("month", month);
        return query.uniqueResult() == null ? 0 : (Integer) query.uniqueResult();
    }

    public int getTotalCommissionByUserAmountMonth(List<Integer> userIds, int amount, Date month) {
        Query query = getSession().getNamedQuery("PublisherSettlementDao.getTotalCommissionByUserAmountMonth");
        query.setParameterList("userIds", userIds);
        query.setDate("month", month);
        query.setInteger("amount", amount);
        return query.uniqueResult() == null ? 0 : (Integer) query.uniqueResult();
    }

    public int getTotalCommissionByAmountMonth(int amount, Date month) {
        Query query = getSession().getNamedQuery("PublisherSettlementDao.getTotalCommissionByAmountMonth");
        query.setDate("month", month);
        query.setInteger("amount", amount);
        return query.uniqueResult() == null ? 0 : (Integer) query.uniqueResult();
    }

    public int getTotalCommissionByMonth(Date month) {
        Query query = getSession().getNamedQuery("PublisherSettlementDao.getTotalCommissionByMonth");
        query.setDate("month", month);
        return query.uniqueResult() == null ? 0 : (Integer) query.uniqueResult();
    }

    public int getTotalCommByStatus(SettlementStatusEnum status) {
        Query query = getSession().getNamedQuery("PublisherSettlementDao.getTotalCommByStatus");
        query.setInteger("status", status.getCode());
        return query.uniqueResult() == null ? 0 : (Integer) query.uniqueResult();
    }
}
