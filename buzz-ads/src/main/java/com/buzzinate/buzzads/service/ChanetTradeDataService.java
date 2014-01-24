package com.buzzinate.buzzads.service;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.analytics.AdAnalyticsConstants;
import com.buzzinate.buzzads.common.exception.ServiceException;
import com.buzzinate.buzzads.core.dao.AdDetailCpsDao;
import com.buzzinate.buzzads.core.service.AdEntryService;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.dao.ChanetTradeDataDao;
import com.buzzinate.buzzads.domain.AdDetailCps;
import com.buzzinate.buzzads.domain.AdEntry;
import com.buzzinate.buzzads.domain.ChanetTradeData;
import com.buzzinate.buzzads.domain.enums.TradeStatusEnum;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.buzzads.enums.OrderSourceEnum;
import com.buzzinate.buzzads.enums.TradeConfirmEnum;
import com.buzzinate.common.util.kestrel.KestrelClient;
import com.buzzinate.common.util.serialize.HessianSerializer;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-11-23
 */
@Service
public class ChanetTradeDataService {
    
    private static Log log = LogFactory.getLog(ChanetTradeDataService.class);
    
    // 默认成果网分成比例
    private static final int DEFAULT_PROPORTION = ConfigurationReader.getInt("ads.default.chanet.proportion");

    @Autowired
    private transient KestrelClient analyticsKestrelClient;
    
    @Autowired
    private ChanetTradeDataDao chanetTradeDataDao;
    @Autowired
    private AdDetailCpsDao adDetailCpsDao;
    @Autowired
    private AdEntryService adEntryService;
    
    public ChanetTradeData getChanetTradeDataByOcd(String ocd) {
        return chanetTradeDataDao.getChanetDataByOcd(ocd);
    }

    /**
     * 1、保存chanet交易数据 2、增加站长账户收入 3、增加站长明细
     * 
     * @param tradeData
     * @return
     */
    @Transactional(value = "buzzads", readOnly = false)
    public void saveTradeDate(ChanetTradeData tradeData) {
        // 保存chanet交易数据
        //chanetdata数据直接保存，新建事务直接提交，不必回滚
        Long id = chanetTradeDataDao.createChanetTradeData(tradeData);
        saveOrUpdateCommissionData(tradeData, id);
    }
    
    private void saveOrUpdateCommissionData(ChanetTradeData tradeData, long id) {

        AdDetailCps detail = getUuidAndAdInfo(tradeData.getUserinfo(), id);
        detail.setComm((int) tradeData.getCommission());
        detail.setPubComm((int) (tradeData.getCommission() * DEFAULT_PROPORTION / 100));
        detail.setTotalPrice((int) tradeData.getTotalPrice());
        detail.setTradeTime(tradeData.getDatetime());
        detail.setSource(OrderSourceEnum.CHANET);
        detail.setCpsOid(tradeData.getOcd());
        detail.setStatus(TradeConfirmEnum.findByValue(tradeData.getConfirm()));
        
        AdDetailCps oldDetail = adDetailCpsDao.getAdDetailCpsByOcdId(tradeData.getOcd(), 
                OrderSourceEnum.CHANET);
        
        //处理CPS订单完成通知
        if (TradeStatusEnum.TRADE_COMPLETE.getCode() == tradeData.getStatus() && 
                TradeConfirmEnum.NO_CONFIRM.getCode() == tradeData.getConfirm()) {
            if (oldDetail != null) {
                logAndThrowException("成果网交易数据异常：CPS订单重复。chanet_data_id:" + id);
            }

            adDetailCpsDao.create(detail);
            analyticsKestrelClient.put(AdAnalyticsConstants.ADCPS_QUEUE, HessianSerializer.serialize(detail));
        } else if (TradeConfirmEnum.NO_CONFIRM.getCode() != tradeData.getConfirm()) {
            //处理CPS订单认证通知
            if (oldDetail == null) {
                logAndThrowException("成果网交易数据异常：确认不存在的CPS订单。chanet_data_id:" + id);
            } else if (oldDetail.getStatus() != TradeConfirmEnum.NO_CONFIRM) {
                logAndThrowException("成果网交易数据异常：重复确认CPS订单。chanet_data_id:" + id);
            }
            
            Date confirmTime = new Date();
            adDetailCpsDao.updateForConfirmation(oldDetail.getId(), 
                            detail.getPubComm(), detail.getComm(), tradeData.getConfirm(), confirmTime);
            
            if (TradeConfirmEnum.CONFIRM_OK.getCode() == tradeData.getConfirm()) {
                detail.setConfirmTime(confirmTime);
                analyticsKestrelClient.put(AdAnalyticsConstants.ADCPS_QUEUE, HessianSerializer.serialize(detail));
            }
        }
    }
    
    
    private AdDetailCps getUuidAndAdInfo(String userInfo, Long id) {
        if (StringUtils.isEmpty(userInfo)) {
            logAndThrowException("成果网交易数据异常：无UserInfo。chanet_data_id:" + id);
        }
        String[] uuidAdEntryIdProduct = userInfo.split("_");
        
        String uuid = uuidAdEntryIdProduct[0];
        //检验uuid的合法性
        try {
            UUID.fromString(uuid);
        } catch (Exception e) {
            logAndThrowException("成果网交易数据异常：UUID错误。chanet_data_id:" + id);
        }
        
        Integer adEntryId = null;
        try {
            adEntryId = Integer.valueOf(uuidAdEntryIdProduct[1]);
        } catch (Exception e) {
            logAndThrowException("成果网交易数据异常：无效的EntryID。chanet_data_id:" + id);
        }
        
        AdEntry entry = adEntryService.getEntryById(adEntryId);
        if (entry == null) {
            logAndThrowException("成果网交易数据异常：无效的广告项。chanet_data_id:" + id);
        }
        AdNetworkEnum network = AdNetworkEnum.LEZHI;
        try {
            network = AdNetworkEnum.valueOf(uuidAdEntryIdProduct[2]);
        } catch (Exception e) {
            log.info("没有正确的Product类型， 默认为LEZHI");
        }
        
        AdDetailCps detail = new AdDetailCps();
        detail.setUuid(uuid);
        detail.setAdOrderId(entry.getOrderId());
        detail.setAdEntryId(adEntryId);
        detail.setNetwork(network);
        return detail;
    }
    
    private void logAndThrowException(String msg) {
        log.warn(msg);
        throw new ServiceException(msg);
    }
}
