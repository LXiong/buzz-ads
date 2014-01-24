package com.buzzinate.buzzads.core.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.domain.UserAuthority;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2013-5-28
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class UserAuthorityDao extends AdsDaoBase<UserAuthority, Integer> {

    public UserAuthorityDao() {
        super(UserAuthority.class, "userId");
    }
}
