package com.buzzinate.buzzads.user;

import com.buzzinate.buzzads.common.dao.MainDaoBase;
import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author John Chen Nov 2, 2009 Copyright 2009 Buzzinate Co. Ltd.
 *
 */
@Component
@Transactional(readOnly = true)
public class UserBaseDao extends MainDaoBase<UserBase, Integer> {

    public UserBaseDao() {
        super(UserBase.class, "userID");
    }
    
    
    /**
     * Get the user whose status is not Delete.
     * @param userID
     * @return The user bean or null.
     */
    public UserBase getUser(int userID) {
        Query query = getSession().getNamedQuery("user.getUser");
        query.setInteger("userID", userID);
        return (UserBase) query.uniqueResult();
    }

    
    /**
     * Get the user whose status is not Delete or Suspend with email.
     * @param emailID
     * @param session
     * @return The user bean or null.
     */
    public UserBase getUserByEmail(String email) {
        Query query = getSession().getNamedQuery("user.getUserByEmail");
        query.setString("email", email);
        return (UserBase) query.uniqueResult();
    }

    
}
