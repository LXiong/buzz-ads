package com.buzzinate.buzzads.action.advertiser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.core.service.AdEntryService;
import com.buzzinate.buzzads.core.service.AdOrderService;
import com.buzzinate.buzzads.core.service.AdvertiserAccountService;
import com.buzzinate.buzzads.core.util.UrlUtil;
import com.buzzinate.buzzads.core.util.ValidationResult;
import com.buzzinate.buzzads.domain.AdEntry;
import com.buzzinate.buzzads.domain.AdOrder;
import com.buzzinate.buzzads.enums.AdEntrySizeEnum;
import com.buzzinate.buzzads.enums.AdEntryTypeEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.common.util.ApiUtil;
import com.opensymphony.xwork2.ActionSupport;

import flexjson.JSONSerializer;


/**
 * 
 * @author Johnson
 * 广告主创建广告API
 */
@Controller
@Scope("request")
public class AdvertisingEntryApiAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
    
    private static final long serialVersionUID = -691261836116897351L;
    
    private static final long EXPIRED_TS = 1000 * 60 * 10;
    private static final String DEFAULT = "default";
    private static final Pattern NAME_PATTERN =  Pattern.compile("^[0-9A-Za-z_ \\u0100-\\uFFFF\\.\\,\\-@]+$");

    private int advertiserId;
    private int entryId;
    private int orderId;
    private String name;
    private String link;
    private String imageUrl = DEFAULT;
    private String flashUrl = DEFAULT;
    private int size = -1;
    private String title = DEFAULT;
    private String description = DEFAULT;
    private String displayUrl = DEFAULT;
    private long ts;
    private String sig;
    
    private String resourceUrl = "";
    private AdEntryTypeEnum resourceType = AdEntryTypeEnum.TEXT;
    private AdOrder order;
    private AdEntry entry;
    private ValidationResult validateRs = new ValidationResult();
    
    private HttpServletRequest request;
    private HttpServletResponse response;
    
    @Autowired
    private AdOrderService adOrderService;
    @Autowired
    private AdEntryService adEntryService;
    @Autowired
    private AdvertiserAccountService advertiserAccountService;
    
    public String execute() {
        Map<String, String> jsonMap = new HashMap<String, String>();
        //验证必须参数和签名
        
        
        if (validateRs.getErrcode().equals(ValidationResult.VALID_CODE)) {
            validateParameters();
            if (validateRs.getErrcode().equals(ValidationResult.VALID_CODE)) {
              //设置参数
                resetParameters();
                //验证广告活动和广告组相关参数
                validateRs = validateEntry();
            }
        }
        if (!validateRs.getErrcode().equals(ValidationResult.VALID_CODE)) {
            jsonMap.put("success", "false");
            jsonMap.put(ValidationResult.ERRCODE, validateRs.getErrcode());
            jsonMap.put(ValidationResult.ERRMSG, validateRs.getErrmsg());
        } else {
            try {
                if (entry == null) {
                    entry = new AdEntry();
                }
                entry.setStatus(AdStatusEnum.VERIFYING);
                entry.setAdvertiserId(advertiserId);
                entry.setOrderId(orderId);
                entry.setCampaignId(order.getCampaignId());
                entry.setName(name);
                entry.setSize(AdEntrySizeEnum.findByValue(size));
                entry.setResourceType(resourceType);
                entry.setLink(link);
                entry.setTitle(title);
                entry.setDescription(description);
                entry.setResourceUrl(resourceUrl);
                entry.setDisplayUrl(displayUrl);
                
                adEntryService.saveOrUpdate(entry);
                
                jsonMap.put("success", "true");
                jsonMap.put("entryId", String.valueOf(entry.getId()));
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
     * @return
     */
    private ValidationResult validateParameters() {
        if (advertiserId <= 0 || advertiserAccountService.getAdvertiserAccount(advertiserId) == null) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("invalid advertiderId: " + advertiserId);
            return validateRs;
        }
        if (orderId <= 0 || adOrderService.getOrderById(orderId) == null) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("invalid order: " + orderId);
            return validateRs;
        } else {
            order = adOrderService.getOrderById(orderId);
            if (order.getAdvertiserId() != advertiserId) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("invalid order: " + orderId);
                return validateRs;
            }
        }
        if (entryId > 0) {
            entry = adEntryService.getEntryById(entryId);
            if (order.getAdvertiserId() != advertiserId) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("invalid entry: " + entryId);
                return validateRs;
            }
        }
        
        if (StringUtils.isEmpty(name) || name.length() > 50) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("Entry name required, less than 50 chars");
            return validateRs;
        } else {
            if (!NAME_PATTERN.matcher(name).matches()) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("Entry name can't contain special chars");
                return validateRs;
            }
        }
        if (StringUtils.isEmpty(link) || !UrlUtil.isUrlOptionalHttp(link)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("Entry valid link required");
            return validateRs;
        }
        if (ApiUtil.isSigExpired(ts, EXPIRED_TS)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("ts alreay expired, please retry");
            return validateRs;
        }
        if (StringUtils.isEmpty(sig)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("sig required");
            return validateRs;
        }
        
        HashMap<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("advertiserId", String.valueOf(advertiserId));
        parameterMap.put("orderId", String.valueOf(orderId));
        parameterMap.put("name", name);
        parameterMap.put("link", link);
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
        if (StringUtils.equals(imageUrl, DEFAULT) || StringUtils.isEmpty(imageUrl)) {
            if (StringUtils.equals(flashUrl, DEFAULT)) {
                if (entry != null) {
                    resourceUrl = entry.getResourceUrl();
                    resourceType = entry.getResourceType();
                }
            } else {
                resourceUrl = flashUrl;
                resourceType = AdEntryTypeEnum.Flash;
            }
        } else {
            resourceUrl = imageUrl;
            resourceType = AdEntryTypeEnum.IMAGE;
        }
        if (size == -1) {
            if (entry != null) {
                size = entry.getSize().getCode();
            } else {
                size = AdEntrySizeEnum.SIZE80x80.getCode();
            }
        }
        if (StringUtils.equals(title, DEFAULT)) {
            if (entry != null) {
                title = entry.getTitle();
            } else {
                title = "";
            }
        }
        if (StringUtils.equals(description, DEFAULT)) {
            if (entry != null) {
                description = entry.getDescription();
            } else {
                description = "";
            }
        }
        if (StringUtils.equals(displayUrl, DEFAULT)) {
            if (entry != null) {
                displayUrl = entry.getDisplayUrl();
            } else {
                displayUrl = "";
            }
        }
    }
    
    /**
     * 验证广告相关参数
     * @return
     */
    private ValidationResult validateEntry() {
        if (StringUtils.isNotEmpty(resourceUrl) && !UrlUtil.isUrlOptionalHttp(resourceUrl)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("invalid imgageUrl or flashUrl");
            return validateRs;
        }
        if (StringUtils.isNotEmpty(title) && title.length() > 50) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("title should be less than 50 chars");
            return validateRs;
        }
        if (StringUtils.isNotEmpty(title) && !NAME_PATTERN.matcher(title).matches()) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("title can't contain special chars");
            return validateRs;
        }
        if (StringUtils.isNotEmpty(displayUrl) && !UrlUtil.isUrlOptionalHttp(displayUrl)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("invalid displayUrl");
            return validateRs;
        }
        return validateRs;
    }
    
    
    public void setAdvertiserId(String advertiserId) {
        if (!NumberUtils.isDigits(advertiserId)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("invalid advertiserId");
        } else {
            this.advertiserId = Integer.valueOf(advertiserId);
        }
    }

    public void setEntryId(String entryId) {
        if (StringUtils.isNotEmpty(entryId)) {
            if (!NumberUtils.isDigits(entryId)) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("invalid entryId");
            } else {
                this.entryId = Integer.valueOf(entryId);
            }
        }
    }

    public void setOrderId(String orderId) {
        if (!NumberUtils.isDigits(orderId)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("invalid orderId");
        } else {
            this.orderId = Integer.valueOf(orderId);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setFlashUrl(String flashUrl) {
        this.flashUrl = flashUrl;
    }

    public void setSize(String size) {
        if (StringUtils.isNotEmpty(size)) {
            if (NumberUtils.isDigits(size)) {
                if (Integer.valueOf(size) < 0) {
                    validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                    validateRs.setErrmsg("invalid size, should be a int enum value");
                }
                this.size = Integer.valueOf(size);
            } else {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("invalid size, should be a int enum value");
            }
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
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

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public void setResourceType(AdEntryTypeEnum resourceType) {
        this.resourceType = resourceType;
    }

    public void setEntry(AdEntry entry) {
        this.entry = entry;
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
