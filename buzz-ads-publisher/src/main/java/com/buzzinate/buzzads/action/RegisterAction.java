package com.buzzinate.buzzads.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.buzzinate.buzzads.user.LoginHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.core.service.SiteService;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.common.util.string.StringUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.TextProvider;

/**
 * First part of registration. If successful, the user will be created and
 * directed to another page to add more info.
 * 
 * @author Johnson
 */
@Controller
@Scope("request")
public class RegisterAction extends ActionSupport implements
        ServletRequestAware {
    private static final long serialVersionUID = 3148867761207650482L;

    private static final String REGISTER_URL = ConfigurationReader.getString("auth.server") + "/register";
    private static final String REGISTER_TARGET_URL = ConfigurationReader.getString("auth.client") +
            "/registerAction";
    private static final String SOURCE = ConfigurationReader.getString("auth.source");

    private HttpServletRequest request;
    private TextProvider textProvider;
    
    @Autowired
    private ServiceProperties serviceProperties;
    @Autowired
    private SiteService siteService;

    private String dynamicAction;
    private String redirectUrl;
    // 需要向乐知传递的uuid
    private String uuid;
    
    @Autowired
    private LoginHelper loginHelper;

    
    public String register() {
        StringBuilder casRegisterUrl = new StringBuilder();
        casRegisterUrl.append(REGISTER_URL).append("?");
        String queryString = request.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            casRegisterUrl.append(queryString).append("&");
        }

        casRegisterUrl.append("service=").append(StringUtil.urlEncode(serviceProperties.getService()));
        casRegisterUrl.append("&source=").append(SOURCE);
        casRegisterUrl.append("&targetUrl=").append(StringUtil.urlEncode(REGISTER_TARGET_URL));

        redirectUrl = casRegisterUrl.toString();
        return SUCCESS;
    }
    
    
    public String execute() {
        addActionMessage(textProvider
                .getText("bshare.user.register.successfully"));

        dynamicAction = (String) request.getSession().getAttribute(
                "dynamicAction");
        if (!StringUtils.isEmpty(dynamicAction)) {
            return "dynamicAction";
        }
        if (loginHelper.isLoginAsPublisher()) {
            // 取得站点uuid传递到乐知
            List<String> uuidArr = siteService.getUuids(loginHelper.getUserId());
            if (uuidArr.size() > 0) {
                this.uuid = uuidArr.get(0);
            } else {
                this.uuid = "nologin";
            }
            return "publisher";
        }

        return Action.SUCCESS;
    }

    public String getUuid() {
        return uuid;
    }
    
    public void setTextProvider(TextProvider textProvider) {
        this.textProvider = textProvider;
    }

    public String getDynamicAction() {
        return dynamicAction;
    }

    public void setDynamicAction(String dynamicAction) {
        this.dynamicAction = dynamicAction;
    }
    
    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }


    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }
}