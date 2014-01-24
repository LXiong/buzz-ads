package com.buzzinate.buzzads.domain;

import java.io.Serializable;

/**
 * adx dsp
 *
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 *         2013-07-23
 */
public class AdxDspRtbStatistic implements Serializable {

    private static final long serialVersionUID = 2798112832569679533L;
    private int id;
    // 用户在系统获取到的key(目前为advertiserId)
    private Integer key;
    // 请求数量
    private Long requestCount;
    // 返回数量
    private Long responseCount;
    // 竞价成功数量
    private Long bidSuccCount;
    // 广告元素有效性数量
    private Long validCount;

    public AdxDspRtbStatistic(Integer key, Long requestCount,
            Long responseCount, Long bidSuccCount, Long validCount) {
        super();
        this.key = key;
        this.requestCount = requestCount;
        this.responseCount = responseCount;
        this.bidSuccCount = bidSuccCount;
        this.validCount = validCount;
    }
    
    public AdxDspRtbStatistic(Integer key, Integer requestCount,
            Integer responseCount, Integer bidSuccCount, Integer validCount) {
        super();
        this.key = key;
        this.requestCount = requestCount.longValue();
        this.responseCount = responseCount.longValue();
        this.bidSuccCount = bidSuccCount.longValue();
        this.validCount = validCount.longValue();
    }

    public int getId() {
        return id;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }

    public Long getResponseCount() {
        return responseCount;
    }

    public void setResponseCount(Long responseCount) {
        this.responseCount = responseCount;
    }

    public Long getBidSuccCount() {
        return bidSuccCount;
    }

    public void setBidSuccCount(Long bidSuccCount) {
        this.bidSuccCount = bidSuccCount;
    }

    public Long getValidCount() {
        return validCount;
    }

    public void setValidCount(Long validCount) {
        this.validCount = validCount;
    }

    public void setId(int id) {
        this.id = id;
    }

}
