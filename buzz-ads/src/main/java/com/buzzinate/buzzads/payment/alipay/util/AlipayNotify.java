/**
 *
 */
package com.buzzinate.buzzads.payment.alipay.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author Jerry.Ma
 */
@SuppressWarnings({"rawtypes" })
public final class AlipayNotify {

    /***/
    private AlipayNotify() {
        // ignore.
    }

    /**
     * 功能：根据反馈回来的信息，生成签名结果
     *
     * @param params 通知返回来的参数数组
     * @param key    安全校验码
     * @return 生成的签名结果
     */
    public static String getMysign(final Map params, final String key) {
        // 过滤空值、sign与sign_type参数
        Map sParaNew = AlipayBase.paraFilter(params);
        // 获得签名结果
        String mysign = AlipayBase.buildMysign(sParaNew, key);

        return mysign;
    }

    /**
     * 功能：获取远程服务器ATN结果,验证返回URL
     *
     * @param notifyId 通知校验ID
     * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
     *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    public static String verify(final String notifyId) {
        // 获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
        String transport = AlipayConfig.TRANSPORT;
        String partner = AlipayConfig.PARTNER_ID;
        String veryfyUrl = "";
        if (transport.equalsIgnoreCase("https")) {
            veryfyUrl = "https://www.alipay.com/cooperate/gateway.do?service=notify_verify";
        } else {
            veryfyUrl = "http://notify.alipay.com/trade/notify_query.do?";
        }
        veryfyUrl = veryfyUrl + "&partner=" + partner + "&notify_id=" + notifyId;
        return checkUrl(veryfyUrl);
    }

    /**
     * *功能：获取远程服务器ATN结果
     *
     * @param veryfyUrl 指定URL路径地址
     * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
     *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    public static String checkUrl(final String veryfyUrl) {
        String inputLine = "";

        try {
            URL url = new URL(veryfyUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            inputLine = in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("AlipayNotify.checkUrl():[" + inputLine + "]");
        return inputLine;
    }

}
