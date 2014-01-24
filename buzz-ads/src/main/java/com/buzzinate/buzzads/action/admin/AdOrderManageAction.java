package com.buzzinate.buzzads.action.admin;

import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdOrderCategoryService;
import com.buzzinate.buzzads.core.service.AdOrderService;
import com.buzzinate.buzzads.core.service.AdvertiserAccountService;
import com.buzzinate.buzzads.core.service.ChannelService;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdEntry;
import com.buzzinate.buzzads.domain.AdOrder;
import com.buzzinate.buzzads.domain.AdOrderCategory;
import com.buzzinate.buzzads.domain.AdvertiserAccount;
import com.buzzinate.buzzads.domain.Channel;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.AdsTypeEnum;
import com.buzzinate.buzzads.util.DateRangeUtil;
import com.buzzinate.common.util.JsonResults;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author John Chen
 */

@Controller
@Scope("request")
public class AdOrderManageAction extends ActionSupport {

    private static final int MAX_BID_PRICE = ConfigurationReader.getInt("ads.max.bid.price") * 100;
    private static final Pattern NAME_PATTERN = Pattern.compile("^[0-9A-Za-z_ \\u0100-\\uFFFF\\.\\,\\-@]+$");
    private static final long serialVersionUID = -7777628056420757520L;
    private static final Log LOG = LogFactory.getLog(AdOrderManageAction.class);
    
    
    private int id;
    private AdOrder adOrder;
    private AdCampaign adCampaign;
    private Pagination page;
    private List<AdOrder> adOrderList;
    private List<AdEntry> adEntryList;
    private Map<AdStatusEnum, String> orderStatuses = AdStatusEnum.getSelectorForAdOrderManage();
    private Integer statusCode;
    @Autowired
    private transient AdOrderService adOrderService;
    @Autowired
    private transient AdvertiserAccountService advertiserAccountService;
    @Autowired
    private transient AdCampaignService adCampaignService;
    @Autowired
    private transient AdOrderCategoryService adOrderCategoryService;
    @Autowired
    private transient ChannelService channelService;
    
    private JsonResults results;
    private List<AdOrderCategory> catelist;
    private List<Channel> channelList;
    
    private int upperStatus = 1;
    private int allAdOrdersCount;
    private int activeAdOrdersCount;
    
    //属性
    private Integer campaignId;
    private String name;
    private int bidPrice;
    private int frequencyCap;
    private String rateSelect;
    private int adsType;
    private Date startDate;
    private Date endDate;
    private String keywords;
    private String scheduleDayStr;
    private String scheduleTimeStr;
    //用逗号分隔开的category串
    private String audienceCategories;
    //以逗号分隔的媒体ID
    private String channelIdStr;
    
    

    public String execute() {
        if (adOrder == null) {
            adOrder = new AdOrder();
        }
        if (page == null) {
            page = new Pagination();
        }
        allAdOrdersCount = adOrderService.getAllAdOrdersCount();
        activeAdOrdersCount = adOrderService.getActiveOrdersCount();
        if (upperStatus == 1) {
            adOrderList = adOrderService.listAdOrdersByUpperStatus(adOrder, page);
        } else {
            adOrderList = adOrderService.listAdOrders(adOrder, page);
        }
        return Action.SUCCESS;
    }

    public String view() {
        if (id <= 0) {
            return Action.INPUT;
        }
        adOrder = adOrderService.getOrderById(id);
        frequencyCap = adOrder.getOrderFrequency();
        if (frequencyCap != 0) {
            rateSelect = "1";
        } else {
            frequencyCap = adOrder.getEntryFrequency();
            rateSelect = "2";
        }
        adCampaign = adCampaignService.getAdCampaignById(adOrder.getCampaignId());
        channelList = channelService.listAllChnanelsForAdv();
        catelist = adOrderCategoryService.getAllCategories();
        return Action.SUCCESS;
    }
    
