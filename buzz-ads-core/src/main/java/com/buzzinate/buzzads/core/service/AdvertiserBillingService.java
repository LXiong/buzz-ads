package com.buzzinate.buzzads.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buzzinate.buzzads.core.dao.AdvertiserBillingDao;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.AdvertiserBilling;
import com.buzzinate.buzzads.enums.AdvertiserBillingType;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 13, 2013 12:03:21 PM
 *
 */
@Service
public class AdvertiserBillingService {

    @Autowired
    private AdvertiserBillingDao advertiserBillingDao;

    public int create(AdvertiserBilling ab) {
        return advertiserBillingDao.create(ab);
    }

    public List<AdvertiserBilling> listBillingsByDay(int advertiserId, Date startDate, Date endDate,
            List<AdvertiserBillingType> types, Pagination page) {
        return advertiserBillingDao.listBillingsByDay(advertiserId, startDate, endDate, types, page);
    }

    public List<AdvertiserBilling> listBillingsByDay(Date startDate, Date endDate, List<AdvertiserBillingType> types,
            Pagination page) {
        return advertiserBillingDao.listBillingsByDay(startDate, endDate, types, page);
    }

    public List<AdvertiserBilling> listBillingsByMonth(int advertiserId, Date startDate, Date endDate,
            List<AdvertiserBillingType> types, Pagination page) {
        List<Integer> params = new ArrayList<Integer>();
        for (AdvertiserBillingType abt : types) {
            params.add(abt.getCode());
        }
        return advertiserBillingDao.listBillingsByMonth(advertiserId, startDate, endDate, params, page);
    }

    public List<AdvertiserBilling> listBillingsByMonth(Date startDate, Date endDate, List<AdvertiserBillingType> types,
            Pagination page) {
        List<Integer> params = new ArrayList<Integer>();
        for (AdvertiserBillingType abt : types) {
            params.add(abt.getCode());
        }
        return advertiserBillingDao.listBillingsByMonth(startDate, endDate, params, page);
    }

    public List<AdvertiserBilling> listBillingByType(List<AdvertiserBillingType> types, Pagination page) {
        return advertiserBillingDao.listBillingsByType(types, page);
    }
    
    public AdvertiserBilling getAdvertiserBillingByDay(int advertiserId, Date startDate, Date endDate) {
        return advertiserBillingDao.getAdvertiserBillingByDay(advertiserId, startDate, endDate);
    }
    
    public AdvertiserBilling getLastAdvertiserBillingByDay(int advertiserId, Date startDate, Date endDate) {
        return advertiserBillingDao.getLastAdvertiserBillingByDay(advertiserId, startDate, endDate);
    }
}
