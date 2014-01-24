package com.buzzinate.buzzads.core.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buzzinate.buzzads.common.service.AdsBaseService;
import com.buzzinate.buzzads.core.dao.AdxDspRtbInfoDao;
import com.buzzinate.buzzads.core.dao.AdxDspRtbStatisticDao;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.domain.AdxDspRtbInfo;
import com.buzzinate.buzzads.domain.AdxDspRtbStatistic;
import com.buzzinate.buzzads.util.MailGunClient;
import com.pennywise.framework.email.MailInfo;

/**
 * @author harry
 */
@Service
public class AdxDspRtbInfoService extends AdsBaseService {

    private static Log log = LogFactory.getLog(AdxDspRtbInfoService.class);
    private static final String BUZZ_MAIL = ConfigurationReader.getString("adx.buzzinate.email");
    private static final String BUZZ_USER = ConfigurationReader.getString("adx.buzzinate.email.user");
    private static final String ADX_MAIL = ConfigurationReader.getString("adx.buzzinate.email");
    private static final String ADX_USER = ConfigurationReader.getString("adx.buzzinate.email.user");
    
    @Autowired
    private AdxDspRtbInfoDao adxDspRtbInfoDao;
    @Autowired
    private AdxDspRtbStatisticDao adxDspRtbStatisticDao;
    
    /**
     * 注册或更新DSP 实时竞价信息
     *
     * @param rtbInfo
     */
    @Transactional(value = "buzzads", readOnly = false)
    public void saveOrUpdate(AdxDspRtbInfo rtbInfo) {
        int key = rtbInfo.getKey();
        int dspId = adxDspRtbInfoDao.getRtbInfoByKey(key).getId();
        if (dspId == 0) {
            adxDspRtbInfoDao.create(rtbInfo);
//            eventServices.sendAdCreateEvent(order.getId(), IdType.ORDER);
        } else {
            adxDspRtbInfoDao.updateRtbInfoByKey(key, rtbInfo.getWinnerNotifyUrl(), rtbInfo.getBidRequestUrl());
        }
        // TODO   通知RTB操作相关的DSP信息 
    }
    
    /**
     * 注册或更新DSP 实时竞价信息
     *
     * @param rtbInfo
     * @throws Exception 
     */
    @Transactional(value = "buzzads", readOnly = false)
    public int deleteRtbInfo(Integer key) throws Exception {
        int dspId = adxDspRtbInfoDao.getRtbInfoByKey(key).getId();
        if (dspId == 0) {
            // not exist return -1
            return -1;
        } else {
            adxDspRtbInfoDao.delete(dspId);
            return 0;
        }
        // TODO   通知RTB操作相关的DSP信息 
    }
    
    /**
     * dsp统计dsp信息
     * 
     * */
    @Transactional(value = "buzzads", readOnly = false)
    public void updateAdxDspStatistic(AdxDspRtbStatistic stats) {
        adxDspRtbStatisticDao.updateAdxDspStatistic(stats);
    }
    
//    public AdxDspRtbInfo getAdxDspRtbInfoById(Integer dspId) {
//        return adxDspRtbInfoDao.getAdxDspRtbInfoById(dspId);
//    }
    
    public AdxDspRtbInfo getAdxDspRtbInfoByKey(Integer key) {
        return adxDspRtbInfoDao.getRtbInfoByKey(key);
    }
    
    /**
     * send dspRtb mail to ads
     * 
     * @param   dspInfo     
     *              AdxDspRtbInfo 
     * @param   dspInfo     
     *              AdxDspRtbStatistic 
     *              
     * */
    public void sendDspWarningEmail(AdxDspRtbInfo dspInfo, AdxDspRtbStatistic stats) {
        MailInfo mailInfo = new MailInfo();
        mailInfo.setFrom(BUZZ_MAIL, BUZZ_USER);
        mailInfo.addTOAddress(ADX_MAIL, ADX_USER);
        mailInfo.setSubject(ConfigurationReader.getString("adx.buzzinate.email.warning.subject"));
        mailInfo.setContentType("html");
        Map<String, String> map = new HashMap<String, String>();
        map.put("&key&", String.valueOf(dspInfo.getKey()));
        map.put("&bidRequestUrl&", dspInfo.getBidRequestUrl());

        String message = replacePlaceHolders(ConfigurationReader.getString("adx.buzzinate.email.warning.body"), map);
        mailInfo.setMessage(message);
        try {
            MailGunClient.sendMail(mailInfo);
        } catch (Exception e) {
            log.error("Failed to send dsp warning email", e);
        }
    }

    /**
     * Replace the place holders in the given string and return the result
     *
     * @param body
     * @param map
     * @return
     */
    private String replacePlaceHolders(String body, Map<String, String> map) {
        String result = body;
        Set<Entry<String, String>> s = map.entrySet();
        Iterator<Entry<String, String>> iterator = s.iterator();
        while (iterator.hasNext()) {
            String temp = iterator.next().getKey();
            result = result.replace(temp, map.get(temp));
        }
        return result;
    }

}