package com.buzzinate.buzzads.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.exception.ServiceException;
import com.buzzinate.buzzads.common.service.AdsBaseService;
import com.buzzinate.buzzads.core.dao.AdvertiserAccountDao;
import com.buzzinate.buzzads.core.dao.AdvertiserBalanceDao;
import com.buzzinate.buzzads.core.dao.AdvertiserContactInfoDao;
import com.buzzinate.buzzads.core.util.LogConstants;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.core.util.RedisClient;
import com.buzzinate.buzzads.domain.AdvertiserAccount;
import com.buzzinate.buzzads.domain.AdvertiserBalance;
import com.buzzinate.buzzads.domain.AdvertiserContactInfo;
import com.buzzinate.buzzads.enums.AdvertiserStatusEnum;

/**
 * 
 * @author Johnson
 * 
 */
@Service
public class AdvertiserAccountService extends AdsBaseService {

    private static final int ADVERTISERACCOUNT_CACHE_EXPIRE = 3600 * 24;
    @Autowired
    private AdvertiserAccountDao advertiserAccountDao;
    @Autowired
    private AdvertiserContactInfoDao advertiserContactInfoDao;
    @Autowired
    private AdvertiserBalanceDao advertiserBalanceDao;
    @Autowired
    private RedisClient redisClient;

    @Transactional(value = "buzzads", readOnly = false)
    public void savaOrUpdate(AdvertiserAccount account, AdvertiserContactInfo advertiserContactInfo) {
        advertiserAccountDao.saveOrUpdate(account);
        advertiserContactInfoDao.saveOrUpdate(advertiserContactInfo);
        AdvertiserBalance ab = advertiserBalanceDao.read(account.getAdvertiserId());
        if (ab == null) {
            ab = new AdvertiserBalance();
            ab.setAdvertiserId(account.getAdvertiserId());
            advertiserBalanceDao.saveOrUpdate(ab);
        }
        // 清理缓存
        if (account.getAdvertiserId() != 0) {
            redisClient.delete(getAdvertiserAccountCacheKey(account.getAdvertiserId()));
        }
    }
    
    @Transactional(value = "buzzads", readOnly = false)
    public void saveOrUpdate(AdvertiserAccount account) {
        advertiserAccountDao.saveOrUpdate(account);
        AdvertiserBalance ab = advertiserBalanceDao.read(account.getAdvertiserId());
        if (ab == null) {
            ab = new AdvertiserBalance();
            ab.setAdvertiserId(account.getAdvertiserId());
            advertiserBalanceDao.saveOrUpdate(ab);
        }
        // 清理缓存
        if (account.getAdvertiserId() != 0) {
            redisClient.delete(getAdvertiserAccountCacheKey(account.getAdvertiserId()));
        }
    }
    
    @Transactional(value = "buzzads", readOnly = false)
    public void saveOrUpdateContact(AdvertiserContactInfo advertiserContactInfo) {
        advertiserContactInfoDao.saveOrUpdate(advertiserContactInfo);
        AdvertiserBalance ab = advertiserBalanceDao.read(advertiserContactInfo.getAdvertiserId());
        if (ab == null) {
            ab = new AdvertiserBalance();
            ab.setAdvertiserId(advertiserContactInfo.getAdvertiserId());
            advertiserBalanceDao.saveOrUpdate(ab);
        }
        // 清理缓存
        if (advertiserContactInfo.getAdvertiserId() != 0) {
            redisClient.delete(getAdvertiserAccountCacheKey(advertiserContactInfo.getAdvertiserId()));
        }
    }

    public AdvertiserAccount getAdvertiserAccount(int advertiserId) {
        AdvertiserAccount account = (AdvertiserAccount) redisClient
                .getObject(getAdvertiserAccountCacheKey(advertiserId));
        if (account == null) {
            account = advertiserAccountDao.read(advertiserId);
            if (account != null) {
                redisClient.set(getAdvertiserAccountCacheKey(advertiserId), ADVERTISERACCOUNT_CACHE_EXPIRE, account);
            }
        }
        return account;
    }

    public List<AdvertiserAccount> getAdvertisers(String companyName, List<Integer> advertiserIds,
            Pagination page) {
        return advertiserAccountDao.getAdvertisers(companyName, advertiserIds, page);
    }
    
    /**
     * 更新广告主状态
     * @param advertiserId
     * @param status
     * @return
     */
    public int updateAdvertiserStatus(int advertiserId, AdvertiserStatusEnum status) {
        if (advertiserId <= 0)
            throw new ServiceException("广告主ID非法");
        
        AdvertiserAccount advertiser = advertiserAccountDao.read(advertiserId);
        if (advertiser == null)
            throw new ServiceException("广告主不存在");
        
        //冻结操作
        if (AdvertiserStatusEnum.FROZEN.equals(status) && 
                AdvertiserStatusEnum.FROZEN.equals(advertiser.getStatus())) {
            throw new ServiceException("非法操作:已经被冻结");
        }
        
        //解冻操作
        if (AdvertiserStatusEnum.NORMAL.equals(status) && 
                AdvertiserStatusEnum.NORMAL.equals(advertiser.getStatus())) {
            throw new ServiceException("非法操作:非冻结状态");
        }
        
        //执行db更新操作
        int count =  advertiserAccountDao.updateAdvertiserStatus(advertiserId, status.getCode());
        if (count == 1) {
            //记录日志
            Map<String, String> params = new HashMap<String, String>();
            params.put("advertiserId", String.valueOf(advertiserId));
            params.put("advertiserCompanyName", advertiser.getCompanyName());
            if (AdvertiserStatusEnum.FROZEN.equals(status)) {
                this.addOperationLog(LogConstants.OPTYPE_ADVERTISER_FROZEN, LogConstants.OBJNAME_ADVERTISER , 
                        String.valueOf(advertiserId), String.valueOf(advertiserId), params);
            } else {
                this.addOperationLog(LogConstants.OPTYPE_ADVERTISER_UNFROZEN, LogConstants.OBJNAME_ADVERTISER , 
                        String.valueOf(advertiserId), String.valueOf(advertiserId), params); 
            }
        }
        return count;
    }
    

    public List<AdvertiserAccount> listAllAdvertisersByAdmin() {
        return advertiserAccountDao.listAllAdvertisers();
    }

    public Map<Integer, String> getAllAdvertiserName() {
        return advertiserAccountDao.getAllAdvertiserName();
    }

    private String getAdvertiserAccountCacheKey(int advertiserId) {
        return "ADVERTISER_ACCOUNT:" + advertiserId;
    }

}
