package com.buzzinate.buzzads.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.core.dao.AdCampaignDao;
import com.buzzinate.buzzads.core.service.AdCampaignService;
import com.buzzinate.buzzads.core.service.AdEntryService;
import com.buzzinate.buzzads.core.service.AdOrderService;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.dao.ChanetCampaignDao;
import com.buzzinate.buzzads.domain.AdCampBudget;
import com.buzzinate.buzzads.domain.AdCampaign;
import com.buzzinate.buzzads.domain.AdEntry;
import com.buzzinate.buzzads.domain.AdOrder;
import com.buzzinate.buzzads.domain.ChanetDTO;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.common.util.DateTimeUtil;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-11-27
 */
@Service
public class ChanetCampService {
    
    @Autowired
    private ChanetCampaignDao chanetCampaignDao;
    @Autowired
    private AdEntryService adEntryService;
    @Autowired
    private AdOrderService adOrderService;
    @Autowired
    private AdCampaignService adCampaignService;
    
    /*
     * 更新chanet广告
     */
    @Transactional(value = "buzzads", readOnly = false)
    public void updateChanetCamp(ChanetDTO source, ChanetDTO target) {
        //终止状态
        if (!source.getStatus().equals(AdStatusEnum.ENABLED)) {
            //激活广告与订单
            chanetCampaignDao.updateChanetCampaignStatus(source.getCampaignId(), AdStatusEnum.ENABLED);
            adOrderService.updateStatus(source.getOrderId(), AdStatusEnum.ENABLED);
        }
        //链接有改变，更新链接
        if (!source.getChanetLink().equals(target.getChanetLink())) {
            chanetCampaignDao.updateChanetLink(target.getCampaignId(), target.getChanetLink());
            adEntryService.updateLinkByOrderId(source.getOrderId(), getAdsLink(target.getChanetLink()));
        }
    }
    
    /*
     * 保存chanet新广告
     */
    @Transactional(value = "buzzads", readOnly = false)
    public void createNewChanetCampaign(ChanetDTO newCamp) {
        // 新建广告活动
        int advertiserId = ConfigurationReader.getInt("ads.advertiser.chanet.advertiserid");
        AdCampaign campaign = new AdCampaign();
        campaign.setAdvertiserId(advertiserId);
        campaign.setName("ChanetCampaign");
        Date nowDate = new Date();
        campaign.setStartDate(nowDate);
        campaign.setEndDate(nowDate);
        int campaignId = adCampaignService.createAdCampaign(campaign, new AdCampBudget());
        
        // 新建订单
        AdOrder order = new AdOrder();
        order.setName(newCamp.getCampaignName());
        order.setStatus(AdStatusEnum.ENABLED);
        //固定值
        order.setAdvertiserId(advertiserId);
        order.setCampaignId(campaignId);
        order.setUpdateAt(DateTimeUtil.getCurrentDate());
        order.setBidPrice(Integer.valueOf(0));
        adOrderService.saveOrUpdate(order);
        
        // 新建广告
        newCamp.setOrderId(order.getId());
        newCamp.setStatus(AdStatusEnum.ENABLED);
        chanetCampaignDao.create(newCamp);
        
        // 创建广告单元
        AdEntry adEntry = new AdEntry();
        adEntry.setName(StringUtils.isBlank(newCamp.getCampaignName()) ? "无名称广告" : newCamp.getCampaignName());
        adEntry.setTitle(newCamp.getCampaignName());
        adEntry.setOrderId(order.getId());
        adEntry.setLink(getAdsLink(newCamp.getChanetLink()));
        adEntry.setCampaignId(ConfigurationReader.getInt("ads.campaign.chanet.campaignid"));
        adEntry.setAdvertiserId(ConfigurationReader.getInt("ads.advertiser.chanet.advertiserid"));
        adEntry.setStatus(AdStatusEnum.ENABLED);
        adEntryService.saveOrUpdate(adEntry);
    }
    
    /*
     * 终止订单
     */
    @Transactional(value = "buzzads", readOnly = false)
    public void terminateChanetCampaign(List<ChanetDTO> terminateList) {
        for (ChanetDTO dto : terminateList) {
            chanetCampaignDao.updateChanetCampaignStatus(dto.getCampaignId(), AdStatusEnum.DISABLED);
            adOrderService.updateStatus(dto.getOrderId(), AdStatusEnum.DISABLED);
        }
    }
    
    private String getAdsLink(String link) {
        return link.replace("[UID]", "{uuid}_{adEntryId}_{product}").replace("[EID]", "").replace("[BACKURL]", "");
    }
}
