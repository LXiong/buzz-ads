package com.buzzinate.buzzads.core.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.exception.ServiceException;
import com.buzzinate.buzzads.common.service.AdsBaseService;
import com.buzzinate.buzzads.core.dao.AdCampaignDao;
import com.buzzinate.buzzads.core.dao.CampDayBudgetDao;
import com.buzzinate.buzzads.core.util.LogConstants;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.core.util.RedisClient;
import com.buzzinate.buzzads.domain.AdCampBudget;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdOrder;
import com.buzzinate.buzzads.domain.AdvertiserAccount;
import com.buzzinate.buzzads.domain.CampaignDayBudget;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.buzzads.enums.IdType;

/**
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *         <p/>
 *         2013-2-28
 */
@Service
public class AdCampaignService extends AdsBaseService {

    private static final int CAMPAIGN_CACHE_EXPIRE = 3600 * 24;
    @Autowired
    private AdCampaignDao campaignDao;
    @Autowired
    private AdCampaignBudgetService adCampaignBudgetService;
    @Autowired
    private AdOrderService adOrderService;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private EventServices eventServices;
    @Autowired
    private AdvertiserAccountService accountService;
    @Autowired
    private AdvertiserBalanceService advertiserBalanceService;
    @Autowired
    private CampDayBudgetDao campDayBudgetDao;
    @Autowired
    private AdEntryService adEntryService;

    @Transactional(value = "buzzads", readOnly = false)
    public int createAdCampaign(AdCampaign campaign, AdCampBudget budget) {
        validate(campaign);
        int id = campaignDao.create(campaign);
        redisClient.set(getAdCampaignCacheKey(id), CAMPAIGN_CACHE_EXPIRE, campaign);

        budget.setCampaignId(id);
        adCampaignBudgetService.createCampBudget(budget);
        //添加每日预算
        List<CampaignDayBudget> budgets = campaign.getDayBudgets();
        if (budgets != null && budgets.size() > 0) {
            for (CampaignDayBudget dayBudget : budgets) {
                dayBudget.setCampId(id);
                campDayBudgetDao.create(dayBudget);
            }
        }
        //发送创建消息
        eventServices.sendAdCreateEvent(id, IdType.CAMPAIGN);
        
        return id;
    }

    /**
     * 更新广告活动的信息
     * 状态的更新需要调用 updateAdCampaignStatus方法
     *
     * @param campaign
     */
    @Transactional(value = "buzzads", readOnly = false)
    public void saveOrUpdate(AdCampaign campaign) {
        int originalId = campaign.getId();
        validate(campaign);
        campaignDao.saveOrUpdate(campaign);
        //添加每日预算
        List<CampaignDayBudget> budgets = campaign.getDayBudgets();
        campDayBudgetDao.deleteByCampaignId(originalId);
        if (budgets != null && budgets.size() > 0) {
            for (CampaignDayBudget dayBudget : budgets) {
                dayBudget.setCampId(originalId);
                campDayBudgetDao.create(dayBudget);
            }
        }
        updateOrderDate(campaign);
        redisClient.set(getAdCampaignCacheKey(campaign.getId()), CAMPAIGN_CACHE_EXPIRE, campaign);
        //发送创建消息
        if (originalId == 0) {
            eventServices.sendAdCreateEvent(campaign.getId(), IdType.CAMPAIGN);
        } else {
            eventServices.sendAdModifyEvent(campaign.getId(), IdType.CAMPAIGN);
        }
    }

    @Transactional(value = "buzzads", readOnly = false)
    public void updateAdCampaign(AdCampaign campaign, AdCampBudget campBudget) {
        saveOrUpdate(campaign);
        adCampaignBudgetService.updateCampBudget(campBudget);
    }

    /**
     * 更新广告活动的状态， 所有需要更新广告活动状态的动作 只应调用此方法
     *
     * @param id
     * @param status
     */
    public void updateAdCampaignStatus(int id, AdStatusEnum status) {
        campaignDao.updateAdCampaignStatus(id, status);
        //发送状态改变消息
        sendStatusChangeEvent(id, status);
        redisClient.delete(getAdCampaignCacheKey(id));
    }

