package com.buzzinate.buzzads.core.dao;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.Channel;
import com.buzzinate.buzzads.domain.Site;
import com.buzzinate.buzzads.enums.ChannelStatusEnum;
import com.buzzinate.buzzads.enums.ChannelTypeEnum;
import com.buzzinate.common.util.DateTimeUtil;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.safehaus.uuid.UUID;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 *         2013-05-13
 */
@Repository
@Transactional(value = "buzzads", readOnly = true)
public class ChannelDao extends AdsDaoBase<Channel, Integer> {

    public ChannelDao() {
        super(Channel.class, "id");
    }

    @SuppressWarnings("unchecked")
    public List<Channel> listChannelByUuid(byte[] uuid) {
        Query query = getSession().getNamedQuery("ChannelDao.listChannelByUuid");
        query.setBinary("uuidBytes", uuid);
        return (List<Channel>) query.list();
    }

    public List<Channel> listChannelByStatusAndType(ChannelStatusEnum status, ChannelTypeEnum type, Pagination page) {
        Criteria criteria = getSession().createCriteria(Channel.class);
        criteria.add(Restrictions.eq("status", status));
        if (type != null) {
            criteria.add(Restrictions.eq("type", type));    
        }
        return getPaginationResult(criteria, page);
    }

    public List<Channel> listChannels(Channel channel, Pagination page) {
        Criteria criteria = getSession().createCriteria(Channel.class);

        if (channel.getStatus() != null) {
            criteria.add(Restrictions.eq("status", channel.getStatus()));
        } else {
            criteria.add(Restrictions.in("status", 
                    new ChannelStatusEnum[]{ChannelStatusEnum.OPENED, 
                        ChannelStatusEnum.FROZENED, ChannelStatusEnum.CLOSED }
            ));
        }
        if (channel.getLevel() != 0) {
            criteria.add(Restrictions.eq("level", channel.getLevel()));
        }
        if (channel.getType() != null) {
            criteria.add(Restrictions.eq("type", channel.getType()));
        }

        return getPaginationResult(criteria, page);
    }

