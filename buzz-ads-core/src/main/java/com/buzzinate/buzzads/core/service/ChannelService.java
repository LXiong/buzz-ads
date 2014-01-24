package com.buzzinate.buzzads.core.service;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.safehaus.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.com.bytecode.opencsv.CSVWriter;

import com.buzzinate.buzzads.analytics.stats.ChannelDailyStatistic;
import com.buzzinate.buzzads.common.exception.ServiceException;
import com.buzzinate.buzzads.core.dao.ChannelDao;
import com.buzzinate.buzzads.core.dao.PublisherContactInfoDao;
import com.buzzinate.buzzads.core.dao.SiteDao;
import com.buzzinate.buzzads.core.dao.StatsChannelDailyDao;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.core.util.RedisClient;
import com.buzzinate.buzzads.domain.Channel;
import com.buzzinate.buzzads.domain.PublisherContactInfo;
import com.buzzinate.buzzads.domain.Site;
import com.buzzinate.buzzads.enums.ChannelStatusEnum;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.string.StringUtil;

/**
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *         <p/>
 *         2013-5-13
 */
@Service
public class ChannelService {
    private static final String LOG_TAG = "ChannelService";
    private static final String[] CHANNEL_CVS_TITLE = new String[] {"uuid", "域名", "媒体类型", 
        "媒体类别", "网络类型", "日PV", "日点击", "日CTR", "状态", "开启时间", "关闭时间", "广告类型", "广告位类型", "最小CPM"};
    private static final int CHANNEL_CACHE_EXPIRE = 3600 * 24;
    private static Log log = LogFactory.getLog(ChannelService.class);
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private StatsChannelDailyDao channelDailyDao;
    @Autowired
    private SiteDao siteDao;
    @Autowired
    private PublisherContactInfoDao publisherContactInfoDao;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private EventServices eventServices;

    /**
     * admin媒体管理列表
     *
     * @return
     */
    public List<Channel> listChannel(Channel channel, Pagination page) {
        List<Channel> channels = listChannelWithoutStats(channel, page);
        if (channels.size() > 0) {
            Date yesterday = DateTimeUtil.getYestoday();
            for (Channel ch : channels) {
                //拉取前一天的数据
                ChannelDailyStatistic stats = channelDailyDao.getChannelDaily(ch.getId(), yesterday);
                if (stats != null) {
                    ch.setDailyViews(stats.getViews());
                    ch.setDailyClicks(stats.getClicks());
                }
                // 取得网站的站长id和mail
                Site siteInfo = siteDao.getUuidSiteByUuid(ch.getUuid());
                if (siteInfo != null) {
                    Integer userId = siteInfo.getUserId();
                    List<PublisherContactInfo> pubConInfoList = publisherContactInfoDao.findByUserIds(Arrays.asList(new Integer[]{userId}));
                    if (pubConInfoList.size() > 0) {
                        ch.setUserId(userId);
                        ch.setEmail(pubConInfoList.get(0).getEmail());
                    }
                }
            }
        }
        return channels;
    }
    
    public List<Channel> listChannelWithoutStats(Channel channel, Pagination page) {
        List<Channel> channels = channelDao.listChannels(channel, page);
        return channels;
    }
    
    public List<Channel> listAllChnanelsForAdv() {
        Channel query = new Channel();
        query.setLevel(1);
        query.setStatus(ChannelStatusEnum.OPENED);
        //查询所有一级媒体
        List<Channel> mainChannelList = listChannelWithoutStats(query, null);
        if (mainChannelList.size() > 0) {
            for (Channel channel : mainChannelList) {
                channel.setSubChannels(getSubChannelsByUUID(channel.getUuid()));
            }
        }
        return mainChannelList;
    }

    /**
     * 媒体总数
     *
     * @return
     */
    public long countChannels(ChannelStatusEnum[] status) {
        return channelDao.countChannelByStatus(status);
    }

    /**
     * 创建媒体
     *
     * @param channel
     */
    public void createChannel(Channel channel) {
        channelDao.create(channel);
        redisClient.delete(getChannelCacheKey(channel.getUuid()));
    }