    /**
     * 管理员更新广告状态,记录日志
     *
     * @param id
     * @param status
     */
    public void adminManageCampStatus(int id, AdStatusEnum status) {
        AdCampaign camp = getAdCampaignById(id);
        if (camp != null) {
            updateAdCampaignStatus(id, status);
            //记录日志
            logStatusUpdate(camp, status);
        }
    }

    public AdCampaign getAdCampaignById(int campaignId) {
        AdCampaign campaign = getAdCampaignByIdWithoutBudget(campaignId);
        if (campaign != null) {
            AdCampBudget budget = adCampaignBudgetService.getCampBudget(campaignId);
            if (budget != null) {
                campaign.setMaxDayBudget(budget.getMaxBudgetDay());
                campaign.setMaxBudgetTotal(budget.getMaxBudgetTotal());
            }
            //取出每日预算
            List<CampaignDayBudget> dayBudgets = campDayBudgetDao.findCampaignDayBudgetByCampId(campaign.getId());
            campaign.setDayBudgets(dayBudgets);
        }
        return campaign;
    }
    
    public AdCampaign getAdCampaignByIdWithoutBudget(int campaignId) {
        AdCampaign campaign = (AdCampaign) redisClient.getObject(getAdCampaignCacheKey(campaignId));
        if (campaign == null) {
            campaign = campaignDao.read(campaignId);
            if (campaign != null) {
                redisClient.set(getAdCampaignCacheKey(campaign.getId()), CAMPAIGN_CACHE_EXPIRE, campaign);
            }
        }
        return campaign;
    }

    public List<Integer> listCampaignIdsByAdvertiserId(int advertiserId) {
        return campaignDao.listCampaignIdsByAdvertiserId(advertiserId);
    }

    @Transactional(value = "buzzads")
    public List<AdCampaign> listCampaigns(int userId, Pagination query) {
        List<AdCampaign> campaigns = campaignDao.listCampaigns(userId, query);
        for (AdCampaign campaign : campaigns) {
            AdCampBudget budget = adCampaignBudgetService.getCampBudget(campaign.getId());
            if (budget != null) {
                campaign.setMaxDayBudget(budget.getMaxBudgetDay());
                campaign.setMaxBudgetTotal(budget.getMaxBudgetTotal());
            }
        }
        return campaigns;
    }

    public List<AdCampaign> adminListCampaigns(AdCampaign camp, Pagination query) {
        List<AdCampaign> campaigns = campaignDao.listCampaigns(camp, query);
        //补充广告主信息，订单信息和广告项信息。
        if (campaigns.size() > 0) {
            for (AdCampaign campaign : campaigns) {
                AdvertiserAccount account = accountService.getAdvertiserAccount(campaign.getAdvertiserId());
                if (account != null)
                    campaign.setCompanyName(account.getCompanyName());
                campaign.setAdOrderCount((int)adOrderService.getAdOrderCountByCampaignId(campaign.getId()));
                campaign.setAdEntryCount((int)adEntryService.getAdEntryCountByCampaignId(campaign.getId()));
            }
        }
        return campaigns;
    }

    public List<AdCampaign> getCampaignsByAdvertiserId(int advertiserId) {
        return campaignDao.listCampaignsByAdvertiserId(advertiserId);
    }
    
    public List<AdCampaign> findCampsByAdvType(int advId, BidTypeEnum bidType) {
        List<AdCampaign> list = getCampaignsByAdvertiserId(advId);
        if (bidType != null) {
            Iterator<AdCampaign> it = list.iterator();
            while (it.hasNext()) {
                AdCampaign camp = it.next();
                if (!camp.getBidType().equals(bidType))
                    it.remove();
            }
        }
        return list;
    }
    
    public List<AdCampaign> listActiveCampaignsByAdvertiserId(int advertiserId) {
        return campaignDao.listActiveCampaignsByAdvertiserId(advertiserId);
    }

    public List<AdCampaign> getCampaigns(List<Integer> campaignIds) {
        return campaignDao.findCampaigns(campaignIds);
    }

    public List<AdCampaign> listCampaignsByStatus(AdStatusEnum status) {
        return campaignDao.listCampaignsByStatus(status);
    }
    
