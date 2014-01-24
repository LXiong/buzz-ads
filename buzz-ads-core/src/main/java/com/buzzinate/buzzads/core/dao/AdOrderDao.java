package com.buzzinate.buzzads.core.dao;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.AdOrder;
import com.buzzinate.buzzads.domain.AdOrderPriceChange;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *         <p/>
 *         2012-11-22
 */
@Component
@Transactional(value = "buzzads", readOnly = true)
public class AdOrderDao extends AdsDaoBase<AdOrder, Integer> {

    public AdOrderDao() {
        super(AdOrder.class, "id");
    }

    @Transactional(value = "buzzads", readOnly = false)
    public int updateStatus(int orderId, AdStatusEnum status) {
        Query query = getSession().getNamedQuery("AdOrderDao.updateOrderStatus");
        query.setLong("orderId", orderId);
        query.setInteger("status", status.getCode());
        return query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<AdOrder> listAdOrders(AdOrder adOrder, Pagination query) {
        Criteria criteria = getSession().createCriteria(AdOrder.class);
        if (!StringUtils.isEmpty(adOrder.getName())) {
            criteria.add(Restrictions.like("name", "%" + adOrder.getName() + "%"));
        }
        if (adOrder.getStatus() != null) {
            criteria.add(Restrictions.eq("status", adOrder.getStatus()));
        }
        if (adOrder.getAdvertiserId() != 0) {
            criteria.add(Restrictions.eq("advertiserId", adOrder.getAdvertiserId()));
        }
        if (adOrder.getCampaignId() > 0) {
            criteria.add(Restrictions.eq("campaignId", adOrder.getCampaignId()));
        }
        criteria.addOrder(Order.desc("updateAt"));
        // first get the total records in the database for this search...
        Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        query.setTotalRecords(count.intValue());
        query.validatePageInfo();

        // Reset projection and get records for the requested page.
        criteria.setProjection(null);
        criteria.setFirstResult(query.getStartRow());
        criteria.setMaxResults(query.getPageSize());
        List<AdOrder> orders = criteria.list();
        query.setReturnRecords(orders.size());

        return orders;
    }

    @SuppressWarnings("unchecked")
    public List<AdOrder> listAdOrders(AdOrder adOrder) {
        Criteria criteria = getSession().createCriteria(AdOrder.class);
        if (!StringUtils.isEmpty(adOrder.getName())) {
            criteria.add(Restrictions.like("name", "%" + adOrder.getName() + "%"));
        }
        if (adOrder.getStatus() != null) {
            criteria.add(Restrictions.eq("status", adOrder.getStatus()));
        }
        if (adOrder.getAdvertiserId() != 0) {
            criteria.add(Restrictions.eq("advertiserId", adOrder.getAdvertiserId()));
        }
        if (adOrder.getCampaignId() > 0) {
            criteria.add(Restrictions.eq("campaignId", adOrder.getCampaignId()));
        }
        criteria.addOrder(Order.desc("updateAt"));
        List<AdOrder> orders = criteria.list();
        return orders;
    }

    /**
     * 根据活动id返回所有订单
     *
     * @param campaignId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AdOrder> findAdOrders(Integer campaignId) {
        Query query = getSession().getNamedQuery("AdOrderDao.listOrdersByCampaignId");
        query.setLong("campaignId", campaignId);
        return query.list();
    }

    /**
     * 根据活动id列表返回所有订单
     *
     * @param campaignId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AdOrder> findAdOrders(List<Integer> campaignIds) {
        Query query = getSession().getNamedQuery("AdOrderDao.listOrdersByCampaignIds");
        query.setParameterList("campaignIds", campaignIds);
        return query.list();
    }

    /**
     * 根据投放平台和结算类型返回所有订单
     *
     * @param networks
     * @param bidTypes
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AdOrder> findOrdersByCondtion(List<AdNetworkEnum> networks, List<BidTypeEnum> bidTypes) {
        Query query = null;
        if (!networks.isEmpty() && bidTypes.isEmpty()) {
            query = getSession().getNamedQuery("AdOrderDao.listOrdersByNetworks");
            query.setParameterList("networks", networks);
        }
        if (networks.isEmpty() && !bidTypes.isEmpty()) {
            query = getSession().getNamedQuery("AdOrderDao.listOrdersByBidTypes");
            query.setParameterList("bidTypes", bidTypes);
        }
        if (!networks.isEmpty() && !bidTypes.isEmpty()) {
            query = getSession().getNamedQuery("AdOrderDao.listOrdersByCondition");
            query.setParameterList("networks", networks);
            query.setParameterList("bidTypes", bidTypes);
        }
        if (networks.isEmpty() && bidTypes.isEmpty()) {
            query = getSession().getNamedQuery("AdOrderDao.listAllOrders");
        }
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<AdOrder> listAdOrdersByAdvertiserId(int advertiserId) {
        Query query = getSession().getNamedQuery("AdOrderDao.listAdOrdersByAdvertiserId");
        query.setInteger("advId", advertiserId);
        return (List<AdOrder>) query.list();
    }

    @SuppressWarnings("unchecked")
    public List<AdOrder> listActiveAdOrdersByAdvertiserId(int advertiserId, List<Integer> activeCampIds) {
        Query query = getSession().getNamedQuery("AdOrderDao.listActiveAdOrdersByAdvertiserIdWithActiveCamps");
        query.setInteger("advId", advertiserId);
        query.setParameterList("activeCampIds", activeCampIds);
        return (List<AdOrder>) query.list();
    }

    public int getAllAdOrdersCount() {
        Criteria criteria = getSession().createCriteria(AdOrder.class);
        criteria.add(Restrictions.ne("status", AdStatusEnum.DELETED));
        Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        return count.intValue();
    }

    @SuppressWarnings("unchecked")
    public List<AdOrder> listAdOrdersByStatus(AdStatusEnum status) {
        Criteria criteria = getSession().createCriteria(AdOrder.class);
        criteria.add(Restrictions.eq("status", status));
        List<AdOrder> orders = criteria.list();
        return orders;
    }

    public void updateOrderByPriceLog(AdOrder oriOrder, AdOrder order, AdOrderPriceChangeDao adOrderPriceChangeDao) {
        int oriPrice = oriOrder.getBidPrice();
        int newPrice = order.getBidPrice();
        if (oriPrice != newPrice) {
            adOrderPriceChangeDao.create(new AdOrderPriceChange(oriOrder.getId(), oriPrice, newPrice));
        }
        this.saveOrUpdate(order);
    }

    public long getAdOrderCountByCampaignId(int campaignId) {
        Criteria criteria = getSession().createCriteria(AdOrder.class);
        criteria.add(Restrictions.eq("campaignId", campaignId));
        criteria.setProjection(Projections.rowCount());
        Long result = (Long) criteria.uniqueResult();
        return result == null ? 0 : result.longValue();
    }
}