    /**
     * admin更新媒体状态
     * <p>删除和冻结媒体需要向DB中录入操作理由</p>
     *
     * @param channelId
     * @param channelStatus
     * @param operateResult 操作缘由
     */
    public void updateChannelStatus(int channelId, ChannelStatusEnum channelStatus, String operateResult) {
        // 由于一二级媒体的状态变更对其它级别状态变更不一样   1级媒体状态会变更所有二级媒体，二级媒体只会变更其本身状态
        Channel channel = channelDao.read(channelId);
        if (channel == null) {
            throw new ServiceException("该媒体已不存在");
        } else {
            if (ChannelStatusEnum.DELETED == channel.getStatus()) {
                throw new ServiceException("该媒体已删除");
            }
            if (ChannelStatusEnum.FROZENED == channel.getStatus() && ChannelStatusEnum.FROZENED == channelStatus) {
                throw new ServiceException("该媒体已冻结");
            }
            if (ChannelStatusEnum.OPENED == channel.getStatus() && ChannelStatusEnum.OPENED == channelStatus) {
                throw new ServiceException("该媒体已解冻");
            }
        }

        if (channel.getLevel() == 1) {
            channelDao.update1stLvlChannelStatus(channel.getUuid(), channelStatus, operateResult);
        } else if (channel.getLevel() == 2) {
            channelDao.update2ndLvlChannelStatus(channelId, channelStatus, operateResult);
        }
        channel.setStatus(channelStatus);
        redisClient.delete(getChannelCacheKey(channel.getUuid()));
        fireChannelStatusChangeEvent(channel);
    }

    /**
     * 获取媒体详情
     *
     * @param channelId
     * @return
     */
    public Channel getChannelById(int channelId) {
        return channelDao.read(channelId);
    }

    @SuppressWarnings("unchecked")
    public List<Channel> listChannelByUuid(String uuid) {
        List<Channel> channels = (List<Channel>) redisClient.getObject(getChannelCacheKey(uuid));
        if (channels == null) {
            channels = channelDao.listChannelByUuid(StringUtil.uuidToByteArray(uuid));
            redisClient.set(getChannelCacheKey(uuid), CHANNEL_CACHE_EXPIRE, channels);
        }
        return channels;
    }

    private String getChannelCacheKey(String uuid) {
        return "Channel:" + uuid;
    }

    /**
     * 根据uuid和domain获取一个媒体
     *
     * @param uuid
     * @param domain
     * @return
     */
    public Channel getChannelByUUIDandDomain(String uuid, String domain) {
        if (StringUtils.isEmpty(uuid) || StringUtils.isEmpty(domain)) {
            return null;
        }
        UUID uuidx = new UUID(uuid);
        return channelDao.findChannelByUUIDandDomain(uuidx.toByteArray(), domain);
    }

    /**
     * 发送状态变化的事件
     *
     * @param channel
     */
    private void postChannelStatusEvent(Channel channel) {
        if (channel == null || StringUtils.isEmpty(channel.getUuid()) || StringUtils.isEmpty(channel.getDomain())) {
            return;
        }
        log.debug(LOG_TAG + ":" + "fire event[" + channel.getStatus() + "]");
        switch (channel.getStatus()) {
        case OPENED:
            eventServices.sendChannelOpenEvent(channel);
            break;
        case CLOSED:
            eventServices.sendChannelCloseEvent(channel);
            break;
        case FROZENED:
            eventServices.sendChannelFrozenEvent(channel);
            break;
        case DELETED:
            eventServices.sendChannelDeletedEvent(channel);
            break;
        default:
            break;
        }
    }

    /**
     * 触发状态变化事件发送
     * 依赖缓存，所以如果有刷新缓存的动作请放在该方法前
     *
     * @param channel
     */
    private void fireChannelStatusChangeEvent(Channel channel) {
        if (channel == null || StringUtils.isEmpty(channel.getUuid()) || StringUtils.isEmpty(channel.getDomain())) {
            return;
        }
        if (channel.getLevel() == 1) {
            List<Channel> channelList = listChannelByUuid(channel.getUuid());
            for (Channel ch : channelList) {
                postChannelStatusEvent(ch);
            }
        } else {
            postChannelStatusEvent(channel);
        }
    }

