package com.buzzinate.buzzads.action.admin;

import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdEntryService;
import com.buzzinate.buzzads.core.service.AdOrderService;
import com.buzzinate.buzzads.core.service.AdvertiserAccountService;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.core.util.UpYun;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdEntry;
import com.buzzinate.buzzads.domain.AdOrder;
import com.buzzinate.buzzads.domain.AdvertiserAccount;
import com.buzzinate.buzzads.enums.AdEntryPosEnum;
import com.buzzinate.buzzads.enums.AdEntrySizeEnum;
import com.buzzinate.buzzads.enums.AdEntryTypeEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.user.LoginHelper;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.JsonResults;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author John Chen
 */

@Controller
@Scope("request")
public class AdEntryManageAction extends ActionSupport {

    private static final long serialVersionUID = 4510647932092204245L;
    private static final Log LOG = LogFactory.getLog(AdEntryManageAction.class);
    private static final String SPLIT_STRING_DOT = ".";
    private static final int MAX_FILE_SIZE = 512 * 1024;
    private static final int MAX_FLASH_SIZE = ConfigurationReader.getInt("buzzads.max.flash.size", 2048 * 1024);
    private static final Pattern NAME_PATTERN = Pattern.compile("^[0-9A-Za-z_ \\u0100-\\uFFFF\\.\\,\\-@]+$");
    private static final String[] IMAGE_NORMAL_EXTENSION = {"jpg", "jpeg", "gif", "png"};
    //private static final String[] IMAGE_NORMAL_FORMAT = {CONTENT_TYPE_JPG1, CONTENT_TYPE_JPG2,
    //    CONTENT_TYPE_GIF, CONTENT_TYPE_PNG1, CONTENT_TYPE_PNG2 };
    private static final String FLASH_NORMAL_EXTENSION = "swf";
    private static Map<AdEntrySizeEnum, String> bannerSizeSelector = AdEntrySizeEnum.bannerSizeSelector();
    private static Map<AdEntrySizeEnum, String> mmSizeSelector = AdEntrySizeEnum.mmSizeSelector();
    private static Log log = LogFactory.getLog(AdEntryManageAction.class);
    private int id;
    private String entryTitle;
    private String resourceUrl;
    private String link;
    private String entryName;
    private String description;
    private String disPlayUrl;
    private int resourceType;
    private int position;
    private AdEntrySizeEnum size;
    private List<AdEntry> adEntryList;
    private AdEntry adEntry;
    private AdOrder adOrder;
    private Pagination page;
    private AdCampaign adCampaign;
    private List<String> resourceTypeSelector;
    private Map<AdStatusEnum, String> entryStatus = AdStatusEnum.getSelectorForAdEntryManage();
    //上传广告图片
    private String fileName;
    private String fileNameExtension;
    @Autowired
    private transient AdEntryService adEntryService;
    @Autowired
    private transient AdOrderService adOrderService;
    @Autowired
    private transient AdvertiserAccountService advertiserAccountService;
    @Autowired
    private transient LoginHelper loginHelper;
    @Autowired
    private transient AdCampaignService adCampaignService;
    private JsonResults results;
    private transient InputStream inputStream;
    private Map<Integer, String> advertiserSelector;
    private int advertiserId;
    private Integer status;
    private int upperStatus = 1;
    private int allEntriesCount;
    private int activeEntriesCount;

    /*
     * 新文件名
     */
    private static String generateFileName(String imageName) {
        String dir = DateTimeUtil.formatDate(DateTimeUtil.getCurrentDate(), DateTimeUtil.FMT_DATE_YYYYMMDD);
        String formatDate = DateTimeUtil.formatDate(DateTimeUtil.getCurrentDate(), DateTimeUtil.FMT_DATE_YYYYMMDDHHMMSS);
        int random = new Random().nextInt(10000);
        int position = imageName.lastIndexOf(SPLIT_STRING_DOT);
        String extension = imageName.substring(position);
        return "/ads/" + dir + "/" + formatDate + random + extension;
    }