    public long countChannelByStatus(ChannelStatusEnum[] status) {
        Criteria criteria = getSession().createCriteria(Channel.class);
        criteria.add(Restrictions.in("status", status));
        return (Long) criteria.setProjection(Projections.rowCount())
                .uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    public List<Channel> findChannelByStatus(ChannelStatusEnum status, int count) {
        Query query = getSession().getNamedQuery("ChannelDao.findChannelByStatus");
        query.setInteger("status", status.getCode());
        query.setMaxResults(count);
        return query.list();
    }

    @Transactional(value = "buzzads", readOnly = false)
    public void update1stLvlChannelStatus(String uuid, ChannelStatusEnum channelStatus, String operateResult) {
        Query query = null;
        if(channelStatus == ChannelStatusEnum.DELETED) {
            // 删除为物理删除
            query = getSession().getNamedQuery("ChannelDao.delete1stLvlChannel");
        } else {
            query = getSession().getNamedQuery("ChannelDao.update1stLvlChannelStatus");
            query.setInteger("status", channelStatus.getCode());
            query.setString("result", operateResult);
            query.setInteger("deletedStatus", ChannelStatusEnum.DELETED.getCode());
        }
        query.setBinary("uuidBytes", new UUID(uuid).asByteArray());
        // 一级媒体状态更新的时候不对已删除的二级媒体进行操作   not (status = 3 AND level = 2)
        query.executeUpdate();
    }

    @Transactional(value = "buzzads", readOnly = false)
    public void update2ndLvlChannelStatus(int channelId, ChannelStatusEnum channelStatus, String operateResult) {
        if(channelStatus == ChannelStatusEnum.DELETED) {
            delete(channelId);
            return;
        }
        Query query = getSession().getNamedQuery("ChannelDao.update2ndLvlChannelStatus");
        query.setInteger("id", channelId);
        query.setInteger("status", channelStatus.getCode());
        query.setString("result", operateResult);
        query.executeUpdate();
    }
    
    @Transactional(value = "buzzads", readOnly = false)
    public void openChannel(List<Integer> openIds) {
        Query query = getSession().getNamedQuery("ChannelDao.openChannel");
        query.setParameterList("openIds", openIds);
        query.setDate("openTime", DateTimeUtil.getCurrentDateDay());
        query.setInteger("status", ChannelStatusEnum.OPENED.getCode());
        query.executeUpdate();
    }
    
    @Transactional(value = "buzzads", readOnly = false)
    public void closeChannel(List<Integer> closeIds) {
        Query query = getSession().getNamedQuery("ChannelDao.closeChannel");
        query.setParameterList("closeIds", closeIds);
        query.setDate("closeTime", DateTimeUtil.getCurrentDateDay());
        query.setInteger("status", ChannelStatusEnum.CLOSED.getCode());
        query.executeUpdate();
    }

    /**
     * 根据UUID和域名获取媒体
     *
     * @param uuid
     * @param domain
     * @return
     */
    public Channel findChannelByUUIDandDomain(byte[] uuid, String domain) {
        Criteria criteria = getSession().createCriteria(Channel.class);
        criteria.add(Restrictions.eq("uuidBytes", uuid));
        criteria.add(Restrictions.eq("domain", domain));
        return (Channel) criteria.uniqueResult();
    }

    /**
     * 根据UUID获取一级媒体
     *
     * @param uuidBytes
     * @return
     */
    public Channel findTopChannelByUUID(byte[] uuidBytes) {
        List<Channel> channels = findChannelByUUIDAndLevel(uuidBytes, 1);
        if (!channels.isEmpty()) {
            return channels.get(0);
        }
        return null;
    }

    public List<Channel> findSubChannelByUUID(byte[] uuidBytes) {
        return findChannelByUUIDAndLevel(uuidBytes, 2);
    }

    @SuppressWarnings("unchecked")
    public List<Channel> findChannelByUUIDAndLevel(byte[] uuidBytes, int level) {
        Criteria criteria = getSession().createCriteria(Channel.class);
        criteria.add(Restrictions.eq("uuidBytes", uuidBytes));
        criteria.add(Restrictions.eq("level", level));
        return (List<Channel>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    public Channel findChannelByDomain(String domain) {
        Criteria criteria = getSession().createCriteria(Channel.class);
        criteria.add(Restrictions.eq("domain", domain));
        List<Channel> channels = criteria.list();
        if (!channels.isEmpty()) {
            return channels.get(0);
        }
        return null;
    }
    
    public List<Site> getSiteListBySiteName(String siteUrlSrh, Date dateStart, Date dateEnd, Pagination page, 
            String orderby) {
        // 处理分页
        List<Site> resList = new ArrayList<Site>();
        Query countQuery = getSession().getNamedQuery("ChannelDao.getSiteListBySiteName.count");
        countQuery.setString("siteUrlSrh", "%" + siteUrlSrh + "%");
        countQuery.setDate("dateStart", dateStart);
        countQuery.setDate("dateEnd", dateEnd);
        page.setTotalRecords((Integer) countQuery.uniqueResult());
        page.validatePageInfo();

        Query query = getSession().createSQLQuery(getSession().getNamedQuery("ChannelDao.getSiteListBySiteName").
                getQueryString() + "\n\t\t ORDER BY " + orderby + "\n\t\t LIMIT " + 
                page.getStartRow() + ", " + page.getPageSize());
        query.setString("siteUrlSrh", "%" + siteUrlSrh + "%");
        query.setDate("dateStart", dateStart);
        query.setDate("dateEnd", dateEnd);
        @SuppressWarnings("unchecked")
        List<Object[]> list = query.list();
        page.setReturnRecords(list.size());
        
        Site site = null;
        for (Object[] tmp : list) {
            site = new Site();
            site.setUuidBytes((byte[]) tmp[0]);
            site.setUrl((String) tmp[1]);
            site.setSiteComm(((BigDecimal) tmp[2]).intValue());
            site.setPageview(((BigDecimal) tmp[3]).intValue());
            site.setClicks(((BigDecimal) tmp[4]).intValue());
            site.setViews(((BigDecimal) tmp[5]).intValue());
            resList.add(site);
        }
        return resList;
    }

}
