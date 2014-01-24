package com.buzzinate.buzzads.action.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.buzzinate.buzzads.core.service.*;
import com.buzzinate.buzzads.domain.*;
import com.buzzinate.buzzads.user.LoginHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.JsonResults;
import com.buzzinate.common.util.ip.ProvinceName;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
/**
 * Admin-活动管理
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2013-4-1
 */
@Controller
@Scope("request")
public class AdminCampManageAction extends ActionSupport {

    private static final long serialVersionUID = -1235590105280773990L;
    
    private static final int MAX_CAMPAIGN_BUDGET = ConfigurationReader.getInt("ads.max.campaign.budget") * 100;
    
    private static final Pattern NAME_PATTERN =  Pattern.compile("^[0-9A-Za-z_ \\u0100-\\uFFFF\\.\\,\\-@]+$");
    
    private static Log log = LogFactory.getLog(AdminCampManageAction.class);
    @Autowired
    private AdCampaignService campService;
    @Autowired
    private AdOrderService adOrderService;
    @Autowired
    private AdvertiserAccountService advertiserAccountService;
    @Autowired
    private AdCampaignBudgetService adCampaignBudgetService;
    @Autowired
    private AdEntryService adEntryService;
    private List<AdCampaign> campaignList;
    private List<AdOrder> adOrderList;
    private List<AdEntry> adEntryList;
    private AdCampaign campaign;
    private Pagination page;
    
    private int id;

    private Map<AdStatusEnum, String> map = AdStatusEnum.getSelectorForCampManage();
    private Map<BidTypeEnum, String> bidTypeSelector = BidTypeEnum.bidTypeSelector();
    private Integer status;

    @Autowired
    private LoginHelper loginHelper;

    private JsonResults results;
    private int allAdCampaignsCount;
    private int activeAdCampaignsCount;
    
    //活动属性
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
    
    /*
     * 所有活动查询列表
     * 
     */
    @Override
    public String execute() {
        if (page == null)
            page = new Pagination();
        if (campaign == null) {
            campaign = new AdCampaign();
        }
        allAdCampaignsCount = campService.getAllAdCampaignsCount();
        activeAdCampaignsCount = campService.getActiveCampaignsCount();
        campaignList = campService.adminListCampaigns(campaign, page);
        return SUCCESS;
    }
    
    public String view() {
        if (id <= 0) {
            return Action.INPUT;
        }
        campaign = campService.getAdCampaignById(id);
        adOrderList = adOrderService.getAdOrdersByCampaignId(id);
        adEntryList = adEntryService.listEntriesByCampaignId(id);
        return Action.SUCCESS;
    }

    public String operate() {
        results = new JsonResults();
        if (!loginHelper.hasRole("ROLE_AD_ADMIN")) {
            results.fail("你没有此权限");
        } else {
            try {
                AdCampaign exist = campService.getAdCampaignById(campaign.getId());
                if (exist == null) {
                    results.fail("广告活动不存在");
                } else {
                    if (!exist.getStatus().equals(AdStatusEnum.findByValue(status.intValue()))) {
                        campService.adminManageCampStatus(campaign.getId(),
                                AdStatusEnum.findByValue(status.intValue()));
                        results.success();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                results.fail("系统异常");
            }
        }
        return JsonResults.JSON_RESULT_NAME;
    }
    
    public String update() {
        results = new JsonResults();
        try {
            campaign = campService.getAdCampaignById(id);
            if (campaign == null) {
                results.fail("广告活动不存在");
                return JsonResults.JSON_RESULT_NAME;
            }
            
            if (!validateCampaign()) {
                return JsonResults.JSON_RESULT_NAME;
            }
            
            campaign.setName(name);
            campaign.setStartDate(start);
            campaign.setEndDate(end);
            campaign.setBidType(BidTypeEnum.findByValue(bidType));
            campaign.setUpdateAt(new Date());

            Set<ProvinceName> province = EnumSet.noneOf(ProvinceName.class);
            if (StringUtils.isNotBlank(location)) {
                String[] pro = location.split(",");
                for (int i = 0; i < pro.length; i++) {
                    ProvinceName pn = ProvinceName.findByValue(ProvinceName.getCode(pro[i]));
                    if (pn != null) {
                        province.add(pn);
                    }
                }
            }
            campaign.setLocations(province);

            Set<AdNetworkEnum> networks = EnumSet.noneOf(AdNetworkEnum.class);
            String[] networkStrs = network.split(",");
            for (int i = 0; i < networkStrs.length; i++) {
                networks.add(AdNetworkEnum.findByValue(Integer.valueOf(networkStrs[i])));
            }
            campaign.setNetwork(networks);
            
            //每日预算
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
            
            AdCampBudget campBudget = adCampaignBudgetService.getCampBudget(id);
            campBudget.setMaxBudgetDay(maxBudgetDay);
            campBudget.setMaxBudgetTotal(maxBudgetTotal);
            campService.updateAdCampaign(campaign, campBudget);
            results.success();
        } catch (Exception e) {
            log.error("Exception.", e);
            results.fail("发生内部错误，请稍候重试！");
        }
        return JsonResults.JSON_RESULT_NAME;
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
    public Pagination getPage() {
        return page;
    }
    public void setPage(Pagination page) {
        this.page = page;
    }
    public List<AdCampaign> getCampaignList() {
        return campaignList;
    }
    public Map<AdStatusEnum, String> getMap() {
        return map;
    }

    public Map<Integer, String> getAdvertiserCompanyMap() {
        Map<Integer, String> companyMap = new HashMap<Integer, String>();
        companyMap.put(0, "全部");
        List<AdvertiserAccount> advertiserAccountList = advertiserAccountService.listAllAdvertisersByAdmin();
        for (AdvertiserAccount account : advertiserAccountList) {
            companyMap.put(account.getAdvertiserId(), account.getCompanyName());
        }
        return companyMap;
    }

    public JsonResults getResults() {
        return results;
    }

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getAllAdCampaignsCount() {
        return allAdCampaignsCount;
    }

    public void setAllAdCampaignsCount(int allAdCampaignsCount) {
        this.allAdCampaignsCount = allAdCampaignsCount;
    }

    public int getActiveAdCampaignsCount() {
        return activeAdCampaignsCount;
    }

    public void setActiveAdCampaignsCount(int activeAdCampaignsCount) {
        this.activeAdCampaignsCount = activeAdCampaignsCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<AdOrder> getAdOrderList() {
        return adOrderList;
    }

    public void setAdOrderList(List<AdOrder> adOrderList) {
        this.adOrderList = adOrderList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public void setBidType(int bidType) {
        this.bidType = bidType;
    }

    public void setMaxBudgetDay(int maxBudgetDay) {
        this.maxBudgetDay = maxBudgetDay;
    }

    public void setMaxBudgetTotal(int maxBudgetTotal) {
        this.maxBudgetTotal = maxBudgetTotal;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDayBudgetStr(String dayBudgetStr) {
        this.dayBudgetStr = dayBudgetStr;
    }

    public Map<BidTypeEnum, String> getBidTypeSelector() {
        return bidTypeSelector;
    }

    public List<AdEntry> getAdEntryList() {
        return adEntryList;
    }

}
