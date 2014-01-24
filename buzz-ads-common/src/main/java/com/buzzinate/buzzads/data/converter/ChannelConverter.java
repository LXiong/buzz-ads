package com.buzzinate.buzzads.data.converter;

import com.buzzinate.buzzads.data.thrift.TFrozenChannel;
import com.buzzinate.buzzads.domain.Channel;

/**
 * 
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 *
 */
public final class ChannelConverter {
    
    private ChannelConverter() { }
    
    
    /**
     * Convert from Channel domain object to thrift object
     * @param info
     * @return
     */
    public static TFrozenChannel toThrift(Channel channel) {
        return new TFrozenChannel.Builder().
                        uuid(channel.getUuid()).
                        channelLevel(channel.getLevel()).
                        domain(channel.getDomain()).
                        build();
    }
    
    
    /**
     * Convert form thrift object to Channel domain object
     * @param tInfo
     * @return
     */
    public static Channel fromThrift(TFrozenChannel tInfo) {
        Channel info = new Channel();
        mergeThrift(info, tInfo);
        return info;
    }
    
    
    /**
     * Merge thrift object into existing Channel
     * @param info
     * @param tInfo
     * @return
     */
    public static void mergeThrift(Channel info, TFrozenChannel tInfo) {
        info.setUuid(tInfo.getUuid());
        info.setLevel(tInfo.getChannelLevel());
        info.setDomain(tInfo.getDomain());
    }

}