    public String execute() {
        if (adEntry == null) {
            adEntry = new AdEntry();
            adEntry.setStatus(AdStatusEnum.ENABLED);
        }
        if (page == null) {
            page = new Pagination();
        }

        allEntriesCount = adEntryService.getAllAdEntriesCount();
        activeEntriesCount = adEntryService.getActiveEntriesCount();
        if (upperStatus == 1) {
            adEntryList = adEntryService.listAdEntriesByUpperStatus(adEntry, page);
        } else {
            adEntryList = adEntryService.listAdEntries(adEntry, page);
        }

        return Action.SUCCESS;
    }

    public String operate() {
        results = new JsonResults();
        if (!loginHelper.hasRole("ROLE_AD_ADMIN")) {
            results.fail("你没有此权限");
        } else {
            AdEntry ad = adEntryService.getEntryById(adEntry.getId());
            if (ad == null) {
                results.fail("此广告不存在");
            } else {
                if (!ad.getStatus().equals(AdStatusEnum.findByValue(status.intValue()))) {
                    adEntryService.adminOperate(adEntry.getId(),
                            AdStatusEnum.findByValue(status.intValue()), ad.getAdvertiserId());
                    results.success();
                }
            }
        }
        return JsonResults.JSON_RESULT_NAME;
    }

    public String view() {
        if (id <= 0) {
            this.addFieldError("id", "No ad entry ID given!");
            return Action.INPUT;
        }
        adEntry = adEntryService.getEntryById(id);
        if (adEntry.getOrderId() <= 0) {
            this.addActionError("Invalid ad order id found in ad entry!");
            return Action.ERROR;
        }
        adOrder = adOrderService.getOrderById(adEntry.getOrderId());
        adCampaign = adCampaignService.getAdCampaignByIdWithoutBudget(adOrder.getCampaignId());
        return Action.SUCCESS;
    }

    public String edit() {
        if (id <= 0) {
            return Action.INPUT;
        }
        adEntry = adEntryService.getEntryById(id);
        resourceTypeSelector = AdEntryTypeEnum.getSelector();
        return Action.SUCCESS;
    }

