package com.buzzinate.buzzads.action.admin;

import java.util.List;
import java.util.Map;

import com.buzzinate.buzzads.enums.BankEnum;
import com.buzzinate.buzzads.enums.FinanceTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.common.exception.ServiceException;
import com.buzzinate.buzzads.core.service.PublisherContactService;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.PublisherContactInfo;
import com.buzzinate.buzzads.enums.PublisherContactStausEnum;
import com.buzzinate.common.util.JsonResults;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 网站主
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-12-11
 */
@Controller
@Scope("request")
public class PublisherContactAction extends ActionSupport {
    
    private static final long serialVersionUID = -5149170612707134364L;

    private static Map<BankEnum, String> bankSelector = BankEnum.bankSelector();
    private static Map<FinanceTypeEnum, String> financeTypeSelector = FinanceTypeEnum.financeTypeSelector();

    @Autowired
    private PublisherContactService publisherContactService;
    
    private List<PublisherContactInfo> publishers;
    
    private PublisherContactInfo publisher;
    
    private int userId;
    
    private Pagination page;
    
    private JsonResults results;

    /**
     * 网站联系人列表
     */
    @Override
    public String execute() throws Exception {
        if (publisher == null) {
            publisher = new PublisherContactInfo();
        }
        
        if (page == null) {
            page = new Pagination();
        }
        publishers = publisherContactService.listPublisherContacts(publisher, page);
        return SUCCESS;
    }
    /**
     * 冻结账户
     * @return
     */
    public String frozen() {
        results = new JsonResults();
        try {
            publisherContactService.updatePublisherStatus(userId, PublisherContactStausEnum.FROZEN);
            results.success();
        } catch (ServiceException e) {
            results.fail(e.getMessage());
        } catch (Exception e) {
            results.fail("系统异常");
        }
        return JsonResults.JSON_RESULT_NAME;
    }
    /**
     * 解冻账户
     * @return
     */
    public String unFrozen() {
        results = new JsonResults();
        try {
            publisherContactService.updatePublisherStatus(userId, PublisherContactStausEnum.NORMAL);
            results.success();
        } catch (ServiceException e) {
            results.fail(e.getMessage());
        } catch (Exception e) {
            results.fail("系统异常");
        }
        return JsonResults.JSON_RESULT_NAME;
    }
    /**
     * 查看
     * @return
     */
    public String view() {
        if (userId <= 0) {
            return INPUT;
        }
        publisher = publisherContactService.viewPublisherContactInfo(userId);
        return SUCCESS;
    }
    /**
     * 更新
     * @return
     */
    public String updatePub() {
        results = new JsonResults();
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

    public List<PublisherContactInfo> getPublishers() {
        return publishers;
    }

    public PublisherContactInfo getPublisher() {
        return publisher;
    }

    public void setPublisher(PublisherContactInfo publisher) {
        this.publisher = publisher;
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }
    public JsonResults getResults() {
        return results;
    }
    public void setResults(JsonResults results) {
        this.results = results;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Map<BankEnum, String> getBankSelector() {
        return bankSelector;
    }

    public Map<FinanceTypeEnum, String> getFinanceTypeSelector() {
        return financeTypeSelector;
    }
}
