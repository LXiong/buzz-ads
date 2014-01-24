package com.buzzinate.buzzads.action;

import java.util.List;
import java.util.Map;

import com.buzzinate.buzzads.user.LoginHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.domain.AdCampaign;
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
    private transient AdCampaignService adCampaignService;
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
        //根据角色不同，跳转到各自默认首页
        //财务
        // if user has multiple roles, first login should jump in the following order...
        //ads admin
        if (loginHelper.hasRole("ROLE_AD_ADMIN")) {
            return "adminHome";
        }
        
        //ads advertiser
        if (loginHelper.hasRole("ROLE_ADVERTISER")) {
            List<AdCampaign> camps = adCampaignService.getCampaignsByAdvertiserId(loginHelper.getUserId());
            if (camps == null || camps.size() == 0) {
                addActionMessage("您还没有广告，请创建首个广告");
                return "campaign/new";
            }
            return "advertiserHome";
        }
        // default homepage
        return "AccessDenied";
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
