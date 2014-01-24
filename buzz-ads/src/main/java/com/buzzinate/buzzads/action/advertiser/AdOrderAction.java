package com.buzzinate.buzzads.action.advertiser;

import com.buzzinate.buzzads.core.service.*;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.domain.*;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.AdsTypeEnum;
import com.buzzinate.buzzads.enums.WeekDay;
import com.buzzinate.buzzads.user.LoginHelper;
import com.buzzinate.buzzads.util.DateRangeUtil;
import com.buzzinate.common.util.JsonResults;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *         <p/>
 *         2013-3-1
 */
@Controller
@Scope("request")
public class AdOrderAction extends ActionSupport {

    private static final long serialVersionUID = 1264791636125010965L;
    private static final int MAX_BID_PRICE = ConfigurationReader.getInt("ads.max.bid.price") * 100;
    private static final Pattern NAME_PATTERN = Pattern.compile("^[0-9A-Za-z_ \\u0100-\\uFFFF\\.\\,\\-@]+$");
    private static Log log = LogFactory.getLog(AdOrderAction.class);
    private Integer id;
    private Integer campaignId;
    private AdCampaign campaign;
    private List<AdCampaign> campaigns;
    private boolean specifiedOrder;
    private boolean specifiedCampPage;
    //属性
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
    private JsonResults results;
    private AdOrder adOrder;
    private List<AdOrderCategory> catelist;
    private List<Channel> channelList;
    private boolean channelTargetOpen;
    @Autowired
    private transient AdOrderService adOrderService;
    @Autowired
    private transient LoginHelper loginHelper;
    @Autowired
    private transient AdCampaignService campaignService;
    @Autowired
    private transient AdCampaignBudgetService adCampaignBudgetService;
    @Autowired
    private transient AdOrderCategoryService adOrderCategoryService;
    @Autowired
    private transient ChannelService channelService;
    @Autowired
    private transient UserAuthorityService userAuthorityService;

    /*
     * 创建广告组页
     */
    public String add() {
        //查询对应的活动信息
        campaigns = campaignService.getCampaignsByAdvertiserId(loginHelper.getUserId());
        if (campaigns == null || campaigns.isEmpty()) {
            this.addActionMessage("请先创建广告活动！");
            return "noCamp";
        }

        if (campaignId == null || campaignId <= 0) {
            campaign = campaigns.get(0);
            campaignId = campaign.getId();
        } else {
            campaign = campaignService.getAdCampaignById(campaignId);
            if (campaign == null || isInvalidUpdateUser(campaign.getAdvertiserId())) {
                this.addActionError("广告活动不存在，或者您无权在该广告活动下创建广告！");
                // allow them to continue by choosing a default campaign:
                campaign = campaigns.get(0);
                campaignId = campaign.getId();
            }
        }
        if (campaign != null) {
            AdCampBudget budget = adCampaignBudgetService.getCampBudget(campaignId);
            if (budget != null) {
                campaign.setMaxDayBudget(budget.getMaxBudgetDay());
                campaign.setMaxBudgetTotal(budget.getMaxBudgetTotal());
            }
        }
        checkChannelTarget();
        catelist = adOrderCategoryService.getAllCategories();
        channelList = channelService.listAllChnanelsForAdv();
        return SUCCESS;
    }

    private void checkChannelTarget() {
        //检查是否开通特殊功能：媒体定向等
        UserAuthority authority = userAuthorityService.getUserAuthorityByUserId(loginHelper.getUserId());
        if (authority == null) {
            channelTargetOpen = false;
        } else {
            channelTargetOpen = authority.getChannelTarget().intValue() == 1;
        }
    }

