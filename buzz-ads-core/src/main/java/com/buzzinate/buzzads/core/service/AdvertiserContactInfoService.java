package com.buzzinate.buzzads.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buzzinate.buzzads.core.dao.AdvertiserContactInfoDao;
import com.buzzinate.buzzads.domain.AdvertiserContactInfo;

/**
 * 
 * @author Johnson
 * 
 */
@Service
public class AdvertiserContactInfoService {

    @Autowired
    private AdvertiserContactInfoDao advertiserContactInfoDao;

    public void saveOrUpdate(AdvertiserContactInfo info) {
        advertiserContactInfoDao.saveOrUpdate(info);
    }

    public List<AdvertiserContactInfo> getAdvertiserContactInfo(int advertiserId) {
        return advertiserContactInfoDao.getAdvertiserContactInfo(advertiserId);
    }

    public List<Integer> getAdvertiserIdsByNameAndEmail(String name, String email) {
        return advertiserContactInfoDao.getAdvertiserIdsByNameAndEmail(name, email);
    }
}
