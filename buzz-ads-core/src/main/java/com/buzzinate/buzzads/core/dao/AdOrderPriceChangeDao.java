package com.buzzinate.buzzads.core.dao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.domain.AdOrderPriceChange;

/**
 * 
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 * 
 *         2013-06-14
 */
@Component
@Transactional(value = "buzzads", readOnly = true)
public class AdOrderPriceChangeDao extends AdsDaoBase<AdOrderPriceChange, Integer> {

    public AdOrderPriceChangeDao() {
        super(AdOrderPriceChange.class, "id");
    }

}
