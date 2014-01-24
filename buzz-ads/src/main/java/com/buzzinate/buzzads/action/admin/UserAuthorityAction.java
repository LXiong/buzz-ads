package com.buzzinate.buzzads.action.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.core.service.UserAuthorityService;
import com.buzzinate.buzzads.user.LoginHelper;
import com.buzzinate.common.util.JsonResults;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 权限管理
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2013-6-4
 */
@Controller
@Scope("request")
public class UserAuthorityAction extends ActionSupport {

    private static final long serialVersionUID = -4694128953602676395L;
    
    @Autowired
    private transient LoginHelper loginHelper;
    @Autowired
    private transient UserAuthorityService userAuthorityService;
    
    private JsonResults results;
    
    private Integer userId;
    
    public String openChannelTarget() {
        results = new JsonResults();
        if (!loginHelper.hasRole("ROLE_AD_ADMIN")) {
            results.fail("没有权限");
        } else {
            try {
                userAuthorityService.addUserAuthority(userId);
                results.success();
            } catch (Exception e) {
                e.printStackTrace();
                results.fail("系统异常");
            }
        }  
        return JsonResults.JSON_RESULT_NAME;
    }
    
    public String closeChannelTarget() {
        results = new JsonResults();
        if (!loginHelper.hasRole("ROLE_AD_ADMIN")) {
            results.fail("没有权限");
        } else {
            try {
                userAuthorityService.removeUserAuthority(userId);
                results.success();
            } catch (Exception e) {
                e.printStackTrace();
                results.fail("系统异常");
            }
        }  
        return JsonResults.JSON_RESULT_NAME;
    }
    

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public JsonResults getResults() {
        return results;
    }

}
