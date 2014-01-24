package com.buzzinate.adx.message;

import com.buzzinate.adx.entity.AdInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: kun Date: 13-7-3 Time: 上午9:59 竞价胜利对象
 *
 * @author kun
 */
public final class WinnerMessage implements Serializable {
    private static final long serialVersionUID = -1363308035658364179L;
    private final List<AdInfo> winners;
    private final List<AdInfo> segmentlosers;
    private final BidResponseMessage bidResponseMessage;

    public WinnerMessage(List<AdInfo> winners, List<AdInfo> losers, BidResponseMessage bidResponseMessage) {
        this.winners = winners;
        this.segmentlosers = losers;
        this.bidResponseMessage = bidResponseMessage;
    }

    public BidResponseMessage getBidResponseMessage() {
        return bidResponseMessage;
    }

    public List<AdInfo> getWinners() {
        return winners;
    }

    public List<AdInfo> getSegmentlosers() {
        return segmentlosers;
    }
    
    public String getSendPath() {
        return bidResponseMessage.getSendPath();
    }

    public boolean hasWiner() {
        return winners != null && winners.size() > 0;
    }

    @Override
    public String toString() {
        return "WinnerMessage{" + "winners=" + winners + ", bidResponseMessage=" + bidResponseMessage + '}';
    }
}
