package com.buzzinate.adx.cache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.buzzinate.adx.CacheConstants;
import com.buzzinate.adx.util.JdbcUtils;
import com.buzzinate.adx.util.RedisUtils;
import com.buzzinate.buzzads.core.util.RedisClient;
import com.buzzinate.buzzads.domain.AdxDspRtbInfo;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author james.chen
 *
 */
@SuppressWarnings("unchecked")
public class DspInfoCache {
    
    private static RedisClient client = RedisUtils.getInstance();
    
    private static JdbcTemplate template = JdbcUtils.getInstance();
    private static Log log = LogFactory.getLog(DspInfoCache.class);
    
    private static final int DEFAULT_CACHE_SIZE = 100;
    private static final String RTB = "rtb";

    private LoadingCache<String, List<AdxDspRtbInfo>> dspCache = CacheBuilder.newBuilder()
            .expireAfterWrite(CacheConstants.EXPIRE_HALF_MINUTE, TimeUnit.SECONDS).maximumSize(DEFAULT_CACHE_SIZE)
            .build(new CacheLoader<String, List<AdxDspRtbInfo>>() {
                @Override
                public List<AdxDspRtbInfo> load(String info) throws Exception {
                    // first get it from redis
                    String key = CacheConstants.KEY_DSP_INFO_CACHE + info;

                    List<AdxDspRtbInfo> dsps = (List<AdxDspRtbInfo>) client.getObject(key);
                    if (dsps == null) {
                        dsps = getDspFromDb();
                        client.set(key, CacheConstants.EXPIRE_ONE_DAY, dsps);
                    }
                    return dsps;
                }
            });
    
    /**
     * getInfo from DB
     * @return
     */
    public List<AdxDspRtbInfo> getDspFromDb() {
        List<AdxDspRtbInfo> results =
                template.query("SELECT * FROM adx_dsp_rtb_info WHERE RTB =1", new RowMapper<AdxDspRtbInfo>() {
                    @Override
                    public AdxDspRtbInfo mapRow(ResultSet rs , int rowNum) throws SQLException {
                        AdxDspRtbInfo rtbInfo = new AdxDspRtbInfo();
                        rtbInfo.setId(rs.getInt("ID"));
                        rtbInfo.setKey(rs.getInt("KEY"));
                        rtbInfo.setWinnerNotifyUrl(rs.getString("WINNERNOTIFYURL"));
                        rtbInfo.setBidRequestUrl(rs.getString("BIDREQUESTURL"));
                        rtbInfo.setRtb(Boolean.TRUE);
                        return rtbInfo;
                    }
                });
        return results;
    }
    
    /**
     * get RTB DSP
     * @return
     */
    public List<AdxDspRtbInfo> getRtbDsps() {
        List<AdxDspRtbInfo> dsps = new ArrayList<AdxDspRtbInfo>();
        try {
            dsps = dspCache.get(RTB);
        } catch (ExecutionException e) {
            log.error("system didn't find any RTB", e);
        }
        return dsps;
    }
    
}
