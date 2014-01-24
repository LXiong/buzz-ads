package com.buzzinate.buzzads.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.buzzinate.buzzads.core.util.RedisClient;

/**
 * 
 * @author Marvin Zhang,Kun.Xue
 * 
 */
public class UserBaseServices {
    
    private static final String USER_CACHE_KEY = "USER";
    private static final int USER_CACHE_EXPIRE = 3600 * 24;
    
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private UserBaseDao userBaseDao;
    

    /**
     * Returns the User bean with the given userID (excludes deleted users).
     * 
     * @param userID
     * @return The user bean or null.
     */
    public UserBase getUser(int userID) {
        UserBase user = (UserBase) redisClient.getObject(USER_CACHE_KEY + userID);
        if (user == null) {
            user = userBaseDao.getUser(userID);
            if (user != null) {
                redisClient.set(USER_CACHE_KEY + user.getUserID(), USER_CACHE_EXPIRE, user);
            }
        }
        return user;
    }


    

    /**
     * Get the user with login id.
     * 
     * @param loginID
     * @return
     */
    public UserBase getUserByLoginID(String loginID) {

        Map<String, Object> modifiers = new HashMap<String, Object>();
        modifiers.put("loginID", loginID);
        List<UserBase> userList = userBaseDao.query(modifiers);
        if (!userList.isEmpty()) {
            UserBase user = (UserBase) userList.get(0);
            redisClient.set(USER_CACHE_KEY + user.getUserID(), USER_CACHE_EXPIRE, user);
            return user;
        } else {
            return null;
        }
    }

    /**
     * Get the user with given email.
     * 
     * @param email
     * @return
     */
    public UserBase getUserByEmail(String email) {

        Map<String, Object> modifiers = new HashMap<String, Object>();
        modifiers.put("email", email);
        List<UserBase> userList = userBaseDao.query(modifiers);
        if (!userList.isEmpty()) {
            UserBase user = (UserBase) userList.get(0);
            redisClient.set(USER_CACHE_KEY + user.getUserID(), USER_CACHE_EXPIRE, user);
            return user;
        } else {
            return null;
        }
    }
    
    
    public UserBase loadUserByUserId(Integer userId) {
        UserBase user = null;
        user = getUser(userId);
        return user;
    }
    
    public void removeUserCacheByUserId(Integer userId) {
        redisClient.delete(USER_CACHE_KEY + userId.intValue());
    }
}
