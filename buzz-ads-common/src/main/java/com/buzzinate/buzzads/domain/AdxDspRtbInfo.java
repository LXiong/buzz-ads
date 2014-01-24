package com.buzzinate.buzzads.domain;

import java.io.Serializable;

/**
 * adx dsp
 *
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 *         2013-07-23
 */
public class AdxDspRtbInfo implements Serializable {

    private static final long serialVersionUID = 2798112832569679533L;
    private int id;
    // 用户在系统获取到的key(目前为advertiserId)
    private Integer key;
    // 接收bidResponse最终结果的地址
    private String winnerNotifyUrl;
    // 接收bidRequest的地址
    private String bidRequestUrl;
    //is accept RTB
    private boolean rtb;
    
    public AdxDspRtbInfo(Integer key, String winnerNotifyUrl,
            String bidRequestUrl) {
        super();
        this.key = key;
        this.winnerNotifyUrl = winnerNotifyUrl;
        this.bidRequestUrl = bidRequestUrl;
    }

    public AdxDspRtbInfo() {
        super();
    }

    public String getBidRequestUrl() {
        return bidRequestUrl;
    }
    
    public void setBidRequestUrl(String bidRequestUrl) {
        this.bidRequestUrl = bidRequestUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getWinnerNotifyUrl() {
        return winnerNotifyUrl;
    }

    public void setWinnerNotifyUrl(String winnerNotifyUrl) {
        this.winnerNotifyUrl = winnerNotifyUrl;
    }

    public boolean isRtb() {
        return rtb;
    }

    public void setRtb(boolean rtb) {
        this.rtb = rtb;
    }

}
