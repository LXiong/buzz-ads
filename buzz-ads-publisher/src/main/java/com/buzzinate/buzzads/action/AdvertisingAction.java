package com.buzzinate.buzzads.action;

import com.buzzinate.buzzads.analytics.AdAnalyticsConstants;
import com.buzzinate.buzzads.analytics.AdClick;
import com.buzzinate.buzzads.analytics.AdShowUps;
import com.buzzinate.buzzads.client.AdsGenerateClient;
import com.buzzinate.buzzads.client.AdvertiseFinagledClient;
import com.buzzinate.buzzads.core.bean.AdBanner;
import com.buzzinate.buzzads.core.service.AdBannerServices;
import com.buzzinate.buzzads.core.service.SiteService;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.core.util.RedisClient;
import com.buzzinate.buzzads.core.util.ValidationResult;
import com.buzzinate.buzzads.domain.Site;
import com.buzzinate.buzzads.entity.BannerInfo;
import com.buzzinate.buzzads.enums.AdEntrySizeEnum;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.buzzads.thrift.AdEntryTypeEnum;
import com.buzzinate.buzzads.thrift.AdItem;
import com.buzzinate.buzzads.thrift.AdParam;
import com.buzzinate.common.util.ApiUtil;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.http.UserAgentUtil;
import com.buzzinate.common.util.kestrel.KestrelClient;
import com.buzzinate.common.util.serialize.HessianSerializer;
import com.buzzinate.common.util.string.CookieIdUtil;
import com.buzzinate.common.util.string.StringUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import flexjson.JSONSerializer;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * 广告投放Action
 *
 * @author zyeming
 */
