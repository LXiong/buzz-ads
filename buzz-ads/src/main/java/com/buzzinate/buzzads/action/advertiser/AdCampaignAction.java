package com.buzzinate.buzzads.action.advertiser;

import com.buzzinate.buzzads.core.service.AdCampaignBudgetService;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdOrderService;
import com.buzzinate.buzzads.core.service.AdvertiserBalanceService;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.AdCampBudget;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.CampaignDayBudget;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.buzzads.user.LoginHelper;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.JsonResults;
import com.buzzinate.common.util.ip.ProvinceName;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *         <p/>
 *         2013-2-28
 */
@Controller
@Scope("request")
public class AdCampaignAction extends ActionSupport {

    private static final long serialVersionUID = -2064378905110719232L;
    private static final int MAX_CAMPAIGN_BUDGET = ConfigurationReader.getInt("ads.max.campaign.budget") * 100;
    private static final Pattern NAME_PATTERN = Pattern.compile("^[0-9A-Za-z_ \\u0100-\\uFFFF\\.\\,\\-@]+$");
    private static Log log = LogFactory.getLog(AdOrderAction.class);
    @Autowired
    private AdCampaignService campaignService;
    @Autowired
    private AdCampaignBudgetService adCampaignBudgetService;
    @Autowired
    private AdvertiserBalanceService advertiserBalanceService;
    @Autowired
    private LoginHelper loginHelper;
    @Autowired
    private AdOrderService adOrderService;
    private AdCampaign campaign;
    private AdCampBudget campBudget;
    private JsonResults results;
    private Integer id;
    // 创建活动属性
    private String name;
    private String network;
    private int bidType;
    private int maxBudgetDay;
    private int maxBudgetTotal;
    private Date start;
    private Date end;
    private String location;
    //每日预算设置，格式:20120612-23_
    private String dayBudgetStr;
    private boolean specifiedCampPage;
    private Pagination page;
    private List<AdCampaign> campaigns;

    public String create() {
        results = new JsonResults();

        try {
            if (!validateCampaign()) {
                return JsonResults.JSON_RESULT_NAME;
            }

            // 校验数据完整性
            generateAdCampaign(AdStatusEnum.ENABLED);
            //如果账户余额不足，将状态设置为挂起
            long lastestBalance = advertiserBalanceService.getLatestBalance(loginHelper.getUserId());
            if (lastestBalance <= 0) {
                campaign.setStatus(AdStatusEnum.SUSPENDED);
            }
            int campId = campaignService.createAdCampaign(campaign, campBudget);
            results.success();
            results.addContent("campId", campId);
        } catch (Exception e) {
            log.error("Exception.", e);
            results.fail("发生内部错误，请稍候重试！");
        }
        return JsonResults.JSON_RESULT_NAME;
    }

    public String delete() {
        return SUCCESS;
    }

    public String update() {
        results = new JsonResults();
        try {
            AdCampaign orgCampaign = campaignService.getAdCampaignById(id);
            if (orgCampaign == null || loginHelper.getUserId() != orgCampaign.getAdvertiserId()) {
                results.fail("广告活动不存在，或者您无权修改该广告活动！");
                return JsonResults.JSON_RESULT_NAME;
            }

            if (!validateCampaign()) {
                return JsonResults.JSON_RESULT_NAME;
            }

            generateAdCampaign(orgCampaign.getStatus());

            campaign.setId(id);
            campBudget = adCampaignBudgetService.getCampBudget(id);
            campBudget.setMaxBudgetDay(maxBudgetDay);
            campBudget.setMaxBudgetTotal(maxBudgetTotal);
            campaignService.updateAdCampaign(campaign, campBudget);
            campaignService.checkAndResetSuspendedCamp(loginHelper.getUserId());
            results.success();
        } catch (Exception e) {
            log.error("Exception.", e);
            results.fail("发生内部错误，请稍候重试！");
        }
        return JsonResults.JSON_RESULT_NAME;
    }

    /**
     * 左侧菜单
     */
    public String leftMenu() {
        results = new JsonResults();
        campaigns = campaignService.getCampaignsByAdvertiserId(loginHelper.getUserId());
        for (AdCampaign camp : campaigns) {
            camp.setAdOrders(adOrderService.getAdOrdersByCampaignId(camp.getId()));
        }
        results.success();
        results.addContent("campaigns", campaigns);
        return JsonResults.JSON_RESULT_NAME;
    }

