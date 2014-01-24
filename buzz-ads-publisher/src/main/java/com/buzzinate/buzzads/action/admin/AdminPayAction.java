package com.buzzinate.buzzads.action.admin;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.buzzinate.buzzads.user.LoginHelper;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.common.exception.ServiceException;
import com.buzzinate.buzzads.core.service.PaymentService;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.Payment;
import com.buzzinate.common.util.JsonResults;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-12-20
 */
@Controller
@Scope("request")
public class AdminPayAction extends ActionSupport implements ServletRequestAware {
    
    private static final long serialVersionUID = 7668214386433510877L;
    @Autowired
    protected LoginHelper loginHelper;
    @Autowired
    private PaymentService paymentService;
    
    private Payment payment;
    
    private double paid;
    
    private double tax;
    
    private double fee;
    
    private JsonResults results;
    
    private Date settleMonth;
    
    private Integer paymentId;
    
    private List<Payment> payments;
    //站长账户
    private String searchEmail;
    //流水号
    private String receiptNo;
    
    private Pagination page;
    
    @Autowired
    private HttpServletRequest request;
    
    /**
     * 查看支付历史
     */
    public String execute() {
        if (page == null) 
            page = new Pagination();
        payments = paymentService.listPaymentHistory(searchEmail, receiptNo, page);
        setCurrentPageRole();
        return SUCCESS;
    }
    /**
     * 支付佣金
     * @return
     */
    public String pay() {
        results = new JsonResults();
        if (!loginHelper.hasRole("ROLE_AD_FINANCE")) {
            results.fail("没有支付权限");
            return JsonResults.JSON_RESULT_NAME;
        }
        //默认当前结算月
        if (settleMonth == null) {
            settleMonth = new Date();
        }
        try {
            if (paid > 0) {
                payment.setPaid(getIntValue(paid));
                payment.setTax(getIntValue(tax));
                payment.setFee(getIntValue(fee));
                paymentService.pay(payment, DateUtils.addMonths(DateUtils.truncate(settleMonth, Calendar.MONTH), -1));
                results.success();
            } else {
                results.fail("支付金额不能为空");
            }
        } catch (ServiceException e) {
            results.fail("支付失败:" + e.getMessage());
        } catch (Exception e) {
            results.fail("系统异常");
        }
        return JsonResults.JSON_RESULT_NAME;
    }
    
    /*
     * 乘以100，取int型数值
     */
    private int getIntValue(double d) {
        BigDecimal b = new BigDecimal(Double.toString(d));
        return b.multiply(BigDecimal.valueOf(100)).intValue();
    }
    
    
    private void setCurrentPageRole() {
        if (request.getServletPath().contains("/finance")) {
            request.setAttribute("currentPageRole", "finance");
        } else if (request.getServletPath().contains("/settlement")) {
            request.setAttribute("currentPageRole", "admin");
        }
    }
    /**
     * 撤销支付
     * @return
     */
    public String cancel() {
        results = new JsonResults();
        if (!loginHelper.hasRole("ROLE_AD_FINANCE")) {
            results.fail("没有撤销权限");
            return JsonResults.JSON_RESULT_NAME;
        }
        try {
            paymentService.rollbackPay(paymentId);
            results.success();
        } catch (ServiceException e) {
            results.fail("撤销失败:" + e.getMessage());
        } catch (Exception e) {
            results.fail("系统异常");
        }
        return JsonResults.JSON_RESULT_NAME;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public JsonResults getResults() {
        return results;
    }
    public Date getSettleMonth() {
        return settleMonth;
    }
    public void setSettleMonth(Date settleMonth) {
        this.settleMonth = settleMonth;
    }
    public Integer getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }
    public List<Payment> getPayments() {
        return payments;
    }
    public Pagination getPage() {
        return page;
    }
    public void setPage(Pagination page) {
        this.page = page;
    }
    public String getSearchEmail() {
        return searchEmail;
    }
    public void setSearchEmail(String searchEmail) {
        this.searchEmail = searchEmail;
    }
    public String getReceiptNo() {
        return receiptNo;
    }
    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }
    public double getPaid() {
        return paid;
    }
    public void setPaid(double paid) {
        this.paid = paid;
    }
    public double getTax() {
        return tax;
    }
    public void setTax(double tax) {
        this.tax = tax;
    }
    public double getFee() {
        return fee;
    }
    public void setFee(double fee) {
        this.fee = fee;
    }
    
    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }
}