    public String update() {
        results = new JsonResults();
        if (id <= 0 || adOrderService.getOrderById(id) == null) {
            results.fail("不存在id为 " + id + "的order");
            return JsonResults.JSON_RESULT_NAME;
        }
        //检查bidPrice，不能超过1000元
        if (bidPrice < 0 || bidPrice > MAX_BID_PRICE) {
            results.fail("广告出价不能低于0元或者大于" + MAX_BID_PRICE / 100 + "元");
            return JsonResults.JSON_RESULT_NAME;
        }

        // 检测广告组名
        if (StringUtils.isEmpty(name)) {
            results.fail("广告组名不能为空");
            return JsonResults.JSON_RESULT_NAME;
        }
        if (name.length() > 50) {
            results.fail("广告组名不能大于50个字符");
            return JsonResults.JSON_RESULT_NAME;
        }
        Matcher matcher = NAME_PATTERN.matcher(name);
        if (!matcher.matches()) {
            results.fail("广告组名只能包含中英文,数字,下划线，逗号，句号，横线");
            return JsonResults.JSON_RESULT_NAME;
        }
        
        AdCampaign camp = adCampaignService.getAdCampaignById(campaignId);
        if (camp == null) {
            results.fail("广告活动不存在");
            return JsonResults.JSON_RESULT_NAME;
        }

        //检测活动时间
        if (!DateRangeUtil.isValidOrderDate(startDate, endDate, camp.getStartDate(), camp.getEndDate())) {
            results.fail("广告组投放时间非法或者超出活动的限制。你的广告组投放时间需在" + camp.getStartDateStr() + " - " + 
                            camp.getEndDateStr() + "之间.");
            return JsonResults.JSON_RESULT_NAME;
        }
        
        adOrder = adOrderService.getOrderById(id);
        adOrder.setName(name);
        adOrder.setCampaignId(campaignId);
        adOrder.setBidPrice(bidPrice);
        adOrder.setAdsType(AdsTypeEnum.findByValue(adsType));
        int orderFrequency = 0;
        int entryFrequency = 0;
        if (StringUtils.isNotBlank(rateSelect)) {
            if (Integer.valueOf(rateSelect) == 1) {
                orderFrequency = frequencyCap;
            } else {
                entryFrequency = frequencyCap;
            }
        }
        adOrder.setOrderFrequency(orderFrequency);
        adOrder.setEntryFrequency(entryFrequency);
        adOrder.setStartDate(startDate);
        adOrder.setEndDate(endDate);
        adOrder.setKeywords(keywords);
        adOrder.setAudienceCategories(audienceCategories);
        adOrder.setChannelsTarget(channelIdStr);

        adOrder.setScheduleDayStr(scheduleDayStr);
        adOrder.setScheduleTimeStr(scheduleTimeStr);
        
        //检测时间限制
        if (adOrder.getScheduleTime() != null) {
            LocalTime start = LocalTime.parse(adOrder.getScheduleTime().getStartStr());
            LocalTime end = LocalTime.parse(adOrder.getScheduleTime().getEndStr());
            if (start.isAfter(end) || start.isEqual(end)) {
                results.fail("广告组投放计划的时间定向有误！");
                return JsonResults.JSON_RESULT_NAME;
            }
        }
        adOrderService.saveOrUpdate(adOrder);
        return JsonResults.JSON_RESULT_NAME;
    }

    public String updateStatus() {
        results = new JsonResults();
        if (getId() <= 0 || statusCode == null) {
            results.fail("buzzads.adorder.update.validation.require.adorder.message");
            return "json";
        }

        AdOrder order = adOrderService.getOrderById(getId());
        if (order == null) {
            results.fail(getText("buzzads.adorder.update.no.exist.error.message"));
            return "json";
        }

        if (order.getStatus().getCode() == statusCode) {
            results.success();
            return "json";
        }

        try {
            adOrderService.adminManageCampStatus(getId(), AdStatusEnum.findByValue(statusCode));
            results.addContent("order", adOrder);
            results.success();
        } catch (Exception e) {
            LOG.error("adOrderManageAction::updateStatus", e);
            results.fail("system error!");

        }
        return "json";
    }
    
    public List<AdOrder> getAdOrderList() {
        return adOrderList;
    }

    public List<AdEntry> getAdEntryList() {
        return adEntryList;
    }

    public AdOrder getAdOrder() {
        return adOrder;
    }

    public void setAdOrder(AdOrder adOrder) {
        this.adOrder = adOrder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<AdStatusEnum, String> getOrderStatuses() {
        return orderStatuses;
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
        if (adOrder == null || adOrder.getAdvertiserId() <=0) {
           return campaignMap;
        }
        List<AdCampaign> campaignList = adCampaignService.getCampaignsByAdvertiserId(adOrder.getAdvertiserId());
        for (AdCampaign campaign : campaignList) {
            campaignMap.put(campaign.getId(), campaign.getName());
        }
        return campaignMap;
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

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public int getUpperStatus() {
        return upperStatus;
    }

    public void setUpperStatus(int upperStatus) {
        this.upperStatus = upperStatus;
    }

    public int getAllAdOrdersCount() {
        return allAdOrdersCount;
    }

    public void setAllAdOrdersCount(int allAdOrdersCount) {
        this.allAdOrdersCount = allAdOrdersCount;
    }

    public int getActiveAdOrdersCount() {
        return activeAdOrdersCount;
    }

    public void setActiveAdOrdersCount(int activeAdOrdersCount) {
        this.activeAdOrdersCount = activeAdOrdersCount;
    }

    public AdCampaign getAdCampaign() {
        return adCampaign;
    }

    public void setAdCampaign(AdCampaign adCampaign) {
        this.adCampaign = adCampaign;
    }

    public List<AdOrderCategory> getCatelist() {
        return catelist;
    }

    public void setCatelist(List<AdOrderCategory> catelist) {
        this.catelist = catelist;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBidPrice(int bidPrice) {
        this.bidPrice = bidPrice;
    }

    public void setFrequencyCap(int frequencyCap) {
        this.frequencyCap = frequencyCap;
    }
    
    public int getFrequencyCap() {
        return frequencyCap;
    }

    public void setAdsType(int adsType) {
        this.adsType = adsType;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setScheduleDayStr(String scheduleDayStr) {
        this.scheduleDayStr = scheduleDayStr;
    }

    public void setScheduleTimeStr(String scheduleTimeStr) {
        this.scheduleTimeStr = scheduleTimeStr;
    }

    public void setAudienceCategories(String audienceCategories) {
        this.audienceCategories = audienceCategories;
    }

    public String getRateSelect() {
        return rateSelect;
    }

    public void setRateSelect(String rateSelect) {
        this.rateSelect = rateSelect;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<Channel> channelList) {
        this.channelList = channelList;
    }

    public String getChannelIdStr() {
        return channelIdStr;
    }

    public void setChannelIdStr(String channelIdStr) {
        this.channelIdStr = channelIdStr;
    }
}
