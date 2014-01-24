package com.buzzinate.buzzads.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.exception.ServiceException;
import com.buzzinate.buzzads.common.service.AdsBaseService;
import com.buzzinate.buzzads.core.dao.AdOrderDao;
import com.buzzinate.buzzads.core.dao.AdOrderPriceChangeDao;
import com.buzzinate.buzzads.core.util.LogConstants;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.core.util.RedisClient;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdOrder;
import com.buzzinate.buzzads.domain.AdOrderPriceChange;
import com.buzzinate.buzzads.domain.AdvertiserAccount;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.buzzads.enums.IdType;

/**
 * @author harry
 */
@Service
public class AdOrderService extends AdsBaseService {

    private static final int ORDER_CACHE_EXPIRE = 3600 * 24;
    @Autowired
    private AdOrderDao adOrderDao;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private EventServices eventServices;
    @Autowired
    private AdOrderCategoryService adOrderCategoryService;
    @Autowired
    private AdvertiserAccountService advertiserAccountService;
    @Autowired
    private AdCampaignService adCampaignService;
    @Autowired
    private AdOrderPriceChangeDao adOrderPriceChangeDao;

    /**
     * 创建或者修改广告订单的信息
     * 广告订单状态的更新需要调用 updateStatus方法
     *
     * @param order
     */
    @Transactional(value = "buzzads", readOnly = false)
    public int saveOrUpdate(AdOrder order) {
        int originalId = order.getId();
        validate(order);
        if (originalId == 0) {
            originalId = adOrderDao.create(order);
            eventServices.sendAdCreateEvent(order.getId(), IdType.ORDER);
        } else {
            // 如果有价格变动，则对价格的变动信息进行记录
            AdOrder oriOrder = (AdOrder) redisClient.getObject(getAdOrderCacheKey(originalId));
            if (oriOrder == null) {
                oriOrder = adOrderDao.read(originalId);
            }
            adOrderDao.updateOrderByPriceLog(oriOrder, order, adOrderPriceChangeDao);
            eventServices.sendAdModifyEvent(order.getId(), IdType.ORDER);
        }
        order.setAudienceCategoriesName(adOrderCategoryService.getCategoriesName(order.getAudienceCategories()));
        redisClient.set(getAdOrderCacheKey(order.getId()), ORDER_CACHE_EXPIRE, order);
        return originalId;
    }

    /**
     * 管理员更新广告组状态,记录日志
     *
     * @param id
     * @param status
     */
    public void adminManageCampStatus(int id, AdStatusEnum status) {
        AdOrder order = getOrderById(id);
        if (order != null) {
            updateStatus(id, status);
            //记录日志
            Map<String, String> params = new HashMap<String, String>();
            params.put("orderId", String.valueOf(id));
            params.put("orderName", order.getName());
            params.put("preStatus", order.getStatusName());
            params.put("toStatus", AdStatusEnum.getCnName(status));
            addOperationLog(LogConstants.OPTYPE_ADORDER_OPERATE, LogConstants.OBJNAME_ADORDER,
                    String.valueOf(order.getAdvertiserId()), String.valueOf(id), params);
        }
    }

    /**
     * 更新广告订单的状态, 所有需要更新广告订单状态的动作 只应调用此方法
     *
     * @param orderId
     * @param status
     * @return
     */
    public void updateStatus(int orderId, AdStatusEnum status) {
        adOrderDao.updateStatus(orderId, status);
        //发送状态改变消息
        sendStatusChangeEvent(orderId, status);
        redisClient.delete(getAdOrderCacheKey(orderId));
    }

    /**
     * 根据ID获取广告订单
     *
     * @param orderId
     * @return
     */
    public AdOrder getOrderById(int orderId) {
        AdOrder order = (AdOrder) redisClient.getObject(getAdOrderCacheKey(orderId));
        if (order == null) {
            order = adOrderDao.read(orderId);
            if (order != null) {
                order.setAudienceCategoriesName(adOrderCategoryService.getCategoriesName(
                        order.getAudienceCategories()));
                //媒体定向
                redisClient.set(getAdOrderCacheKey(orderId), ORDER_CACHE_EXPIRE, order);
            }
        }
        return order;
    }

    /**
     * 根据条件查询广告订单
     *
     * @param adOrder
     * @param query
     * @return
     */
    public List<AdOrder> listAdOrders(AdOrder adOrder, Pagination query) {
        List<AdOrder> adOrderList = adOrderDao.listAdOrders(adOrder, query);
        AdvertiserAccount account;
        for (AdOrder order : adOrderList) {
            account = advertiserAccountService.getAdvertiserAccount(order.getAdvertiserId());
            if (account != null) {
                order.setCompanyName(account.getCompanyName());
            }
        }
        return adOrderList;
    }

