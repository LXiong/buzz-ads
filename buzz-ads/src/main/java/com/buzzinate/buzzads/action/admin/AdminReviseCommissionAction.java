package com.buzzinate.buzzads.action.admin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.core.service.ReviseCommissionService;
import com.buzzinate.common.util.DateTimeUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Johnson
 *
 */
@Controller
@Scope("request")
public class AdminReviseCommissionAction extends ActionSupport {
    private static final long serialVersionUID = -1221776261042817355L;
    
    private Date reviseDate;
    @Autowired
    private ReviseCommissionService reviseCommissionService;
    
    public String execute() {
        reviseDate = new Date();
        return Action.SUCCESS;
    }
    
    public String revise() {
        try {
            Date day;
            if (reviseDate != null) {
                day = DateTimeUtil.getDateDay(reviseDate);
            } else {
                // 默认昨天
                day = DateTimeUtil.getDateDay(DateTimeUtil.subtractDays(new Date(), 1));
            }
            
            //订正站长前一天的佣金，减少精度误差
            reviseCommissionService.revisePublisherCommission(day);
            
            //订正广告主的前一天的支出
            reviseCommissionService.reviseAdvertiserBilling(day);
            
            //订正站长的佣金, 时间从前天所在月的月初到前天的时间间隔
            reviseCommissionService.revisePublisherSettlement(day);
            
            this.addActionMessage("成功订正 " + DateTimeUtil.formatDate(reviseDate) + " 的佣金");
        } catch (Exception e) {
            this.addActionError(DateTimeUtil.formatDate(reviseDate) + "的数据订正发生异常");
        }
        
        return Action.SUCCESS;
    }
    
    public String revisePublisherSettlement() {
        try {
//            reviseCommissionService.getSettlementReviseAndUpdate();
            this.addActionMessage("成功订正该月的站长结账数据");
        } catch (Exception e) {
            this.addActionMessage("订正该月站长结算数据时出现异常");
        }
        return Action.SUCCESS;
    }

    public String getReviseDate() {
        return DateTimeUtil.formatDate(reviseDate);
    }

    public void setReviseDate(Date reviseDate) {
        this.reviseDate = reviseDate;
    }

}
