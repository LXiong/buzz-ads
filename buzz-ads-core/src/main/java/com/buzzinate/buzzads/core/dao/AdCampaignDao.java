package com.buzzinate.buzzads.core.dao;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *         <p/>
 *         2013-2-27
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class AdCampaignDao extends AdsDaoBase<AdCampaign, Integer> {

    public AdCampaignDao() {
        super(AdCampaign.class, "id");
    }

    public List<AdCampaign> listCampaigns(int advertiserId, Pagination query) {
        Criteria criteria = getSession().createCriteria(AdCampaign.class);
        if (advertiserId > 0) {
            criteria.add(Restrictions.eq("advertiserId", advertiserId));
        }
        criteria.add(Restrictions.ne("status", AdStatusEnum.DELETED));
        criteria.addOrder(Order.desc("updateAt"));

        // first get the total records in the database for this search...
        Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        query.setTotalRecords(count.intValue());
        query.validatePageInfo();

        // Reset projection and get records for the requested page.
        criteria.setProjection(null);
        criteria.setFirstResult(query.getStartRow());
        criteria.setMaxResults(query.getPageSize());

        @SuppressWarnings("unchecked")
        List<AdCampaign> list = criteria.list();
        query.setReturnRecords(list.size());

        return list;
    }

    public List<AdCampaign> listCampaigns(AdCampaign camp, Pagination query) {
        Criteria criteria = getSession().createCriteria(AdCampaign.class);
        if (camp != null) {
            if (camp.getAdvertiserId() > 0) {
                criteria.add(Restrictions.eq("advertiserId", camp.getAdvertiserId()));
            }
            if (camp.getStatus() != null) {
                criteria.add(Restrictions.eq("status", camp.getStatus()));
            }
            if (StringUtils.isNotBlank(camp.getName())) {
                criteria.add(Restrictions.ilike("name", camp.getName(), MatchMode.ANYWHERE));
            }
            if (camp.getBidType() != null) {
                criteria.add(Restrictions.eq("bidType", camp.getBidType()));
            }
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

        @SuppressWarnings("unchecked")
        List<AdCampaign> list = criteria.list();
        query.setReturnRecords(list.size());

        return list;
    }

    @SuppressWarnings("unchecked")
    public List<AdCampaign> listCampaignsByAdvertiserId(int advertiserId) {
        Query query = getSession().getNamedQuery("AdCampaignDao.listCampaignsByAdvertiserId");
        query.setInteger("advertiserId", advertiserId);
        return (List<AdCampaign>) query.list();
    }

    @SuppressWarnings("unchecked")
    public List<AdCampaign> listActiveCampaignsByAdvertiserId(int advertiserId) {
        Query query = getSession().getNamedQuery("AdCampaignDao.listActiveCampaignsByAdvertiserId");
        query.setInteger("advertiserId", advertiserId);
        return (List<AdCampaign>) query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Integer> listCampaignIdsByAdvertiserId(int advertiserId) {
        Query query = getSession().getNamedQuery("AdCampaignDao.listCampaignIdsByAdvertiserId");
        query.setInteger("advertiserId", advertiserId);
        return (List<Integer>) query.list();
    }

    @SuppressWarnings("unchecked")
    public List<AdCampaign> findCampaigns(List<Integer> campaignIds) {
        Query query = getSession().getNamedQuery("AdCampaignDao.listCampaignsByIds");
        query.setParameterList("campaignIds", campaignIds);
        return (List<AdCampaign>) query.list();
    }

    @SuppressWarnings("unchecked")
    public List<AdCampaign> listCampaignsByStatus(AdStatusEnum status) {
        Query query = getSession().getNamedQuery("AdCampaignDao.listCampaignsByStatus");
        query.setInteger("status", status.getCode());
        return (List<AdCampaign>) query.list();
    }

    @SuppressWarnings("unchecked")
    public List<AdCampaign> listCampaignsBidType(BidTypeEnum bidType) {
        Criteria criteria = getSession().createCriteria(AdCampaign.class);
        criteria.add(Restrictions.eq("bidType", bidType));
        return (List<AdCampaign>) criteria.list();
    }

    @Transactional(value = "buzzads", readOnly = false)
    public void updateAdCampaignStatus(int id, AdStatusEnum status) {
        Query query = getSession().getNamedQuery("AdCampaignDao.updateAdCampaignStatus");
        query.setInteger("status", status.getCode());
        query.setInteger("id", id);
        query.executeUpdate();
    }

    public int getAllAdCampaignsCount() {
        Criteria criteria = getSession().createCriteria(AdCampaign.class);
        criteria.add(Restrictions.ne("status", AdStatusEnum.DELETED));
        Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        return count.intValue();
    }

    @SuppressWarnings("unchecked")
    public List<AdCampaign> listAdCampaignsByStatus(AdStatusEnum status) {
        Criteria criteria = getSession().createCriteria(AdCampaign.class);
        criteria.add(Restrictions.eq("status", status));
        List<AdCampaign> campaigns = criteria.list();
        return campaigns;
    }

    public Long countCampsByBidType(BidTypeEnum bidType, int advId) {
        Criteria criteria = getSession().createCriteria(AdCampaign.class);
        if (bidType != null) {
            criteria.add(Restrictions.eq("bidType", bidType));
        }
        if (advId > 0) {
            criteria.add(Restrictions.eq("advertiserId", advId));
        }
        criteria.add(Restrictions.eq("status", AdStatusEnum.ENABLED));
        return (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
    }
}
