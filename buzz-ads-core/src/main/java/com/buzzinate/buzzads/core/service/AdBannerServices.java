package com.buzzinate.buzzads.core.service;

import com.alibaba.fastjson.JSON;
import com.buzzinate.buzzads.common.service.AdsBaseService;
import com.buzzinate.buzzads.core.bean.AdBanner;
import com.buzzinate.buzzads.core.dao.AdBannerDao;
import com.buzzinate.buzzads.core.util.RedisClient;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.StringUtils;
import org.safehaus.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-7-24
 * Time: 下午1:52
 * admax uuid url services
 */
@Service
public class AdBannerServices extends AdsBaseService {
    private static final int UUID_URL_CACHE_EXPIRED_TIME = 3600 * 24;
    @Autowired
    private AdBannerDao adBannerDao;
    @Autowired
    private RedisClient redisClient;
    private LoadingCache<String, AdBanner> cookieCache = CacheBuilder.newBuilder()
            .expireAfterWrite(500, TimeUnit.SECONDS).maximumSize(100)
            .build(new CacheLoader<String, AdBanner>() {
                @Override
                public AdBanner load(String uuidStr) throws Exception {
                    String data = redisClient.get(getKey(uuidStr));
                    AdBanner adBanner = null;
                    if (StringUtils.isEmpty(data)) {
                        Map<String, Object> matcher = new HashMap<String, Object>();
                        matcher.put("uuid", new UUID(uuidStr).asByteArray());
                        List<AdBanner> results = adBannerDao.query(matcher);
                        if (!results.isEmpty()) {
                            adBanner = results.get(0);
                            adBanner.setUuid(null);
                            redisClient.set(getKey(uuidStr), UUID_URL_CACHE_EXPIRED_TIME, JSON.toJSONString(adBanner));
                        }
                    } else {
                        try {
                            adBanner = JSON.parseObject(data, AdBanner.class);
                            return adBanner;
                        } catch (Exception e) {
                            return null;
                        }
                    }
                    return adBanner;
                }
            });

    public AdBanner getUrlByUuid(String uuidStr) {
        try {
            return cookieCache.get(uuidStr);
        } catch (Exception e) {
            return null;
        }
    }

    private String getKey(String uuidStr) {
        return "AdBanner::" + uuidStr;
    }

}
