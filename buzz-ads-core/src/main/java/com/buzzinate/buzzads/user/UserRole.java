package com.buzzinate.buzzads.user;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * 
 * @author zyeming
 *
 */
public class UserRole implements Serializable {

    private static final long serialVersionUID = 3832374915659710375L;
    
    private int userId;
    private Set<GrantedAuthority> grantedAuthorities;

    
    public UserRole() {
        this.grantedAuthorities = new HashSet<GrantedAuthority>();
    }

    public UserRole(int userId, Set<GrantedAuthority> grantedAuthorities) {
        super();
        assert grantedAuthorities != null;
        this.userId = userId;
        this.grantedAuthorities = grantedAuthorities;
    }
    
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Set<GrantedAuthority> getGrantedAuthorities() {
        return grantedAuthorities;
    }
    
    public GrantedAuthority[] getGrantedAuthoritiesArray() {
        GrantedAuthority[] authorities = new GrantedAuthority[grantedAuthorities.size()];
        return grantedAuthorities.toArray(authorities);
    }
    
    public boolean hasRole(String roleName) {
        Iterator<GrantedAuthority> ga = grantedAuthorities.iterator();
        while (ga.hasNext()) {
            GrantedAuthority auth = ga.next();
            if (auth.getAuthority().equals(roleName)) {
                return true;
            }
        }
        
        return false;
    }

    public void setGrantedAuthorities(Set<GrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    public void addRole(GrantedAuthority authority) {
        this.grantedAuthorities.add(authority);
    }
    
    public void removeRole(String authority) {
        Iterator<GrantedAuthority> ga = grantedAuthorities.iterator();
        while (ga.hasNext()) {
            GrantedAuthority auth = ga.next();
            if (auth.getAuthority().equals(authority)) {
                ga.remove();
                break;
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((grantedAuthorities == null) ? 0 : grantedAuthorities.hashCode());
        result = prime * result + userId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserRole other = (UserRole) obj;
        if (grantedAuthorities == null) {
            if (other.grantedAuthorities != null)
                return false;
        } else if (!grantedAuthorities.equals(other.grantedAuthorities))
            return false;
        if (userId != other.userId)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UserAuthorization [grantedAuthorities=" + grantedAuthorities + ", userId=" + userId + "]";
    }

}
