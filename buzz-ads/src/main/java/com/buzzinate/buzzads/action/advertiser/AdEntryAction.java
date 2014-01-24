package com.buzzinate.buzzads.action.advertiser;

import com.buzzinate.buzzads.core.service.*;
import com.buzzinate.buzzads.domain.AdCampBudget;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdEntry;
import com.buzzinate.buzzads.domain.AdOrder;
import com.buzzinate.buzzads.enums.AdEntryPosEnum;
import com.buzzinate.buzzads.enums.AdEntrySizeEnum;
import com.buzzinate.buzzads.enums.AdEntryTypeEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.user.LoginHelper;
import com.buzzinate.common.util.JsonResults;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Johnson
 */
@Controller
@Scope("request")
public class AdEntryAction extends ActionSupport {

    private static final long serialVersionUID = 2117997393892589280L;
    private static final Pattern NAME_PATTERN = Pattern.compile("^[0-9A-Za-z_ \\u0100-\\uFFFF\\.\\,\\-@]+$");
    private static Log log = LogFactory.getLog(AdEntryAction.class);
    private static Map<AdEntrySizeEnum, String> bannerSizeSelector = AdEntrySizeEnum.bannerSizeSelector();
    private static Map<AdEntrySizeEnum, String> mmSizeSelector = AdEntrySizeEnum.mmSizeSelector();
    @Autowired
    private LoginHelper loginHelper;
    @Autowired
    private AdCampaignService adCampaignService;
    @Autowired
    private AdOrderService adOrderService;
    @Autowired
    private AdEntryService adEntryService;
    @Autowired
    private AdCampaignBudgetService adCampaignBudgetService;
    @Autowired
    private CampDayBudgetService campDayBudgetService;
    private JsonResults results;
    private int entryId;
    private int orderId;
    private int campaignId;
    private String entryName;
    private String entryTitle;
    private String description;
    private int resourceType;
    private int position;
    private String resourceUrl;
    private String disPlayUrl;
    private String link;
    private List<AdCampaign> campaigns;
    private List<AdOrder> groups;
    private AdCampaign selectedCampaign;
    private AdOrder selectedGroup;
    private AdEntry adEntry;
    private AdEntrySizeEnum size = AdEntrySizeEnum.SIZE80x80;

    public String add() {
        int advertiserId = loginHelper.getUserId();
        campaigns = adCampaignService.getCampaignsByAdvertiserId(advertiserId);
        if (campaigns.size() == 0) {
            addActionMessage("您还没有广告活动，请先创建!");
            return "noCamp";
        }

        // select the campaign:
        if (campaignId > 0) {
            selectedCampaign = getAdCampaign(campaigns, campaignId);
            if (selectedCampaign == null) {
                addFieldError("campaignId", "不是合法的活动ID!");
            } else {
                // continue to select the ad order:
                groups = adOrderService.getAdOrdersByCampaignId(selectedCampaign.getId());
                if (orderId > 0) {
                    selectedGroup = getAdOrder(groups, orderId);
                    if (selectedGroup == null) {
                        addFieldError("orderId", "不是合法的广告组ID!");
                    }
                } else {
                    // no order id was given... get the first ad order in this campaign
                    if (groups.size() == 0) {
                        addActionMessage("该活动下还没有广告组，请先创建!");
                        return "noGroup";
                    }

                    // select the group, default the first of the list
                    if (orderId > 0) {
                        selectedGroup = getAdOrder(groups, orderId);
                        if (selectedGroup == null) {
                            addFieldError("orderId", "不是合法的广告组ID!");
                        }
                    } else {
                        selectedGroup = groups.get(0);
                    }
                }
            }
        } else {
            // no campaignId was given... 
            List<AdOrder> aoList = adOrderService.getAdOrdersByAdvertiserId(advertiserId);

            // see if a group id was given:
            if (orderId > 0) {
                selectedGroup = getAdOrder(aoList, orderId);
                if (selectedGroup == null) {
                    addFieldError("orderId", "不是合法的广告组ID!");
                } else {
                    selectedCampaign = getAdCampaign(campaigns, selectedGroup.getCampaignId());
                    groups = adOrderService.getAdOrdersByCampaignId(selectedCampaign.getId());
                }
            } else {
                // find the first ad group as the default
                if (aoList == null || aoList.isEmpty()) {
                    // this advertiser has no ad orders! the user will need to create an ad group
                    // default choose the first campaign:
                    selectedCampaign = campaigns.get(0);
                    campaignId = selectedCampaign.getId();
                    addActionMessage("你还没有广告组，请先创建!");
                    return "noGroup";
                } else {
                    // set the selected group as the first ad group and
                    // set the selected campaign as the first ad group in the list's campaign:
                    selectedGroup = aoList.get(0);
                    selectedCampaign = getAdCampaign(campaigns, aoList.get(0).getCampaignId());
                    groups = adOrderService.getAdOrdersByCampaignId(selectedCampaign.getId());
                }
            }
        }

        if (selectedCampaign != null) {
            campaignId = selectedCampaign.getId();
            AdCampBudget budget = adCampaignBudgetService.getCampBudget(campaignId);
            if (budget != null) {
                selectedCampaign.setMaxDayBudget(budget.getMaxBudgetDay());
                selectedCampaign.setMaxBudgetTotal(budget.getMaxBudgetTotal());
            }
            selectedCampaign.setDayBudgets(campDayBudgetService.findCampaignDayBudgetByCampId(campaignId));
        }
        if (selectedGroup != null) {
            orderId = selectedGroup.getId();
        }
        selectedGroup.setCampBidType(selectedCampaign.getBidType());
        return Action.SUCCESS;
    }

