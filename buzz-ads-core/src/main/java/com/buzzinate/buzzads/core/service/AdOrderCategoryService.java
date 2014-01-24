package com.buzzinate.buzzads.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buzzinate.buzzads.common.service.AdsBaseService;
import com.buzzinate.buzzads.core.dao.AdOrderCategoryDao;
import com.buzzinate.buzzads.core.util.RedisClient;
import com.buzzinate.buzzads.domain.AdOrderCategory;

/**
 * 
 * @author Johnson
 *
 */
@Service
public class AdOrderCategoryService extends AdsBaseService {
    private static final int ENTRY_CACHE_EXPIRE = 3600 * 24;
    
    private static final String AD_ORDER_CATEGORY_KEY = "AdOrderCategory:";
    private static final String AD_ORDER_CATEGORY_MAP_KEY = "AdOrderCategoryMap:";
    
    @Autowired
    private AdOrderCategoryDao adOrderCategoryDao;
    @Autowired
    private RedisClient redisClient;
    
    
    @SuppressWarnings("unchecked")
    public List<AdOrderCategory> getAllCategories() {
        List<AdOrderCategory> parentCategories = (List<AdOrderCategory>) redisClient.getObject(AD_ORDER_CATEGORY_KEY);
        if (parentCategories != null) {
            return parentCategories;
        } else {
            parentCategories = adOrderCategoryDao.getParentCategories();
            for (AdOrderCategory parentCategory: parentCategories) {
                List<AdOrderCategory> subCategories = adOrderCategoryDao.
                        getSubCategoriesByParent(parentCategory.getId());
                parentCategory.setSubCategories(subCategories);
            }
            redisClient.set(AD_ORDER_CATEGORY_KEY, ENTRY_CACHE_EXPIRE, parentCategories);
        }
        
        return parentCategories;
    }
    
    @SuppressWarnings("unchecked")
    public String getCategoriesName(String categoriesId) {
        String categoriesName = "";
        if (StringUtils.isNotBlank(categoriesId)) {
            Map<Integer, String> categoryMap = (Map<Integer, String>) redisClient.getObject(AD_ORDER_CATEGORY_MAP_KEY);
            if (categoryMap == null) {
                categoryMap = new HashMap<Integer, String>();
                List<AdOrderCategory> categories = getAllCategories();
                for (AdOrderCategory category : categories) {
                    categoryMap.put(category.getId(), category.getName());
                    if (category.getSubCategories() != null) {
                        for (AdOrderCategory subCategory : category.getSubCategories()) {
                            categoryMap.put(subCategory.getId(), subCategory.getName());
                        }
                    }
                }
                redisClient.set(AD_ORDER_CATEGORY_MAP_KEY, ENTRY_CACHE_EXPIRE, categoryMap);
            }
            String[] ids = categoriesId.split(",");
            for (String id : ids) {
                categoriesName = categoriesName + categoryMap.get(Integer.valueOf(id)) + ",";
            }
            categoriesName = StringUtils.removeEnd(categoriesName, ",");
            
        }
        return categoriesName;
    }
    
}
