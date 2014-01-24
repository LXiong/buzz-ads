package com.buzzinate.buzzads.payment.alipay.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;


/**
 *功能：支付宝接口公用函数 详细：该页面是请求、通知返回两个文件所调用的公用函数核心处理文件，不需要修改 版本：3.0 修改日期：2010-07-16
 * 
 *@author Jerry.Ma
 * 
 */
@SuppressWarnings({"rawtypes", "unchecked" })
public final class AlipayBase {

    /****/
    private AlipayBase() {
        // ignore
    }

    /**
     * 功能：生成签名结果
     * 
     * @param sArray
     *            要加密的数组
     * @param key
     *            安全校验码
     * @return 签名结果字符串
     */
    public static String buildMysign(Map sArray, String key) {
        // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String prestr = createLinkString(sArray);
        // 把拼接后的字符串再与安全校验码直接连接起来
        prestr = prestr + key; 
        String mysign = null;
        try {
            mysign = DigestUtils.md5Hex(prestr.getBytes(AlipayConfig.CHAR_SET));
        } catch (UnsupportedEncodingException e) {
            // if unsupport encoding, use the default charset.
            mysign = DigestUtils.md5Hex(prestr);
        }
        return mysign;
    }

    /**
     * 功能：除去数组中的空值和签名参数
     * 
     * @param sArray
     *            加密参数组
     * @return 去掉空值与签名参数后的新加密参数组
     */
    public static Map paraFilter(Map sArray) {
        
        List keys = new ArrayList(sArray.keySet());
        Map sArrayNew = new HashMap();

        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = (String) sArray.get(key);

            if (value == null || value.equals("") || 
                            key.equalsIgnoreCase("sign") || 
                            key.equalsIgnoreCase("sign_type")) {
                continue;
            }

            sArrayNew.put(key, value);
        }

        return sArrayNew;
    }

    /**
     * 功能：把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * 
     * @param params
     *            需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map params) {
        List keys = new ArrayList(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = (String) params.get(key);

            if (i == keys.size() - 1) {
                // 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    /**
     * 功能：把数组所有元素按照“参数=参数值”的模式用“&”字符拼接成字符串 应用场景：使用场景：GET方式请求时，对URL的中文进行编码
     * 
     * @param params
     *            需要排序并参与字符拼接的参数组
     * @param inputCharset
     *            编码格式
     * @return 拼接后字符串
     */
    public static String createLinkStringUrlEncode(Map params,
            String inputCharset) {
        List keys = new ArrayList(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = (String) params.get(key);

            try {
                prestr = prestr + key + "=" + URLEncoder.encode(value, inputCharset) + "&";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return prestr;
    }

    /**
     * 功能：用于防钓鱼，调用接口queryTimestamp来获取时间戳的处理函数 注意：远程解析XML出错，与服务器是否支持SSL等配置有关
     * 
     * @param partner
     *            合作身份者ID
     * @return 时间戳字符串
     * @throws java.io.IOException
     * @throws org.dom4j.DocumentException
     * @throws java.net.MalformedURLException
     */

    public static String queryTimestamp(String partner) throws
            DocumentException, IOException {
        
        String strUrl = "https://mapi.alipay.com/gateway.do?service=query_timestamp&partner=" + partner;
        StringBuffer buf1 = new StringBuffer();
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new URL(strUrl).openStream());

        List<Node> nodeList = doc.selectNodes("//alipay/*");

        for (Node node : nodeList) {
            // 截取部分不需要解析的信息
            if (node.getName().equals("is_success") && node.getText().equals("T")) {
                // 判断是否有成功标示
                List<Node> nodeList1 = doc.selectNodes("//response/timestamp/*");
                for (Node node1 : nodeList1) {
                    buf1.append(node1.getText());
                }
            }
        }

        return buf1.toString();
    }
}