    public String update() {
        results = new JsonResults();
        try {
            AdEntry entry = adEntryService.getEntryById(id);
            if (entry == null) {
                results.fail("广告不存在！");
                return JsonResults.JSON_RESULT_NAME;
            } else if (entry.getResourceType() == AdEntryTypeEnum.Flash) {
                resourceType = entry.getResourceType().getCode();
            }

            if (!validateEntry()) {
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

            if (size != null && !entry.getSize().equals(size)) {
                entry.setSize(size);
                changed = true;
            }
            if (position > 0 && entry.getPosition().getCode() != position) {
                entry.setPosition(AdEntryPosEnum.findAdEntryPosEnumByCode(position));
                changed = true;
            }


            if (changed) {
                adEntryService.saveOrUpdate(entry);
            } else {
                results.fail("广告信息没有改！");
                return JsonResults.JSON_RESULT_NAME;
            }

            id = entry.getId();
            results.success();
        } catch (Exception e) {
            log.error("Exception.", e);
            results.fail("发生内部错误，请稍候重试！");
        }

        return JsonResults.JSON_RESULT_NAME;
    }

    /**
     * 上传广告富媒体（图片或FLASH）
     *
     * @return
     */
    public String uploadAdEntryResource() {
        results = new JsonResults();
        // validation:
        if (StringUtils.isEmpty(fileName) || StringUtils.isEmpty(fileNameExtension)) {
            results.fail("文件名不能为空");
            buildResponse(results);
            return SUCCESS;
        }
        boolean isFlash = StringUtils.equals(fileNameExtension, FLASH_NORMAL_EXTENSION);
        boolean isImage = ArrayUtils.contains(IMAGE_NORMAL_EXTENSION, fileNameExtension);
        if (!isFlash && !isImage) {
            results.fail("未知的文件格式");
            buildResponse(results);
            return SUCCESS;
        }

        // get file:
        MultiPartRequestWrapper multipartRequest = (MultiPartRequestWrapper) ServletActionContext.getRequest();
        File[] files = multipartRequest.getFiles("tempFile");
        if (files.length <= 0) {
            results.fail("未获取到有效文件");
            buildResponse(results);
            return SUCCESS;
        }
        // check for file size:
        File fileSrc = files[0];
        if ((isImage && fileSrc.length() > MAX_FILE_SIZE) || (isFlash && fileSrc.length() > MAX_FLASH_SIZE)) {
            results.fail("文件太大");
            buildResponse(results);
            return SUCCESS;
        }

        String targetFileName = generateFileName(fileName);
        UpYun yun = new UpYun(isFlash ? "buzzads" : "lezhi", "buzzinate", "buzzinate");
        try {
            yun.writeFile(targetFileName, fileSrc, true);
            results.addContent("isFlash", isFlash);
            results.addContent("isImage", isImage);
            results.addContent("resourceUrl", isFlash ? yun.getClientReadUrl(targetFileName) : yun.getClientReadUrl(targetFileName, "buzz"));
        } catch (Exception e) {
            LOG.error("upyun error", e);
            results.fail("上传又拍云出错");
        }
        buildResponse(results);
        return SUCCESS;
    }

    private void buildResponse(JsonResults results) {
        inputStream = new ByteArrayInputStream(results.toJsonString().getBytes(Charset.forName("utf-8")));
    }

    /**
     * 查看未验证的广告
     *
     * @return
     */
    public String verify() {
        if (page == null) {
            page = new Pagination();
        }
        adEntryList = adEntryService.listAdEntriesByStatus(advertiserId, AdStatusEnum.VERIFYING, page);
        return SUCCESS;
    }

    public String accept() {
        if (id <= 0) {
            addActionError("不正确的广告ID");
        }
        results = new JsonResults();
        try {
            adEntry = adEntryService.getEntryById(id);
            if (adEntry == null) {
                results.fail("广告项不存在，或者您无权修改该广告项！");
                return JsonResults.JSON_RESULT_NAME;
            }
            adEntryService.adminOperate(id, AdStatusEnum.ENABLED,
                    adEntry.getAdvertiserId());
        } catch (Exception e) {
            return results.fail(e.getMessage());
        }
        return results.success();
    }

    public String reject() {
        if (id <= 0) {
            addActionError("不正确的广告ID");
        }
        results = new JsonResults();
        try {
            adEntry = adEntryService.getEntryById(id);
            if (adEntry == null) {
                results.fail("广告项不存在，或者您无权修改该广告项！");
                return JsonResults.JSON_RESULT_NAME;
            }
            adEntryService.adminOperate(id, AdStatusEnum.REJECTED,
                    adEntry.getAdvertiserId());
        } catch (Exception e) {
            return results.fail(e.getMessage());
        }
        return results.success();
    }

    private boolean validateEntry() {
        Matcher matcher = NAME_PATTERN.matcher(entryTitle);
        if (!matcher.matches()) {
            if (resourceType != AdEntryTypeEnum.Flash.getCode()) {
                results.fail("广告标题只能包含中英文,数字,下划线，逗号，句号，横线");
                //检查广告标题
                if (StringUtils.isEmpty(entryTitle)) {
                    results.fail("广告标题不能为空");
                    return false;
                }
                if (entryTitle.length() > 50) {
                    results.fail("广告标题不能大于50个字符");
                    return false;
                }

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
        }

        //检查广告名		        //检查广告名
        if (StringUtils.isEmpty(entryName)) {
            if (StringUtils.isEmpty(entryName)) {
                results.fail("广告名不能为空");
                results.fail("广告名不能为空");
                return false;
            }
        }
        if (entryName.length() > 50) {
            if (entryName.length() > 50) {
                results.fail("广告名不能大于50个字符");
                results.fail("广告名不能大于50个字符");
                return false;
            }
        }
        matcher = NAME_PATTERN.matcher(entryName);
        if (!matcher.matches()) {
            if (!matcher.matches()) {
                results.fail("广告名只能包含中英文,数字");
                results.fail("广告名只能包含中英文,数字");
                return false;
            }
        }

        return true;
    }

    public List<AdEntry> getAdEntryList() {
        return adEntryList;
    }

    public AdEntry getAdEntry() {
        return adEntry;
    }

    public void setAdEntry(AdEntry adEntry) {
        this.adEntry = adEntry;
    }

    public AdOrder getAdOrder() {
        return adOrder;
    }

    public int getId() {
        return id;
    }

    public void setId(String id) {
        try {
            this.id = Integer.valueOf(id);
        } catch (Exception e) {
            this.id = 0;
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public int getResourceType() {
        return resourceType;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNameExtension() {
        return fileNameExtension;
    }

    public void setFileNameExtension(String fileNameExtension) {
        this.fileNameExtension = fileNameExtension;
    }

    public JsonResults getResults() {
        return results;
    }

    public List<String> getResourceTypeSelector() {
        return resourceTypeSelector;
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public Map<Integer, String> getAdvertiserSelector() {
        List<AdvertiserAccount> accounts = advertiserAccountService.listAllAdvertisersByAdmin();
        if (advertiserSelector == null) {
            advertiserSelector = new HashMap<Integer, String>();
        }
        for (AdvertiserAccount account : accounts) {
            advertiserSelector.put(account.getAdvertiserId(), account.getCompanyName());
        }
        return advertiserSelector;
    }

    public int getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(int advertiserId) {
        this.advertiserId = advertiserId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Map<AdStatusEnum, String> getEntryStatus() {
        return entryStatus;
    }

    public void setEntryStatus(Map<AdStatusEnum, String> entryStatus) {
        this.entryStatus = entryStatus;
    }

    public Map<Integer, String> getAdvertisers() {
        Map<Integer, String> advertiserMap = new HashMap<Integer, String>();
        advertiserMap.put(0, "全部");
        List<AdvertiserAccount> advertiserAccountList = advertiserAccountService.listAllAdvertisersByAdmin();
        for (AdvertiserAccount account : advertiserAccountList) {
            advertiserMap.put(account.getAdvertiserId(), account.getCompanyName());
        }
        return advertiserMap;
    }

    public Map<Integer, String> getCampaigns() {
        Map<Integer, String> campaignMap = new HashMap<Integer, String>();
        campaignMap.put(0, "全部");
        if (adEntry == null || adEntry.getAdvertiserId() <= 0) {
            return campaignMap;
        }
        List<AdCampaign> campaignList = adCampaignService.getCampaignsByAdvertiserId(adEntry.getAdvertiserId());
        for (AdCampaign campaign : campaignList) {
            campaignMap.put(campaign.getId(), campaign.getName());
        }
        return campaignMap;
    }

    public Map<Integer, String> getAdOrders() {
        Map<Integer, String> adOrderMap = new HashMap<Integer, String>();
        adOrderMap.put(0, "全部");
        if (adEntry == null || adEntry.getCampaignId() <= 0) {
            return adOrderMap;
        }
        List<AdOrder> adOrderList = adOrderService.getAdOrdersByCampaignId(adEntry.getCampaignId());
        for (AdOrder order : adOrderList) {
            adOrderMap.put(order.getId(), order.getName());
        }
        return adOrderMap;
    }

    public AdCampaign getAdCampaign() {
        return adCampaign;
    }

    public void setAdCampaign(AdCampaign adCampaign) {
        this.adCampaign = adCampaign;
    }

    public Integer getUpperStatus() {
        return upperStatus;
    }

    public void setUpperStatus(Integer upperStatus) {
        this.upperStatus = upperStatus;
    }

    public int getAllEntriesCount() {
        return allEntriesCount;
    }

    public void setAllEntriesCount(int allEntriesCount) {
        this.allEntriesCount = allEntriesCount;
    }

    public int getActiveEntriesCount() {
        return activeEntriesCount;
    }

    public void setActiveEntriesCount(int activeEntriesCount) {
        this.activeEntriesCount = activeEntriesCount;
    }

    public String getEntryTitle() {
        return entryTitle;
    }

    public void setEntryTitle(String entryTitle) {
        this.entryTitle = entryTitle;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisPlayUrl() {
        return disPlayUrl;
    }

    public void setDisPlayUrl(String disPlayUrl) {
        this.disPlayUrl = disPlayUrl;
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
