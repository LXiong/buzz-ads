package com.buzzinate.buzzads.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.domain.ChanetTradeData;
import com.buzzinate.buzzads.domain.enums.PaidStatusEnum;
import com.buzzinate.buzzads.domain.enums.TradeStatusEnum;
import com.buzzinate.buzzads.enums.TradeConfirmEnum;
import com.buzzinate.buzzads.service.ChanetCampTask;
import com.buzzinate.buzzads.service.ChanetTradeDataService;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.JsonResults;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 与chanet实时通知接口 成交时候会收到通知
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-11-23
 */
@Controller
@Scope("request")
public class ChanetNotifyAction extends ActionSupport {

    private static final long serialVersionUID = -5791522001923311466L;

    private static Log log = LogFactory.getLog(ChanetNotifyAction.class);

    private static final String CHANET_SIGNATURE_KEY = ConfigurationReader.getString("ads.network.chanet.key");
    private static final String REQUEST_QUERY_SPLIT_STRING_DOT = "&";

    private String date;
    private String time;
    private Integer type;
    private Long promotionID;
    private String promotionName;
    private String extinfo;
    private String userinfo;
    private Double comm;
    private Double totalPrice;
    private String ocd;
    private Integer status;
    private Integer paid;
    private Integer confirm;
    // 签名串
    private String sig;

    @Autowired
    private transient ChanetTradeDataService chanetTradeDataService;
    @Autowired
    private transient ChanetCampTask chanetService;

    private transient InputStream inputStream;
    private JsonResults results;
    
    private String source;
    private String notifyUrl;

    /**
     * 接收成果网实时交易数据
     */
    public String execute() {
        String requestStr = ServletActionContext.getRequest().getQueryString();
        log.info("Receive a CPS Order From Chanet:chanet_data=" + requestStr);
        // 签名校验
//        if (StringUtils.isEmpty(sig) || !checkSig()) {
//            inputStream = new ByteArrayInputStream("3".getBytes(Charset.forName("utf-8")));
//            return SUCCESS;
//        }
        try {
            ChanetTradeData tradeData = new ChanetTradeData();
            if (!validateNullTradeParams()) {
                inputStream = new ByteArrayInputStream("2".getBytes(Charset.forName("utf-8")));
                return SUCCESS;
            }
            // 属性
            tradeData.setDatetime(DateTimeUtil.getDateByString(date + time,
                    DateTimeUtil.FMT_DATE_YYYYMMDDHHMMSS));
            tradeData.setBillingType(type);
            tradeData.setCampaignId(promotionID);
            tradeData.setCampaignName(promotionName);
            tradeData.setCommission(getLongValue(comm));
            tradeData.setTotalPrice(getLongValue(totalPrice));
            tradeData.setConfirm(confirm);
            tradeData.setExtinfo(extinfo);
            tradeData.setUserinfo(userinfo);
            tradeData.setOcd(ocd);
            tradeData.setPaid(paid);
            tradeData.setStatus(status);
            chanetTradeDataService.saveTradeDate(tradeData);
            inputStream = new ByteArrayInputStream("1".getBytes(Charset.forName("utf-8")));
            return SUCCESS;
        } catch (Exception e) {
            inputStream = new ByteArrayInputStream("2".getBytes(Charset.forName("utf-8")));
            log.error("Save chanet trade data error: ", e);
        }
        return SUCCESS;
    }
    /*
     * 乘以100，取long型数值
     */
    private long getLongValue(Double d) {
        BigDecimal b = new BigDecimal(d.toString());
        return b.multiply(BigDecimal.valueOf(100)).longValue();
    }

    /*
     * 刷新chanet计划数据
     */
    public String loadChanetCampaign() {
        results = new JsonResults();
        try {
            chanetService.doJob();
            results.success("刷新成功");
        } catch (Exception e) {
            log.error("refresh chanet camp error:" + e);
            results.fail();
        }
        return JsonResults.JSON_RESULT_NAME;
    }
    
    /*
     * 成果网最新调整
     * @param requestStr
     * @return
     */
    private boolean checkSig() {
        String originalStr = "date=" + date + "&time=" + time + "&promotionID=" + promotionID +
                "&comm=" + comm + "&totalPrice=" + totalPrice + "&ocd=" + 
                ocd + "&" + CHANET_SIGNATURE_KEY; 
        String expectSig = DigestUtils.md5Hex(originalStr);
        if (sig.equals(expectSig))
            return true;
        return false;
    }
    /**
     * 交易数据校验
     * 
     * @return
     */
    private boolean validateNullTradeParams() {
        // 不能为空
        if (StringUtils.isEmpty(date) || StringUtils.isEmpty(time) || type == null || promotionID == null || 
                StringUtils.isEmpty(promotionName) || comm == null || totalPrice == null || 
                StringUtils.isEmpty(userinfo) || status == null || paid == null || confirm == null || 
                StringUtils.isEmpty(ocd)) {
            return false;
        }
        // 检查状态信息
        if (TradeStatusEnum.findByValue(status) == null || TradeConfirmEnum.findByValue(confirm) == null || 
                PaidStatusEnum.findByValue(paid) == null) {
            return false;
        }
        return true;
    }
    /**
     * 测试页面
     * @return
     */
    public String test() {
        return SUCCESS;
    }
    
    public String generateTestUrl() throws IOException {
        log.info("test utf8 md5:source=" + source);
        String str = URLDecoder.decode(source, "UTF-8") + REQUEST_QUERY_SPLIT_STRING_DOT + CHANET_SIGNATURE_KEY;
        log.info("test utf8 md5:after utf8 decode" + str);
        String encryptStr = DigestUtils.md5Hex(str);
        notifyUrl = source + "&sig=" + encryptStr;
        return SUCCESS;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getPromotionID() {
        return promotionID;
    }

    public void setPromotionID(Long promotionID) {
        this.promotionID = promotionID;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getExtinfo() {
        return extinfo;
    }

    public void setExtinfo(String extinfo) {
        this.extinfo = extinfo;
    }

    public String getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(String userinfo) {
        this.userinfo = userinfo;
    }

    public Double getComm() {
        return comm;
    }

    public void setComm(Double comm) {
        this.comm = comm;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOcd() {
        return ocd;
    }

    public void setOcd(String ocd) {
        this.ocd = ocd;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPaid() {
        return paid;
    }

    public void setPaid(Integer paid) {
        this.paid = paid;
    }

    public Integer getConfirm() {
        return confirm;
    }

    public void setConfirm(Integer confirm) {
        this.confirm = confirm;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public JsonResults getResults() {
        return results;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}

