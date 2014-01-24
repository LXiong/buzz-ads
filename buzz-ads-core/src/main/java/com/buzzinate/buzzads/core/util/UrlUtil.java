package com.buzzinate.buzzads.core.util;

import com.buzzinate.common.util.string.StringUtil;
import org.apache.abdera.i18n.text.Punycode;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * This class provides functions for manipulation for URLs
 * @author John Chen Jan 12, 2010 Copyright 2009 Buzzinate Co. Ltd.
 *
 */
public final class UrlUtil {
    
    private UrlUtil() { } 
    
    /**
     * Checks if the given string is an URL (must start with http:// or https://)
     * @param url
     * @return
     */
    public static boolean isUrlHttp(String url) {
        if (url == null) return false;
        if (StringUtils.isEmpty(url)) {
            return false;
        }
        try {
            String regex = "^(http|https)\\://[\\w\\-_]+(\\.[\\w\\-_]+)+" + 
                            "([\\w\\-\\.,@?^=%&amp;:/~\\+#\u4e00-\u9fa5]*[\\w\\-\\@?^=%&amp;/~\\+#\u4e00-\u9fa5])?";
            return StringUtils.trim(url).matches(regex);
        } catch (Exception e) {
            return false;
        }
    } 
    
    
    /**
     * Checks if the given string is an URL (with optional http:// or https:// in the front)
     * @param url
     * @return
     */
    public static boolean isUrlOptionalHttp(String url) {
        try {
            String regex = "(^(http|https)\\://)?[\\w\\-_]+(\\.[\\w\\-_]+)+" + 
                            "([\\w\\-\\.,@?^=%&amp;:/~\\+#\u4e00-\u9fa5]*[\\w\\-\\@?^=%&amp;/~\\+#\u4e00-\u9fa5])?";
            return url.matches(regex);
        } catch (PatternSyntaxException p) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    
    /**
     * Gets the full domain name from the URL (http://www.buzzinate.com will return www.buzzinate.com)
     * @param url
     * @return
     */
    public static String getFullDomainName(String url) {
        if (url == null)
            return null;
        
        try {
            // extract the full domain:
            URL urlObj = new URL(url);
            String domainFull = urlObj.getHost();
            if (domainFull == null || domainFull.isEmpty()) {
                return url;
            } else {
                return domainFull.trim().toLowerCase();
            }
        } catch (MalformedURLException e) {
            return url;
        } catch (PatternSyntaxException p) {
            return url;
        } catch (IllegalStateException ise) {
            return url;
        } catch (IndexOutOfBoundsException ioe) {
            return url;
        }
    }
    
    public static String getFullDomainNameWithHttpPrefix(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        String fullDomainName = getFullDomainName(url);
        if (isUrlHttp(url)) {
            if (url.startsWith("https\\://")) {
                fullDomainName = "https://" + fullDomainName;
            } else {
                fullDomainName = "http://" + fullDomainName;
            }
        } else if (StringUtils.isNotBlank(fullDomainName)) {
            fullDomainName = "http://" + fullDomainName;
        }
        return fullDomainName;
    }
    
    public static boolean isFullDomainNameWithHttpPrefix(String url) {
        String fullDomainNameWithHttpPrefix = getFullDomainNameWithHttpPrefix(url);
        if (!(StringUtils.isEmpty(url) || StringUtils.isEmpty(fullDomainNameWithHttpPrefix))) {
            return url.equals(fullDomainNameWithHttpPrefix);
        }
        return false;

    }
    
    /**
     * Gets the domain name from the URL (http://www.buzzinate.com will return buzzinate.com)
     * @param url
     * @return
     */
    public static String getDomainName(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }
        
        String urlAddress = url.trim();
        String domainFull = "?";
        String domainError = "?";
        try {
            if (urlAddress.startsWith("file://")) {
                return null;
            }

            // extract the full domain:
            if (urlAddress.startsWith("http")) {
                URL urlObj = new URL(urlAddress);
                domainFull = urlObj.getHost();
                if (domainFull == null || domainFull.isEmpty()) {
                    return urlAddress;
                }
                domainFull = domainFull.trim();
            } else {
                domainFull = urlAddress;
            }

            // Check for an ip address here:
            if (isIpAddress(domainFull)) {
                return domainFull;
            }

            // Extract the site domain. Below are references for valid endings:
            // http://en.wikipedia.org/wiki/CcTLD
            // http://en.wikipedia.org/wiki/Generic_top-level_domain
            // http://www.iana.org/domains/root/db/
            // http://www.net.cn/static/domain/
            String regex = "\\.*([^.\\s]+)\\.(" + "biz|com|info|name|net|org|pro" + 
                            // general
                            "|aero|asia|cat|coop|edu|gov|int|jobs|mil|mobi|museum|tel|travel" + 
                            // sponsored
                            "|arpa|nato" + 
                            // Infrastructure/Deleted/retired
                            "|example|invalid|localhost|test" + 
                            // Reserved
                            "|bitnet|csnet|local|root|uucp|onion|exit" + 
                            // Pseudo
                            "|berlin|lat|nyc|bcn|paris" + 
                            // Locations
                            "|bzh|cym|eus|gal|lli|scot|sic" + 
                            // Language and nationality
                            "|geo|mail" + 
                            // Technical
                            "|kids|post|shop|web|xxx|eco|music" + 
                            // Other
                            "|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi" + 
                            "|bj|bm|bn|bo|br|bs|bt|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cu|cv|cx" + 
                            "|cy|cz|de|dj|dk|dm|do|dz|ec|ee|eg|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gd|ge|gf|gg" + 
                            "|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq" + 
                            "|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt" + 
                            "|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na" + 
                            "|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pn|pr|ps|pt|pw|py|qa" + 
                            "|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sk|sl|sm|sn|sr|st|su|sv|sy|sz|tc|td|tf" + 
                            "|tg|th|tj|tk|tl|tm|tn|to|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu" + 
                            "|wf|ws|ye|za|zm|zw" + 
                            // country codes
                            "|um|bl|eh|mf|bv|gb|pm|sj|so|yt|tp|yu|bu|cs|dd|zr" + 
                            // Others
                            "|xn--j1amh|xn--90ae|xn--p1ai" + 
                            // Others punycode (укр,бг,рф)
                            "|xn--fiqs8s|xn--io0a7i|xn--55qx5d|xn--0zwm56d" + 
                            // Others China punycode (中国,网络,公司,测试)
                            "|co\\.uk|org\\.uk|ac\\.uk|org\\.au|com\\.au|org\\.ru|cz\\.cc|com\\.nu|net\\.ru" + 
                            "|us\\.com|uk\\.com|eu\\.com|co\\.nz|net\\.nz|org\\.nz|co.\\tv" + 
                            // dual codes (world)
                            "|com\\.cn|org\\.cn|gov\\.cn|edu\\.cn|net\\.cn|com\\.tw|com\\.hk|org\\.tw|co\\.jp" + 
                            "|com\\.sp|com\\.ar|com\\.my|com\\.mo|com\\.im  |com\\.tw|com\\.hk|org\\.tw|co\\.jp" + 
                            // dual codes (asia)
                            "|ac\\.cn|bj\\.cn|sh\\.cn|tj\\.cn|cq\\.cn|he\\.cn|sx\\.cn|nm\\.cn|ln\\.cn|jl\\.cn" + 
                            "|hl\\.cn|js\\.cn|zj\\.cn|ah\\.cn|fj\\.cn|jx\\.cn|sd\\.cn|ha\\.cn|hb\\.cn|hn\\.cn" +
                            "|gd\\.cn|gx\\.cn|hi\\.cn|sc\\.cn|gz\\.cn|yn\\.cn|xz\\.cn|sn\\.cn|gs\\.cn|qh\\.cn" +
                            "|nx\\.cn|xj\\.cn|tw\\.cn|hk\\.cn|mo\\.cn" +
                            // China city dual codes
                            ")$";

            String domainName = domainFull.toLowerCase();
            domainError = domainName;

            Pattern domainNamePattern = Pattern.compile(regex);
            Matcher m = domainNamePattern.matcher(domainName);
            if (m.find()) {
                domainName = m.group(0);
                if (domainName.length() > 0) {
                    if (domainName.startsWith(".")) {
                        return decodePunycode(domainName.substring(1));
                    }
                    return decodePunycode(domainName);
                }
            }
            return domainName;
        } catch (MalformedURLException e) {
            return urlAddress;
        } catch (PatternSyntaxException p) {
            return domainFull;
        } catch (IllegalStateException ise) {
            return domainError;
        } catch (IndexOutOfBoundsException ioe) {
            return domainError;
        }
    }
    
    
    /**
     * Decodes the given string from punycode to UTF8
     * For example: decodePunycode("www.xn--zc1a399a.xn--fiqs8s") will return "www.舒铭.中国".
     * An exception will return the original string.
     * Note: you can only pass in domain names. "xn--zc1a399a.xn--fiqs8s/3.html" will not be decoded correctly.
     * "http://www.xn--zc1a399a.xn--fiqs8s" is allowed.
     * 
     * @param dnString a punycode encoded string
     * @return a UTF8 encoded string.
     */
    private static String decodePunycode(String dnString) {
        try {
            // if does not contain "xn--", this is not punycode.
            if (dnString.indexOf("xn--") == -1) {
                return dnString;
            }
            String[] components = dnString.split("\\.");
            StringBuffer output = new StringBuffer();
            for (int i = 0; i < components.length; i++) {
                // decode punycode if necessary
                if (components[i].startsWith("xn--")) {
                    // this is punycode.
                    String dnSBc = Punycode.decode(components[i].substring(4));
                    output.append(".");
                    output.append(dnSBc);
                } else {
                    // not punycode, just append.
                    output.append(".");
                    output.append(components[i]);
                }   
            }
            
            if (output.length() > 1) {
                return output.substring(1);
            }
        } catch (NullPointerException np) {
            return "";
        } 
        return dnString;
    }
    
    
    /**
     * Checks if the given string is an ip address
     * 
     * @param ip
     * @return whether the string is an ip address
     */
    public static boolean isIpAddress(String ip) {
        String regex = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
        if (ip.matches(regex)) {
            return true;
        }
        return false;
    }
    
    
    /**
     * Checks if the given string is an image file.
     * @param imageFile
     * @return
     */
    public static boolean isImageFile(String imageFile) {
        String img = imageFile.toLowerCase().trim();
        if (img.endsWith(".gif") || img.endsWith(".jpg") || 
                        img.endsWith(".png") || img.endsWith(".jpeg")) {
            return true;
        }
        return false;
    }
    

