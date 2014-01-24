package com.buzzinate.buzzads.action.advertiser;

import static com.buzzinate.buzzads.payment.PayConfig.PAYMENT_FAIL;
import static com.buzzinate.buzzads.payment.PayConfig.PAYMENT_SUCCEED;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.domain.AlipayRecord;
import com.buzzinate.buzzads.domain.enums.TradeStatus;
import com.buzzinate.buzzads.payment.OrdernoGenerator;
import com.buzzinate.buzzads.payment.PayConfig;
import com.buzzinate.buzzads.payment.alipay.util.AlipayBase;
import com.buzzinate.buzzads.payment.alipay.util.AlipayConfig;
import com.buzzinate.buzzads.payment.alipay.util.AlipayNotify;
import com.buzzinate.buzzads.payment.alipay.util.AlipayService;
import com.buzzinate.buzzads.payment.bean.PaymentOrderInfo;
import com.buzzinate.buzzads.payment.service.PayOrderService;
import com.buzzinate.buzzads.service.AdvertiserRechargeServices;
import com.buzzinate.buzzads.service.AlipayRecordService;
import com.buzzinate.buzzads.user.LoginHelper;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Created with IntelliJ IDEA.
 *
 * @author chris.xue
 *         Date: 13-3-18
 *         Time: 下午5:50
 *         广告主充值
 */
@Controller
@Scope("request")
public class AdvertiserRecharge extends ActionSupport implements ServletRequestAware {
    public static final String BUZZADS_SHOPPING_CART_INVALID_ORDER_ID = "buzzads.shopping.cart.invalid.order.id";
    public static final String BUZZADS_SHOPPING_CART_INVALID_RECHARGE_GATEWAY =
            "buzzads.shopping.cart.invalid.recharge.gateway";
    public static final String BUZZADS_SHOPPING_CART_INVALID_USER = "buzzads.shopping.cart.invalid.user";
    public static final String ORDER_NO = "orderNo";
    public static final String PAYMENT_ORDER_INFO = "paymentOrderInfo";
    public static final String POI_LIST = "poiList";
    public static final String PAYMENT_ORDER_INFO_ID = "paymentOrderInfoId";
    public static final String PAYGATE_ID = "paygateId";
    public static final String ERROR_IGNORE = "error ignore";
    public static final String BUZZADS_RECHARGE_NAME = "buzzads.recharge.name";
    public static final String BUZZADS_HOST_URL;
    public static final String BUZZADS_RECHARGE_PAYGATE_ALIPAY_NOTIFY_URL;
    public static final String BUZZADS_RECHARGE_PAYGATE_ALIPAY_RETURN_URL;
    public static final String BUZZADS_RECHARGE_FAILURE_ERROR_MESSAGE = "buzzads.recharge.failure.error.message";
    private static final long serialVersionUID = -8576195730548063047L;
    private static final long MAX_RECHARGE_PRICE = 
                    ConfigurationReader.getLong("ads.max.advertiser.recharge", 10000000L);

    private static final Log LOG = LogFactory.getLog(AdvertiserRecharge.class);

    private static final boolean IS_TEST = ConfigurationReader.getBoolean("buzzads.recharge.istest", false);
    private static String bshareDomain = ConfigurationReader.getString("buzzads.bshare.domain", "bshare.cn");
    private static DecimalFormat format;
    private int id;
    private String paymentUrl;
    private String notifyMessage;
    @Autowired
    private PayOrderService payOrderService;
    @Autowired
    private LoginHelper loginHelper;
    @Autowired
    private AlipayRecordService alipayRecordService;
    @Autowired
    private AdvertiserRechargeServices advertiserRechargeServices;
    @Transient
    private HttpServletRequest request;
    private int amount;
    private int recordId;

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getAmount() {
        return amount;
    }


    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    /**
     * @return
     */
    public String generateOrderInfo() {
        if (amount < 0 || amount > MAX_RECHARGE_PRICE) {
            addActionMessage(getText("buzzads.recharge.amount.max.error"));
            return INPUT;
        }
        //int userId = loginHelper.getUserId();
        AlipayRecord alipayRecord = getOrderInfo();
        alipayRecordService.save(alipayRecord);
        return SUCCESS;
    }

    public String loadOrderInfo() {

        PaymentOrderInfo p = this.payOrderService.read(id);
        if (p == null) {
            this.addActionError(getText(BUZZADS_SHOPPING_CART_INVALID_ORDER_ID));
            return Action.ERROR;
        }
        if (p.getUserId() != loginHelper.getUserId()) {
            this.addActionError(getText(BUZZADS_SHOPPING_CART_INVALID_USER));
            return Action.ERROR;
        }
        request.setAttribute(ORDER_NO, p.getOrderNo());
        request.setAttribute(PAYMENT_ORDER_INFO, p);
        return Action.SUCCESS;
    }

