package com.buzzinate.buzzads.struts2;

import com.buzzinate.buzzads.user.LoginHelper;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
/**
 * 
 * @author Johnson
 *
 */
public class AdvertiserInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = -990841838527644556L;
    
    @Autowired
    private LoginHelper loginHelper;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        if (loginHelper.isLoginAsAdvertiser() && !loginHelper.hasAdvertiserContactInfo()) {
            //没有通过乐知开通广告
            return "newAdvertiserContactInfo";
        }
        return invocation.invoke();
    }

}