    /**
     * 根据活动id返回所有订单
     *
     * @param campaignId
     * @return
     */
    public List<AdOrder> getAdOrdersByCampaignId(Integer campaignId) {
        return adOrderDao.findAdOrders(campaignId);
    }

    public long getAdOrderCountByCampaignId(int campaignId) {
        return adOrderDao.getAdOrderCountByCampaignId(campaignId);
    }
    
    /**
     * 根据advertiser id返回所有订单
     *
     * @param advertiserId
     * @return
     */
    public List<AdOrder> getAdOrdersByAdvertiserId(int advertiserId) {
        return adOrderDao.listAdOrdersByAdvertiserId(advertiserId);
    }

    /**
     * 根据活动列表id返回所有订单
     *
     * @param campaignId
     * @return
     */
    public List<AdOrder> getAdOrdersByCampaignIds(List<Integer> campaignIds) {
        return adOrderDao.findAdOrders(campaignIds);
    }

    /**
     * 根据投放平台和结算类型返回所有订单
     *
     * @param campaignId
     * @return
     */
    public List<AdOrder> getAdOrdersByCondition(List<AdNetworkEnum> networks, List<BidTypeEnum> bidTypes) {
        return adOrderDao.findOrdersByCondtion(networks, bidTypes);
    }

    /**
     * 根据广告主列出广告组
     *
     * @param advertiserId
     * @return
     */
    public List<AdOrder> listAdOrdersByAdvertiserId(int advertiserId) {
        return adOrderDao.listAdOrdersByAdvertiserId(advertiserId);
    }
    
    public List<AdOrder> listActiveAdOrdersByAdvertiserId(int advertiserId, List<Integer> activeCamps) {
        return adOrderDao.listActiveAdOrdersByAdvertiserId(advertiserId, activeCamps);
    }
    
    public int getAllAdOrdersCount() {
        return adOrderDao.getAllAdOrdersCount();
    }
    
    public int getActiveOrdersCount() {
        List<AdOrder> allOrders = adOrderDao.listAdOrdersByStatus(AdStatusEnum.ENABLED);
        List<AdOrder> orders = new ArrayList<AdOrder>();
        for (AdOrder order : allOrders) {
            AdCampaign camp = adCampaignService.getAdCampaignByIdWithoutBudget(order.getCampaignId());
            if (camp != null && camp.getStatus().equals(AdStatusEnum.ENABLED)) {
                orders.add(order);
            }
        }
        return orders.size();
    }
    
    public List<AdOrder> listAdOrdersByUpperStatus(AdOrder adOrder, Pagination page) {
        List<AdOrder> allOrders = adOrderDao.listAdOrders(adOrder);
        List<AdOrder> orders = new ArrayList<AdOrder>();
        for (AdOrder order : allOrders) {
            AdvertiserAccount account = advertiserAccountService.getAdvertiserAccount(order.getAdvertiserId());
            if (account != null) {
                order.setCompanyName(account.getCompanyName());
            }
            AdCampaign camp = adCampaignService.getAdCampaignByIdWithoutBudget(order.getCampaignId());
            if (camp != null && camp.getStatus().equals(AdStatusEnum.ENABLED)) {
                orders.add(order);
            }
        }
        // 分页处理
        page.setTotalRecords(orders.size());

        int fromIndex = (page.getPageNum() > 0 ? page.getPageNum() - 1 : page.getPageNum()) * page.getPageSize();
        if (fromIndex > orders.size()) {
            orders = new ArrayList<AdOrder>();
        } else {
            int toIndex = Math.min((page.getPageNum()) * page.getPageSize(), orders.size());
            orders = orders.subList(fromIndex, toIndex);
        }
        return orders;
    }

    private void validate(AdOrder order) {
        if (order == null ||
                order.getCampaignId() <= 0 ||
                order.getAdvertiserId() <= 0 ||
                StringUtils.isBlank(order.getName())) {
            throw new ServiceException("广告订单信息不完整");
        }
    }

    private String getAdOrderCacheKey(int orderId) {
        return "Order:" + orderId;
    }

    //发送状态改变消息
    private void sendStatusChangeEvent(int orderId, AdStatusEnum status) {
        switch (status) {
            case ENABLED:
                eventServices.sendAdEnableEvent(orderId, IdType.ORDER);
                break;
            case PAUSED:
                eventServices.sendAdPauseEvent(orderId, IdType.ORDER);
                break;
            case DISABLED:
                eventServices.sendAdDisableEvent(orderId, IdType.ORDER);
                break;
            case DELETED:
                eventServices.sendAdDeleteEvent(orderId, IdType.ORDER);
                break;
            case SUSPENDED:
                eventServices.sendAdSuspendEvent(orderId, IdType.ORDER);
                break;
            default:
                break;
        }
    }
}