    private boolean isInvalidUser(int userId) {
        if (!loginHelper.isLoginAsAdmin() && userId != loginHelper.getUserId()) {
            return true;
        }
        return false;
    }

    public String create() {
        results = new JsonResults();
        try {
            if (!validateEntry()) {
                return JsonResults.JSON_RESULT_NAME;
            }

            selectedCampaign = adCampaignService.getAdCampaignById(campaignId);
            if (selectedCampaign == null || isInvalidUser(selectedCampaign.getAdvertiserId())) {
                results.fail("广告活动不存在，或者您无权在该广告活动下创建广告！");
                return JsonResults.JSON_RESULT_NAME;
            }
            selectedGroup = adOrderService.getOrderById(orderId);
            if (selectedGroup == null || selectedGroup.getAdvertiserId() != loginHelper.getUserId()) {
                results.fail("广告组不存在，或者您无权在该广告组下创建广告！");
                return JsonResults.JSON_RESULT_NAME;
            }
            AdEntry entry = new AdEntry();
            entry.setId(entryId);
            entry.setResourceType(AdEntryTypeEnum.findByValue(resourceType));
            entry.setPosition(AdEntryPosEnum.findAdEntryPosEnumByCode(position));
            if (entry.getResourceType().equals(AdEntryTypeEnum.IMAGE)) {
                if (StringUtils.isEmpty(resourceUrl)) {
                    results.fail("图片不能为空，请上传图片");
                    return JsonResults.JSON_RESULT_NAME;
                }
            } else if (entry.getResourceType().equals(AdEntryTypeEnum.Flash)) {
                if (StringUtils.isEmpty(resourceUrl)) {
                    results.fail("FLASH不能为空，请上传或提供资源url");
                    return JsonResults.JSON_RESULT_NAME;
                }
            }

            if (size != null) {
                entry.setSize(size);
            }
            entry.setName(entryName);
            entry.setTitle(entryTitle);
            entry.setLink(StringUtils.defaultIfEmpty(link, ""));
            entry.setDescription(description);
            entry.setDisplayUrl(disPlayUrl);
            entry.setCampaignId(campaignId);
            entry.setOrderId(orderId);
            entry.setAdvertiserId(loginHelper.getUserId());
            entry.setResourceUrl(StringUtils.defaultIfEmpty(resourceUrl, ""));
            adEntryService.saveOrUpdate(entry);
            entryId = entry.getId();
            results.success();
        } catch (Exception e) {
            log.error("Exception.", e);
            results.fail("发生内部错误，请稍候重试！");
        }
        return JsonResults.JSON_RESULT_NAME;
    }

