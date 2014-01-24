package com.buzzinate.buzzads.core.service;

import com.buzzinate.buzzads.domain.Channel;
import com.buzzinate.buzzads.enums.IdType;
import com.buzzinate.buzzads.enums.PartnerEnum;
import com.buzzinate.buzzads.enums.PublisherSiteConfigType;
import com.buzzinate.buzzads.event.*;
import com.buzzinate.common.util.kestrel.KestrelClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 广告事件通知service
 *
 * @author Martin
 */
@Service
public class EventServices {
    private static final String AUDIENCE_QUEUE = "audience_event";
    private static final String ADEVENT_QUEUE = "ad_event";
    private static final String SITE_CONFIG_EVENT_QUEUE = "site_config";
    private static final String CHANNEL_QUEUE = "channel_event";
    private static Log log = LogFactory.getLog(EventServices.class);
    @Autowired
    private KestrelClient eventKestrelClient;

    /**
     * 发送创建广告通知
     *
     * @param id
     * @param idType
     * @return
     */
    public boolean sendAdCreateEvent(int id, IdType idType) {
        AdCreateEvent adcEvent = new AdCreateEvent();
        adcEvent.setId(id);
        adcEvent.setType(idType);
        try {
            eventKestrelClient.put(ADEVENT_QUEUE, adcEvent);
        } catch (Exception e) {
            log.error("Send ad create event error!", e);
            return false;
        }
        return true;
    }

    /**
     * 发送启用广告通知
     *
     * @param id
     * @param idType
     * @return
     */
    public boolean sendAdEnableEvent(int id, IdType idType) {
        AdEnableEvent adeEvent = new AdEnableEvent();
        adeEvent.setId(id);
        adeEvent.setType(idType);
        try {
            eventKestrelClient.put(ADEVENT_QUEUE, adeEvent);
        } catch (Exception e) {
            log.error("Send ad enable event error!", e);
            return false;
        }
        return true;
    }

    /**
     * 发送暂停广告通知
     *
     * @param id
     * @param idType
     * @return
     */
    public boolean sendAdPauseEvent(int id, IdType idType) {
        AdPauseEvent adpEvent = new AdPauseEvent();
        adpEvent.setId(id);
        adpEvent.setType(idType);
        try {
            eventKestrelClient.put(ADEVENT_QUEUE, adpEvent);
        } catch (Exception e) {
            log.error("Send ad pause event error!", e);
            return false;
        }
        return true;
    }

    /**
     * 发送禁用广告通知
     *
     * @param id
     * @param idType
     * @return
     */
    public boolean sendAdDisableEvent(int id, IdType idType) {
        AdDisableEvent addEvent = new AdDisableEvent();
        addEvent.setId(id);
        addEvent.setType(idType);
        try {
            eventKestrelClient.put(ADEVENT_QUEUE, addEvent);
        } catch (Exception e) {
            log.error("Send ad disable event error!", e);
            return false;
        }
        return true;
    }

    /**
     * 发送删除广告通知
     *
     * @param id
     * @param idType
     * @return
     */
    public boolean sendAdDeleteEvent(int id, IdType idType) {
        AdDeleteEvent addEvent = new AdDeleteEvent();
        addEvent.setId(id);
        addEvent.setType(idType);
        try {
            eventKestrelClient.put(ADEVENT_QUEUE, addEvent);
        } catch (Exception e) {
            log.error("Send ad delete event error!", e);
            return false;
        }
        return true;
    }

    /**
     * 发送修改广告通知
     *
     * @param id
     * @param idType
     * @return
     */
    public boolean sendAdModifyEvent(int id, IdType idType) {
        AdModifyEvent admEvent = new AdModifyEvent();
        admEvent.setId(id);
        admEvent.setType(idType);
        try {
            eventKestrelClient.put(ADEVENT_QUEUE, admEvent);
        } catch (Exception e) {
            log.error("Send ad modify event error!", e);
            return false;
        }
        return true;
    }

    /**
     * 发送挂起广告通知
     *
     * @param id
     * @param idType
     * @return
     */
    public boolean sendAdSuspendEvent(int id, IdType idType) {
        AdSuspendEvent adsEvent = new AdSuspendEvent();
        adsEvent.setId(id);
        adsEvent.setType(idType);
        try {
            eventKestrelClient.put(ADEVENT_QUEUE, adsEvent);
        } catch (Exception e) {
            log.error("Send ad suspend event error!", e);
            return false;
        }
        return true;
    }