    public List<AdCampaign> listCampaignsBidType(BidTypeEnum bidType) {
        return campaignDao.listCampaignsBidType(bidType);
    }
    
    public Long countCampsByBidType(BidTypeEnum bidType, int advId) {
        return campaignDao.countCampsByBidType(bidType, advId);
    }
    
    public int getAllAdCampaignsCount() {
        return campaignDao.getAllAdCampaignsCount();
    }
    
    public int getActiveCampaignsCount() {
        return campaignDao.listAdCampaignsByStatus(AdStatusEnum.ENABLED).size();
    }
    
    public int getActiveCampaignsCountByAdv(int advId) {
        return campaignDao.listActiveCampaignsByAdvertiserId(advId).size();
    }

    private void validate(AdCampaign campaign) {
        if (campaign == null ||
                campaign.getAdvertiserId() <= 0 ||
                StringUtils.isBlank(campaign.getName())) {
            throw new ServiceException("广告活动信息不完整");
        }
    }

    private String getAdCampaignCacheKey(int campaignId) {
        return "Campaign:" + campaignId;
    }

    //发送状态改变消息
    private void sendStatusChangeEvent(int campaignId, AdStatusEnum status) {
        switch (status) {
            case ENABLED:
                eventServices.sendAdEnableEvent(campaignId, IdType.CAMPAIGN);
                break;
            case PAUSED:
                eventServices.sendAdPauseEvent(campaignId, IdType.CAMPAIGN);
                break;
            case DISABLED:
                eventServices.sendAdDisableEvent(campaignId, IdType.CAMPAIGN);
                break;
            case DELETED:
                eventServices.sendAdDeleteEvent(campaignId, IdType.CAMPAIGN);
                break;
            case SUSPENDED:
                eventServices.sendAdSuspendEvent(campaignId, IdType.CAMPAIGN);
                break;
            default:
                break;
        }
    }

    /**
     * 检查广告主所属的广告活动，如果有余额且预算充足则恢复状态为Enabled.
     *
     * @param advertiserId
     */
    public void checkAndResetSuspendedCamp(int advertiserId) {
        long balance = advertiserBalanceService.getLatestBalance(advertiserId);
        if (balance <= 0) {
            return;
        }
        List<AdCampaign> campaigns = getCampaignsByAdvertiserId(advertiserId);
        for (AdCampaign campaign : campaigns) {
            if (campaign.getStatus() != AdStatusEnum.SUSPENDED) {
                continue;
            }
            AdCampBudget budget = adCampaignBudgetService.getLatestCampBudget(campaign.getId());
            if (!budget.isExceedBudget()) {
                updateAdCampaignStatus(campaign.getId(), AdStatusEnum.ENABLED);
            }
        }
    }
    
    private void updateOrderDate(AdCampaign camp) {
        // 更新广告组时间
        List<AdOrder> orders = adOrderService.getAdOrdersByCampaignId(camp.getId());
        for (AdOrder order : orders) {
            boolean modified = false;
            if (camp.getEndDate() != null && 
                            (order.getEndDate() == null || camp.getEndDate().before(order.getEndDate()))) {
                order.setEndDate(camp.getEndDate());
                modified = true;
            }
            if (camp.getStartDate().after(order.getStartDate())) {
                order.setStartDate(camp.getStartDate());
                modified = true;
            }
            if (order.getEndDate() != null && order.getStartDate().after(order.getEndDate())) {
                order.setStartDate(order.getEndDate());
                modified = true;
            }
            if (modified) {
                adOrderService.saveOrUpdate(order);
            }
        }
    }

    private void logStatusUpdate(AdCampaign campaign, AdStatusEnum targetStatus) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("campId", String.valueOf(campaign.getId()));
        params.put("campName", campaign.getName());
        params.put("preStatus", campaign.getStatusName());
        params.put("toStatus", AdStatusEnum.getCnName(targetStatus));
        addOperationLog(LogConstants.OPTYPE_CAMPAIGN_OPERATE, LogConstants.OBJNAME_CAMPAIGN,
                String.valueOf(campaign.getAdvertiserId()), String.valueOf(campaign.getId()), params);
    }
}