    public String change() {
        results = new JsonResults();
        try {
            AdEntry entry = adEntryService.getEntryById(entryId);

            if (entry == null) {
                results.fail("广告不存在！");
                return JsonResults.JSON_RESULT_NAME;
            } else if (entry.getResourceType() == AdEntryTypeEnum.Flash) {
                resourceType = entry.getResourceType().getCode();
            }
            if (!validateEntry()) {
                return JsonResults.JSON_RESULT_NAME;
            }


            if (entry.getAdvertiserId() != loginHelper.getUserId()) {
                results.fail("广告不存在！");
                return JsonResults.JSON_RESULT_NAME;
            }
            if (entry.getCampaignId() != campaignId) {
                results.fail("广告活动不存在！");
                return JsonResults.JSON_RESULT_NAME;
            }
            if (entry.getOrderId() != orderId) {
                results.fail("广告组不存在！");
                return JsonResults.JSON_RESULT_NAME;
            }

            boolean changed = false;

            if (!entry.getResourceType().equals(AdEntryTypeEnum.findByValue(resourceType))) {
                entry.setResourceType(AdEntryTypeEnum.findByValue(resourceType));
                changed = true;
            }

            if (entry.getResourceType().equals(AdEntryTypeEnum.IMAGE)) {
                if (StringUtils.isEmpty(resourceUrl)) {
                    results.fail("图片不能为空，请上传图片");
                    return JsonResults.JSON_RESULT_NAME;
                }
                if (!entry.getResourceUrl().equals(resourceUrl)) {
                    entry.setResourceUrl(resourceUrl);
                    changed = true;
                }
            } else if (entry.getResourceType().equals(AdEntryTypeEnum.Flash)) {
                if (StringUtils.isEmpty(resourceUrl)) {
                    results.fail("Flash不能为空,请上传或提供资源url");
                    return JsonResults.JSON_RESULT_NAME;
                }
                if (!entry.getResourceUrl().equals(resourceUrl)) {
                    entry.setResourceUrl(resourceUrl);
                    changed = true;
                }
            } else {
                entry.setResourceUrl("");
            }


            if (!entry.getName().equals(entryName)) {
                entry.setName(entryName);
                changed = true;
            }
            if (!entry.getTitle().equals(entryTitle)) {
                entry.setTitle(entryTitle);
                changed = true;
            }
            if (link != null && !entry.getLink().equals(link)) {
                entry.setLink(link);
                changed = true;
            }
            if (description != null && !entry.getDescription().equals(description)) {
                entry.setDescription(description);
                changed = true;
            }
            if (disPlayUrl != null && !entry.getDisplayUrl().equals(disPlayUrl)) {
                entry.setDisplayUrl(disPlayUrl);
                changed = true;
            }
            if (position != 0 && position != entry.getPosition().getCode()) {
                entry.setPosition(AdEntryPosEnum.findAdEntryPosEnumByCode(position));
                changed = true;
            }
            if (size != null && !entry.getSize().equals(size)) {
                entry.setSize(size);
                changed = true;
            }

            if (changed) {
                //广告修改后需设置为待审核
                entry.setStatus(AdStatusEnum.VERIFYING);
                adEntryService.saveOrUpdate(entry);
            } else {
                results.fail("广告信息没有改！");
                return JsonResults.JSON_RESULT_NAME;
            }

            entryId = entry.getId();
            results.success();
        } catch (Exception e) {
            log.error("Exception.", e);
            results.fail("发生内部错误，请稍候重试！");
        }
        return JsonResults.JSON_RESULT_NAME;
    }

    public String edit() {
        if (entryId <= 0) {
            addFieldError("Id", "不正确的广告ID");
            return Action.INPUT;
        }
        adEntry = adEntryService.getEntryById(entryId);
        if (adEntry == null || isInvalidUser(adEntry.getAdvertiserId())) {
            addActionError("广告项不存在，或者您无权修改该广告项！");
            return Action.ERROR;
        }
        selectedCampaign = adCampaignService.getAdCampaignById(adEntry.getCampaignId());
        selectedGroup = adOrderService.getOrderById(adEntry.getOrderId());
        return Action.SUCCESS;
    }

    public String enable() {
        if (entryId <= 0) {
            addActionError("不正确的广告ID");
        }
        results = new JsonResults();
        try {
            adEntry = adEntryService.getEntryById(entryId);
            if (adEntry == null || isInvalidUser(adEntry.getAdvertiserId())) {
                results.fail("广告项不存在，或者您无权修改该广告项！");
                return JsonResults.JSON_RESULT_NAME;
            }
            if (!adEntry.getStatus().equals(AdStatusEnum.PAUSED)) {
                results.fail("您只能启用暂停的广告！");
                return JsonResults.JSON_RESULT_NAME;
            }
            adEntryService.updateEntryStatus(entryId, AdStatusEnum.ENABLED);
            return results.success();
        } catch (Exception e) {
            log.error("Exception.", e);
            return results.fail("发生内部错误，请稍候重试！");
        }
    }

