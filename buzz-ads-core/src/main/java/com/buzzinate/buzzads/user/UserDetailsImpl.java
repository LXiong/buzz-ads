package com.buzzinate.buzzads.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * 
 * @author zyeming
 *
 */
public class UserDetailsImpl implements UserDetails, Serializable {

    
    private static final long serialVersionUID = -7021562886842882265L;
    
    private UserBase userBase;
    private UserRole userRole;
    
    
    public UserDetailsImpl(UserBase userBase, UserRole userRole) {
        super();
        this.userBase = userBase;
        this.userRole = userRole;
    }
    
    
    public int getUserId() {
        return userBase.getUserID();
    }
    
    
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return userRole.getGrantedAuthorities();
    }

    
    @Override
    public String getPassword() {
        return userBase.getPassword();
    }

    
    @Override
    public String getUsername() {
        return userBase.getLoginID();
    }

    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    
    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    
    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userBase == null) ? 0 : userBase.hashCode());
        result = prime * result + ((userRole == null) ? 0 : userRole.hashCode());
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
        UserDetailsImpl other = (UserDetailsImpl) obj;
        if (userBase == null) {
            if (other.userBase != null)
                return false;
        } else if (!userBase.equals(other.userBase))
            return false;
        if (userRole == null) {
            if (other.userRole != null)
                return false;
        } else if (!userRole.equals(other.userRole))
            return false;
        return true;
    }
    

}