    /*
     * 创建广告组
     */
    public String create() {
        results = new JsonResults();
        AdStatusEnum originalStatus = AdStatusEnum.ENABLED;
        try {
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

            //更新操作
            if (id != null) {
                AdOrder o = adOrderService.getOrderById(id);
                if (o == null || isInvalidUpdateUser(o.getAdvertiserId())) {
                    results.fail("广告组不存在，或者您无权修改该广告组！");
                    return JsonResults.JSON_RESULT_NAME;
                }
                originalStatus = o.getStatus();
            }

            AdCampaign camp = campaignService.getAdCampaignById(campaignId);
            if (camp == null || isInvalidUpdateUser(camp.getAdvertiserId())) {
                results.fail("广告活动不存在，或者您无权在该广告活动下创建广告！");
                return JsonResults.JSON_RESULT_NAME;
            }

            //检测活动时间
            if (!DateRangeUtil.isValidOrderDate(startDate, endDate, camp.getStartDate(), camp.getEndDate())) {
                results.fail("广告组投放时间非法或者超出活动的限制。你的广告组投放时间需在" + camp.getStartDateStr() + " - " +
                        camp.getEndDateStr() + "之间.");
                return JsonResults.JSON_RESULT_NAME;
            }
            generateAdOrder(originalStatus);
            //检测时间限制
            if (adOrder.getScheduleTime() != null) {
                LocalTime start = LocalTime.parse(adOrder.getScheduleTime().getStartStr());
                LocalTime end = LocalTime.parse(adOrder.getScheduleTime().getEndStr());
                if (start.isAfter(end) || start.isEqual(end)) {
                    results.fail("广告组投放计划的时间定向有误！");
                    return JsonResults.JSON_RESULT_NAME;
                }
            }

            int orderId = adOrderService.saveOrUpdate(adOrder);
            results.success();
            results.addContent("orderId", orderId);
        } catch (Exception e) {
            log.error("Exception.", e);
            results.fail("发生内部错误，请稍候重试！");
        }
        return JsonResults.JSON_RESULT_NAME;
    }

    /*
     * 查看广告组
     */
    public String view() {
        adOrder = adOrderService.getOrderById(id);

        catelist = adOrderCategoryService.getAllCategories();
        channelList = channelService.listAllChnanelsForAdv();
        checkChannelTarget();
        if (adOrder == null || isInvalidUpdateUser(adOrder.getAdvertiserId())) {
            this.addActionError("广告组不存在，或者您无权查看该广告组！");
            return ERROR;
        } else {
            // 设置选中值
            frequencyCap = adOrder.getOrderFrequency();
            if (frequencyCap != 0) {
                rateSelect = "1";
            } else {
                frequencyCap = adOrder.getEntryFrequency();
                rateSelect = "2";
            }
            campaign = campaignService.getAdCampaignById(adOrder.getCampaignId());
            if (campaign == null) {
                this.addActionError("广告活动不存在，或者您无权查看该广告组！");
                return ERROR;
            } else {
                adOrder.setCampBidType(campaign.getBidType());
                return SUCCESS;
            }
        }
    }

    /**
     * 判断是否非法用户
     *
     * @param adverterId
     * @return
     */
    private boolean isInvalidUpdateUser(int adverterId) {
        if (adverterId != loginHelper.getUserId() && !loginHelper.isLoginAsAdmin()) {
            return true;
        }
        return false;
    }

    public String enable() {
        results = new JsonResults();
        try {
            adOrder = adOrderService.getOrderById(id);
            if (adOrder == null || isInvalidUpdateUser(adOrder.getAdvertiserId())) {
                results.fail("广告组不存在，或者您无权修改该广告组！");
                return JsonResults.JSON_RESULT_NAME;
            }
            if (!adOrder.getStatus().equals(AdStatusEnum.PAUSED)) {
                results.fail("您只能启用暂停的广告组!");
                return JsonResults.JSON_RESULT_NAME;
            }
            adOrderService.updateStatus(id, AdStatusEnum.ENABLED);
            return results.success();
        } catch (Exception e) {
            log.error("Exception.", e);
            return results.fail("发生内部错误，请稍候重试！");
        }
    }

