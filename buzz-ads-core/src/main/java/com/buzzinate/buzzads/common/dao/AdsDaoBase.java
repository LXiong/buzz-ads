package com.buzzinate.buzzads.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.common.util.dao.GenericDaoHibernateImpl;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-11-22
 * @param <T>
 * @param <PK>
 */
public class AdsDaoBase<T, PK extends Serializable> extends
        GenericDaoHibernateImpl<T, PK> {

    public AdsDaoBase() {
    }

    public AdsDaoBase(Class<T> type, String pkName) {
        super(type, pkName);
    }

    public AdsDaoBase(Class<T> type, String pkName,
            SessionFactory sessionFactory) {
        super(type, pkName, sessionFactory);
    }

    @Override
    @Transactional(value = "buzzads", readOnly = false)
    public PK create(T o) {
        return super.create(o);
    }

    @Override
    @Transactional(value = "buzzads", readOnly = true)
    public T read(PK id) {
        return super.read(id);
    }

    @Override
    @Transactional(value = "buzzads", readOnly = false)
    public void update(T o) {
        super.update(o);
    }

    @Override
    @Transactional(value = "buzzads", readOnly = false)
    public void saveOrUpdate(T o) {
        super.saveOrUpdate(o);
    }

    @Override
    @Transactional(value = "buzzads", readOnly = false)
    public void update(Map<String, Object> modifiers,
            Map<String, Object> matchers) {
        super.update(modifiers, matchers);
    }

    @Override
    @Transactional(value = "buzzads", readOnly = false)
    public void update(Map<String, Object> modifiers, PK id) {
        super.update(modifiers, id);
    }

    @Override
    @Transactional(value = "buzzads", readOnly = true)
    public List<T> query(Map<String, Object> matchers) {
        return super.query(matchers);
    }

    @Override
    @Transactional(value = "buzzads", readOnly = false)
    public void delete(T o) {
        super.delete(o);
    }

    @Override
    @Transactional(value = "buzzads", readOnly = false)
    public void delete(PK id) {
        super.delete(id);
    }

    @Override
    @Transactional(value = "buzzads", readOnly = false)
    public void merge(T transientObject) {
        super.merge(transientObject);
    }

    @Override
    @Transactional(value = "buzzads", readOnly = false)
    public List<T> nameQuery(String queryName, Map<String, Object> map) {
        return super.nameQuery(queryName, map);
    }

    @SuppressWarnings("unchecked")
    @Transactional(value = "buzzads", readOnly = true)
    public List<T> getPaginationResult(Criteria criteria, Pagination page) {
        if (page == null) {
            return criteria.list();
        } else {
            // first get the total records in the database for this search...
            Long count = (Long) criteria.setProjection(Projections.rowCount())
                    .uniqueResult();
            page.setTotalRecords(count.intValue());
            page.validatePageInfo();
            // Reset projection and get records for the requested page.
            criteria.setProjection(null);
            criteria.setFirstResult(page.getStartRow());
            criteria.setMaxResults(page.getPageSize());
            List<T> entries = criteria.list();
            page.setReturnRecords(entries.size());
            return entries;
        }
    }
}
