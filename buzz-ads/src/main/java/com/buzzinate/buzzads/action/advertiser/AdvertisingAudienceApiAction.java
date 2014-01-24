package com.buzzinate.buzzads.action.advertiser;

import com.buzzinate.buzzads.core.service.AdvertiserAccountService;
import com.buzzinate.buzzads.core.util.UrlUtil;
import com.buzzinate.buzzads.core.util.ValidationResult;
import com.buzzinate.buzzads.enums.PartnerEnum;
import com.buzzinate.buzzads.service.AudienceApiServices;
import com.buzzinate.common.util.ApiUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-5-28
 * Time: 上午11:02
 * UQ Audience Api
 */
@Controller
@Scope("request")
public class AdvertisingAudienceApiAction implements ServletResponseAware {

    private static final String LOG_TAG = "Audience API";
    private static final long EXPIRED_TS = 1000 * 60 * 10;
    private static Log LOG = LogFactory.getLog(AdvertisingAudienceApiAction.class);
    private String filePath;
    private int partnerCode;
    private int advertiserId;
    private long ts;
    private String sig;
    private transient HttpServletResponse response;
    @Autowired
    private transient AudienceApiServices audienceApiServices;
    @Autowired
    private transient AdvertiserAccountService advertiserAccountService;
    private ValidationResult validateRs = new ValidationResult();

    public String execute() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(LOG_TAG + "::receive notify[l=" + filePath + "]");
        }

        validateParameters();
        PartnerEnum partnerEnum = PartnerEnum.getPartnerEnumByCode(partnerCode);
        if (partnerEnum != PartnerEnum.UNKNOWN && validateRs.getErrcode().equals(ValidationResult.VALID_CODE)) {
            audienceApiServices.receiveNotify(partnerEnum, filePath);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            if (LOG.isDebugEnabled()) {
                LOG.debug(LOG_TAG + "::error info=[" + validateRs.getErrmsg() + "," + validateRs.getErrcode() + "]");
                try {
                    response.getWriter().write(validateRs.getErrcode() + "," + validateRs.getErrmsg());
                } catch (Exception e) {

                }
            }

        }
        return null;
    }

    public void setL(String l) {
        filePath = l;
    }

    public void setPartnerCode(int partnerCode) {
        this.partnerCode = partnerCode;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public int getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(int advertiserId) {
        this.advertiserId = advertiserId;
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
        if (StringUtils.isEmpty(filePath) || !UrlUtil.isUrlOptionalHttp(filePath)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("valid link required");
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
        parameterMap.put("l", filePath);
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
}
