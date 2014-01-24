package com.buzzinate.buzzads.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.buzzinate.buzzads.common.exception.ServiceException;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.dao.ChanetCampaignDao;
import com.buzzinate.buzzads.domain.ChanetDTO;
import com.buzzinate.common.util.http.SimpleHttpClient;
import com.buzzinate.common.util.task.AbstractSingleServerTask;
/**
 * 获取成果网cps广告，加入buzzAds广告市场
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-11-19
 */
public class ChanetCampTask extends AbstractSingleServerTask {
    
    private static final Log LOGGER = LogFactory.getLog(ChanetCampTask.class);
    
    //CPS请求url
    private static final String CHANET_CPS_URL = ConfigurationReader.getString("ads.network.chanet.apiurl");
    //与成果网约定的key
    private static final String CHANET_KEY = ConfigurationReader.getString("ads.network.chanet.key");
    //与成果网约定的网站id
    private static final String OUR_CHANET_SITEID = ConfigurationReader.getString("ads.network.chanet.siteid");
    //root element name
    private static final String ROOT_ELEMENT_NAME = "billingLinks";
    //成果失败状态
    private static final String CHANET_FAIL_STATUS = "-1";
    //成果成功状态
    private static final String CHANET_SUCCESS_STATUS = "0";
    
    @Autowired
    private ChanetCampaignDao chanetCampaignDao;
    @Autowired
    private ChanetCampService chanetCampService;
    
    
    public ChanetCampTask() {
        //定时任务运行主机
        super(ConfigurationReader.getString("ads.network.chanet.refreshtask.host"));
    }
    
    
    /**
     * 定时执行chanet计划的更新
     * 同步更新订单
     */
    @Override
    public void doJob() {
        List<ChanetDTO> newChanetCamps = getCpsDatas();
        try {
            long time = System.currentTimeMillis();
            List<Long> liveCampids = new ArrayList<Long>(); 
            for (ChanetDTO dto : newChanetCamps) {
                if (StringUtils.isBlank(dto.getChanetLink()))
                    continue;
                //根据campId查询，有更新，无插入
                ChanetDTO dbObj = chanetCampaignDao.getChanetCampaignByCampId(dto.getCampaignId());
                if (dbObj != null) {
                    //更新
                    chanetCampService.updateChanetCamp(dbObj, dto);
                } else {
                    //新建
                    chanetCampService.createNewChanetCampaign(dto);
                }
                //保存live状态广告
                liveCampids.add(dto.getCampaignId());
            }
            //终止计划
            List<ChanetDTO> terminateList = null;
            if (!liveCampids.isEmpty()) {
                terminateList = chanetCampaignDao.listWantTerminateChanetCamp(liveCampids);
            } else {
                terminateList = chanetCampaignDao.listLiveChanetCampaign();
            }
            if (terminateList != null) {
                chanetCampService.terminateChanetCampaign(terminateList);
            }
            LOGGER.info("刷新广告耗时：" + (System.currentTimeMillis() - time) + "毫秒");
        } catch (Exception e) {
            throw new ServiceException("刷新CHANET广告发生异常", e);
        }
    }

    public List<ChanetDTO> getCpsDatas() {
        String xml = requestChanetXmlData();
        //转换xml数据
        List<ChanetDTO> datas = parseXmlToDtoWithDom4j(xml);
        return datas;
    }
    
    /*
     * dom4j解析
     */
    @SuppressWarnings("rawtypes")
    private List<ChanetDTO> parseXmlToDtoWithDom4j(String xml) {
        Long time = System.currentTimeMillis();
        List<ChanetDTO> datas = new ArrayList<ChanetDTO>();
        try {
            Document docment = DocumentHelper.parseText(xml);
            Element root = docment.getRootElement();
            if (ROOT_ELEMENT_NAME.equals(root.getName())) {
                String successOrFail = root.attributeValue("status");
                if (CHANET_FAIL_STATUS.equals(successOrFail)) {
                    //失败，日志打出错误信息
                    LOGGER.error("chengguo xml data error,errorcode=" + root.elementText("errorcode") + 
                            " ,errormsg=" + root.elementText("errormsg"));
                } else if (CHANET_SUCCESS_STATUS.equals(successOrFail)) {
                    //成功
                    //枚举所有子节点
                    for (Iterator it = root.elementIterator(); it.hasNext();) {
                        Element e = (Element) it.next();
                        ChanetDTO dto = new ChanetDTO();
                        dto.setAdvertiser(e.elementText("advertiser"));
                        dto.setCampaignId(Long.valueOf(e.elementText("campaignId")));
                        dto.setCampaignName(e.elementText("campaignName"));
                        dto.setCampaignDomain(e.elementText("campaignDomain"));
                        dto.setSiteId(e.elementText("siteId"));
                        dto.setSiteName(e.elementText("siteName"));
                        dto.setChanetLink(e.elementText("chanetLink"));
                        dto.setAdvertiserLink(e.elementText("advertiserLink"));
                        dto.setRuleXml(e.elementText("campaignRules"));
                        datas.add(dto);
                    }
                }
            }
        } catch (DocumentException e) {
            LOGGER.error("parse xml to document", e);
            throw new ServiceException("获取CHANET数据发生异常", e);
        }
        time = System.currentTimeMillis() - time;
        LOGGER.info("获取成果网数据耗时：" + time + "毫秒");
        return datas;
    }
    
    /*
     * 请求成果网数据
     */
    private String requestChanetXmlData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("asid", OUR_CHANET_SITEID);
        params.put("content", "all");
        long epoch = System.currentTimeMillis() / 1000;
        params.put("timestamp", String.valueOf(epoch));
        params.put("sig", getChanetSign(String.valueOf(epoch)));
        SimpleHttpClient client = new SimpleHttpClient(1, 10, 10000, 10000);
        return client.post(CHANET_CPS_URL, params);
    }
    
    /*
     * 构造成果网签名串
     */
    private String getChanetSign(String currentTimestamp) {
        String sign = CHANET_KEY + currentTimestamp;
        return DigestUtils.md5Hex(sign);
    }
}
