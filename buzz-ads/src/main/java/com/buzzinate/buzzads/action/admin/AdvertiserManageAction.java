package com.buzzinate.buzzads.action.admin;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.common.exception.ServiceException;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdvertiserAccountService;
import com.buzzinate.buzzads.core.service.AdvertiserBalanceService;
import com.buzzinate.buzzads.core.service.AdvertiserContactInfoService;
import com.buzzinate.buzzads.core.service.UserAuthorityService;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.AdvertiserAccount;
import com.buzzinate.buzzads.domain.AdvertiserBalance;
import com.buzzinate.buzzads.domain.AdvertiserContactInfo;
import com.buzzinate.buzzads.domain.UserAuthority;
import com.buzzinate.buzzads.enums.AdvertiserStatusEnum;
import com.buzzinate.common.util.JsonResults;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Apr 1, 2013 3:08:10 PM
 * 
 */
@Controller
@Scope("request")
public class AdvertiserManageAction extends ActionSupport {
    private static final long serialVersionUID = -514917061134364L;

    @Autowired
    private AdvertiserAccountService advertiserAccountService;
    @Autowired
    private AdvertiserContactInfoService advertiserContactInfoService;
    @Autowired
    private AdvertiserBalanceService balanceService;
    @Autowired
    private AdCampaignService campaignService;
    @Autowired
    private UserAuthorityService userAuthorityService;

    private String name;
    private String companyName = "";
    private String email;
    private Integer advertiserId;
    private Pagination page;
    private Integer contactId;

    private AdvertiserAccount advertiserAccount;
    private List<AdvertiserAccount> accounts;
    private List<AdvertiserContactInfo> advertiserContactInfos;
    private AdvertiserContactInfo advertiserContactInfo;
    private JsonResults results;
    private UserAuthority userAuthority;

    @Override
    public String execute() {
        if (page == null) {
            page = new Pagination();
        }
        List<Integer> advertiserIds = null;
        if (StringUtils.isNotBlank(name) || StringUtils.isNotBlank(email)) {
            advertiserIds = advertiserContactInfoService.getAdvertiserIdsByNameAndEmail(name, email);
            if (advertiserIds == null || advertiserIds.isEmpty()) {
                return Action.SUCCESS;
            }
        }
        accounts = advertiserAccountService.getAdvertisers(companyName, advertiserIds, page);
        //补充信息
        if (accounts.size() > 0) {
            for (AdvertiserAccount ac : accounts) {
                //账户信息
                AdvertiserBalance balance = balanceService.getByAdvertiserId(ac.getAdvertiserId());
                if (balance != null) {
                    ac.setBalance(balance.getBalance());
                    ac.setDebitsTotal(balance.getDebitsTotal());
                }
                //活动信息
                ac.setLiveCampCount(campaignService.getActiveCampaignsCountByAdv(ac.getAdvertiserId()));
            }
        }

        return Action.SUCCESS;
    }

    public String view() {
        advertiserAccount = advertiserAccountService.getAdvertiserAccount(advertiserId);
        advertiserContactInfos = advertiserContactInfoService.getAdvertiserContactInfo(advertiserId);
        //检查权限
        userAuthority = userAuthorityService.getUserAuthorityByUserId(advertiserId);
        return Action.SUCCESS;
    }
    
    public String edit() {
        advertiserAccount = advertiserAccountService.getAdvertiserAccount(advertiserId);
        return Action.SUCCESS;
    }
    
    public String contactEdit() {
        if (contactId < 0) {
            addActionError("Invalid contact id provided!");
            return Action.ERROR;
        }
        
        advertiserAccount = advertiserAccountService.getAdvertiserAccount(advertiserId);
        if (advertiserAccount == null) {
            addActionError("This advertiser id does not exist!");
            return Action.ERROR;
        }
        
        List<AdvertiserContactInfo> aciList = advertiserContactInfoService.getAdvertiserContactInfo(advertiserId);
        AdvertiserContactInfo aciFound = null;
        for (AdvertiserContactInfo aci : aciList) {
            if (aci.getId() == contactId) {
                aciFound = aci;
                break;
            }
        }
        if (aciFound == null) {
            addActionError("This contact id does not exist!");
            return Action.ERROR;
        }
        
        advertiserContactInfo = aciFound;
        
        return Action.SUCCESS;
    }
    
    public String contactNew() {
        advertiserAccount = advertiserAccountService.getAdvertiserAccount(advertiserId);
        if (advertiserAccount == null) {
            addActionError("This advertiser id does not exist!");
            return Action.ERROR;
        }
        
        return Action.SUCCESS;
    }
    
    public String saveAccountInfo() {
        try {
            advertiserAccountService.saveOrUpdate(advertiserAccount);
            advertiserId = advertiserAccount.getAdvertiserId();
        } catch (Exception e) {
            addActionError("更改或保存信息失败");
        }

        return Action.SUCCESS;
    }
    
    public String saveAccountContactInfo() {
        try {
            advertiserAccountService.saveOrUpdateContact(advertiserContactInfo);
            advertiserId = advertiserContactInfo.getAdvertiserId();
        } catch (Exception e) {
            addActionError("更改或保存信息失败");
        }

        return Action.SUCCESS;
    }

    /**
     * 冻结账户
     * 
     * @return
     */
    public String frozen() {
        results = new JsonResults();
        try {
            advertiserAccountService.updateAdvertiserStatus(advertiserId, AdvertiserStatusEnum.FROZEN);
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
     * 
     * @return
     */
    public String unFrozen() {
        results = new JsonResults();
        try {
            advertiserAccountService.updateAdvertiserStatus(advertiserId, AdvertiserStatusEnum.NORMAL);
            results.success();
        } catch (ServiceException e) {
            results.fail(e.getMessage());
        } catch (Exception e) {
            results.fail("系统异常");
        }
        return JsonResults.JSON_RESULT_NAME;
    }

    public List<AdvertiserAccount> getAccounts() {
        return accounts;
    }
    public void setAccounts(List<AdvertiserAccount> accounts) {
        this.accounts = accounts;
    }
    public List<AdvertiserContactInfo> getAdvertiserContactInfos() {
        return advertiserContactInfos;
    }
    public void setAdvertiserContactInfos(List<AdvertiserContactInfo> advertiserContactInfos) {
        this.advertiserContactInfos = advertiserContactInfos;
    }
    public AdvertiserAccount getAdvertiserAccount() {
        return advertiserAccount;
    }
    public void setAdvertiserAccount(AdvertiserAccount advertiserAccount) {
        this.advertiserAccount = advertiserAccount;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Integer getAdvertiserId() {
        return advertiserId;
    }
    public void setAdvertiserId(Integer advertiserId) {
        this.advertiserId = advertiserId;
    }
    public Integer getContactId() {
        return contactId;
    }
    public void setContactId(Integer contactId) {
        this.contactId = contactId;
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
    public AdvertiserContactInfo getAdvertiserContactInfo() {
        return advertiserContactInfo;
    }

    public void setAdvertiserContactInfo(AdvertiserContactInfo advertiserContactInfo) {
        this.advertiserContactInfo = advertiserContactInfo;
    }

    public UserAuthority getUserAuthority() {
        return userAuthority;
    }

    public void setUserAuthority(UserAuthority userAuthority) {
        this.userAuthority = userAuthority;
    }
}
