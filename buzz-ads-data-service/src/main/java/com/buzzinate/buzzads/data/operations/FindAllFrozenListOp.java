package com.buzzinate.buzzads.data.operations;

import java.util.ArrayList;
import java.util.List;

import com.buzzinate.buzzads.core.service.ChannelService;
import com.buzzinate.buzzads.data.converter.ChannelConverter;
import com.buzzinate.buzzads.data.thrift.TFrozenChannel;
import com.buzzinate.buzzads.domain.Channel;
import com.twitter.util.ExceptionalFunction0;

/**
 * 
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 *
 * 2012-12-13
 */
public class FindAllFrozenListOp extends ExceptionalFunction0<List<TFrozenChannel>> {
    
    private ChannelService channelService;
    
    
    public FindAllFrozenListOp(ChannelService channelService) {
        this.channelService = channelService;
    }
    
    
    @Override
    public List<TFrozenChannel> applyE() throws Exception {
        List<Channel> channelList = channelService.findAllFrozenList();
        List<TFrozenChannel> resList = new ArrayList<TFrozenChannel>();
        for (Channel tmp : channelList) {
            resList.add(ChannelConverter.toThrift(tmp));
        }
        return resList;
    }
    
}