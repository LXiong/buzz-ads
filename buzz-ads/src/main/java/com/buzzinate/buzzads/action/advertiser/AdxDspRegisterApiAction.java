package com.buzzinate.buzzads.action.advertiser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.core.service.AdvertiserAccountService;
import com.buzzinate.buzzads.core.service.AdxDspRtbInfoService;
import com.buzzinate.buzzads.core.util.ValidationResult;
import com.buzzinate.buzzads.domain.AdxDspRtbInfo;
import com.buzzinate.common.util.ApiUtil;
import com.opensymphony.xwork2.ActionSupport;

import flexjson.JSONSerializer;

/**
 * @author Johnson
 *         广告主创建广告API
 */
@Controller
@Scope("request")
public class AdxDspRegisterApiAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private static final long serialVersionUID = -120428565619747939L;
    private static final long EXPIRED_TS = 1000 * 60 * 10;
    
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ValidationResult validateRs = new ValidationResult();
    @Autowired
    private AdvertiserAccountService advertiserAccountService;
    @Autowired
    private AdxDspRtbInfoService adxDspRtbInfoService;
    
    private String key;
    private String z;
    private long ts;
    private String winnerNotifyUrl;
    private String bidRequestUrl;
    private String sig;
    // 是否为注册逻辑
    private boolean register;
    
    public String execute() {
        Map<String, String> jsonMap = new HashMap<String, String>();

        // 验证必须参数和签名
        validateParameters();
        if (!validateRs.getErrcode().equals(ValidationResult.VALID_CODE)) {
            jsonMap.put("success", "false");
            jsonMap.put(ValidationResult.ERRCODE, validateRs.getErrcode());
            jsonMap.put(ValidationResult.ERRMSG, validateRs.getErrmsg());
        } else {
            try {
                int status = 0;
                int key = Integer.valueOf(this.key);
                if (register) {
                    AdxDspRtbInfo rtbInfo = new AdxDspRtbInfo(key, winnerNotifyUrl, bidRequestUrl);
                    adxDspRtbInfoService.saveOrUpdate(rtbInfo);
                } else {
                    status = adxDspRtbInfoService.deleteRtbInfo(key);
                }
                jsonMap.put("success", "true");
                jsonMap.put(ValidationResult.ERRCODE, "0");
                jsonMap.put(ValidationResult.ERRMSG, validateRs.getErrmsg());
                if (status == -1) {
                    jsonMap.put(ValidationResult.ERRMSG, "dsp already had been unregisted.");
                }
            } catch (Exception e) {
                jsonMap.put("success", "false");
                jsonMap.put(ValidationResult.ERRCODE, validateRs.getErrcode());
                jsonMap.put(ValidationResult.ERRMSG, "server exception");
            }
        }
        String resultString = new JSONSerializer().deepSerialize(jsonMap);
        response.setContentType("application/json");
        try {
            PrintWriter writer = response.getWriter();
            writer.println(resultString);
            writer.flush();
        } catch (IOException e) {
            LOG.debug("DspRegisterApiAction::/api/notification/" + (register ? "register":"unregister"), e);
        } finally {
//            if (writer != null) {
//                writer.close();
//            }
        }
        return null;
    }

    /**
     * 检查必须参数 和签名
     *
     * @return
     */
    private ValidationResult validateParameters() {

        if (StringUtils.isEmpty(key)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("缺少必须的参数：key");
            return validateRs;
        } else {
            if (!StringUtils.isNumeric(key)) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("非法的key值");
                return validateRs;
            }
        }
        
        if (StringUtils.isEmpty(z)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("缺少必须的参数：z");
            return validateRs;
        } else {
            if (z.trim().length() < 16) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("随机数z长度少于16位");
                return validateRs;
            }
        }
        
        if (StringUtils.isEmpty(sig)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("缺少必须的参数：sig");
            return validateRs;
        }
        
        // 注册逻辑才需要校验以下两个参数
        HashMap<String, String> parameterMap = new HashMap<String, String>();
        if (register) {
            if (StringUtils.isEmpty(winnerNotifyUrl)) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("缺少必须的参数：winnerNotifyUrl");
                return validateRs;
            }
            parameterMap.put("winnerNotifyUrl", winnerNotifyUrl);
            
            if (StringUtils.isEmpty(bidRequestUrl)) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("缺少必须的参数：bidRequestUrl");
                return validateRs;
            }
            parameterMap.put("bidRequestUrl", bidRequestUrl);
        }
        
        if (ApiUtil.isSigExpired(ts, EXPIRED_TS)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("时间戳非法或过期");
            return validateRs;
        }
        parameterMap.put("z", String.valueOf(z));
        parameterMap.put("ts", String.valueOf(ts));
        String secret = advertiserAccountService.getAdvertiserAccount(Integer.valueOf(key)).getSecret();

        if (!ApiUtil.validateSignature(parameterMap, secret, sig)) {
            validateRs.setErrcode(ValidationResult.INVALID_SIG_CODE);
            validateRs.setErrmsg("签名不匹配");
            return validateRs;
        }
        return validateRs;
    }

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public void setWinnerNotifyUrl(String winnerNotifyUrl) {
        this.winnerNotifyUrl = winnerNotifyUrl;
    }

    public void setBidRequestUrl(String bidRequestUrl) {
        this.bidRequestUrl = bidRequestUrl;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public void setRegister(String register) {
        if ("1".equals(register)) {
            this.register = true;
        }
    }

}
