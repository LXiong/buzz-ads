package com.buzzinate.buzzads.action.advertiser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.core.service.AdxDspRtbInfoService;
import com.buzzinate.buzzads.core.util.ValidationResult;
import com.buzzinate.buzzads.domain.AdxDspRtbInfo;
import com.buzzinate.buzzads.domain.AdxDspRtbStatistic;
import com.buzzinate.common.util.ApiUtil;
import com.opensymphony.xwork2.ActionSupport;

import flexjson.JSONSerializer;

/**
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 *         adx register/unregister api
 */
@Controller
@Scope("request")
public class AdxDspStatisticApiAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private static final long serialVersionUID = -120428565619747939L;
    private static final long EXPIRED_TS = 1000 * 60 * 10;
    
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ValidationResult validateRs = new ValidationResult();
    @Autowired
    private AdxDspRtbInfoService adxDspRtbInfoService;
    private Integer dspId;
    // 请求数量
    private Integer requestCount;
    // 返回数量
    private Integer responseCount;
    // 广告元素有效性数量
    private Integer validCount;
    // 竞价成功数量（即显示在page上）
    private Integer winnerCount;
    
    private long ts;
    
    public String execute() {
        Map<String, String> jsonMap = new HashMap<String, String>();

        // 验证必须参数和签名
//        validateParameters();
        if (!validateRs.getErrcode().equals(ValidationResult.VALID_CODE)) {
            jsonMap.put("success", "false");
        } else {
            try {
                //TODO should store related table
//                AdxDspRtbInfo rtbInfo = new AdxDspRtbInfo(dspId, requestCount, responseCount, validCount, winnerCount);
//                adxDspRtbInfoService.updateAdxDspStatistic(rtbInfo);
                jsonMap.put("success", "true");
            } catch (Exception e) {
                LOG.error("exception", e);
                jsonMap.put("success", "false");
            }
        }
        String resultString = new JSONSerializer().deepSerialize(jsonMap);
        response.setContentType("application/json");
        try {
            PrintWriter writer = response.getWriter();
            writer.println(resultString);
            writer.flush();
        } catch (IOException e) {
            LOG.debug("AdxDspStatisticApiAction::/api/statisticCount/", e);
        } finally {
            
        }
        return null;
    }
    
    public String test() {
        Map<String, String> jsonMap = new HashMap<String, String>();

        // 验证必须参数和签名
//        validateParameters();
        if (!validateRs.getErrcode().equals(ValidationResult.VALID_CODE)) {
            jsonMap.put("success", "false");
        } else {
            try {
                AdxDspRtbInfo rtbInfo = new AdxDspRtbInfo(2, "www.sina.com", "www.sina.com");
                adxDspRtbInfoService.saveOrUpdate(rtbInfo);
                
                AdxDspRtbStatistic stats = new AdxDspRtbStatistic(2, 1000, 900, 700, 300);
                adxDspRtbInfoService.updateAdxDspStatistic(stats);
//                adxDspRtbInfoService.sendDspWarningEmail(rtbInfo, stats);
                adxDspRtbInfoService.deleteRtbInfo(rtbInfo.getKey());
                jsonMap.put("success", "true");
                System.out.println("rtbInfo: \t" + rtbInfo);
            } catch (Exception e) {
                LOG.error("exception", e);
                jsonMap.put("success", "false");
            }
        }
        String resultString = new JSONSerializer().deepSerialize(jsonMap);
        response.setContentType("application/json");
        try {
            PrintWriter writer = response.getWriter();
            writer.println(resultString);
            writer.flush();
        } catch (IOException e) {
            LOG.debug("AdxDspStatisticApiAction::/api/statisticCount/", e);
        } finally {
            
        }
        return null;
    }

    /**
     * 检查必须参数 和签名
     *
     * @return
     */
    private ValidationResult validateParameters() {

        
//        if (StringUtils.isEmpty(sig)) {
//            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
//            validateRs.setErrmsg("缺少必须的参数：sig");
//            return validateRs;
//        }
        
        if (ApiUtil.isSigExpired(ts, EXPIRED_TS)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("时间戳非法或过期");
            return validateRs;
        }
//        HashMap<String, String> parameterMap = new HashMap<String, String>();
//        parameterMap.put("key", String.valueOf(key));
//        parameterMap.put("ts", String.valueOf(ts));
//        String secret = ConfigurationReader.getString("adx.");

//        if (!ApiUtil.validateSignature(parameterMap, secret, sig)) {
//            validateRs.setErrcode(ValidationResult.INVALID_SIG_CODE);
//            validateRs.setErrmsg("签名不匹配");
//            return validateRs;
//        }
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

    public Integer getDspId() {
        return dspId;
    }

    public void setDspId(Integer dspId) {
        this.dspId = dspId;
    }

    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }

    public void setResponseCount(Integer responseCount) {
        this.responseCount = responseCount;
    }

    public void setValidCount(Integer validCount) {
        this.validCount = validCount;
    }

    public void setWinnerCount(Integer winnerCount) {
        this.winnerCount = winnerCount;
    }

}