    /**
     * 发送广告活动预算预警通知
     *
     * @param campaignIds
     * @return
     */
    public boolean sendCampaignBudgetWarnEvent(List<Integer> campaignIds) {
        if (campaignIds == null || campaignIds.isEmpty()) {
            return true;
        }

        CampaignBudgetWarnEvent event = new CampaignBudgetWarnEvent();
        event.setCampaignIds(campaignIds);
        try {
            eventKestrelClient.put(ADEVENT_QUEUE, event);
        } catch (Exception e) {
            log.error("Send ad budget warn event error!", e);
            return false;
        }
        return true;
    }

    /**
     * 发送广告活动预算改变通知
     *
     * @param campaignIds
     * @return
     */
    public boolean sendCampaignBudgetModifyEvent(List<Integer> campaignIds) {
        if (campaignIds == null || campaignIds.isEmpty()) {
            return true;
        }

        CampaignBudgetModifyEvent event = new CampaignBudgetModifyEvent();
        event.setCampaignIds(campaignIds);
        try {
            eventKestrelClient.put(ADEVENT_QUEUE, event);
        } catch (Exception e) {
            log.error("Send ad budget warn event error!", e);
            return false;
        }
        return true;
    }

    public boolean sendSiteConfigModifyEvent(String uuid, PublisherSiteConfigType type) {
        PublisherSiteConfigEvent event = new PublisherSiteConfigEvent();
        event.setUuid(uuid);
        event.setType(type);
        try {
            eventKestrelClient.put(SITE_CONFIG_EVENT_QUEUE, event);
        } catch (Exception e) {
            log.error("Send site config change event error!", e);
            return false;
        }
        return true;
    }

    public boolean sendChannelOpenEvent(Channel channel) {
        ChannelOpenEvent event = new ChannelOpenEvent();
        event.setUuid(channel.getUuid());
        event.setDomain(channel.getDomain());
        event.setLevel(channel.getLevel());
        try {
            eventKestrelClient.put(CHANNEL_QUEUE, event);
        } catch (Exception e) {
            log.error("Send channel open event error!", e);
            return false;
        }
        return true;
    }

    public boolean sendChannelCloseEvent(Channel channel) {
        ChannelCloseEvent event = new ChannelCloseEvent();
        event.setUuid(channel.getUuid());
        event.setDomain(channel.getDomain());
        event.setLevel(channel.getLevel());
        try {
            eventKestrelClient.put(CHANNEL_QUEUE, event);
        } catch (Exception e) {
            log.error("Send channel close event error!", e);
            return false;
        }
        return true;
    }

    public boolean sendChannelFrozenEvent(Channel channel) {
        ChannelFrozenEvent event = new ChannelFrozenEvent();
        event.setUuid(channel.getUuid());
        event.setDomain(channel.getDomain());
        event.setLevel(channel.getLevel());
        try {
            eventKestrelClient.put(CHANNEL_QUEUE, event);
        } catch (Exception e) {
            log.error("Send channel frozen event error!", e);
            return false;
        }
        return true;
    }

    public boolean sendChannelDeletedEvent(Channel channel) {
        ChannelDeletedEvent event = new ChannelDeletedEvent();
        event.setUuid(channel.getUuid());
        event.setDomain(channel.getDomain());
        event.setLevel(channel.getLevel());
        try {
            eventKestrelClient.put(CHANNEL_QUEUE, event);
        } catch (Exception e) {
            log.error("Send channel deleted event error!", e);
            return false;
        }
        return true;
    }

    public boolean sendAudienceFileReceivedEvent(long timestamp, PartnerEnum partnerEnum, int advertiserId, String data) {
        AudienceFileReceivedEvent event = new AudienceFileReceivedEvent(timestamp, partnerEnum, advertiserId, data);
        if (log.isDebugEnabled()) {
            log.debug("send event[" + event + "]");
        }
        try {
            eventKestrelClient.put(AUDIENCE_QUEUE, event);
        } catch (Exception e) {
            log.error("Send audience file download event error!", e);
            return false;
        }
        return true;
    }
}
