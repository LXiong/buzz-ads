package com.buzzinate.buzzads.dao;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.domain.RechargeOperateHis;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * RechargeOperateHis Dao
 * handle the Data access object from database
 *
 * @author james.chen
 * @since 2012-6-25
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class RechargeOperateHisDao extends AdsDaoBase<RechargeOperateHis, Integer> implements Serializable {

    private static final long serialVersionUID = -758732337863623129L;

    public RechargeOperateHisDao() {
        super(RechargeOperateHis.class, "id");
    }

    @SuppressWarnings("unchecked")
    public List<RechargeOperateHis> getOperateHis() {
        Query query = getSession().createQuery("from adsOperateHis");
        return (List<RechargeOperateHis>) query.list();
    }

}
