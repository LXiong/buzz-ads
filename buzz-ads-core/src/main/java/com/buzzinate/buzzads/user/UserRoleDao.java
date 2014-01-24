package com.buzzinate.buzzads.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.MainDaoBase;

/**
 * 
 * @author zyeming
 *
 */
@Transactional(readOnly = true)
public class UserRoleDao extends MainDaoBase<Role, Integer> {
    
    public UserRoleDao() {
        super(Role.class, "id");
    }
    
    @SuppressWarnings("unchecked")
    public Set<GrantedAuthority> getGrantedAuthority(int userId) {
        List<String> result = getSession().getNamedQuery("role.grantedAuthorities").setInteger("userId", userId).list();
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>(result.size());
        for (String roleName : result) {
            authorities.add(new GrantedAuthorityImpl(roleName));
        }
        return authorities;
    }
    
    
    @SuppressWarnings("unchecked")
    public List<Role> getAllRoles() {
        Query query = getSession().getNamedQuery("getAllRoles");
        return (List<Role>) query.list();
    }
    

}
