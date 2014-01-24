package com.buzzinate.buzzads.core.service;

import com.buzzinate.buzzads.common.exception.ServiceException;
import com.buzzinate.buzzads.common.service.AdsBaseService;
import com.buzzinate.buzzads.core.dao.AdEntryDao;
import com.buzzinate.buzzads.core.util.LogConstants;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.core.util.RedisClient;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdEntry;
import com.buzzinate.buzzads.domain.AdOrder;
import com.buzzinate.buzzads.domain.AdvertiserAccount;
import com.buzzinate.buzzads.enums.AdEntryTypeEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.IdType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author harry
 */
@Service
public class AdEntryService extends AdsBaseService {

    private static final int ENTRY_CACHE_EXPIRE = 3600 * 24;
    @Autowired
    private AdEntryDao adEntryDao;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private EventServices eventServices;
    @Autowired
    private AdvertiserAccountService advertiserAccountService;
    @Autowired
    private AdOrderService adOrderService;
    @Autowired
    private AdCampaignService adCampaignService;
    @Autowired
    private AdminEmailService adminEmailService;

    /**
     * 更新广告链接
     *
     * @param orderId
     * @param link
     * @return
     */
    @Transactional(value = "buzzads", readOnly = false)
    public void updateLinkByOrderId(int orderId, String link) {
        if (StringUtils.isBlank(link)) {
            throw new ServiceException("广告信息不完整");
        }
        List<AdEntry> entries = listEntriesByOrderId(orderId);
        for (AdEntry entry : entries) {
            if (!link.equals(entry.getLink())) {
                entry.setLink(link);
                saveOrUpdate(entry);
            }
        }
    }

    /**
     * 创建或更新广告的信息
     * 广告状态的更新需要调用 updateEntryStatus方法
     *
     * @param entry
     * @return
     */
    public void saveOrUpdate(AdEntry entry) {
        int originalId = entry.getId();
        validate(entry);
        adEntryDao.saveOrUpdate(entry);
        redisClient.set(getAdEntryCacheKey(entry.getId()), ENTRY_CACHE_EXPIRE, entry);
        //发送状态改变消息
        if (originalId == 0) {
            eventServices.sendAdCreateEvent(entry.getId(), IdType.ENTRY);
        } else {
            eventServices.sendAdModifyEvent(entry.getId(), IdType.ENTRY);
        }
        entry.setAdvertiserName(advertiserAccountService.getAdvertiserAccount(
                entry.getAdvertiserId()).getCompanyName());
        adminEmailService.sendAdEntryVerifyEmail(entry);
    }

    /**
     * 更新广告的状态, 所有需要更新广告状态的动作 只应调用此方法
     *
     * @param entryId
     * @param status
     */
    public void updateEntryStatus(int entryId, AdStatusEnum status) {
        adEntryDao.updateEntryStatus(entryId, status);
        sendStatusChangeEvent(entryId, status);
        redisClient.delete(getAdEntryCacheKey(entryId));
        //添加必要的操作日志
    }

    /**
     * admin对广告状态的操作
     *
     * @param entryId
     * @param status
     * @param advertiserName
     */
    public void adminOperate(int entryId, AdStatusEnum status, int advertiserId) {
        AdEntry ad = getEntryById(entryId);
        updateEntryStatus(entryId, status);
        //添加操作日志
        Map<String, String> params = new HashMap<String, String>();
        params.put("adEntryId", String.valueOf(entryId));
        params.put("advertiserId", String.valueOf(advertiserId));
        if (status == AdStatusEnum.ENABLED) {
            this.addOperationLog(LogConstants.OPTYPE_ADENTRY_ACCEPT, LogConstants.OPNAME_ADENTRY_ACCEPT,
                    String.valueOf(advertiserId), String.valueOf(entryId), params);
        } else if (status == AdStatusEnum.REJECTED) {
            this.addOperationLog(LogConstants.OPTYPE_ADENTRY_REJECT, LogConstants.OPNAME_ADENTRY_REJECT,
                    String.valueOf(advertiserId), String.valueOf(entryId), params);
        } else {
            params.put("preStatus", ad.getStatusName());
            params.put("targetStatus", AdStatusEnum.getCnName(status));
            this.addOperationLog(LogConstants.OPTYPE_ADENTRY_OPERATE, LogConstants.OPNAME_ADENTRY_OPERATE,
                    String.valueOf(advertiserId), String.valueOf(entryId), params);
        }
    }

    /**
     * 根据ID获取广告
     *
     * @param entryId
     * @return
     */
    public AdEntry getEntryById(int entryId) {
        AdEntry entry = (AdEntry) redisClient.getObject(getAdEntryCacheKey(entryId));
        if (entry == null) {
            entry = adEntryDao.read(entryId);
            if (entry != null) {
                redisClient.set(getAdEntryCacheKey(entryId), ENTRY_CACHE_EXPIRE, entry);
            }
        }
        return entry;
    }

    /**
     * 根据条件查询广告
     *
     * @param adEntry
     * @param page
     * @return
     */
    public List<AdEntry> listAdEntries(AdEntry adEntry, Pagination page) {
        return adEntryDao.listAdEntries(adEntry, page);
    }

