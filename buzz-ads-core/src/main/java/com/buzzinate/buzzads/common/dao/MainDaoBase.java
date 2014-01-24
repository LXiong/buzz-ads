package com.buzzinate.buzzads.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.common.util.dao.GenericDaoHibernateImpl;


/**
 * Implement GenericDao using Hibernate session factory.
 * 
 * @author Marvin Zhang
 * 
 * @param <T>
 * @param <PK>
 */
public class MainDaoBase<T, PK extends Serializable> extends GenericDaoHibernateImpl<T, PK> {
    
    public MainDaoBase() { }
    
    public MainDaoBase(Class<T> type) {
        super(type);
    }
    
    public MainDaoBase(Class<T> type, String pkName) {
        super(type, pkName);
    }

    public MainDaoBase(Class<T> type, String pkName, SessionFactory sessionFactory) {
        super(type, pkName, sessionFactory);
    }

    @Override
    @Transactional(readOnly = false)
    public PK create(T o) {
        return super.create(o);
    }

    @Override
    @Transactional(readOnly = true)
    public T read(PK id) {
        return super.read(id);
    }

    @Override
    @Transactional(readOnly = false)
    public void update(T o) {
        super.update(o);
    }

    @Override
    @Transactional(readOnly = false)
    public void saveOrUpdate(T o) {
        super.saveOrUpdate(o);
    }

    @Override
    @Transactional(readOnly = false)
    public void update(Map<String, Object> modifiers,
            Map<String, Object> matchers) {
        super.update(modifiers, matchers);
    }

    @Override
    @Transactional(readOnly = false)
    public void update(Map<String, Object> modifiers, PK id) {
        super.update(modifiers, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> query(Map<String, Object> matchers) {
        return super.query(matchers);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(T o) {
        super.delete(o);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(PK id) {
        super.delete(id);
    }

    @Override
    @Transactional(readOnly = false)
    public void merge(T transientObject) {
        super.merge(transientObject);
    }
}
