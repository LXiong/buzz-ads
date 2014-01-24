package com.buzzinate.buzzads.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buzzinate.buzzads.common.service.AdsBaseService;
import com.buzzinate.buzzads.core.dao.UserAuthorityDao;
import com.buzzinate.buzzads.core.util.RedisClient;
import com.buzzinate.buzzads.domain.UserAuthority;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2013-5-28
 */
@Service
public class UserAuthorityService extends AdsBaseService {
    
    private static final int ENTRY_CACHE_EXPIRE = 3600 * 24;
    @Autowired
    private UserAuthorityDao userAuthorityDao;
    @Autowired
    private RedisClient redisClient;
    
    public UserAuthority getUserAuthorityByUserId(int userId) {
        UserAuthority userAuthority = (UserAuthority) redisClient.getObject(getAdEntryCacheKey(userId));
        if (userAuthority == null) {
            userAuthority = userAuthorityDao.read(Integer.valueOf(userId));
            if (userAuthority != null) {
                redisClient.set(getAdEntryCacheKey(userId), ENTRY_CACHE_EXPIRE, userAuthority);
            }
        }
        return userAuthority;
    }
    
    public void addUserAuthority(int userId) {
        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setUserId(userId);
        userAuthority.setChannelTarget(Integer.valueOf(1));
        userAuthorityDao.create(userAuthority);
    }
    
    public void removeUserAuthority(int userId) {
        redisClient.delete(getAdEntryCacheKey(userId));
        userAuthorityDao.delete(Integer.valueOf(userId));
    }
    
    private String getAdEntryCacheKey(int userId) {
        return "UserAuthority:" + userId;
    }

}