    public String pause() {
        results = new JsonResults();
        try {
            adOrder = adOrderService.getOrderById(id);
            if (adOrder == null || isInvalidUpdateUser(adOrder.getAdvertiserId())) {
                results.fail("广告组不存在，或者您无权修改该广告组！");
                return JsonResults.JSON_RESULT_NAME;
            }
            if (!adOrder.getStatus().equals(AdStatusEnum.ENABLED)) {
                results.fail("您只能启用暂停的广告组!");
                return JsonResults.JSON_RESULT_NAME;
            }
            adOrder.setStatus(AdStatusEnum.PAUSED);
            adOrderService.updateStatus(id, AdStatusEnum.PAUSED);
            return results.success();
        } catch (Exception e) {
            log.error("Exception.", e);
            return results.fail("发生内部错误，请稍候重试！");
        }
    }

    private void generateAdOrder(AdStatusEnum status) {
        if (adOrder == null)
            adOrder = new AdOrder();
        if (id != null) {
            adOrder.setId(id);
        }
        adOrder.setStatus(status);
        adOrder.setName(name);
        adOrder.setCampaignId(campaignId);
        adOrder.setAdvertiserId(loginHelper.getUserId());
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

        if (StringUtils.isNotBlank(scheduleDayStr)) {
            Set<WeekDay> weekDay = EnumSet.noneOf(WeekDay.class);
            String[] scheduleDay = scheduleDayStr.split(",");
            for (int i = 0; i < scheduleDay.length; i++) {
                weekDay.add(WeekDay.findByValue(Integer.valueOf(scheduleDay[i])));
            }
            adOrder.setScheduleDay(weekDay);
        }
        adOrder.setScheduleTimeStr(scheduleTimeStr);
    }

    public JsonResults getResults() {
        return results;
    }

    public void setResults(JsonResults results) {
        this.results = results;
    }

    public AdOrder getAdOrder() {
        return adOrder;
    }

    public void setAdOrder(AdOrder adOrder) {
        this.adOrder = adOrder;
    }

    public AdOrderService getAdOrderService() {
        return adOrderService;
    }

    public void setAdOrderService(AdOrderService adOrderService) {
        this.adOrderService = adOrderService;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public int getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(int bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getScheduleDayStr() {
        return scheduleDayStr;
    }

    public void setScheduleDayStr(String scheduleDayStr) {
        this.scheduleDayStr = scheduleDayStr;
    }

    public String getScheduleTimeStr() {
        return scheduleTimeStr;
    }

    public void setScheduleTimeStr(String scheduleTimeStr) {
        this.scheduleTimeStr = scheduleTimeStr;
    }

    public AdCampaign getCampaign() {
        return campaign;
    }

    public void setCampaign(AdCampaign campaign) {
        this.campaign = campaign;
    }

    public List<AdCampaign> getCampaigns() {
        return campaigns;
    }

    public boolean isSpecifiedOrder() {
        return specifiedOrder;
    }

    public void setSpecifiedOrder(boolean specifiedOrder) {
        this.specifiedOrder = specifiedOrder;
    }

    public boolean isSpecifiedCampPage() {
        return specifiedCampPage;
    }

    public void setSpecifiedCampPage(boolean specifiedCampPage) {
        this.specifiedCampPage = specifiedCampPage;
    }

    public List<AdOrderCategory> getCatelist() {
        return catelist;
    }

    public void setCatelist(List<AdOrderCategory> catelist) {
        this.catelist = catelist;
    }

    public String getAudienceCategories() {
        return audienceCategories;
    }

    public void setAudienceCategories(String audienceCategories) {
        this.audienceCategories = audienceCategories;
    }

    public int getFrequencyCap() {
        return frequencyCap;
    }

    public void setFrequencyCap(String frequencyCap) {
        if (NumberUtils.isDigits(frequencyCap)) {
            this.frequencyCap = Integer.valueOf(frequencyCap);
        }
    }

    public int getAdsType() {
        return adsType;
    }

    public void setAdsType(int adsType) {
        this.adsType = adsType;
    }

    public String getChannelIdStr() {
        return channelIdStr;
    }

    public void setChannelIdStr(String channelIdStr) {
        this.channelIdStr = channelIdStr;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    public String getRateSelect() {
        return rateSelect;
    }

    public void setRateSelect(String rateSelect) {
        this.rateSelect = rateSelect;
    }

    public boolean isChannelTargetOpen() {
        return channelTargetOpen;
    }
}