    public String pause() {
        if (entryId <= 0) {
            addActionError("不正确的广告ID");
        }
        results = new JsonResults();
        try {
            adEntry = adEntryService.getEntryById(entryId);
            if (adEntry == null || isInvalidUser(adEntry.getAdvertiserId())) {
                results.fail("广告项不存在，或者您无权修改该广告项！");
                return JsonResults.JSON_RESULT_NAME;
            }
            if (!adEntry.getStatus().equals(AdStatusEnum.ENABLED)) {
                results.fail("您只能暂停启用的广告!");
                return JsonResults.JSON_RESULT_NAME;
            }
            adEntryService.updateEntryStatus(entryId, AdStatusEnum.PAUSED);
            return results.success();
        } catch (Exception e) {
            log.error("Exception.", e);
            return results.fail("发生内部错误，请稍候重试！");
        }
    }

    /**
     * 查看详情
     *
     * @return
     */
    public String view() {
        adEntry = adEntryService.getEntryById(entryId);
        return SUCCESS;
    }

    private boolean validateEntry() {
        //检查广告标题
        Matcher matcher;
        if (resourceType != AdEntryTypeEnum.Flash.getCode()) {
            if (StringUtils.isEmpty(entryTitle)) {
                results.fail("广告标题不能为空");
                return false;
            }
            if (entryTitle.length() > 50) {
                results.fail("广告标题不能大于50个字符");
                return false;
            }
            matcher = NAME_PATTERN.matcher(entryTitle);
            if (!matcher.matches()) {
                results.fail("广告标题只能包含中英文,数字,下划线，逗号，句号，横线");
                return false;
            }
            //检查广告链接
            if (StringUtils.isEmpty(link) || "http:\\\\".equals(link) ||
                    "https:\\\\".equals(link)) {
                results.fail("广告链接不能为空");
                return false;
            }
        }

        //检查广告名
        if (StringUtils.isEmpty(entryName)) {
            results.fail("广告名不能为空");
            return false;
        }
        if (entryName.length() > 50) {
            results.fail("广告名不能大于50个字符");
            return false;
        }
        matcher = NAME_PATTERN.matcher(entryName);
        if (!matcher.matches()) {
            results.fail("广告名只能包含中英文,数字");
            return false;
        }


        return true;
    }

    private AdCampaign getAdCampaign(List<AdCampaign> adCampaigns, int id) {
        for (AdCampaign campaign : adCampaigns) {
            if (campaign.getId() == id) {
                return campaign;
            }
        }
        return null;
    }

    private AdOrder getAdOrder(List<AdOrder> orders, int id) {
        for (AdOrder order : orders) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getEntryTitle() {
        return entryTitle;
    }

    public void setEntryTitle(String entryTitle) {
        this.entryTitle = entryTitle;
    }

    public int getResourceType() {
        return resourceType;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getDisPlayUrl() {
        return disPlayUrl;
    }

    public void setDisPlayUrl(String disPlayUrl) {
        this.disPlayUrl = disPlayUrl;
    }

    public Integer getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<Integer, String> getCampaignSelector() {
        Map<Integer, String> campaignSelector = new HashMap<Integer, String>();
        if (campaigns != null) {
            for (AdCampaign adCampaign : campaigns) {
                campaignSelector.put(adCampaign.getId(), adCampaign.getName());
            }
        }
        return campaignSelector;
    }

    public Map<Integer, String> getGroupSelector() {
        Map<Integer, String> groupSelector = new HashMap<Integer, String>();
        if (groups != null) {
            for (AdOrder adOrder : groups) {
                groupSelector.put(adOrder.getId(), adOrder.getName());
            }
        }
        return groupSelector;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public AdCampaign getSelectedCampaign() {
        return selectedCampaign;
    }

    public void setSelectedCampaign(AdCampaign selectedCampaign) {
        this.selectedCampaign = selectedCampaign;
    }

    public AdOrder getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(AdOrder selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public AdEntry getAdEntry() {
        return adEntry;
    }

    public void setAdEntry(AdEntry adEntry) {
        this.adEntry = adEntry;
    }

    public JsonResults getResults() {
        return results;
    }

    public void setResults(JsonResults results) {
        this.results = results;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public AdEntrySizeEnum getSize() {
        return size;
    }

    public void setSize(AdEntrySizeEnum size) {
        this.size = size;
    }

    public Map<AdEntrySizeEnum, String> getBannerSizeSelector() {
        return bannerSizeSelector;
    }

    public Map<AdEntrySizeEnum, String> getMmSizeSelector() {
        return mmSizeSelector;
    }

}
