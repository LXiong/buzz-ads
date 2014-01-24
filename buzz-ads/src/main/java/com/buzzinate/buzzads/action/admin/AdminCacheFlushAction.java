package com.buzzinate.buzzads.action.admin;

import com.buzzinate.buzzads.user.LoginHelper;
import com.buzzinate.buzzads.user.UserBaseServices;
import com.buzzinate.buzzads.user.UserRoleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2013-1-15
 */
@Controller
@Scope("request")
public class AdminCacheFlushAction extends ActionSupport {

    private static final long serialVersionUID = -1482508566295014097L;
    
    @Autowired
    protected LoginHelper loginHelper;
    @Autowired
    private UserBaseServices userBaseServices;
    @Autowired
    private UserRoleServices userRoleServices;
    
    private int userId;
    
    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }
    /*
     * 清除缓存
     */
    public String flush() {
        try {
            userRoleServices.removeRoleCacheByUserId(userId);
            userBaseServices.removeUserCacheByUserId(userId);
            this.addActionMessage("成功刷新 " + userId + " 的缓存");
        } catch (Exception e) {
            this.addActionError("刷新" + userId + "的缓存发生异常");
        }
        return SUCCESS;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
