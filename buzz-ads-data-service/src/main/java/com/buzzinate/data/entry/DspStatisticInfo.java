package com.buzzinate.data.entry;

import com.buzzinate.adx.enums.BidStatus;

/**
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 *      2013-7-24
 *      
 * */
public class DspStatisticInfo {
    private Integer key;
    // 请求数量
    private Integer requestCount;
    // 返回数量
    private Integer responseCount;
    // 竞价成功数量
    private Integer bidSuccCount;
    // 广告元素有效性数量
    private Integer validCount;
    
    public DspStatisticInfo(Integer key, Integer requestCount,
            Integer responseCount, Integer bidSuccCount, Integer validCount) {
        super();
        this.key = key;
        this.requestCount = requestCount;
        this.responseCount = responseCount;
        this.bidSuccCount = bidSuccCount;
        this.validCount = validCount;
    }
    
    public DspStatisticInfo(Integer key) {
        this(key, 0, 0, 0, 0);
    }
    
    public void mergeCountInfo(DspStatisticInfo mergeInfo) {
        this.requestCount += mergeInfo.getRequestCount();
        this.responseCount += mergeInfo.getResponseCount();
        this.validCount += mergeInfo.getValidCount();
        this.bidSuccCount += mergeInfo.getBidSuccCount();
    }
    
    // count the msg via msg type
    public void increaseMsgCount(BidStatus status) {
        // NO_BID处理nashi
        switch (status)
        {
            case SUCCESS :
                requestCount ++;
                responseCount ++;
                bidSuccCount ++;
                validCount ++;
                break;
            case SEGEMNT_FAIL : // 校验失败  在竞价成功之后进行校验
                requestCount ++;
                responseCount ++;
                bidSuccCount ++;
                break;
            case FAILURE :
                requestCount ++;
                responseCount ++;
                break;
            case TIMEOUT :
                requestCount ++;
                break;
            default :
                break;
        }
    }

    public Integer getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }

    public Integer getResponseCount() {
        return responseCount;
    }

    public void setResponseCount(Integer responseCount) {
        this.responseCount = responseCount;
    }

    public Integer getValidCount() {
        return validCount;
    }

    public void setValidCount(Integer validCount) {
        this.validCount = validCount;
    }

    public Integer getBidSuccCount() {
        return bidSuccCount;
    }

    public void setBidSuccCount(Integer bidSuccCount) {
        this.bidSuccCount = bidSuccCount;
    }

    // 响应率
    public Integer getResponsePercent() {
        if (requestCount == 0) {
            return 0;
        }
        return responseCount * 100 / requestCount;
    }
    
    // 广告素材有效性
    public Integer getValidPercent() {
        if (requestCount == 0) {
            return 0;
        }
        return validCount * 100 / requestCount;
    }

    public Integer getWinnerPercent() {
        if (requestCount == 0) {
            return 0;
        }
        return bidSuccCount * 100 / requestCount;
    }
    
    public Integer getSuccessPercent() {
        return getResponsePercent() * getValidPercent() / 100;
    }
    
    public Integer getFailPercent() {
        return 100 - getSuccessPercent();
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "DspInfo{" +
                "key=" + key + 
                ", requestCount=" + requestCount +
                ", responseCount=" + responseCount +
                ", validCount=" + validCount +
                ", bidSuccCount=" + bidSuccCount +
                '}';
    }

}