    /*
     * 设置列表
     */
    public String list() {
        if (page == null) {
            page = new Pagination();
        }
        campaigns = campaignService.listCampaigns(loginHelper.getUserId(), page);
        return SUCCESS;
    }

    public String add() {
        return SUCCESS;
    }

    public String view() {
        campaign = campaignService.getAdCampaignById(id);
        if (campaign == null || loginHelper.getUserId() != campaign.getAdvertiserId()) {
            this.addActionError("广告活动不存在，或者您无权查看该广告活动！");
            return ERROR;
        } else {
            return SUCCESS;
        }
    }

    public String getCampaignJson() {
        results = new JsonResults();
        try {
            campaign = campaignService.getAdCampaignById(id);
            if (campaign == null || campaign.getAdvertiserId() != loginHelper.getUserId()) {
                results.fail("广告活动不存在，或者您无权修改该广告活动！");
            } else {
                results.success();
                results.addContent("campaign", campaign);
            }
        } catch (Exception e) {
            log.error("Exception.", e);
            results.fail("发生内部错误，请稍候重试！");
        }
        return JsonResults.JSON_RESULT_NAME;
    }

    public String enable() {
        results = new JsonResults();
        try {
            boolean isAdmin = false;
            if (loginHelper.hasRole("ROLE_AD_ADMIN")) {
                isAdmin = true;
            }
            campaign = campaignService.getAdCampaignById(id);
            if (campaign == null || (!isAdmin && loginHelper.getUserId() != campaign.getAdvertiserId())) {
                results.fail("广告活动不存在，或者您无权修改该广告活动！");
                return JsonResults.JSON_RESULT_NAME;
            }
            if (!campaign.getStatus().equals(AdStatusEnum.PAUSED)) {
                results.fail("您只能启用暂停的活动！");
                return JsonResults.JSON_RESULT_NAME;
            }
            campaignService.updateAdCampaignStatus(id, AdStatusEnum.ENABLED);
            return results.success();
        } catch (Exception e) {
            log.error("Exception.", e);
            return results.fail("发生内部错误，请稍候重试！");
        }
    }

    public String pause() {
        results = new JsonResults();
        try {
            boolean isAdmin = false;
            if (loginHelper.hasRole("ROLE_AD_ADMIN")) {
                isAdmin = true;
            }
            campaign = campaignService.getAdCampaignById(id);
            if (campaign == null || (!isAdmin && loginHelper.getUserId() != campaign.getAdvertiserId())) {
                results.fail("广告活动不存在，或者您无权修改该广告活动！");
                return JsonResults.JSON_RESULT_NAME;
            }
            if (!campaign.getStatus().equals(AdStatusEnum.ENABLED)) {
                results.fail("您只能暂停已启用的活动！");
                return JsonResults.JSON_RESULT_NAME;
            }
            campaignService.updateAdCampaignStatus(id, AdStatusEnum.PAUSED);
        } catch (Exception e) {
            log.error("Exception.", e);
            return results.fail(e.getMessage());
        }

        return results.success();
    }

    private void generateAdCampaign(AdStatusEnum status) {
        if (campaign == null) {
            campaign = new AdCampaign();
            campaign.setStatus(status);
            campaign.setAdvertiserId(loginHelper.getUserId());
            campaign.setName(name);
            campaign.setStartDate(start);
            campaign.setEndDate(end);
            campaign.setBidType(BidTypeEnum.findByValue(bidType));
            campaign.setUpdateAt(new Date());

            if (StringUtils.isNotBlank(location)) {
                Set<ProvinceName> province = EnumSet.noneOf(ProvinceName.class);
                String[] pro = location.split(",");
                for (int i = 0; i < pro.length; i++) {
                    ProvinceName pn = ProvinceName.findByValue(ProvinceName.getCode(pro[i]));
                    if (pn != null) {
                        province.add(pn);
                    }
                }
                campaign.setLocations(province);
            }

            Set<AdNetworkEnum> networks = EnumSet.noneOf(AdNetworkEnum.class);
            String[] networkStrs = network.split(",");
            for (int i = 0; i < networkStrs.length; i++) {
                networks.add(AdNetworkEnum.findByValue(Integer.valueOf(networkStrs[i])));
            }
            campaign.setNetwork(networks);

            //增加每日预算
            if (StringUtils.isNotBlank(dayBudgetStr)) {
                List<CampaignDayBudget> dayBudgets = new ArrayList<CampaignDayBudget>();
                String[] str = dayBudgetStr.split("_");
                if (str.length > 0) {
                    for (String d : str) {
                        String[] dbg = d.split(",");
                        CampaignDayBudget cdb = new CampaignDayBudget(DateTimeUtil.getDateByString(dbg[0],
                                DateTimeUtil.FMT_DATE_YYYY_MM_DD), Integer.valueOf(dbg[1]));
                        dayBudgets.add(cdb);
                    }
                }
                campaign.setDayBudgets(dayBudgets);
            }
        }
        if (campBudget == null) {
            campBudget = new AdCampBudget();
            campBudget.setAdvertiserId(loginHelper.getUserId());
            campBudget.setMaxBudgetDay(maxBudgetDay);
            campBudget.setMaxBudgetTotal(maxBudgetTotal);
            campBudget.setDateDay(new Date());
        }
    }

