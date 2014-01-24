package com.buzzinate.buzzads.core.dao;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.core.bean.AdBanner;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-7-24
 * Time: 下午12:23
 * Admax uuid url映射
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class AdBannerDao extends AdsDaoBase<AdBanner, Integer> {

    public AdBannerDao() {
        super(AdBanner.class, "id");
    }
}
