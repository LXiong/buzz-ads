package com.buzzinate.buzzads.action;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.analytics.AdAnalyticsConstants;
import com.buzzinate.buzzads.analytics.AdClick;
import com.buzzinate.buzzads.core.service.SiteService;
import com.buzzinate.buzzads.core.util.RedisClient;
import com.buzzinate.buzzads.domain.Site;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.common.util.ApiUtil;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.http.UserAgentUtil;
import com.buzzinate.common.util.kestrel.KestrelClient;
import com.buzzinate.common.util.serialize.HessianSerializer;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 
 * 点击302跳转
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2013-4-9
 */
@Controller
@Scope("request")
public class ClickAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private static final long serialVersionUID = -791762368916141427L;
    
    private static final Log LOG = LogFactory.getLog(ClickAction.class);
    //站长id
    private String uuid;
    //广告id
    private String eid;
    //目标地址
    private String url;
    //随机密钥
    private String z;
    //广告网络
    private String nw;
    //对应lezhi、weijifen的cookieId
    private String vid;
    //对应lezhi、微积分的用户Id
    private String uid;
    //链接生成时的时间戳
    private String ts;
    //签名
    private String sig;
    
    private String p;
    
    private String link;
    
    private HttpServletResponse response;
    private HttpServletRequest request;
    
    @Autowired
    private transient SiteService siteService;
    
    @Autowired
    private transient RedisClient redisClient;
    @Autowired
    private transient KestrelClient analyticsKestrelClient;

    
    @Override
    public String execute() {
        // 处理点击
        if (StringUtils.isBlank(p) || StringUtils.isBlank(link)) {
          //记录日志
            LOG.info("Bad Parameter Click:" + request.getQueryString());
            return redirect301();
        }
        //解析p值
        if (!getRealParam()) 
            return redirect301();
        
        //过期直接返回:展示时间与点击时间相差超过20分钟的点击作为过期
        long current = DateTimeUtil.getCurrentDate().getTime();
        long linkTime = Long.valueOf(ts).longValue();
        if ((current - linkTime) > (20 * 60 * 1000)) {
            //记录日志
            LOG.info("Expire Click:" + request.getQueryString());
            return redirect301();
        }
        
        //签名不正确，直接返回
        Site site = siteService.getUuidSiteByUuid(uuid);
        if (site == null || StringUtils.isBlank(site.getSecret())) {
            return redirect301();
        } else {
            HashMap<String, String> infoMap = new HashMap<String, String>();
            infoMap.put("uuid", uuid);
            infoMap.put("url", url);
            infoMap.put("z", z);
            infoMap.put("eid", eid);
            infoMap.put("ts", ts);
            String sigStr = ApiUtil.encryptForApi(infoMap, site.getSecret());
            if (!sig.equals(sigStr)) {
                //记录日志
                LOG.info("Wrong sig:" + request.getQueryString());
                return redirect301();
            }
        }
        
        //z参数不正确
        String zKey = "z:" + z;
        Object check =  redisClient.getObject(zKey);
        if (check == null) {
            // 记录日志
            LOG.info("No z Click View:" + request.getQueryString());
            return redirect301();
        } 
        redisClient.delete(zKey);
        
        //正确：计算点击
        AdClick adClick = new AdClick();
        adClick.setPublisherUuid(uuid);
        adClick.setAdEntryId(Long.valueOf(eid).longValue());
        adClick.setCookieId(vid);
        adClick.setNetwork(AdNetworkEnum.findByAbbr(nw));
        adClick.setCreateAt(new Date(current));
        adClick.setUa(request.getHeader("User-Agent"));
        adClick.setIp(UserAgentUtil.getClientIp(request));
        
        analyticsKestrelClient.put(AdAnalyticsConstants.ADCLICK_QUEUE, HessianSerializer.serialize(adClick));
        //301跳转
        return redirect301();
    }

    private boolean getRealParam() {
        try {
            String[] ss = Base64.decode(p).split("&");
            for (String s : ss) {
                String[] kv = s.split("=");
                if ("uuid".equals(kv[0]))
                    this.uuid = kv[1];
                if ("eid".equals(kv[0]))
                    this.eid = kv[1];
                if ("z".equals(kv[0]))
                    this.z = kv[1];
                if ("url".equals(kv[0]))
                    this.url = StringUtils.isBlank(kv[1]) ? "" : kv[1];
                if ("ts".equals(kv[0]))
                    this.ts = kv[1];
                if ("sig".equals(kv[0]))
                    this.sig = kv[1];
                if ("nw".equals(kv[0]))
                    this.nw = StringUtils.isBlank(kv[1]) ? "" : kv[1];
                if ("vid".equals(kv[0]))
                    this.vid = StringUtils.isBlank(kv[1]) ? "" : kv[1];
                if ("uid".equals(kv[0]))
                    this.uid = StringUtils.isBlank(kv[1]) ? "" : kv[1];
            }
        } catch (Exception e) {
            LOG.error("Click Param Error", e);
            return false;
        }
        
        // 参数不完整直接返回
        if (StringUtils.isBlank(uuid) || StringUtils.isBlank(eid) || 
                StringUtils.isBlank(z) || StringUtils.isBlank(ts) || StringUtils.isBlank(sig)) {
            // 记录日志
            LOG.info("Bad Parameter Click:" + request.getQueryString());
            return false;
        }
        
        return true;
    }

    private String redirect301() {
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", getLink());
        response.setHeader("Cache-Control", "no-cache");
        return null;
    }

    public String getUuid() {
        return uuid;
    }


    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public String getEid() {
        return eid;
    }


    public void setEid(String eid) {
        this.eid = eid;
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getZ() {
        return z;
    }


    public void setZ(String z) {
        this.z = z;
    }


    public String getNw() {
        return nw;
    }


    public void setNw(String nw) {
        this.nw = nw;
    }


    public String getVid() {
        return vid;
    }


    public void setVid(String vid) {
        this.vid = vid;
    }


    public String getUid() {
        return uid;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getTs() {
        return ts;
    }


    public void setTs(String ts) {
        this.ts = ts;
    }


    public String getSig() {
        return sig;
    }


    public void setSig(String sig) {
        this.sig = sig;
    }

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
