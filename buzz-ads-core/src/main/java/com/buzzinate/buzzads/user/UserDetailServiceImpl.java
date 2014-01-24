package com.buzzinate.buzzads.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Yeming
 *
 */
@Transactional(readOnly = true)
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserBaseServices userBaseServices;
    @Autowired
    private UserRoleServices userRoleServices;
    
    
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) 
        throws UsernameNotFoundException, DataAccessException {
        
        UserDetails userDetails = null;
        if (username.contains("@")) {
            // get user by email
            userDetails = loadUserByEmail(username);
        } 
        if (userDetails == null) {
            // get user by loginID
            userDetails = loadUserByLoginID(username);
        }
        
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found, the username or email is " + username);
        }
        
        return userDetails;
    }
    
    
    private UserDetails loadUserByEmail(String email) {
        UserBase userBase = userBaseServices.getUserByEmail(email);
        if (userBase == null) {
            return null;
        }
        return createUserDetail(userBase);
    }

    
    private UserDetails loadUserByLoginID(String loginID) {
        UserBase userBase = userBaseServices.getUserByLoginID(loginID);
        if (userBase == null) {
            return null;
        }
        return createUserDetail(userBase);
    }
    
    
    private UserDetails createUserDetail(UserBase userBase) {
        UserRole userRole = userRoleServices.getUserRole(userBase.getUserID());
        return new UserDetailsImpl(userBase, userRole);
    }

    
}
