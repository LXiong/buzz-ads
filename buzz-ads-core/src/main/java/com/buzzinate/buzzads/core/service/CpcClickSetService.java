package com.buzzinate.buzzads.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buzzinate.buzzads.analytics.AdClick;
import com.buzzinate.buzzads.core.bean.CpcTimeSegment;
import com.buzzinate.buzzads.core.util.RedisClient;
import com.buzzinate.buzzads.enums.AdNetworkEnum;

/**
 * 
 * @author zyeming
 *
 */
@Service
public class CpcClickSetService {

    @Autowired
    private RedisClient redisClient;
    
    
    /**
     * 添加一个广告点击
     * @param click
     */
    public void addCpcClick(AdClick click) {
        CpcTimeSegment seg;
        if (click.getCreateAt() == null) {
            seg = CpcTimeSegment.getCurrentSegment();
        } else {
            seg = CpcTimeSegment.getSegment(click.getCreateAt());
        }
        
        CpcMetaTuple metaTuple = new CpcMetaTuple((int) click.getAdEntryId(), 
                        click.getPublisherUuid(), click.getNetwork());
        
        String metaStr = convertCpcMetaKey(metaTuple);
        String cpcSetKey = "Cpc:" + seg.toCacheKey() + ":" + metaStr;
        redisClient.sadd(cpcSetKey, StringUtils.defaultIfEmpty(click.getCookieId(), ""));
        String cpcMetaSetKey = "CpcMeta:" + seg.toCacheKey();
        redisClient.sadd(cpcMetaSetKey, metaStr);
    }
    
    
    /**
     * 获取时间段内有Cpc点击的Meta组
     * @param seg
     * @return
     */
    public List<CpcMetaTuple> getMetaList(CpcTimeSegment seg) {
        Set<String> metaRawSet = redisClient.smembers("CpcMeta:" + seg.toCacheKey());
        List<CpcMetaTuple> metaList = new ArrayList<CpcMetaTuple>();
        for (String meta : metaRawSet) {
            CpcMetaTuple t = parseCpcMetaTuple(meta);
            if (t != null)
                metaList.add(t);
        }
        return metaList;
    }
    
    /**
     * 获取时间段内对应Meta组的Cpc点击数
     * @param seg
     * @param metaTuple
     * @return
     */
    public int getCpcClicks(CpcTimeSegment seg, CpcMetaTuple metaTuple) {
        String cpcCacheKey = "Cpc:" + seg.toCacheKey() + ":" + convertCpcMetaKey(metaTuple);
        return (int) redisClient.scard(cpcCacheKey);
    }
    
    /**
     * 从Meta集合中移除一个CpcMeta组
     * @param seg
     * @param metaTuple
     */
    public void remMeta(CpcTimeSegment seg, CpcMetaTuple metaTuple) {
        redisClient.srem("CpcMeta:" + seg.toCacheKey(), convertCpcMetaKey(metaTuple));
    }
    
    /**
     * 移除一个Cpc点击集合
     * @param seg
     * @param metaTuple
     */
    public void delCpcClicks(CpcTimeSegment seg, CpcMetaTuple metaTuple) {
        String cpcCacheKey = "Cpc:" + seg.toCacheKey() + ":" + convertCpcMetaKey(metaTuple);
        redisClient.delete(cpcCacheKey);
    }
    
    
    public CpcMetaTuple parseCpcMetaTuple(String meta) {
        String[] tuples = meta.split(":");
        if (tuples.length == 3) {
            try {
                int entryId = Integer.parseInt(tuples[0]);
                String publisherUuid = tuples[1];
                AdNetworkEnum network = AdNetworkEnum.valueOf(tuples[2]);
                CpcMetaTuple tuple =  new CpcMetaTuple(entryId, publisherUuid, network);
                return tuple;
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }
    
    public String convertCpcMetaKey(CpcMetaTuple tuple) {
        return tuple.getEntryId() + ":" + tuple.getPublisherUuid() + ":" + tuple.getNetwork().name();
    }
    
    public String convertCpcMetaKey(AdClick click) {
        return click.getAdEntryId() + ":" + click.getPublisherUuid() + ":" + click.getNetwork().name();
    }
    
    
    /**
     * 
     * @author zyeming
     *
     */
    public static final class CpcMetaTuple {
        private int entryId;
        private String publisherUuid;
        private AdNetworkEnum network;
        
        public CpcMetaTuple(int entryId, String publisherUuid, AdNetworkEnum network) {
            this.entryId = entryId;
            this.publisherUuid = publisherUuid;
            this.network = network;
        }

        public int getEntryId() {
            return entryId;
        }

        public String getPublisherUuid() {
            return publisherUuid;
        }

        public AdNetworkEnum getNetwork() {
            return network;
        }
    }
}
