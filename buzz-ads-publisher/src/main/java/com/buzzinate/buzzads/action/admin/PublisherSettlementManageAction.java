package com.buzzinate.buzzads.action.admin;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.core.service.PaymentService;
import com.buzzinate.buzzads.core.service.PublisherContactService;
import com.buzzinate.buzzads.core.service.PublisherSettlementService;
import com.buzzinate.buzzads.core.util.FormatUtil;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.Payment;
import com.buzzinate.buzzads.domain.PublisherContactInfo;
import com.buzzinate.buzzads.domain.PublisherSettlement;
import com.buzzinate.buzzads.enums.SettlementStatusEnum;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>                              hulai
 *
 * 2012-11-26
 */
@Controller
@Scope("request")
public class PublisherSettlementManageAction extends ActionSupport implements ServletRequestAware {
    
    private static final long serialVersionUID = 5348912997749926882L;

    private List<PublisherSettlement> monthSettles;
    private Date settleMonth;
    private int totalCommission;
    
    
    @Autowired
    private PublisherSettlementService pubSettleService;
    @Autowired
    private PublisherContactService pubContactService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private HttpServletRequest request;
    
    private Integer userId;
    
    //检索条件
    private String searchName;
    private Integer settleAmount;
    private Pagination page;
    /*
     * 默认查询当月结算数据
     * 佣金余额为上月余额
     */
    @Override
    public String execute() throws Exception {
        // truncate to the 1st day of the pre month
        if (settleMonth == null) {
            settleMonth = new Date();
        }
        if (page == null) {
            page = new Pagination();
        }
        monthSettles = pubSettleService.listByProperties(searchName, settleAmount,
                DateUtils.addMonths(DateUtils.truncate(settleMonth, Calendar.MONTH), -1), page);
        totalCommission = pubSettleService.getTotalCommission(searchName, settleAmount, 
                DateUtils.addMonths(DateUtils.truncate(settleMonth, Calendar.MONTH), -1));
        
        setCurrentPageRole();
        return Action.SUCCESS;
    }
    
    /*
     * 佣金明细列表
     */
    public String detail() {
        if (userId == null) {
            this.addFieldError("userId", "Invalid userId provided!");
            return Action.INPUT;
        }
        PublisherContactInfo contact = pubContactService.getPublisherContactInfoByUserId(userId);
        if (contact != null) {
            ActionContext.getContext().getSession().put("account", contact.getEmail());
        }
        monthSettles = pubSettleService.getByUserId(userId);
        //补充支付信息
        for (PublisherSettlement ps : monthSettles) {
            if (SettlementStatusEnum.PAID.equals(ps.getStatus())) {
                if (ps.getPaymentId() > 0) {
                    Payment pay = paymentService.getPaymentById(ps.getPaymentId());
                    if (pay != null) {
                        ps.setReceiptNo(pay.getReceiptNo());
                        ps.setComment(pay.getComment());
                    } else {
                        ps.setComment("系统设定：数据异常-没有支付信息");
                    }
                } else {
                    ps.setComment("系统设定：数据异常-没有支付ID");
                }
                totalCommission += ps.getCommission();
            }
        }
        setCurrentPageRole();
        return SUCCESS;
    }
    
    private void setCurrentPageRole() {
        if (request.getServletPath().contains("/finance")) {
            request.setAttribute("currentPageRole", "finance");
        } else if (request.getServletPath().contains("/admin")) {
            request.setAttribute("currentPageRole", "admin");
        }
    }
    
    public List<PublisherSettlement> getMonthSettles() {
        return monthSettles;
    }
    
    public Date getSettleMonth() {
        return settleMonth;
    }
    
    public void setSettleMonth(Date settleMonth) {
        this.settleMonth = settleMonth;
    }

    public int getTotalCommission() {
        return totalCommission;
    }
    
    public String getTotalCommissionDouble() {
        return FormatUtil.getDouble(totalCommission);
    }
    
    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public Integer getSettleAmount() {
        return settleAmount;
    }

    public void setSettleAmount(Integer settleAmount) {
        this.settleAmount = settleAmount;
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }
}
