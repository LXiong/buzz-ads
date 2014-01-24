package com.buzzinate.buzzads.core.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.domain.AdOrderCategory;

/**
 * 
 * @author Johnson
 *
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class AdOrderCategoryDao extends AdsDaoBase<AdOrderCategory, Integer> {
    
    @SuppressWarnings("unchecked")
    public List<AdOrderCategory> getParentCategories() {
        Query query = getSession().getNamedQuery("AdOrderCategory.getParentCategories");
        return (List<AdOrderCategory>) query.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<AdOrderCategory> getSubCategoriesByParent(int parentId) {
        Query query = getSession().getNamedQuery("AdOrderCategory.getSubCategoriesByParent");
        query.setInteger("parentId", parentId);
        return (List<AdOrderCategory>) query.list();
    }

}
