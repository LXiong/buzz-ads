package com.buzzinate.buzzads.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.buzzinate.buzzads.core.service.AdvertiserAccountService;
import com.buzzinate.buzzads.core.service.PublisherContactService;



/**
 * 
 * @author Yeming
 * 
 */
@Component
public class LoginHelper {

    public static final String ROLE_ADMIN = "ROLE_AD_ADMIN";
    public static final String ROLE_ADVERTISER = "ROLE_ADVERTISER";
    public static final String ROLE_PUBLISHER = "ROLE_PUBLISHER";
    
    @Autowired
    private AdvertiserAccountService advertiserAccountService;
    @Autowired
    private PublisherContactService publisherContactService;
    
    
    /**
     * Check whether the current user is logged in or not
     * @return
     */
    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null & !(authentication instanceof AnonymousAuthenticationToken);
    }
    
    
    /**
     * Get the current user's ID; if not logged in, just return 0
     * @return
     */
    public int getUserId() {
        UserDetailsImpl userDetail = getUserDetail();
        return userDetail == null ? 0 : userDetail.getUserId();
    }
    
    public boolean isLoginAsAdmin() {
        return hasRole(ROLE_ADMIN);
    }
    
    public boolean isLoginAsAdvertiser() {
        return hasRole(ROLE_ADVERTISER);
    }
    
    public boolean isLoginAsPublisher() {
        return hasRole(ROLE_PUBLISHER);
    }
    
    public boolean hasAdvertiserContactInfo() {
        return advertiserAccountService.getAdvertiserAccount(getUserId()) != null;
    }
    
    public boolean hasPublisherContactInfo() {
        return publisherContactService.getPublisherContactInfoByUserId(getUserId()) != null;
    }
    
    public boolean hasRole(String roleName) {
        if (!isLoggedIn()) {
            return false;
        }
        UserDetailsImpl currentUser = getUserDetail();
        return hasRole(currentUser.getAuthorities(), roleName);
    }
    
    public boolean hasRole(Collection<? extends GrantedAuthority> authorities, String roleName) {
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(roleName)) {
                return true;
            }
        }
        return false;
    }
    
    
    private UserDetailsImpl getUserDetail() {
        if (!isLoggedIn()) {
            return null;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImpl) authentication.getPrincipal();
    }
   
}
