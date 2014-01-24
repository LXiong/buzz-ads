package com.buzzinate.buzzads.action.admin;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.Adjustment;
import com.buzzinate.buzzads.service.AdjustmentServices;
import com.buzzinate.buzzads.user.LoginHelper;
import com.buzzinate.buzzads.user.UserRole;
import com.buzzinate.buzzads.user.UserRoleServices;
import com.buzzinate.common.util.JsonResults;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author chris
 * @since Date: 13-3-28 Time: 下午2:55
 */
@Controller
@Scope("request")
public class AdjustmentManageAction extends ActionSupport implements ServletRequestAware {
    
    private static final long serialVersionUID = 5642657799928387156L;

    private static Log log = LogFactory.getLog(AdjustmentManageAction.class);

    private static final long MAX_RECHARGE_PRICE = 
                    ConfigurationReader.getLong("ads.max.advertiser.recharge", 10000000L);

    @Autowired
    private LoginHelper loginHelper;
    @Autowired
    private UserRoleServices userRoleServices;
    @Autowired
    private AdjustmentServices adjustmentServices;

    private JsonResults jsonResults;

    private Pagination page;

    private Adjustment adjustment;

    private List<Adjustment> adjustments;

    @Transient
    private HttpServletRequest request;

    public String saveAdjustment() {
        jsonResults = new JsonResults();
        try {
            // use current user id as the admin id (this is the person who made the adjustment).
            adjustment.setAdminId(loginHelper.getUserId());
            
            log.info("Admin[id=" + adjustment.getAdminId() + "]开始为用户[id=" + adjustment.getReceiverId() + 
                            "]充值￥" + adjustment.getReceiveAmount());
            // validate
            if (adjustment.getReceiverId() <= 0) {
                jsonResults.fail("Invalid receiver id!");
                return "json";
            }
            if (adjustment.getDetail() == null || adjustment.getDetail().trim().isEmpty()) {
                jsonResults.fail("You must fill in details!");
                return "json";
            }
            
            UserRole role = userRoleServices.getUserRole(adjustment.getReceiverId());
            //BigDecimal sourceRecharge = new BigDecimal(adjustment.getReceiveAmount());
            //只能给广告主的账户充值
            if (role != null && role.hasRole(LoginHelper.ROLE_ADVERTISER)) {
                if (adjustment.getAmount() > MAX_RECHARGE_PRICE * 100 || 
                                adjustment.getAmount() < -MAX_RECHARGE_PRICE * 100) {
                    jsonResults.fail("Invalid amount given. Must be between -10000,000 and 100,00000!");
                    log.info("Invalid recharge amount given: [id=" + adjustment.getReceiverId() + 
                                    ",amount=" + adjustment.getAmount() + "]");
                } else {
                    adjustmentServices.addAdjustment(adjustment);
                    jsonResults.success();
                    log.info("充值成功,金额为￥" + adjustment.getReceiveAmount());
                }
            } else {
                jsonResults.fail(getText("buzzads.admin.recharge.invalid.role"));
                log.info("该用户不是广告主[id=" + adjustment.getReceiverId() + "]");
            } 
        } catch (Exception e) {
            jsonResults.fail();
            jsonResults.setMessage(e.getMessage());
        }
        return "json";
    }

    public String findAdjustments() {
        if (page == null) {
            page = new Pagination();
            page.setPageNum(1);
            page.setPageSize(10);
        }
        adjustments = adjustmentServices.listAdjustment(page);
        setCurrentPageRole();
        return SUCCESS;
    }

    public List<Adjustment> getAdjustments() {
        return adjustments;
    }

    public void setAdjustment(Adjustment adjustment) {
        this.adjustment = adjustment;
    }

    public Adjustment getAdjustment() {
        return adjustment;
    }

    public JsonResults getResults() {
        return jsonResults;
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }

    private void setCurrentPageRole() {
        if (request.getServletPath().contains("/finance")) {
            request.setAttribute("currentPageRole", "finance");
        } else if (request.getServletPath().contains("/settlement")) {
            request.setAttribute("currentPageRole", "admin");
        }
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }
}
