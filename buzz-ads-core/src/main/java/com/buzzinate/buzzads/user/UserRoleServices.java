package com.buzzinate.buzzads.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import com.buzzinate.buzzads.core.util.RedisClient;


/**
 * 
 * @author zyeming
 *
 */
public class UserRoleServices {
    
    private static final String USER_ROLE_CACHE_KEY = "USER_ROLE";
    private static final int USER_ROLE_CACHE_EXPIRE = 3600 * 24;
    private static final String ALL_ROLE_CACHE_KEY = "ALL_ROLE";
    private static final int ALL_ROLE_CACHE_EXPIRE = 3600 * 24 * 30;
    
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private RoleHierarchy roleHierarchy;

    
    public UserRole getUserRole(Integer userID) {
        UserRole userRole = (UserRole) redisClient.getObject(USER_ROLE_CACHE_KEY + userID);
        if (userRole == null) {
            Collection<? extends GrantedAuthority> ga = getAllGrantedAuthorities(userID);
            userRole = new UserRole(userID, new HashSet<GrantedAuthority>(ga));
            if (ga.size() > 0) {
                redisClient.set(USER_ROLE_CACHE_KEY + userID, USER_ROLE_CACHE_EXPIRE, userRole);
            }
        }
        return userRole;
    }
    
    public void removeRoleCacheByUserId(Integer userId) {
        redisClient.delete(USER_ROLE_CACHE_KEY + userId);
    }
    
    
    /**
     * 
     * @param roleName
     * @return null if no role matched search condition
     */
    public GrantedAuthority getGrantedAuthroity(String roleName) {
        List<Role> roles = getAllRole();
        if (roles != null) {
            for (Role role : roles) {
                if (role.getName().equals(roleName)) {
                    return new GrantedAuthorityImpl(role.getName());
                }
            }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public List<Role> getAllRole() {
        List<Role> roles = (List<Role>) redisClient.getObject(ALL_ROLE_CACHE_KEY);
        if (roles == null) {
            roles = userRoleDao.getAllRoles();
            if (roles != null) {
                redisClient.set(ALL_ROLE_CACHE_KEY, ALL_ROLE_CACHE_EXPIRE, roles);
            }
        }
        return roles;
    }
    
    
    /**
     * get all authorities include inherited authorities
     * @param user
     */
    private Collection<GrantedAuthority> getAllGrantedAuthorities(int userId) {
        Set<GrantedAuthority> ga = userRoleDao.getGrantedAuthority(userId);
        return roleHierarchy.getReachableGrantedAuthorities(ga);
    }
    
    
}
