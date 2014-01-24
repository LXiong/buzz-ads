/* *
 *功能：设置帐户有关信息及返回路径（基础配置页面）
 *版本：3.0
 *日期：2010-06-18
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
    
 *提示：如何获取安全校验码和合作身份者ID
 *1.访问支付宝首页(www.alipay.com)，然后用您的签约支付宝账号登陆.
 *2.点击导航栏中的“商家服务”，即可查看
    
 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 * */
package com.buzzinate.buzzads.payment.alipay.util;

import com.buzzinate.buzzads.core.util.ConfigurationReader;

/**
 * @author Jerry.Ma
 *         <p>
 *         支付宝即时到账API所需要的参数清单。为了应对变化，所有参数，均从配置文件获取.
 *         </p>
 */
public final class AlipayConfig {
    /**
     * 收款方名称，如：公司名称、网站名称、收款人姓名等
     */
    public static final String MAIN_NAME = "擘纳(上海)信息科技有限公司";

    /**
     * 合作身份者ID
     * 2088202669153323
     */
    public static final String PARTNER_ID = getValueByKey("bshare.payment.paygate.alipay.partner.id");

    /**
     * 安全检验码
     */
    public static final String KEY = getValueByKey("bshare.payment.paygate.alipay.key");

    /**
     * 签约支付宝账号或卖家收款支付宝帐户
     */
    public static final String SELLER_EMAIL = getValueByKey("bshare.payment.paygate.alipay.seller.email");

    /**
     * notify_url 交易过程中服务器通知的页面 要用 http://格式的完整路径，不允许加?id=123这类自定义参数
     */
    public static final String NOTIFY_URL = getValueByKey("bshare.main.server") + 
                    getValueByKey("bshare.payment.paygate.alipay.notify.url");

    /**
     * notify_url 交易过程中服务器通知的页面 要用 http://格式的完整路径，不允许加?id=123这类自定义参数
     */
    public static final String POINTS_NOTIFY_URL = getValueByKey("bshare.points.server") + 
                    getValueByKey("bshare.payment.paygate.alipay.notify.url");

    /**
     * 付完款后跳转的页面 要用 http://格式的完整路径，不允许加?id=123这类自定义参数
     */
    public static final String RETURN_URL = getValueByKey("bshare.main.server") + 
                    getValueByKey("bshare.payment.paygate.alipay.return.url");

    /**
     * 付完款后跳转的页面 要用 http://格式的完整路径，不允许加?id=123这类自定义参数
     */
    public static final String POINTS_RETURN_URL = getValueByKey("bshare.points.server") + 
                    getValueByKey("bshare.payment.paygate.alipay.return.url");

    /**
     * 网站商品的展示地址，不允许加?id=123这类自定义参数
     */
    public static final String SHOW_URL = getValueByKey("bshare.main.server");

    /**
     * 防钓鱼功能开关，'0'表示该功能关闭，'1'表示该功能开启。默认为关闭.
     * ::::注意:::
     * 一旦开启，就无法关闭，根据商家自身网站情况请慎重选择是否开启。
     * 申请开通方法：联系我们的客户经理或拨打商户服务电话0571-88158090，帮忙申请开通。
     * 开启防钓鱼功能后，服务器、本机电脑必须支持远程XML解析，请配置好该环境。
     * 若要使用防钓鱼功能，建议使用POST方式请求数据.
     */
    public static final String ANTIPHISHING = getValueByKey("bshare.payment.paygate.alipay.antiphishing");

    /**
     * 页面编码方式
     */
    public static final String CHAR_SET = getValueByKey("bshare.payment.paygate.alipay.charset");

    /**
     * 加密方式 不需修改
     */
    public static final String SIGN_TYPE = getValueByKey("bshare.payment.paygate.alipay.sign.type");

    /**
     * 访问模式,根据自己的服务器是否支持ssl访问，若支持请选择https；若不支持请选择http
     */
    public static final String TRANSPORT = getValueByKey("bshare.payment.paygate.alipay.transport");

    private AlipayConfig() {
        // ignore.
    }

    /**
     * The configure name's value.
     *
     * @param key the property name.
     * @return the configure name's value.
     */
    public static String getValueByKey(String key) {
        return ConfigurationReader.getString(key);
    }
}