    /**
     * 根据上层活动和组的状态 一级本身的搜素条件查询广告
     *
     * @param adEntry
     * @param page
     * @return
     */
    public List<AdEntry> listAdEntriesByUpperStatus(AdEntry adEntry, Pagination page) {
        List<AdEntry> allEntries = adEntryDao.listAdEntries(adEntry);
        List<AdEntry> entries = new ArrayList<AdEntry>();
        for (AdEntry entry : allEntries) {
            AdOrder order = adOrderService.getOrderById(entry.getOrderId());
            AdCampaign camp = adCampaignService.getAdCampaignByIdWithoutBudget(entry.getCampaignId());
            //upperStatus应该是Enabled状态 
            if (camp != null && order != null &&
                    camp.getStatus().equals(AdStatusEnum.ENABLED) &&
                    order.getStatus().equals(AdStatusEnum.ENABLED)) {
                entries.add(entry);
            }
        }
        // 分页处理
        page.setTotalRecords(entries.size());

        int fromIndex = (page.getPageNum() > 0 ? page.getPageNum() - 1 : page.getPageNum()) * page.getPageSize();
        if (fromIndex > entries.size()) {
            entries = new ArrayList<AdEntry>();
        } else {
            int toIndex = Math.min((page.getPageNum()) * page.getPageSize(), entries.size());
            entries = entries.subList(fromIndex, toIndex);
        }
        return entries;
    }

    public int getActiveEntriesCount() {
        List<AdEntry> allEntries = adEntryDao.listEntriesByStatus(AdStatusEnum.ENABLED);
        List<AdEntry> entries = new ArrayList<AdEntry>();
        for (AdEntry entry : allEntries) {
            AdOrder order = adOrderService.getOrderById(entry.getOrderId());
            AdCampaign camp = adCampaignService.getAdCampaignByIdWithoutBudget(entry.getCampaignId());
            //upperStatus应该是Enabled状态 
            if (camp != null && order != null &&
                    camp.getStatus().equals(AdStatusEnum.ENABLED) &&
                    order.getStatus().equals(AdStatusEnum.ENABLED)) {
                entries.add(entry);
            }
        }
        return entries.size();
    }

    /**
     * 所有广告数量(不包括 DELETED)
     *
     * @return
     */
    public int getAllAdEntriesCount() {
        return adEntryDao.getAllAdEntriesCount();
    }

    /**
     * 根据广告主列出未验证广告
     * 广告主id为0返回所有未验证广告
     *
     * @param adEntry
     * @param page
     * @return
     */
    public List<AdEntry> listAdEntriesByStatus(int advertiserId, AdStatusEnum status, Pagination page) {
        List<AdEntry> entries = adEntryDao.getAdEntriesByStatus(advertiserId, status, page);
        for (AdEntry entry : entries) {
            AdvertiserAccount account = advertiserAccountService.getAdvertiserAccount(entry.getAdvertiserId());
            if (account != null) {
                entry.setAdvertiserName(account.getCompanyName());
            }
        }
        return entries;
    }

    /**
     * 获取广告订单下的广告列表
     *
     * @param adOrderId
     * @return
     */
    public List<AdEntry> listEntriesByOrderId(int adOrderId) {
        return adEntryDao.listEntriesByOrderId(adOrderId);
    }

    /**
     * 获取广告活动下的广告列表
     *
     * @param adOrderId
     * @return
     */
    public List<AdEntry> listEntriesByCampaignId(int campaignId) {
        return adEntryDao.listEntriesByCampaignId(campaignId);
    }

    public List<AdEntry> listActiveEntriesByAdvertiserId(int advertiserId, List<Integer> activeOrderIds) {
        return adEntryDao.listActiveEntriesByAdvertiserId(advertiserId, activeOrderIds);
    }

    /**
     * 获取多个广告订单下的广告列表
     *
     * @param adOrderIds
     * @return
     */
    public List<AdEntry> listEntriesByOrderIds(List<Integer> adOrderIds) {
        return adEntryDao.listEntriesByOrderIds(adOrderIds);
    }

    /**
     * 根据标题查询广告
     *
     * @param title
     * @return 结果Map，健为AdEntryID，值为Title。
     */
    public Map<Integer, String> searchAdsByTitle(String title) {
        return adEntryDao.searchAdsByTitle(title);
    }

    /**
     * 获取广告
     *
     * @param entryIds
     * @return 结果Map，健为AdEntryID，值为Title。
     */
    public Map<Integer, String> getAdsByIds(List<Integer> entryIds) {
        return adEntryDao.getAdsByIds(entryIds);
    }

    private void validate(AdEntry entry) {
        if (entry == null ||
                entry.getOrderId() <= 0 ||
                entry.getCampaignId() <= 0 ||
                entry.getAdvertiserId() <= 0 ||
                (entry.getResourceType() != AdEntryTypeEnum.Flash
                        && (StringUtils.isBlank(entry.getLink()) || StringUtils.isBlank(entry.getName())))) {
            throw new ServiceException("广告信息不完整");
        }
    }

    private String getAdEntryCacheKey(int entryId) {
        return "Entry:" + entryId;
    }

    //发送状态改变消息
    private void sendStatusChangeEvent(int entryId, AdStatusEnum status) {
        switch (status) {
            case ENABLED:
                eventServices.sendAdEnableEvent(entryId, IdType.ENTRY);
                break;
            case PAUSED:
                eventServices.sendAdPauseEvent(entryId, IdType.ENTRY);
                break;
            case DISABLED:
                eventServices.sendAdDisableEvent(entryId, IdType.ENTRY);
                break;
            case DELETED:
                eventServices.sendAdDeleteEvent(entryId, IdType.ENTRY);
                break;
            case SUSPENDED:
                eventServices.sendAdSuspendEvent(entryId, IdType.ENTRY);
                break;
            default:
                break;
        }
    }

    public long getAdEntryCountByCampaignId(int id) {
        return adEntryDao.getAdEntryCountByCampaignId(id);
    }
}
