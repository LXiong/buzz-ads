package com.buzzinate.buzzads.action;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.user.LoginHelper;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author John Chen Sep 24, 2009 Copyright 2009 Buzzinate Co. Ltd.
 *
 */
@Controller
@Scope("request")
public class LoginAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 3804763520631498968L;

    private String dynamicAction;

    private String defaultPage = "/jsp/user/Login.jsp";
    
    private int error;
    
    private Map<String, Object> session;
    
    @Autowired
    private transient LoginHelper loginHelper;

    public String execute() {
        initDefaultPage();
        
        if (!loginHelper.isLoggedIn()) {
            if (getError() == 1) {
                addActionError(getText("user.authentication.invalid"));
                return Action.ERROR;
            } else {    
                // error=2
                addActionError(getText("user.authentication.invalid"));
                setDynamicAction(dynamicAction);
                return "dynamicAction";
            }
        }
        session.put("userId", loginHelper.getUserId());
        if (StringUtils.isNotEmpty(dynamicAction)) {
            setDynamicAction(dynamicAction);
            return "dynamicAction";
        }
        
        dynamicAction = (String) session.get("dynamicAction");
        if (StringUtils.isNotEmpty(dynamicAction)) {
            return "dynamicAction";
        }
        //管理员
        if (loginHelper.hasRole("ROLE_AD_ADMIN")) {
            return "adminHome";
        }
        if (loginHelper.hasRole("ROLE_AD_FINANCE")) {
            return "financeHome";
        }
        //站长
        if (loginHelper.hasRole("ROLE_PUBLISHER")) {
            return "publisherHome";
        }
        // default homepage
        return Action.SUCCESS; 
    }
    
    /**
     * for user customize default render page
     */
    private void initDefaultPage() {
        String customizeDefaultPage = (String) session.get("defaultPage");
        if (!StringUtils.isEmpty(customizeDefaultPage)) {
            this.defaultPage = customizeDefaultPage;
        }
    }
    

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getDynamicAction() {
        return this.dynamicAction;
    }

    public void setDynamicAction(String dynamicAction) {
        this.dynamicAction = dynamicAction;
    }

    public String getDefaultPage() {
        return defaultPage;
    }

    public void setDefaultPage(String defaultPage) {
        this.defaultPage = defaultPage;
    }
    

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
    
}
