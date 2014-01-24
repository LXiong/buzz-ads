package com.buzzinate.buzzads.struts2;

import com.buzzinate.buzzads.user.LoginHelper;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.buzzinate.buzzads.core.service.PublisherContactService;
import com.buzzinate.buzzads.domain.PublisherContactInfo;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2013-3-19
 */
public class PublisherInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = -990841838527644556L;
    
    @Autowired
    private LoginHelper loginHelper;
    @Autowired
    private PublisherContactService publisherContactService;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        if(!ServletActionContext.getRequest().getServletPath().contains("/publisher/settings")) {
            int userId = loginHelper.getUserId();
            PublisherContactInfo pci = publisherContactService.getPublisherContactInfoByUserId(userId);
            if (pci == null) {
                ServletActionContext.getRequest().setAttribute("publisher", false);
                //没有通过乐知开通广告
                return "newPublisherContactInfo";
            } else {
                ServletActionContext.getRequest().setAttribute("publisher", true);
            }
        }
        return invocation.invoke();
    }

}