    /**
     * Parse the URL query string into map
     * @param query the query string
     * @return
     */
    public static Map<String, String> getQueryMap(String query)   { 
        Map<String, String> qmap = new HashMap<String, String>();  
        if (StringUtils.isEmpty(query)) {
            return qmap;
        }
        String[] params = query.split("&");  
        for (String param : params)   {
            String[] pair = param.split("=");
            String name = pair[0];
            String value = "";
            if (pair.length > 1) {
                value = StringUtil.urlDecode(pair[1]);
            }
            qmap.put(name, value);  
        }  
        return qmap;  
    }
    
    
    /**
     * Parse the URL's query string into map
     * @param url the url which contains the query string
     * @return
     */
    public static Map<String, String> getQueryMapFromUrl(String url)   { 
        URL theUrl;
        try {
            theUrl = new URL(url);
        } catch (MalformedURLException e) {
            return new HashMap<String, String>();
        }
        
        return getQueryMap(theUrl.getQuery());
    }
    
    
    /**
     * Parse the URL and get the value from query
     * @param url the url
     * @param name the query name
     * @return
     */
    public static String getQueryValueFromUrl(String url, String name)   { 
        
        URL theUrl;
        try {
            theUrl = new URL(url);
        } catch (MalformedURLException e) {
            return "";
        }
        
        String query = theUrl.getQuery();
        if (query != null && !query.isEmpty()) {
            String[] params = query.split("&");  
            for (String param : params)   {
                String[] pair = param.split("=");
                if (name.equals(pair[0]) && pair.length > 1) {
                    return pair[1];
                } 
            }  
        }
        return "";  
    }
    