    private boolean validateCampaign() {
        //校验开始、结束时间
        Date current = DateTimeUtil.getCurrentDateDay();
        if (end != null && (end.before(current) || end.before(start))) {
            results.fail("活动时间无效");
            return false;
        }

        //校验活动名是否符合规则
        if (StringUtils.isEmpty(name)) {
            results.fail("活动名不能为空");
            return false;
        }
        if (name.length() > 50) {
            results.fail("活动名不能大于50个字符");
            return false;
        }
        Matcher matcher = NAME_PATTERN.matcher(name);
        if (!matcher.matches()) {
            results.fail("活动名只能包含中英文,数字,下划线，逗号，句号，横线");
            return false;
        }

        //校验每日预算和总预算
        if (maxBudgetDay < 0 || maxBudgetTotal < 0) {
            results.fail("预算错误:每日预算、总预算不能小于零！");
            return false;
        } else if (maxBudgetDay != 0 && maxBudgetTotal != 0 && maxBudgetDay > maxBudgetTotal) {
            results.fail("预算错误:每日预算不能大于总预算！");
            return false;
        } else if (maxBudgetDay > MAX_CAMPAIGN_BUDGET || maxBudgetTotal > MAX_CAMPAIGN_BUDGET) {
            results.fail("预算错误:每日预算、总预算不能超过1000万");
            return false;
        }

        if (StringUtils.isEmpty(network)) {
            results.fail("未选择投放网络");
            return false;
        }

        if (bidType < 0 || bidType > 3) {
            results.fail("未选择投放类型");
            return false;
        }

        return true;
    }

    public AdCampaign getCampaign() {
        return campaign;
    }

    public void setCampaign(AdCampaign campaign) {
        this.campaign = campaign;
    }

    public AdCampaignService getCampaignService() {
        return campaignService;
    }

    public void setCampaignService(AdCampaignService campaignService) {
        this.campaignService = campaignService;
    }

    public LoginHelper getLoginHelper() {
        return loginHelper;
    }

    public void setLoginHelper(LoginHelper loginHelper) {
        this.loginHelper = loginHelper;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public int getBidType() {
        return bidType;
    }

    public void setBidType(int bidType) {
        this.bidType = bidType;
    }

    public int getMaxBudgetDay() {
        return maxBudgetDay;
    }

    public void setMaxBudgetDay(int maxBudgetDay) {
        this.maxBudgetDay = maxBudgetDay;
    }

    public int getMaxBudgetTotal() {
        return maxBudgetTotal;
    }

    public void setMaxBudgetTotal(int maxBudgetTotal) {
        this.maxBudgetTotal = maxBudgetTotal;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public AdCampBudget getCampBudget() {
        return campBudget;
    }

    public void setCampBudget(AdCampBudget campBudget) {
        this.campBudget = campBudget;
    }

    public JsonResults getResults() {
        return results;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }

    public List<AdCampaign> getCampaigns() {
        return campaigns;
    }

    public boolean isSpecifiedCampPage() {
        return specifiedCampPage;
    }

    public void setSpecifiedCampPage(boolean specifiedCampPage) {
        this.specifiedCampPage = specifiedCampPage;
    }

    public String getDayBudgetStr() {
        return dayBudgetStr;
    }

    public void setDayBudgetStr(String dayBudgetStr) {
        this.dayBudgetStr = dayBudgetStr;
    }

}