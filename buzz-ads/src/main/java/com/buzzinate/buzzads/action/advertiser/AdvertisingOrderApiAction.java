package com.buzzinate.buzzads.action.advertiser;

import com.buzzinate.buzzads.core.service.*;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.core.util.ValidationResult;
import com.buzzinate.buzzads.domain.AdCampBudget;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdOrder;
import com.buzzinate.buzzads.domain.Channel;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.common.util.ApiUtil;
import com.buzzinate.common.util.DateTimeUtil;
import com.opensymphony.xwork2.ActionSupport;
import flexjson.JSONSerializer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Johnson
 *         广告主创建广告API
 */
@Controller
@Scope("request")
public class AdvertisingOrderApiAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private static final long serialVersionUID = -120428565619747939L;
    private static final long EXPIRED_TS = 1000 * 60 * 10;
    private static final String DEFAULT = "default";
    private static final Pattern NAME_PATTERN = Pattern.compile("^[0-9A-Za-z_ \\u0100-\\uFFFF\\.\\,\\-@]+$");
    private static final int MAX_CAMPAIGN_BUDGET = ConfigurationReader.getInt("ads.max.campaign.budget") * 100;
    private static final int MAX_BID_PRICE = ConfigurationReader.getInt("ads.max.bid.price") * 100;
    private int advertiserId;
    private int orderId;
    private int campId;
    private String name;
    //值为CPC,CPM,CPS,CPA,CPD,CPT
    private String bidType = DEFAULT;
    private int bidPrice = -1;
    private long maxBudgetDay = -1;
    private long maxBudgetTotal = -1;
    private int frequencyCap = -1;
    // 频次类型   order或entry  default value :adOrder
    private String capType;
    private String keywords = DEFAULT;
    private String channels = DEFAULT;
    private String startDate;
    private String endDate;
    private long ts;
    private String sig;
    private String channelIds = "";
    private Date dateStart;
    private Date dateEnd;
    //值可以为all(默认),buzzads，lezhi， bshare， wjf
    private String network = DEFAULT;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private BidTypeEnum bidTypeEnum;
    private Set<AdNetworkEnum> networks = EnumSet.noneOf(AdNetworkEnum.class);
    private AdCampaign camp;
    private AdCampBudget campBudget;
    private AdOrder order;
    private ValidationResult validateRs = new ValidationResult();
    @Autowired
    private AdOrderService adOrderService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private AdCampaignService adCampaignService;
    @Autowired
    private AdvertiserAccountService advertiserAccountService;
    @Autowired
    private AdCampaignBudgetService adCampaignBudgetService;
    @Autowired
    private AdvertiserBalanceService advertiserBalanceService;

    public String execute() {
        Map<String, String> jsonMap = new HashMap<String, String>();

        if (validateRs.getErrcode().equals(ValidationResult.VALID_CODE)) {
            //验证必须参数和签名
            validateParameters();
            if (validateRs.getErrcode().equals(ValidationResult.VALID_CODE)) {
                //设置参数
                resetParameters();
                //验证广告活动和广告组相关参数
                validateRs = validateCampaignAndOrder();
            }

        }
        if (!validateRs.getErrcode().equals(ValidationResult.VALID_CODE)) {
            jsonMap.put("success", "false");
            jsonMap.put(ValidationResult.ERRCODE, validateRs.getErrcode());
            jsonMap.put(ValidationResult.ERRMSG, validateRs.getErrmsg());
        } else {
            try {
                if (orderId > 0) {
                    order = adOrderService.getOrderById(orderId);
                    campId = order.getCampaignId();
                    camp = adCampaignService.getAdCampaignById(campId);
                    campBudget = adCampaignBudgetService.getCampBudget(campId);
                    generateAdCampaign();
                    adCampaignService.updateAdCampaign(camp, campBudget);
                    generateAdOrder();
                    adOrderService.saveOrUpdate(order);
                } else {
                    generateAdCampaign();
                    //如果账户余额不足，将状态设置为挂起
                    long lastestBalance = advertiserBalanceService.getLatestBalance(advertiserId);
                    if (lastestBalance <= 0) {
                        camp.setStatus(AdStatusEnum.SUSPENDED);
                    }
                    campId = adCampaignService.createAdCampaign(camp, campBudget);
                    generateAdOrder();
                    orderId = adOrderService.saveOrUpdate(order);
                }
                jsonMap.put("success", "true");
                jsonMap.put("campaignId", String.valueOf(campId));
                jsonMap.put("orderId", String.valueOf(orderId));
            } catch (Exception e) {
                jsonMap.put("success", "false");
                jsonMap.put(ValidationResult.ERRCODE, validateRs.getErrcode());
                jsonMap.put(ValidationResult.ERRMSG, "unknown excpetion");
            }
        }
        String resultString = new JSONSerializer().deepSerialize(jsonMap);
        response.setContentType("text/javascript;charset=utf-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.println(resultString);
        } catch (IOException e) {
            LOG.debug("AdvertisingOrderApiAction::api/ads/order/update", e);
        }
        return null;
    }

    /**
     * 检查必须参数 和签名
     *
     * @return
     */
    private ValidationResult validateParameters() {
        if (advertiserId <= 0 || advertiserAccountService.getAdvertiserAccount(advertiserId) == null) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("invalid advertiderId: " + advertiserId);
            return validateRs;
        }
        if (orderId > 0) {
            order = adOrderService.getOrderById(orderId);
            if (order == null || order.getAdvertiserId() != advertiserId) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("invalid orderId :" + orderId);
                return validateRs;
            }
            camp = adCampaignService.getAdCampaignById(order.getCampaignId());
        } else {
            if (StringUtils.equals(bidType, DEFAULT)) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("bidType required when create");
                return validateRs;
            }
        }
        if (StringUtils.isEmpty(name) || name.length() > 50) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("Campaign and Order name required, less than 50 chars");
            return validateRs;
        } else {
            Matcher matcher = NAME_PATTERN.matcher(name);
            if (!matcher.matches()) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("Campaign and Order name can't contain special chars");
                return validateRs;
            }
        }

        if (ApiUtil.isSigExpired(ts, EXPIRED_TS)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("ts alreay expired, please retry");
            return validateRs;
        }

        if (StringUtils.isNotEmpty(capType)) {
            if (!(capType.equalsIgnoreCase("order") || capType.equalsIgnoreCase("entry"))) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("invalid capType :" + capType);
                return validateRs;
            }
        } else {
            // 默认值为广告组频次
            capType = "order";
        }

        if (StringUtils.isEmpty(sig)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("sig required");
            return validateRs;
        }

        HashMap<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("advertiserId", String.valueOf(advertiserId));
        parameterMap.put("name", name);
        parameterMap.put("ts", String.valueOf(ts));

        String secret = advertiserAccountService.getAdvertiserAccount(advertiserId).getSecret();

        if (!ApiUtil.validateSignature(parameterMap, secret, sig)) {
            validateRs.setErrcode(ValidationResult.INVALID_SIG_CODE);
            validateRs.setErrmsg("invalid sig: " + sig + ", expected sig: " +
                    ApiUtil.encryptForApi(parameterMap, secret));
            return validateRs;
        }
        return validateRs;
    }

    /**
     * 如果是update且参数没有传递，设置为原来的值
     * 如果是create且参数没有传递 则设置为默认
     */
    private void resetParameters() {
        if (StringUtils.equals(bidType, DEFAULT)) {
            if (camp != null) {
                bidTypeEnum = camp.getBidType();
            } else {
                bidTypeEnum = BidTypeEnum.CPC;
            }
        } else {
            bidTypeEnum = BidTypeEnum.valueOf(bidType);
        }

        if (StringUtils.equals(network, DEFAULT)) {
            if (camp != null) {
                networks = camp.getNetwork();
            } else {
                for (AdNetworkEnum e : AdNetworkEnum.values()) {
                    networks.add(e);
                }
            }
        }
        if (bidPrice == -1) {
            if (order != null) {
                bidPrice = order.getBidPrice();
            } else {
                bidPrice = (bidTypeEnum == BidTypeEnum.CPT || bidTypeEnum == BidTypeEnum.CPD) ? 1 : 0;
            }
        }
        if (maxBudgetDay == -1) {
            if (camp != null) {
                maxBudgetDay = camp.getMaxDayBudget();
            } else {
                maxBudgetDay = 0;
            }
        }
        if (maxBudgetTotal == -1) {
            if (camp != null) {
                maxBudgetTotal = camp.getMaxBudgetTotal();
            } else {
                maxBudgetTotal = 0;
            }

        }
        if (frequencyCap == -1) {
            if (order != null) {
                // 显示数据库中的频次
                frequencyCap = order.getOrderFrequency();
                if (frequencyCap == 0) {
                    frequencyCap = order.getEntryFrequency();
                }
            } else {
                frequencyCap = 0;
            }
        }
        if (StringUtils.equals(keywords, DEFAULT)) {
            if (order != null) {
                keywords = order.getKeywords();
            } else {
                keywords = "";
            }
        }
        if (StringUtils.equals(channels, DEFAULT)) {
            if (order != null) {
                channelIds = order.getChannelsTarget();
            }
        }
        if (StringUtils.isEmpty(startDate)) {
            if (camp != null) {
                dateStart = camp.getStartDate();
            } else {
                dateStart = DateTimeUtil.getCurrentDateDay();
            }
        } else {
            try {
                dateStart = DateTimeUtil.convertDate(startDate);
            } catch (Exception e) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("invalid startDate");
            }

        }
        if (StringUtils.isEmpty(endDate)) {
            if (camp != null) {
                dateEnd = camp.getEndDate();
            }
        } else {
            try {
                dateEnd = DateTimeUtil.convertDate(endDate);
            } catch (Exception e) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("invalid endDate");
            }
        }
    }

    /**
     * 验证广告活动和广告组相关参数
     *
     * @return
     */
    private ValidationResult validateCampaignAndOrder() {
        Date current = DateTimeUtil.getCurrentDateDay();
        if (dateEnd != null && (dateEnd.before(current) || dateEnd.before(dateStart))) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("invalid endDate or startDate");
            return validateRs;
        }

        //校验每日预算和总预算
        if (maxBudgetDay < 0 || maxBudgetTotal < 0 || maxBudgetDay > MAX_CAMPAIGN_BUDGET ||
                maxBudgetTotal > MAX_CAMPAIGN_BUDGET) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("invalid maxBudgetDay or maxBudgetTotal");
            return validateRs;
        } else if (maxBudgetDay != 0 && maxBudgetTotal != 0 && maxBudgetDay > maxBudgetTotal) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("invalid maxBudgetDay or maxBudgetTotal, " +
                    "maxBudgetDay can't be larger than maxBudgetTotal");
            return validateRs;
        }

        if (bidTypeEnum.equals(BidTypeEnum.CPC) || bidTypeEnum.equals(BidTypeEnum.CPM)) {
            if (bidPrice <= 0) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("bidPrice is required when bidType is CPC or CPM");
            }
            return validateRs;
        }
        if (bidPrice > MAX_BID_PRICE) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("invalid bidPrice");
            return validateRs;
        }

        return validateRs;
    }

    private void generateAdCampaign() {
        if (camp == null) {
            camp = new AdCampaign();
            campBudget = new AdCampBudget();
        }
        camp.setAdvertiserId(advertiserId);
        camp.setName(name);
        camp.setNetwork(networks);
        camp.setBidType(bidTypeEnum);
        camp.setMaxDayBudget(maxBudgetDay);
        camp.setMaxBudgetTotal(maxBudgetTotal);
        camp.setStartDate(dateStart);
        camp.setEndDate(dateEnd);

        campBudget.setAdvertiserId(advertiserId);
        campBudget.setMaxBudgetDay(maxBudgetDay);
        campBudget.setMaxBudgetTotal(maxBudgetTotal);
        campBudget.setDateDay(new Date());
    }

    private void generateAdOrder() {
        if (order == null) {
            order = new AdOrder();
        }
        order.setAdvertiserId(advertiserId);
        order.setCampaignId(campId);
        order.setBidPrice(bidPrice);
        order.setName(name);
        order.setStartDate(dateStart);
        order.setEndDate(dateEnd);
        order.setKeywords(keywords);
        order.setChannelsTarget(channelIds);
        if (capType.equalsIgnoreCase("order")) {
            order.setOrderFrequency(frequencyCap);
        } else {
            order.setEntryFrequency(frequencyCap);
        }
    }

    public void setAdvertiserId(String advertiserId) {
        if (!NumberUtils.isDigits(advertiserId)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("invalid advertiserId");
        } else {
            this.advertiserId = Integer.valueOf(advertiserId);
        }
    }

    public void setOrderId(String orderId) {
        if (!StringUtils.isEmpty(orderId)) {
            if (!NumberUtils.isDigits(orderId)) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("invalid orderId");
            } else {
                this.orderId = Integer.valueOf(orderId);
            }
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNetwork(String network) {
        this.network = StringUtils.trim(network);
        if (StringUtils.isNotEmpty(network)) {
            String[] networkStrs = network.split(",");
            for (int i = 0; i < networkStrs.length; i++) {
                if (networkStrs[i].equals("buzzads")) {
                    networks.add(AdNetworkEnum.BUZZADS);
                } else if (networkStrs[i].equals("lezhi")) {
                    networks.add(AdNetworkEnum.LEZHI);
                } else if (networkStrs[i].equals("bshare")) {
                    networks.add(AdNetworkEnum.BSHARE);
                } else if (networkStrs[i].equals("wjf")) {
                    networks.add(AdNetworkEnum.WJF);
                } else if (networkStrs[i].equals("all")) {
                    for (AdNetworkEnum e : AdNetworkEnum.values()) {
                        networks.add(e);
                    }
                } else {
                    validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                    validateRs.setErrmsg("invalid network, values should in (all, buzzads, lezhi, bshare, wjf)");
                }
            }
            // network为空的话则为全部类型
        } else {
            this.network = DEFAULT;
        }
    }

    public void setBidType(String bidType) {
        if (StringUtils.isNotEmpty(bidType)) {
            if (!bidType.equals("CPC") && !bidType.equals("CPM") && !bidType.equals("CPS") &&
                    !bidType.equals("CPA") && !bidType.equals("CPT") && !bidType.equals("CPD")) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("invalid bidType, should be a value in (CPC, CPM, CPS, CPA, CPT, CPD)");
            }
            // 创建的时候提示错误
        } else {
            bidType = DEFAULT;
        }
        this.bidType = bidType;
    }

    public void setBidPrice(String bidPrice) {
        if (StringUtils.isNotEmpty(bidPrice)) {
            if (NumberUtils.isDigits(bidPrice)) {
                if (Double.valueOf(bidPrice) <= 0) {
                    validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                    validateRs.setErrmsg("invalid bidPrice, should larger than 0");
                }
                this.bidPrice = Integer.valueOf(bidPrice);
            } else {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("invalid bidPrice, should be a number");
            }
        }
    }

    public void setMaxBudgetDay(String maxBudgetDay) {
        if (StringUtils.isNotEmpty(maxBudgetDay)) {
            if (NumberUtils.isDigits(maxBudgetDay)) {
                if (Long.valueOf(maxBudgetDay) <= 0) {
                    validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                    validateRs.setErrmsg("invalid maxBudgetDay, should larger than 0");
                }
                this.maxBudgetDay = Long.valueOf(maxBudgetDay);
            } else {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("invalid maxBudgetDay, should be a number");
            }
        }
    }

    public void setMaxBudgetTotal(String maxBudgetTotal) {
        if (StringUtils.isNotEmpty(maxBudgetTotal)) {
            if (NumberUtils.isDigits(maxBudgetTotal)) {
                if (Long.valueOf(maxBudgetTotal) <= 0) {
                    validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                    validateRs.setErrmsg("invalid maxBudgetTotal, should larger than 0");
                }
                this.maxBudgetTotal = Long.valueOf(maxBudgetTotal);
            } else {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("invalid maxBudgetTotal, should be a number");
            }
        }
    }

    public void setFrequencyCap(String frequencyCap) {
        if (StringUtils.isNotEmpty(frequencyCap)) {
            if (NumberUtils.isDigits(frequencyCap)) {
                if (Integer.valueOf(frequencyCap) < 0) {
                    validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                    validateRs.setErrmsg("invalid frequencyCap, should larger than 0");
                }
                this.frequencyCap = Integer.valueOf(frequencyCap);
            } else {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("invalid frequencyCap, should be a number");
            }
        }

    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setChannels(String channels) {
        this.channels = channels;
        if (StringUtils.isNotEmpty(channels)) {
            String[] channelList = channels.split(",");
            for (String channelStr : channelList) {
                try {
                    Channel channel = channelService.getChannelByDomain(channelStr);
                    if (channel != null) {
                        channelIds += channel.getId() + ",";
                    }
                } catch (Exception e) {
                    validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                    validateRs.setErrmsg("invalid channel url, shoule be utf-8 encoded");
                    break;
                }

            }
            channelIds = StringUtils.removeEnd(channelIds, ",");
        }
    }

    public void setStartDate(String startDate) {
        if (StringUtils.isNotEmpty(startDate) && !DateTimeUtil.isNormalFormat(startDate)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("invalid date format, should be format like  2012-01-02");
        }
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        if (StringUtils.isNotEmpty(endDate) && !DateTimeUtil.isNormalFormat(endDate)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("invalid date format, should be format like  2012-01-02");
        }
        this.endDate = endDate;
    }

    public void setTs(String ts) {
        if (!NumberUtils.isDigits(ts)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("invalid ts, should be a long timestamp");
        } else {
            this.ts = Long.valueOf(ts);
        }
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public void setCapType(String capType) {
        this.capType = capType;
    }

    public void setAdOrderService(AdOrderService adOrderService) {
        this.adOrderService = adOrderService;
    }

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

}
