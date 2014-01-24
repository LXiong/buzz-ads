package com.buzzinate.buzzads.action.publisher;

import com.buzzinate.buzzads.enums.BankEnum;
import com.buzzinate.buzzads.enums.FinanceTypeEnum;
import com.buzzinate.buzzads.user.LoginHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.common.exception.ServiceException;
import com.buzzinate.buzzads.core.service.PublisherContactService;
import com.buzzinate.buzzads.domain.PublisherContactInfo;
import com.buzzinate.common.util.JsonResults;
import com.opensymphony.xwork2.ActionSupport;

import java.util.Map;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2013-1-15
 */
@Controller
@Scope("request")
public class PublisherSettingAction extends ActionSupport {
    
    private static final long serialVersionUID = 4485933964355883161L;

    private static Map<BankEnum, String> bankSelector = BankEnum.bankSelector();
    private static Map<FinanceTypeEnum, String> financeTypeSelector = FinanceTypeEnum.financeTypeSelector();

    @Autowired
    protected LoginHelper loginHelper;
    @Autowired
    private PublisherContactService publisherContactService;
    
    private PublisherContactInfo publisher;
    
    private JsonResults results;
    private boolean hasContactInfo = true;

    @Override
    public String execute() throws Exception {
        int userId = loginHelper.getUserId();
        publisher = publisherContactService.viewPublisherContactInfo(userId);
        if (publisher.getUserId() == 0) {
            hasContactInfo = false;
            publisher.setUserId(userId);
            addActionMessage("请填写相应的联系人信息");
        }
        return SUCCESS;
    }
    
    public String update() {
        results = new JsonResults();
        if (!validatePubInfo()) {
            results.fail("信息不完整");
            return JsonResults.JSON_RESULT_NAME;
        }
        if (hasSpecialChars(publisher.getName()) || hasSpecialChars(publisher.getEmail()) || 
                hasSpecialChars(publisher.getMobile()) || hasSpecialChars(publisher.getReceiveAccount()) || 
                hasSpecialChars(publisher.getReceiveName()) || hasSpecialChars(publisher.getQq())) {
            results.fail("含有非法字符");
            return JsonResults.JSON_RESULT_NAME;
        }
        try {
            publisherContactService.saveOrUpdatePublisher(publisher);
            results.success();
        } catch (ServiceException e) {
            results.fail(e.getMessage());
        } catch (Exception e) {
            results.fail("系统异常");
        }
        return JsonResults.JSON_RESULT_NAME;
    }
    
    private boolean validatePubInfo() {
        //检验完整性
        if (StringUtils.isBlank(publisher.getName()) ||
                StringUtils.isBlank(publisher.getEmail()) ||
                StringUtils.isBlank(publisher.getMobile()) ||
                publisher.getReceiveMethod() == null ||
                StringUtils.isBlank(publisher.getReceiveAccount()) ||
                StringUtils.isBlank(publisher.getReceiveName())) {
            return false;
        }
        return true;
    }
    
    private boolean hasSpecialChars(String input) {
        boolean flag = false;
        char c;
        for (int i = 0; i <= input.length() - 1; i++) {
            c = input.charAt(i);
            switch (c) {
            case '>':
                flag = true;
                break;
            case '<':
                flag = true;
                break;
            case '"':
                flag = true;
                break;
            case '&':
                flag = true;
                break;
            //default:
            //    flag = true;
            }
        }
        return flag;
    }

    public PublisherContactInfo getPublisher() {
        return publisher;
    }

    public void setPublisher(PublisherContactInfo publisher) {
        this.publisher = publisher;
    }

    public JsonResults getResults() {
        return results;
    }
    
    public boolean isHasContactInfo() {
        return hasContactInfo;
    }

    public void setHasContactInfo(boolean hasContactInfo) {
        this.hasContactInfo = hasContactInfo;
    }

    public Map<FinanceTypeEnum, String> getFinanceTypeSelector() {
        return financeTypeSelector;
    }

    public Map<BankEnum, String> getBankSelector() {
        return bankSelector;
    }
}