    @Transactional(value = "buzzads", readOnly = false)
    public void updateChannel(Channel srcChannel, Channel channel) {
        if (srcChannel.getStatus() == ChannelStatusEnum.DELETED) {
            //不变更已被deleted的媒体
            channel.setStatus(ChannelStatusEnum.DELETED);
            throw new ServiceException("媒体已被删除");
        }
        channel.setStatus(srcChannel.getStatus());
        channel.setId(srcChannel.getId());
        channel.setUuid(srcChannel.getUuid());
        channel.setLevel(srcChannel.getLevel());
        channelDao.update(channel);
        redisClient.delete(getChannelCacheKey(channel.getUuid()));
    }

    public Channel getTopChannelsByUUID(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            return null;
        }

        List<Channel> channelList = listChannelByUuid(uuid);
        if (channelList == null || channelList.isEmpty()) {
            return null;
        }
        Channel temp;
        for (Iterator<Channel> it = channelList.iterator(); it.hasNext(); ) {
            temp = it.next();
            if (temp.getLevel() == 1) {
                return temp;
            }
        }
        return null;
    }

    public List<Channel> getSubChannelsByUUID(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            return new ArrayList<Channel>(0);
        }
        List<Channel> channelList = listChannelByUuid(uuid);
        if (channelList == null || channelList.isEmpty()) {
            return new ArrayList<Channel>(0);
        }
        Channel temp;
        for (Iterator<Channel> it = channelList.iterator(); it.hasNext(); ) {
            temp = it.next();
            if (temp.getLevel() == 1 || temp.getStatus() != ChannelStatusEnum.OPENED) {
                it.remove();
            }
        }
        return channelList;
    }

    /**
     * 获取指定时间段内的平台分享及回流信息
     *
     * @param channelList
     * @return
     */
    public String buildChannelInfoCSV(List<Channel> channelList) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new OutputStreamWriter(new BufferedOutputStream(out)));
            writer.writeNext(CHANNEL_CVS_TITLE);
            for (Channel channel : channelList) {
                writer.writeNext(new String[]{
                        channel.getUuid(),
                        channel.getDomain(),
                        String.valueOf(channel.getLevel()),
                        channel.getType() == null ? "" : channel.getDisplayType(),
                        channel.getNetWork() == null ? "[]" : Arrays.toString(channel.getNetWork().toArray()),
                        String.valueOf(channel.getDailyViews()),
                        String.valueOf(channel.getDailyClicks()),
                        String.valueOf(channel.getDailyCTR()),
                        channel.getOpenTime() == null ? "" : channel.getOpenTime().toString(),
                        channel.getCloseTime() == null ? "" : channel.getCloseTime().toString(),
                        channel.getStatus() == null ? "" : channel.getStatus().name(),
                        channel.getAdType() == null ? "" : channel.getAdType().name(),
                        channel.getStyle() == null ? "[]" : Arrays.toString(channel.getStyle().toArray()),
                        channel.getMinCPMText()
                });
            }
        } catch (Exception e) {
            log.error("export top platform file error", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error("export top platform file close stream error", e);
                }
            }
        }
        return out.toString();
    }

    public List<Channel> findAllFrozenList() {
        return channelDao.listChannelByStatusAndType(ChannelStatusEnum.FROZENED, null, null);
    }
    
    public Channel getChannelByDomain(String domain) {
        return channelDao.findChannelByDomain(domain);
    }

    public List<Site> getSiteListBySiteName(String siteUrlSrh, Date dateStart, Date dateEnd, Pagination page, 
            String sortColumn, String sequence) {
        if (StringUtils.isEmpty(siteUrlSrh)) {
            siteUrlSrh = "";
        }
        // 排序默认按照comm ASC即收支降序排序
        if (StringUtils.isEmpty(sortColumn)) {
            sortColumn = "comm";
        }
        if (StringUtils.isEmpty(sequence)) {
            sequence = "DESC";
        }
        return channelDao.getSiteListBySiteName(siteUrlSrh, dateStart, dateEnd, page, sortColumn + " " + sequence);
    }

}