    /**
     * 获取请求的URL，不包括参数
     * 
     * @param url
     * @return
     */
    public static String getRequestURL(String url) {
        if (StringUtils.isNotBlank(url)) {
            int index = url.indexOf('?');
            if (index > 0) {
                return url.substring(0, index);
            }
        }
        return url;
    }
    
    /**
     * Return the hash part of an URL
     * @param url
     * @return
     */
    public static String getHashFromUrl(String url) {
        int pos = url.indexOf('#');
        if (pos > 0) {
            return url.substring(pos + 1);
        } else {
            return "";
        }
    }
    
    
    /**
     * Remove the protocol (http or https) and the trailing slash from URL
     * @param url
     * @return The url without protocal and trailing slash
     */
    public static String removeProtocolAndTrailingSlash(String url) {
        return removeProtocolAndTrailingSlash(url, true);
    }
    
    /**
     * Remove the protocol (http or https) and the trailing slash from URL.
     * If toLowerCase is true, it will convert the URL to all lower case.
     * @param url
     * @param toLowerCase
     * @return The url without protocal and trailing slash
     */
    public static String removeProtocolAndTrailingSlash(String url , boolean toLowerCase) {
        return removeTrailingSlash(removeProtocal(toLowerCase ? url.toLowerCase() : url));
    }

    
    /**
     * Return the root of the url, without protocol
     * @param url
     * @return
     */
    public static String getRootUrl(String url) {
        String u = url;
        int in = u.indexOf("://");
        if (in <= 0) {
            u = "http://" + u;
        }

        try {
            URL theUrl = new URL(u);
            return theUrl.getHost();
        } catch (MalformedURLException e) {
            return u;
        }
    }
    
    
    /**
     * Remove protocol from url
     * @param url
     * @return
     */
    public static String removeProtocal(String url) {
        String u = url;
        int in = u.indexOf("://");
        if (in > 0) { 
            u = u.substring(in + 3);
        }
        return u;
    }

    
    /**
     * Remove trailing slash from url
     * @param url
     * @return
     */
    public static String removeTrailingSlash(String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        } else {
            return url;
        }
    }
    
    /**
     * Remove preceding slash from url
     * @param url
     * @return
     */
    public static String removePrecedingSlash(String url) {
        if (url.startsWith("/")) {
            return url.substring(1);
        } else {
            return url;
        }
    }
    
    /**
     * Decodes a URI's file name using UTF-8.
     * @return
     */
    public static String decodeFileNameUrl(String url) {
        String u = removeTrailingSlash(url);
        int i = u.lastIndexOf("/");
        if (i < 0) return u;
        String fileName = StringUtil.urlEncode(u.substring(i + 1));
        return u.substring(0, i + 1) + fileName; 
    }
    
    
    /**
     * Get the doman, subdomain and path from url
     * @param url
     * @param limit
     * @return
     */
    public static List<String> getPaths(String url, int limit) {
        List<String> list = new ArrayList<String>();
        if (StringUtils.isEmpty(url) || !isUrlHttp(url)) {
            return list;
        }
        
        String u = url;
        if (u.contains("?")) {
            u = u.split("\\?")[0];
        }
        String domain = com.buzzinate.common.util.string.UrlUtil.getDomainName(u);
        String fulldomain = com.buzzinate.common.util.string.UrlUtil.getFullDomainName(u);
        
        // add domain to list
        list.add(domain);    
        
        // add subdomain to list
        String subdomain = StringUtils.substringBefore(fulldomain, domain);
        String[] subdomains = StringUtils.split(subdomain, '.');
        String temp = domain;
        for (int index = subdomains.length - 1; index >= 0; index--) {
            temp = subdomains[index] + "." + temp;
            list.add(temp);
        }
        
        // add namespace to list
        String namespace = StringUtils.substringAfter(u, fulldomain);
        String[] namespaces = namespace.split("/", limit + 2);
        temp = fulldomain;
        for (int i = 1; i < namespaces.length - 1; i++) {
            //System.out.println(i+"="+namespaces[i]);
            if (!StringUtils.isEmpty(namespaces[i])) {
                temp += "/" + namespaces[i];
                list.add(temp);
            }
        }
        return list;
    }
    
    /**
     * 该URL是否在domain下
     * 
     * @param url
     * @param domain
     * @return
     */
    public static boolean isUrlUnderDomain(String url, String domain) {
        if (StringUtils.isEmpty(url)) {
            return false;
        }
        
        if (StringUtils.isEmpty(domain)) {
            return true;
        }
        if (isUrlHttp(domain)) {
            // domain是非顶级域名
            return url.startsWith(domain);
        } else {
            // domain是顶级域名
            String domainName = getDomainName(url);
            return domain.equals(domainName);
        }
    }
    
    public static String constructParam(Map<String, String> parameters) {
        StringBuffer sb = new StringBuffer();
        for (String key : parameters.keySet()) {
            sb.append("&").append(key).append("=").append(StringUtil.urlEncode(parameters.get(key)));
        }
        sb.setCharAt(0, '?');
        return sb.toString();
    }
    

    /**
     * 把参数加在URL后面
     * @param url
     * @param params
     * @return
     */
    public static String appendParamsToUrl(String url, Map<String, String> params) {
        if (StringUtils.isEmpty(url)) {
            return url;
        }
        
        StringBuilder urlBuffer = new StringBuilder(url);
        String paramsStr = getParamsStr(params);
        if (!StringUtils.isAllLowerCase(paramsStr)) {
            int index = url.indexOf("?");
            if (index < 0) {
                urlBuffer.append('?');
            } else if (index < (urlBuffer.length() - 1)) {
                urlBuffer.append('&');
            }
            urlBuffer.append(paramsStr);
        }
        return urlBuffer.toString();
    }
    

    /**
     * 把params参数列表拼成a=valu1&b=value2&...的形式返回
     * 
     * 默认对参数进行编码
     * 
     * @param params
     * @return
     */
    public static String getParamsStr(Map<String, String> params) {
        return getParamsStr(params, true);
    }
    
    /**
     * @param params
     * @param encoding  是否需要encoding
     * @return
     */
    public static String getParamsStr(Map<String, String> params, boolean encoding) {
        StringBuilder paramsStr = new StringBuilder();
        if (params != null) {
            for (String key : params.keySet()) {
                String value = (params.get(key) == null) ? "" : params.get(key);
                if (encoding) {
                    value = StringUtil.urlEncode(value);
                }
                paramsStr.append(key).append('=').append(value).append('&');
            }
            return paramsStr.substring(0, paramsStr.length() - 1);
        }
        return paramsStr.toString();
    }
    
    /**
     * 把params参数列表按照字母a-z顺序排序，拼成a=valu1&b=value2&...的形式返回
     * 
     * @param params
     * @return
     */
    public static String getParamsSortStr(Map<String, String> params) {
        TreeMap<String, String> treeMap = new TreeMap<String, String>(params);
        return getParamsStr(treeMap, false);
    }
    
    public static String getFullUrlWithPrefix(String url) {
        return isUrlHttp(url) ? url : "http://" + url;
    }
    
    /**
     * 获取上次请求的URL
     */
    public static String getReferer(HttpServletRequest request) {
        return request.getHeader("Referer");
    }
    
    /**compare whether url contain domain.
     * if url is null then false
     * domain is null then true
     * else url contain domain ignore case
     * @param url
     * @param domain
     * @return
     */
    public static boolean isIllegalDoamin(String url , String domain) {
        if (StringUtils.isBlank(url)) {
            return false;
        }
        if (StringUtils.isBlank(domain)) {
            return true;
        }
        return StringUtils.contains(url.toUpperCase(),
                StringUtils.remove(removeProtocal(StringUtils.trim(domain)).toUpperCase(), "WWW."));
    }

    /**
     * 计算MD5的签名值：把params参数列表按照字母顺序排序，拼成key1=value1&key2=value2&...的形式，
     * 后面再追加上appendCode，再进行MD5
     * 
     * 注意：待签名数据应该是原生值而不是encoding之后的值
     * 
     * @param params
     * @param appendCode
     * @return
     */
    public static String getMD5Sign(Map<String, String> params, String appendCode) {
        String paramsStr = getParamsSortStr(params);
        if (!StringUtils.isBlank(appendCode)) {
            paramsStr += appendCode;
        }
        return DigestUtils.md5Hex(paramsStr);
    }
    
    /**
     * 校验MD5签名是正确
     * @param params
     * @param appendCode
     * @param sign
     * @return
     */
    public static boolean checkMD5Sign(Map<String, String> params, String appendCode, String sign) {
        String md5Sign = getMD5Sign(params, appendCode);
        return md5Sign.equals(sign);
    }
    
    /**
     * 获取请求全路径
     * 
     * @param request
     * @return
     */
    public static String getFullRequestUrl(HttpServletRequest request) {
        String reqUrl = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if (queryString != null) {
            reqUrl += "?" + queryString;
        }
        return reqUrl;
    }
}
