package com.buzzinate.buzzads.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.dao.AdsDaoBase;
import com.buzzinate.buzzads.domain.ChanetDTO;
import com.buzzinate.buzzads.enums.AdStatusEnum;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-11-22
 */
@Component
@Transactional(value = "buzzads", readOnly = true)
public class ChanetCampaignDao extends AdsDaoBase<ChanetDTO, Long> {
    
    public ChanetCampaignDao() {
        super(ChanetDTO.class, "orderId");
    }

    /**
     * 查询所有chanet所有投放的 广告
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ChanetDTO> listLiveChanetCampaign() {
        Query query = getSession().getNamedQuery("ChanetCampaignDao.listLiveChanetCampaign");
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<ChanetDTO> listWantTerminateChanetCamp(List<Long> liveCampIds) {
        Query query = getSession().getNamedQuery("ChanetCampaignDao.listWantTerminateChanetCamp");
        query.setParameterList("liveCampIds", liveCampIds);
        return query.list();
    }
    
    public ChanetDTO getChanetCampaignByCampId(Long campId) {
        Query query = getSession().getNamedQuery("ChanetCampaignDao.getChanetCampaignByCampId");
        query.setLong("campId", campId);
        return (ChanetDTO) query.uniqueResult();
    }

    /**
     * 终止chanet广告
     * @param campId
     * @return
     */
    @Transactional(value = "buzzads", readOnly = false)
    public int updateChanetCampaignStatus(Long campId, AdStatusEnum status) {
        Query query = getSession().getNamedQuery("ChanetCampaignDao.updateChanetCampaignStatus");
        query.setLong("campId", campId);
        query.setInteger("status", status.getCode());
        return query.executeUpdate();
    }

    @Transactional(value = "buzzads", readOnly = false)
    public int updateChanetLink(Long campId, String chanetLink) {
        Query query = getSession().getNamedQuery("ChanetCampaignDao.updateChanetLink");
        query.setLong("campId", campId);
        query.setString("chanetLink", chanetLink);
        return query.executeUpdate();
    }
    
    @SuppressWarnings("unchecked")
    public List<ChanetDTO> getChanetCampaigns() {
        Query query = getSession().getNamedQuery("ChanetCampaignDao.getChanetCampaigns");
        return (List<ChanetDTO>) query.list();
    }
}
