package com.buzzinate.buzzads.action.advertiser;

import java.util.List;

import com.buzzinate.buzzads.user.LoginHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdvertiserAccountService;
import com.buzzinate.buzzads.core.service.AdvertiserContactInfoService;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdvertiserAccount;
import com.buzzinate.buzzads.domain.AdvertiserContactInfo;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.TextProvider;

/**
 * 
 * @author Johnson
 * 
 */
@Controller
@Scope("request")
public class AdvertiserAccountAction extends ActionSupport {

    private static final long serialVersionUID = 4219418091800963547L;

    private AdvertiserAccount advertiserAccount;
    private int contactId;

    private List<AdvertiserContactInfo> advertiserContactInfos;
    private AdvertiserContactInfo advertiserContactInfo;
    @Autowired
    private AdvertiserAccountService advertiserAccountService;
    @Autowired
    private AdCampaignService adCampaignService;

    @Autowired
    private AdvertiserContactInfoService advertiserContactInfoService;
    @Autowired
    private LoginHelper loginHelper;
    private TextProvider textProvider;
    private boolean hasContactInfo = true;

    public String execute() {
        if (loginHelper.isLoginAsAdvertiser()) {
            int advertiserId = loginHelper.getUserId();
            // if already have an account, navigate to info page
            advertiserAccount = advertiserAccountService.getAdvertiserAccount(advertiserId);

            if (advertiserAccount == null) {
                advertiserAccount = new AdvertiserAccount();
                advertiserAccount.setAdvertiserId(loginHelper.getUserId());
            } else {
                return "account";
            }
            hasContactInfo = false;
            return Action.SUCCESS;
        } else {
            addActionError(textProvider.getText("buzzads.advertiser.unauthorized"));
            return Action.ERROR;
        }

    }

    public String account() {
        if (loginHelper.isLoginAsAdvertiser()) {
            int advertiserId = loginHelper.getUserId();
            advertiserAccount = advertiserAccountService.getAdvertiserAccount(advertiserId);
            if (advertiserAccount == null) {
                return "new";
            } else {
                advertiserContactInfos = advertiserContactInfoService.getAdvertiserContactInfo(advertiserId);
                //目前  前台只支持添加一个联系人  以后会支持多个 
                if (advertiserContactInfos != null && advertiserContactInfos.size() > 0) {
                    advertiserContactInfo = advertiserContactInfos.get(0);
                }
                return Action.SUCCESS;
            }
        } else {
            addActionError(textProvider.getText("buzzads.advertiser.unauthorized"));
            return Action.ERROR;
        }
    }

    public String save() {
        if (loginHelper.isLoginAsAdvertiser()) {
            int advertiserId = loginHelper.getUserId();
            advertiserAccount.setAdvertiserId(advertiserId);
            advertiserContactInfo.setAdvertiserId(advertiserId);
            try {
                advertiserAccountService.savaOrUpdate(advertiserAccount, advertiserContactInfo);
            } catch (Exception e) {
                addActionError("更改或保存信息失败");
            }
            
            List<AdCampaign> camps = adCampaignService.getCampaignsByAdvertiserId(advertiserId);
            if (camps == null || camps.size() == 0) {
                addActionMessage("您还没有广告，请创建首个广告");
                return "campaign/new";
            }

            return Action.SUCCESS;
        } else {
            addActionError(textProvider.getText("buzzads.advertiser.unauthorized"));
            return Action.ERROR;
        }
    }
    
    public String saveAccountInfo() {
        if (loginHelper.isLoginAsAdvertiser()) {
            int advertiserId = loginHelper.getUserId();
            advertiserAccount.setAdvertiserId(advertiserId);
            try {
                advertiserAccountService.saveOrUpdate(advertiserAccount);
            } catch (Exception e) {
                addActionError("更改或保存信息失败");
            }

            return Action.SUCCESS;
        } else {
            addActionError(textProvider.getText("buzzads.advertiser.unauthorized"));
            return Action.ERROR;
        }
    }
    
    public String contactEdit() {
        if (loginHelper.isLoginAsAdvertiser()) {
            int advertiserId = loginHelper.getUserId();
            advertiserAccount = advertiserAccountService.getAdvertiserAccount(advertiserId);
            if (advertiserAccount == null) {
                addActionError(textProvider.getText("buzzads.advertiser.unauthorized"));
                return Action.ERROR;
            }
            
            if (contactId < 0) {
                addActionError("更改或保存信息失败");
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
                addActionError(textProvider.getText("buzzads.advertiser.unauthorized"));
                return Action.ERROR;
            }
            
            advertiserContactInfo = aciFound;
            
            return Action.SUCCESS;
        } else {
            addActionError(textProvider.getText("buzzads.advertiser.unauthorized"));
            return Action.ERROR;
        }
    }
    
    public String contactNew() {
        if (loginHelper.isLoginAsAdvertiser()) {
            int advertiserId = loginHelper.getUserId();
            advertiserAccount = advertiserAccountService.getAdvertiserAccount(advertiserId);
            if (advertiserAccount == null) {
                addActionError(textProvider.getText("buzzads.advertiser.unauthorized"));
                return Action.ERROR;
            }
            
            return Action.SUCCESS;
        } else {
            addActionError(textProvider.getText("buzzads.advertiser.unauthorized"));
            return Action.ERROR;
        }
    }
    
    public String saveAccountContactInfo() {
        if (loginHelper.isLoginAsAdvertiser()) {
            int advertiserId = loginHelper.getUserId();
            advertiserContactInfo.setAdvertiserId(advertiserId);
            try {
                advertiserAccountService.saveOrUpdateContact(advertiserContactInfo);
            } catch (Exception e) {
                addActionError("更改或保存信息失败");
            }

            return Action.SUCCESS;
        } else {
            addActionError(textProvider.getText("buzzads.advertiser.unauthorized"));
            return Action.ERROR;
        }
    }

    public String edit() {
        if (loginHelper.isLoginAsAdvertiser()) {
            advertiserAccount = advertiserAccountService.getAdvertiserAccount(loginHelper.getUserId());
            advertiserContactInfos = advertiserContactInfoService.getAdvertiserContactInfo(loginHelper.getUserId());
            //目前  前台只支持添加一个联系人  以后会支持多个 
            if (advertiserContactInfos != null && advertiserContactInfos.size() > 0) {
                advertiserContactInfo = advertiserContactInfos.get(0);
            }
            return Action.SUCCESS;
        } else {
            addActionError(textProvider.getText("buzzads.advertiser.unauthorized"));
            return Action.ERROR;
        }
    }
    
    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public AdvertiserAccount getAdvertiserAccount() {
        return advertiserAccount;
    }

    public void setAdvertiserAccount(AdvertiserAccount advertiserAccount) {
        this.advertiserAccount = advertiserAccount;
    }

    public AdvertiserContactInfo getAdvertiserContactInfo() {
        return advertiserContactInfo;
    }

    public void setAdvertiserContactInfo(AdvertiserContactInfo advertiserContactInfo) {
        this.advertiserContactInfo = advertiserContactInfo;
    }

    public List<AdvertiserContactInfo> getAdvertiserContactInfos() {
        return advertiserContactInfos;
    }

    public void setAdvertiserContactInfos(List<AdvertiserContactInfo> advertiserContactInfos) {
        this.advertiserContactInfos = advertiserContactInfos;
    }

    public void setTextProvider(TextProvider textProvider) {
        this.textProvider = textProvider;
    }

    public boolean isHasContactInfo() {
        return hasContactInfo;
    }

    public void setHasContactInfo(boolean hasContactInfo) {
        this.hasContactInfo = hasContactInfo;
    }

}