    /**
     * Loads the current user's completed order history...
     */
    public String loadOrderHistory() {
        int userId = loginHelper.getUserId();
        List<PaymentOrderInfo> poiList = payOrderService
                .getPaymentOrderInfoByUserId(userId, PayConfig.PAYMENT_SUCCEED);
        // Collections.reverse(poiList);

        request.setAttribute(POI_LIST, poiList);
        return Action.SUCCESS;
    }


    @SkipValidation
    /**
     * Checkout the user recharge.
     */
    public String checkout() {
        String paymentOrderInfoIdStr = request
                .getParameter(PAYMENT_ORDER_INFO_ID);
        String paygateId = request.getParameter(PAYGATE_ID);

        int paymentOrderInfoId;
        try {
            paymentOrderInfoId = Integer.parseInt(paymentOrderInfoIdStr);
        } catch (Exception e) {
            // invalid id given... was probably hacked
            request.setAttribute(PAYMENT_ORDER_INFO, new PaymentOrderInfo());
            addActionError(getText(BUZZADS_SHOPPING_CART_INVALID_ORDER_ID));
            return Action.ERROR;
        }

        int iPaygateId = PayConfig.PAYGATE_ALIPAY;
        try {
            iPaygateId = Integer.parseInt(paygateId);
        } catch (Exception e) {
            LOG.error(ERROR_IGNORE);
            // ignore for now... every order will be alipay.
        }

        AlipayRecord alipayRecord = alipayRecordService.read(paymentOrderInfoId);
        if (alipayRecord == null) {
            request.setAttribute(PAYMENT_ORDER_INFO, new PaymentOrderInfo());
            addActionError(getText(BUZZADS_SHOPPING_CART_INVALID_ORDER_ID));
            return Action.ERROR;
        }
        if (IS_TEST) {
            alipayRecord.setAmount(1);
        }
        if (PayConfig.PAYGATE_ALIPAY == iPaygateId) {
            this.paymentUrl = alipayURL(alipayRecord);
            return "redirect";
        }

        // invalid recharge gateway...
        request.setAttribute(PAYMENT_ORDER_INFO, alipayRecord);
        addActionError(getText(BUZZADS_SHOPPING_CART_INVALID_RECHARGE_GATEWAY));
        return Action.ERROR;
    }

    private String alipayURL(AlipayRecord alipayRecord) {

        String inputCharset = AlipayConfig.CHAR_SET;
        String signType = AlipayConfig.SIGN_TYPE;
        String sellerEmail = AlipayConfig.SELLER_EMAIL;
        String partner = AlipayConfig.PARTNER_ID;
        String key = AlipayConfig.KEY;

        String showUrl = AlipayConfig.SHOW_URL;

        String notifyUrl = BUZZADS_RECHARGE_PAYGATE_ALIPAY_NOTIFY_URL;
        String returnUrl = BUZZADS_RECHARGE_PAYGATE_ALIPAY_RETURN_URL;

        String antiphishing = AlipayConfig.ANTIPHISHING;

        // /////////////////////////////////////////////////////////////////////////////////
        // 订单名称，显示在支付宝收银台里的“商品名称”里，显示在支付宝的交易管理的“商品名称”的列表里。
        String subject = getText(BUZZADS_RECHARGE_NAME);
        // 订单描述、订单详细、订单备注，显示在支付宝收银台里的“商品描述”里
        String body = subject;

        // 以下参数是需要通过下单时的订单数据传入进来获得
        // 必填参数
        // 请与贵网站订单系统中的唯一订单号匹配
        String outTradeNoIsOurOrderNo = alipayRecord.getTradeNo();
        // 订单总金额，显示在支付宝收银台里的“应付总额”里
        String totalFee = format.format(alipayRecord.getAmount() / 100.0);

        // 扩展功能参数——网银提前
        // 默认支付方式，四个值可选：bankPay(网银);
        // cartoon(卡通); directPay(余额);
        // CASH(网点支付)
        String paymethod = "directPay";
        // 默认网银代号，代号列表见http://club.alipay.com/read.php?tid=8681379
        String defaultbank = "";

        // 扩展功能参数——防钓鱼
        // 防钓鱼时间戳，初始值
        String encryptKey = "";
        // 客户端的IP地址，初始值
        String exterInvokeIp = "";
        if (antiphishing.equals("1")) {
            try {
                encryptKey = AlipayBase.queryTimestamp(partner);
            } catch (MalformedURLException e) {
                LOG.error(e);
            } catch (DocumentException e) {
                LOG.error(e);
            } catch (IOException e) {
                LOG.error(e);
            }
            // 获取客户端的IP地址，建议：编写获取客户端IP地址的程序
            exterInvokeIp = "";
        }

        // 扩展功能参数——其他
        // 自定义参数，可存放任何内容（除=、&等特殊字符外），不会显示在页面上
        String extraCommonParam = "";
        // 默认买家支付宝账号
        String buyerEmail = "";

        // 扩展功能参数——分润(若要使用，请按照注释要求的格式赋值)
        // 提成类型，该值为固定值：10，不需要修改
        String royaltyType = "";
        String royaltyParameters = "";

        // 扩展功能参数——自定义超时(若要使用，请按照注释要求的格式赋值)
        // 该功能默认不开通，需联系客户经理咨询
        // 超时时间，不填默认是15天。八个值可选：1h(1小时),2h(2小时),3h(3小时),1d(1天),3d(3天),7d(7天),15d(15天),1c(当天)
        String itBPay = "";

        // ///////////////////////////////////////////////////////////////////////////////////////////////////

        // build redirect
        String paygateUrl = AlipayService.createUrl(partner, sellerEmail,
                returnUrl, notifyUrl, showUrl, outTradeNoIsOurOrderNo, subject,
                body, totalFee, paymethod, defaultbank, encryptKey,
                exterInvokeIp, extraCommonParam, buyerEmail, royaltyType,
                royaltyParameters, itBPay, inputCharset, key, signType);

        return paygateUrl;
    }