@Controller
@Scope("request")
public class AdvertisingAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
    private static final long serialVersionUID = -4424299856581057163L;
    private static final long EXPIRED_TS = 1000 * 60 * 10;
    private static final String CLICK_BASE_URL = ConfigurationReader.getString("click.path");
    private static final int Z_EXPIRED_TIME = ConfigurationReader.getInt("ads.z.expired.time", 20 * 60);
    private static final int ThirdAdId = ConfigurationReader.getInt("admax.3rd.ad.id");
    private static Log log = LogFactory.getLog(AdvertisingAction.class);
    private static JSONSerializer json = new JSONSerializer();
    @Autowired
    private transient KestrelClient analyticsKestrelClient;
    // 站长UUID
    private String uuid;
    // 页面url
    private String url;
    // 展示标识
    private String z;
    // 页面标题，参数名ti
    private String title;
    // 页面Referrer，参数名ref
    private String referrer;
    // 广告框高度，参数名h
    private String height;
    // 广告框宽度，参数名w
    private String width;
    // 广告类型，参数rt
    // 0 - 任意（默认），1 - 图片，2 - 文字，3 - Flash
    private int type = 0;
    // 页面Keywords，参数mk
    private String metaKeywords;
    // 页面Description，参数md
    private String metaDescription;
    // 性别参数ge
    // 0 - 未知（默认），1 - 男，2 - 女
    private int gender;
    // 出生日期，参数bd
    private String birthDate;
    // 民族代码，参数na
    private int nationality;
    // 位置，参数lo
    private String location;
    // 广告网络，Lezhi（l）、bShare(s)、Wejifen（w）或者BuzzAds（b，默认）
    private AdNetworkEnum network = AdNetworkEnum.LEZHI;
    // 对应的Lezhi、bShare或者Weijifen的CookieID。
    private String vid;
    // 对应的Lezhi、bShare或者Weijifen的用户Id。
    private String uid;
    // buzzinfo信息
    private String buzzinfo;
    // jsonp回调函数
    private String callback;
    // 时间戳
    private long ts;
    // 签名
    private String sig;
    // 用户对应的密钥
    private String secret;
    // 广告ID
    private int eid;
    //版位信息
    private String s;
    // 广告slot id
    private int sid = -1;
    //在click方法是否返回结果 1返回 -1不返回
    private int rc = -1;
    private HttpServletRequest request;
    private HttpServletResponse response;
    @Autowired
    private transient SiteService siteService;
    @Autowired
    private transient AdvertiseFinagledClient advertiserClient;
    @Autowired
    private transient RedisClient redisClient;
    @Autowired
    private transient AdBannerServices adBannerServices;

    public String getNw() {
        if (network == null) {
            return AdNetworkEnum.LEZHI.getAbbr();
        }
        return network.getAbbr();
    }

    public void setNw(String nw) {
        this.network = AdNetworkEnum.findByAbbr(nw);
    }

    public String getAdsJsonNoValid() {
        return getAdsJson(false);
    }

    public String getAdsJsonValid() {
        return getAdsJson(true);
    }

    public String fireAdsShowup() {
        checkCookie();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        ValidationResult result = validateTsAndSig(false);
        if (!result.getErrcode().equals(ValidationResult.VALID_CODE) || eid <= 0) {
            jsonMap.put("success", "false");
        }
        try {
            String zKey = "z:" + z;
            Boolean checked = (Boolean) redisClient.getObject(zKey);
            if (checked != null) {
                fireShowUp(eid);
            }
            jsonMap.put("success", true);
        } catch (Exception e) {
            jsonMap.put("success", false);
        }
        if (rc == 1)
            writeResponse(jsonMap);
        return null;
    }

    public String fireAdsClick() {
        checkCookie();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        ValidationResult result = validateTsAndSig(false);
        if (!result.getErrcode().equals(ValidationResult.VALID_CODE) || eid <= 0) {
            jsonMap.put("success", "false");
        }
        try {
            String zKey = "z:" + z;
            Boolean checked = (Boolean) redisClient.getObject(zKey);
            if (checked != null) {
                fireClick(eid);
                redisClient.delete(zKey);
            }
            jsonMap.put("success", true);
        } catch (Exception e) {
            jsonMap.put("success", false);
        }
        if (rc == 1)
            writeResponse(jsonMap);
        return null;
    }

    private void writeResponse(Map<String, Object> jsonMap) {
        String resultString;
        if (StringUtils.isNotEmpty(callback)) {
            resultString = callback + "(" + json.deepSerialize(jsonMap) + ")";
        } else {
            resultString = json.deepSerialize(jsonMap);
        }
        response.setContentType("text/javascript;charset=utf-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.println(resultString);
        } catch (IOException e) {
            LOG.debug("AdvertisingAction", e);
        }
    }

    private void checkCookie() {
        if (StringUtils.isEmpty(vid) || !CookieIdUtil.isValidCookieId(vid)) {
            vid = getCookieId();
            if (StringUtils.isEmpty(vid) || !CookieIdUtil.isValidCookieId(vid)) {
                vid = CookieIdUtil.generateCookieId(request);
                Cookie cookie = new Cookie("vid", vid);
                cookie.setMaxAge(Integer.MAX_VALUE);
                cookie.setDomain(ConfigurationReader.getString("buzzads.cookie.domain", "buzzinate.com"));
                response.addCookie(cookie);
            }
        }
    }

    private String getCookieId() {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(), "vid")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void setDefaultValue() {
        if (StringUtils.isBlank(width) || StringUtils.isBlank(height)) {
            if (type == AdEntryTypeEnum.FLASH.getValue()) {
                width = "300";
                height = "250";
            } else {
                width = "80";
                height = "80";
            }
        }
    }

    private void getAdsResults(List<BannerInfo> bannerInfoList, Map<Object, AdItem> adItemMap) {
        if (bannerInfoList == null || bannerInfoList.isEmpty() || adItemMap == null) return;
        Map<String, List<BannerInfo>> group = new HashMap<String, List<BannerInfo>>();
        for (BannerInfo info : bannerInfoList) {
            String key = new StringBuffer().append(info.getWidth()).append(info.getHeight()).append(info.getType()).toString();
            if (group.containsKey(key)) {
                group.get(key).add(info);
            } else {
                List<BannerInfo> subBannerInfos = new ArrayList<BannerInfo>();
                subBannerInfos.add(info);
                group.put(key, subBannerInfos);
            }
        }
        for (Map.Entry<String, List<BannerInfo>> entry : group.entrySet()) {
            List<BannerInfo> subInfos = entry.getValue();
            BannerInfo info = subInfos.get(0);
            List<AdItem> items = getAdsResult(url, uuid, vid, title, metaKeywords, info.getType(), String.valueOf(info.getWidth()), String.valueOf(info.getHeight()), info.getNetwork(), request, subInfos.size());
            for (int i = 0; i < subInfos.size(); i++) {
                if (items == null || items.isEmpty() || i >= items.size()) {
                    adItemMap.put(subInfos.get(i).getId(), null);
                } else {
                    adItemMap.put(subInfos.get(i).getId(), items.get(i));
                }
            }
        }
    }

    public String getAdsJson(boolean needValid) {
        checkCookie();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        ValidationResult validateRs = validateTsAndSig(needValid);
        if (!validateRs.getErrcode().equals(ValidationResult.VALID_CODE)) {
            jsonMap.put("success", false);
            jsonMap.put(ValidationResult.ERRCODE, validateRs.getErrcode());
            jsonMap.put(ValidationResult.ERRMSG, validateRs.getErrmsg());
        } else {
            setDefaultValue();
            AdBanner adBanner = adBannerServices.getUrlByUuid(uuid);
            if (adBanner == null) {
                jsonMap.put("success", false);
                jsonMap.put(ValidationResult.ERRCODE, ValidationResult.RESULT_NOTFOUND_CODE);
                jsonMap.put(ValidationResult.ERRMSG, "没有获取到广告信息");
                writeResponse(jsonMap);
                return null;
            }
            List<BannerInfo> bannerInfoList = parseBannerInfo(adBanner);
            Map<Object, AdItem> adItemMap = new HashMap<Object, AdItem>();
            AdItem adItem;
            if (bannerInfoList == null || bannerInfoList.isEmpty()) {
                adItem = getAdsResult(url, uuid, vid, title, metaKeywords, type, width, height, network, request);
                adItemMap.put(sid, adItem);
            } else {
                getAdsResults(bannerInfoList, adItemMap);
            }
            checkZ();
            List<Map<String, Object>> ads = new ArrayList<Map<String, Object>>(adItemMap.size());
            jsonMap.put("success", false);
            for (Map.Entry<Object, AdItem> entry : adItemMap.entrySet()) {
                AdItem item = entry.getValue();
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("id", entry.getKey());
                resultMap.put("width", adBanner.getWidth());
                resultMap.put("height", adBanner.getHeight());
                resultMap.put("nw", adBanner.getNetwork().getAbbr());
                resultMap.put("rt", adBanner.getType().getCode());
                if (item == null) {
                    resultMap.put("thirdUrl", adBanner.getUrl());
                    resultMap.put("eid", ThirdAdId);
                } else {
                    String adsInfoStr = getAdsInfoStr(item.getAdEntryId());
                    resultMap.putAll(AdsGenerateClient.generate(item, adsInfoStr));
                    if (!needValid && item.getAdEntryIdOption().isDefined()) {
                        resultMap.put("eid", String.valueOf(item.getAdEntryId()));
                    }
                }
                ads.add(resultMap);
            }

            if (ads.isEmpty()) {
                jsonMap.put("success", false);
                jsonMap.put(ValidationResult.ERRCODE, ValidationResult.RESULT_NOTFOUND_CODE);
                jsonMap.put(ValidationResult.ERRMSG, "没有获取到广告信息");
            } else {
                jsonMap.put("success", true);
                jsonMap.put("ads", ads);
            }
        }
        writeResponse(jsonMap);
        return null;
    }

    public String getAdsIframe() {
        request.setAttribute("width", width);
        request.setAttribute("height", height);
        ValidationResult validationResult = validateTsAndSig(true);
        if (validationResult.getErrcode().equals(ValidationResult.VALID_CODE)) {
            AdItem adItem = getAdsResult(url, uuid, vid, title, metaKeywords, type, width, height, network, request);
            if (adItem != null) {
                //获取到广告
                checkZAndFireShowUp(adItem);
                String adsInfoStr = getAdsInfoStr(adItem.getAdEntryId());
                request.setAttribute("adurl", CLICK_BASE_URL + "?p=" + StringUtil.urlEncode(adsInfoStr) +
                        "&link=" + StringUtil.urlEncode(adItem.getUrl()));
                request.setAttribute("adtitle", adItem.getTitle());
                request.setAttribute("adimg", adItem.getPic());
                request.setAttribute("type", type);
                return Action.SUCCESS;
            }
        }
        //未获取到广告或验证失败
        request.setAttribute("type", -1);
        return Action.SUCCESS;
    }

    private void checkZAndFireShowUp(AdItem adItem) {
        if (checkZ()) {
            //触发展示事件
            fireShowUp(adItem.getAdEntryId());
        }
    }

    /**
     * 是否是一个新z请求
     *
     * @return
     */
    private boolean checkZ() {
        String zKey = "z:" + z;
        Boolean checked = (Boolean) redisClient.getObject(zKey);
        if (checked == null) {
            redisClient.set(zKey, Z_EXPIRED_TIME, Boolean.FALSE);
            //触发展示事件
            return true;
        }
        return false;
    }

    /**
     * 发送展示事件。
     *
     * @param eid
     */
    private void fireShowUp(int eid) {
        if (eid <= 0) {
            return;
        }
        long current = DateTimeUtil.getCurrentDate().getTime();
        AdShowUps adShowUps = new AdShowUps();
        adShowUps.setPublisherUuid(uuid);
        adShowUps.setAdEntryIds(Arrays.asList(Long.valueOf(eid).longValue()));
        adShowUps.setCookieId(vid);
        adShowUps.setSourceUrl(getSourceUrl());
        adShowUps.setNetwork(network);
        adShowUps.setCreateAt(new Date(current));
        adShowUps.setUa(request.getHeader("User-Agent"));
        adShowUps.setIp(UserAgentUtil.getClientIp(request));
        log.info("receive showups ==> " + adShowUps);
        analyticsKestrelClient.put(AdAnalyticsConstants.ADSHOWUP_QUEUE, HessianSerializer.serialize(adShowUps));
    }

    private String getSourceUrl() {
        String sourceUrl = request.getHeader("Referer");
        return sourceUrl;
    }

    private void fireClick(int eid) {
        if (eid <= 0) {
            return;
        }
        long current = DateTimeUtil.getCurrentDate().getTime();
        AdClick adClick = new AdClick();
        adClick.setPublisherUuid(uuid);
        adClick.setAdEntryId(eid);
        adClick.setSourceUrl(getSourceUrl());
        adClick.setCookieId(vid);
        adClick.setNetwork(network);
        adClick.setUa(request.getHeader("User-Agent"));
        adClick.setCreateAt(new Date(current));
        adClick.setIp(UserAgentUtil.getClientIp(request));
        analyticsKestrelClient.put(AdAnalyticsConstants.ADCLICK_QUEUE, HessianSerializer.serialize(adClick));
    }

    private ValidationResult validateTsAndSig(boolean checkSig) {
        ValidationResult validateRs = new ValidationResult();

        if (StringUtils.isEmpty(uuid)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("uuid 不能为空");
            return validateRs;
        }
        Site site;
        try {
            site = siteService.getUuidSiteByUuid(uuid);
        } catch (Exception e) {
            LOG.error("AdvertisingAction::validateTsAndSig", e);
            site = null;
        }
        if (null == site) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("该uuid:" + uuid + "不存在");
            return validateRs;
        }
        secret = site.getSecret();

        if (StringUtils.isEmpty(url)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("url不能为空");
            return validateRs;
        }
        if (ApiUtil.isSigExpired(ts, EXPIRED_TS)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("请求时间戳ts:" + ts + "已经过期");
            return validateRs;
        }

        if (StringUtils.isEmpty(z)) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("z不能为空");
            return validateRs;
        }
        if (z.length() < 16) {
            validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
            validateRs.setErrmsg("z不能少于16位");
            return validateRs;
        }

        if (checkSig) {
            if (StringUtils.isEmpty(sig)) {
                validateRs.setErrcode(ValidationResult.INVALID_PARAMETER_CODE);
                validateRs.setErrmsg("sig签名 不能为空");
                return validateRs;
            }
            HashMap<String, String> parameterMap = new HashMap<String, String>();
            parameterMap.put("uuid", uuid);
            parameterMap.put("url", url);
            parameterMap.put("z", z);
            parameterMap.put("ts", String.valueOf(ts));

            if (!ApiUtil.validateSignature(parameterMap, secret, sig)) {
                validateRs.setErrcode(ValidationResult.INVALID_SIG_CODE);
                validateRs.setErrmsg("sig签名:" + sig + "不正确");
                return validateRs;
            }
        }
        return validateRs;
    }

    private String getAdsInfoStr(int eid) {
        HashMap<String, String> infoMap = new HashMap<String, String>();
        infoMap.put("uuid", uuid);
        infoMap.put("url", StringUtil.urlEncode(url));
        infoMap.put("z", z);
        infoMap.put("eid", String.valueOf(eid));
        infoMap.put("ts", String.valueOf(System.currentTimeMillis()));
        String sigStr = ApiUtil.encryptForApi(infoMap, secret);
        infoMap.put("sig", StringUtil.urlEncode(sigStr));
        infoMap.put("nw", getNw());
        if (StringUtils.isNotEmpty(vid)) {
            infoMap.put("vid", vid);
        }
        if (StringUtils.isNotEmpty(uid)) {
            infoMap.put("uid", uid);
        }
        if (StringUtils.isNotEmpty(buzzinfo)) {
            infoMap.put("buzzinfo", buzzinfo);
        }
        String infoStr = "";
        for (Map.Entry<String, String> entry : infoMap.entrySet()) {
            infoStr += entry.getKey() + "=" + entry.getValue() + "&";
        }
        infoStr = StringUtils.removeEnd(infoStr, "&");
        return Base64.encodeBase64String(infoStr.getBytes());
    }

    private AdItem getAdsResult(String url, String uuid, String vid, String title, String metaKeywords, int type, String width, String height, AdNetworkEnum network, HttpServletRequest request) {
        List<AdItem> itemList = getAdsResult(url, uuid, vid, title, metaKeywords, type, width, height, network, request, 1);
        if (itemList != null && itemList.size() > 0) {
            AdItem item = itemList.get(0);
            return item;
        }
        return null;
    }

    private List<AdItem> getAdsResult(String url, String uuid, String vid, String title, String metaKeywords, int type, String width, String height, AdNetworkEnum network, HttpServletRequest request, int count) {
        AdParam param = new AdParam.Builder()
                .url(StringUtil.urlDecode(url))
                .uuid(uuid)
                .userid(StringUtils.defaultIfEmpty(vid, ""))
                .title(StringUtils.defaultIfEmpty(title, ""))
                .keywords(StringUtils.defaultIfEmpty(metaKeywords, ""))
                .resourceType(com.buzzinate.buzzads.thrift.AdEntryTypeEnum.findByValue(type))
                .resourceSize(com.buzzinate.buzzads.thrift.AdEntrySizeEnum.findByValue(AdEntrySizeEnum.findByText(StringUtils.defaultIfEmpty(width, ""), StringUtils.defaultIfEmpty(height, "")).getCode()))
                .netWork(
                        com.buzzinate.buzzads.thrift.AdNetworkEnum
                                .findByValue(network.getCode()))
                .ip(StringUtils.defaultIfEmpty(UserAgentUtil.getClientIp(request), ""))
                .count(count)
                .build();
        try {
            List<AdItem> itemList = advertiserClient.serve(param);
            return itemList;
        } catch (Exception e) {
            LOG.error("AdvertisingAction::getAdsResult error params is[" + param + "]", e);
        }
        return null;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public void setTi(String ti) {
        this.title = ti;
    }

    public void setRef(String ref) {
        this.referrer = ref;
    }

    public void setH(String h) {
        this.height = h;
    }

    public void setW(String w) {
        this.width = w;
    }

    public void setRt(int rt) {
        this.type = rt;
    }

    public void setMk(String mk) {
        this.metaKeywords = mk;
    }

    public void setMd(String md) {
        this.metaDescription = md;
    }

    public void setGe(int ge) {
        this.gender = ge;
    }

    public void setBd(String bd) {
        this.birthDate = bd;
    }

    public void setNa(int na) {
        this.nationality = na;
    }

    public void setLo(String lo) {
        this.location = lo;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    void setUid(String uid) {
        this.uid = uid;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getBuzzinfo() {
        return buzzinfo;
    }

    public void setBuzzinfo(String buzzinfo) {
        this.buzzinfo = buzzinfo;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    public void setS(String s) {
        this.s = s;
    }

    private List<BannerInfo> parseBannerInfo(AdBanner adBanner) {
        if (StringUtils.isEmpty(s)) {
            return null;
        } else {
            try {
                String[] ids = s.split(",");
                List<BannerInfo> bannerInfos = new ArrayList<BannerInfo>(ids.length);
                if (adBanner != null) {
                    for (int i = 0; i < ids.length; i++) {
                        if (StringUtils.isEmpty(ids[i])) continue;
                        BannerInfo bannerInfo = new BannerInfo();
                        bannerInfo.setId(ids[i]);
                        bannerInfo.setWidth(adBanner.getWidth());
                        bannerInfo.setHeight(adBanner.getHeight());
                        bannerInfo.setType(adBanner.getType().getCode());
                        bannerInfo.setNetwork(adBanner.getNetwork());
                        bannerInfos.add(bannerInfo);
                    }
                }
                return bannerInfos;
            } catch (Exception e) {
            }
        }
        return null;
    }
}
