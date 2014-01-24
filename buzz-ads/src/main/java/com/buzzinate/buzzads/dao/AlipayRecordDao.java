package com.buzzinate.buzzads.dao;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.AlipayRecord;
import com.buzzinate.buzzads.domain.enums.TradeStatus;
import com.buzzinate.common.util.DateTimeUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Alipay record Dao
 *
 * @author martin
 * @update
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class AlipayRecordDao extends AdsDaoBase<AlipayRecord, Integer> implements Serializable {

    private static final long serialVersionUID = 3618242886439775316L;

    public AlipayRecordDao() {
        super(AlipayRecord.class, "id");
    }

    public int getTotalAmountByDate(Date date) {
        Query query = getSession().getNamedQuery("AlipayRecord.getAdmounCurrntDay");
        Date dateS = DateTimeUtil.getDateDay(date);
        Date dateE = DateTimeUtil.plusDays(dateS, 1);
        query.setDate("dateS", dateS);
        query.setDate("dateE", dateE);
        return (Integer) query.uniqueResult();
    }

    /**
     * 根据条件获取记录列表
     *
     * @param conditions
     * @param pagination
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AlipayRecord> getAlipayRecords(Map<String, Object> conditions, Pagination pagination) {
        Criteria criteria = constructCriteria(conditions);
        criteria.addOrder(Order.desc("createTime"));
        criteria.setFirstResult(pagination.getStartRow());
        criteria.setMaxResults(pagination.getPageSize());
        return (List<AlipayRecord>) criteria.list();
    }

    /**
     * 根据条件获取总充值金额
     *
     * @param conditions
     * @return
     */
    public Long getTotalAmount(Map<String, Object> conditions) {
        Criteria criteria = constructCriteria(conditions);
        return (Long) criteria.setProjection(Projections.sum("amount")).uniqueResult();
    }

    /**
     * 根据条件计算总的记录数
     *
     * @param conditions
     * @return
     */
    public Long getAlipayRecordsCount(Map<String, Object> conditions) {
        Criteria criteria = constructCriteria(conditions);
        return (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
    }

    private Criteria constructCriteria(Map<String, Object> conditions) {
        Criteria criteria = getSession().createCriteria(AlipayRecord.class);
        if (conditions != null && !conditions.isEmpty()) {
            TradeStatus tradeStatus = (TradeStatus) conditions.get("tradeStatus");
            if (tradeStatus != null) {
                criteria.add(Restrictions.eq("tradeStatus", tradeStatus));
            }

            Date dateStart = (Date) conditions.get("dateStart");
            if (dateStart != null) {
                criteria.add(Restrictions.ge("createTime", dateStart));
            }

            Date dateEnd = (Date) conditions.get("dateEnd");
            if (dateEnd != null) {
                criteria.add(Restrictions.le("createTime", dateEnd));
            }
        }
        return criteria;
    }
}