    /**
     * AliPay use this Method to notify to check whether.
     */
    public String notifyURL() {
        String orderNo = request.getParameter("out_trade_no");
        LOG.info("notifyURL, orderNO=" + orderNo);
        // 支付宝交易号
        String tradeNo = request.getParameter("trade_no");
        // 交易状态
        String tradeStatus = request.getParameter("trade_status");
        LOG.info("returnURL, orderNO=" + orderNo);
        checkSign("GBK");
        if (checkSignNotify()) {
            if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS")) {
                // 为了保证不被重复调用，或重复执行数据库更新程序，请判断该笔交易状态是否是订单未处理状态
                // 根据订单号更新订单，把订单状态处理成交易成功
                updateOrderInfo(orderNo, tradeNo, PAYMENT_SUCCEED);
                notifyMessage = "success";
            }
        }
        return Action.SUCCESS;
    }

    /**
     * 从request中获取数据，封装PaymentOrderInfo 对象.
     *
     * @return
     */
    private AlipayRecord getOrderInfo() {
        AlipayRecord value = new AlipayRecord();
        Date date = new Date();
        BigDecimal baseAmount = new BigDecimal(amount);
        value.setAmount(baseAmount.multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP).intValue());
        value.setAdvertiserId(loginHelper.getUserId());
        value.setCreateTime(date);
        value.setPaymentTime(date);
        value.setTradeNo(OrdernoGenerator.generateOrderNo());
        value.setRecordId(recordId);
        value.setTradeStatus(TradeStatus.WAITPAY);

        // the page view will use it atttibute's value.
        request.setAttribute("orderNo", value.getTradeNo());
        request.setAttribute("paymentOrderInfo", value);

        return value;
    }

    public boolean checkSign(String type) {
        String key = AlipayConfig.KEY;
        // 获取支付宝GET过来反馈信息
        Map<String, String> keysAndValues = getValuesFromAlipay(type);
        // 判断responsetTxt是否为ture，生成的签名结果mysign与获得的签名结果sign是否一致
        // responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        // mysign与sign不等，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
        String mysign = AlipayNotify.getMysign(keysAndValues, key);
        String responseTxt = AlipayNotify.verify(request.getParameter("notify_id"));
        String sign = request.getParameter("sign");
        LOG.info("sign=" + sign + ",mysgin=" + mysign + ",responseTxt" + responseTxt);
        debug(keysAndValues, mysign, responseTxt, sign);
        if (mysign.equals(sign) && responseTxt.equals("true"))
            return true;
        return false;
    }

    /**
     * check the notify_id comes from alipay
     *
     * @return
     */
    public boolean checkSignNotify() {
        // 判断responsetTxt是否为ture，生成的签名结果mysign与获得的签名结果sign是否一致
        // responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        // mysign与sign不等，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
        String responseTxt = AlipayNotify.verify(request.getParameter("notify_id"));
        return "true".equals(responseTxt);
    }

    /**
     * From Alipay return trade tag and other we need informations.
     *
     * @return
     */
    public String returnURL() {
        // 获取支付宝的通知返回参数
        // 支付宝交易号
        String tradeNo = request.getParameter("trade_no");
        // 获取订单号
        String orderNo = request.getParameter("out_trade_no");
        // 交易状态
        String tradeStatus = request.getParameter("trade_status");
        LOG.info("returnURL, orderNO=" + orderNo);
        if (checkSign(AlipayConfig.CHAR_SET)) {

            if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS")) {
                // 为了保证不被重复调用，或重复执行数据库更新程序，请判断该笔交易状态是否是订单未处理状态
                // 根据订单号更新订单，把订单状态处理成交易成功
                AlipayRecord record = updateOrderInfo(orderNo, tradeNo, PAYMENT_SUCCEED);
                if (record != null) {
                    addActionMessage(getText("buzzads.recharge.success.message", 
                                    Arrays.asList(record.getAmount() / 100)));
                } else {
                    return tipErrorInfo();
                }
                return Action.SUCCESS;
            } else {
                updateOrderInfo(orderNo, tradeNo, PAYMENT_FAIL);
                return tipErrorInfo();
            }
        } else {
            return tipErrorInfo();
        }
    }

    private String tipErrorInfo() {
        this.addActionError(getText(BUZZADS_RECHARGE_FAILURE_ERROR_MESSAGE));
        return Action.ERROR;
    }

    /**
     * Get the values from Alipay.
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    private Map<String, String> getValuesFromAlipay(String ecodeType) {
        Map<String, String> keysAndValues = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iterator = requestParams.keySet().iterator(); iterator.hasNext(); ) {
            String name = (String) iterator.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            keysAndValues.put(name, valueStr);
        }
        return keysAndValues;
    }

    /**
     * 更新订单信息，并发送email通知用户
     *
     * @param orderNo       the order info number.
     * @param outTradeNo    from paygate's trade no.
     * @param paymentStatus
     */
    private AlipayRecord updateOrderInfo(String orderNo, String outTradeNo, int paymentStatus) {
        AlipayRecord alipayRecord = alipayRecordService.getOrderInfo(orderNo);

        if (alipayRecord == null) {
            // 没找到相应的订单，记录到日志中，以备查询.
            LOG.warn("系统中没找到订单号:[" + orderNo + "]相应的订单!");
            return alipayRecord;
        }
        if (TradeStatus.WAITPAY != alipayRecord.getTradeStatus()) {
            LOG.info("this orderNo[" + orderNo + "] was done");
            return alipayRecord;
        }
        if (paymentStatus == PAYMENT_SUCCEED) {
            try {
                alipayRecord.setAliTradeNo(NumberUtils.toLong(outTradeNo));
                alipayRecord.setPaymentTime(new Date());
                alipayRecord.setTradeStatus(TradeStatus.SUCCESS);
                alipayRecordService.save(alipayRecord);
                advertiserRechargeServices.updateBillingAndBalanceForRecharge(alipayRecord);
            } catch (RuntimeException e) {
                LOG.error("updateOrderInfo:error", e);
            }
        }
        LOG.info("handle orderNo[" + orderNo + "] done");
        return alipayRecord;
    }

    /**
     * Just for debug.
     *
     * @param params
     * @param mysign
     * @param responseTxt
     * @param sign
     */
    private void debug(Map<String, String> params, String mysign, String responseTxt, String sign) {
        // 调试支付宝返回结果,写入日志
        final StringBuilder str = new StringBuilder();
        str.append("responseTxt=").append(responseTxt).append("\n ");
        str.append("return_url_log:sign=").append(sign).append("&mysign=").append(mysign).append("\n ");
        str.append("returnback:").append(AlipayBase.createLinkString(params));
        LOG.info(str);
    }

    @Override
    public void setServletRequest(HttpServletRequest requestR) {
        this.request = requestR;
    }

    public String getNotifyMessage() {
        return notifyMessage;
    }

    public String getBshareDomain() {
        return bshareDomain;
    }

    static {
        BUZZADS_HOST_URL = ConfigurationReader.getString("buzzads.server");
        BUZZADS_RECHARGE_PAYGATE_ALIPAY_NOTIFY_URL = BUZZADS_HOST_URL +
                ConfigurationReader.getString("buzzads.recharge.paygate.alipay.notify.url");
        BUZZADS_RECHARGE_PAYGATE_ALIPAY_RETURN_URL = BUZZADS_HOST_URL +
                ConfigurationReader.getString("buzzads.recharge.paygate.alipay.return.url");
        format = new DecimalFormat("########0.00");
    }
}
