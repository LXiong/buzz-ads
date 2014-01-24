package com.buzzinate.buzzads.core.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buzzinate.buzzads.common.exception.ServiceException;
import com.buzzinate.buzzads.common.service.AdsBaseService;
import com.buzzinate.buzzads.core.dao.PublisherContactInfoDao;
import com.buzzinate.buzzads.core.dao.StatsAdDailyDao;
import com.buzzinate.buzzads.core.dao.StatsAdOrderDailyDao;
import com.buzzinate.buzzads.core.dao.StatsAdminDailyDao;
import com.buzzinate.buzzads.core.dao.StatsCampaignDailyDao;
import com.buzzinate.buzzads.core.dao.StatsPublisherDailyDao;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.core.util.LogConstants;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.core.util.RedisClient;
import com.buzzinate.buzzads.domain.Channel;
import com.buzzinate.buzzads.domain.PublisherContactInfo;
import com.buzzinate.buzzads.domain.Site;
import com.buzzinate.buzzads.enums.AdEntryTypeEnum;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.buzzads.enums.ChannelStatusEnum;
import com.buzzinate.buzzads.enums.ChannelTypeEnum;
import com.buzzinate.buzzads.enums.PublisherContactStausEnum;
import com.buzzinate.buzzads.user.LoginHelper;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.UuidUtil;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-12-12
 */
@Service
public class PublisherContactService extends AdsBaseService {
    
    // 默认分成比例
    private static final int DEFAULT_PROPORTION = ConfigurationReader.getInt("ads.default.uuid.proportion");
    private static final String PUBLISHER_PROPORTION_CACHE_KEY = "PUBLISHER_PROPORTION:";
    private static final int PUBLISHER_PROPORTION_CACHE_EXPIRE = 3600 * 24 * 30;
    
    @Autowired
    private PublisherContactInfoDao publisherContactInfoDao;
    @Autowired
    private SiteService siteService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private StatsAdDailyDao statsAdDailyDao;
    @Autowired
    private StatsAdOrderDailyDao statsAdOrderDailyDao;
    @Autowired
    private StatsCampaignDailyDao statsCampaignDailyDao;
    @Autowired
    private StatsPublisherDailyDao statsPublisherDailyDao;
    @Autowired
    private StatsAdminDailyDao statsAdminDailyDao;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private LoginHelper loginHelper;
    
    public List<PublisherContactInfo> listPublisherContacts(PublisherContactInfo publisher, Pagination page) {
        List<PublisherContactInfo> publishers = publisherContactInfoDao.listPublisherContacts(publisher, page);
        for (PublisherContactInfo p : publishers) {
            List<Site> sites = siteService.getUuidSiteByUserId(p.getUserId());
            p.setSites(sites);
        }
        return publishers;
    }
    
    /**
     * 更新站长状态
     * @param userId
     * @param status
     * @return
     */
    public int updatePublisherStatus(int userId, PublisherContactStausEnum status) {
        if (userId <= 0)
            throw new ServiceException("网站主ID非法");
        
        PublisherContactInfo publisher = publisherContactInfoDao.read(userId);
        if (publisher == null)
            throw new ServiceException("网站主不存在");
        
        //冻结操作
        if (PublisherContactStausEnum.FROZEN.equals(status) && 
                PublisherContactStausEnum.FROZEN.equals(publisher.getStatus())) {
            throw new ServiceException("非法操作:已经被冻结");
        }
        
        //解冻操作
        if (PublisherContactStausEnum.NORMAL.equals(status) && 
                PublisherContactStausEnum.NORMAL.equals(publisher.getStatus())) {
            throw new ServiceException("非法操作:非冻结状态");
        }
        
        //执行db更新操作
        int count =  publisherContactInfoDao.updatePublisherStatus(userId, status.getCode());
        if (count == 1) {
            //记录日志
            Map<String, String> params = new HashMap<String, String>();
            params.put("pubId", String.valueOf(userId));
            params.put("pubCount", publisher.getEmail());
            if (PublisherContactStausEnum.FROZEN.equals(status)) {
                this.addOperationLog(LogConstants.OPTYPE_PUBLISHER_FROZEN, 
                        LogConstants.OBJNAME_PUBLISHER, String.valueOf(userId), String.valueOf(userId), params);
            } else {
                this.addOperationLog(LogConstants.OPTYPE_PUBLISHER_UNFROZEN, 
                        LogConstants.OBJNAME_PUBLISHER, String.valueOf(userId), String.valueOf(userId), params); 
            }
        }
        return count;
    }
    
    /**
     * 查看站长信息
     * @param userId
     * @return
     */
    public PublisherContactInfo viewPublisherContactInfo(int userId) {
        PublisherContactInfo info = this.getPublisherContactInfoByUserId(userId);
        if (info == null) {
            info = new PublisherContactInfo();
        }
        info.resetProportion(DEFAULT_PROPORTION);
        info.setSites(siteService.getUuidSiteByUserId(userId));
        return info;
    }
    
    /**
     * 根据userId获得站长信息
     * @param userId
     * @return
     */
    public PublisherContactInfo getPublisherContactInfoByUserId(int userId) {
        return publisherContactInfoDao.read(userId);
    }
    
    /**
     * 创建或者更新站长信息，用于api接口
     * @param info
     */
    public void saveOrUpdatePublisherContact(PublisherContactInfo info) {
        validate(info);
        publisherContactInfoDao.saveOrUpdate(info);
    }
    
    /**
     * 更新站长信息，
     * 1、用于admin执行
     * 2、站长自行更新也会执行
     * @param info
     */
    public void saveOrUpdatePublisher(PublisherContactInfo info) {
        validate(info);
        
        PublisherContactInfo orgPublisher = publisherContactInfoDao.read(info.getUserId());
        if (orgPublisher == null) {
            publisherContactInfoDao.create(info);
            //默认创建媒体
            List<Site> sites = siteService.getUuidSiteByUserId(info.getUserId());
            if (sites.size() > 0) {
                for (Site site : sites) {
                    Channel channel = new Channel();
                    channel.setDomain(site.getUrl());
                    channel.setUuid(site.getUuid());
                    channel.setStatus(ChannelStatusEnum.OPENED);
                    channel.setAdType(AdEntryTypeEnum.UNKNOWN);
                    channel.setLevel(1);
                    channel.setNetWork(EnumSet.of(AdNetworkEnum.LEZHI));
                    channel.setType(ChannelTypeEnum.UNKNOWN);
                    channel.setOpenTime(DateTimeUtil.getCurrentDate());
                    //
                    channel.setMinCPM(0);
                    channel.setAdThumb("");
                    channelService.createChannel(channel);
                }
            }
        } else {
            publisherContactInfoDao.update(info);
            // 与原始分成比例进行比较是否需要更新数据(只有admin权限下才能操作)
            orgPublisher.resetProportion(DEFAULT_PROPORTION);
            if (loginHelper.isLoginAsAdmin() && info.getProportion() != 0 && orgPublisher.getProportion() != info.getProportion()) {
                // 刷新分成比例缓存
                redisClient.set(PUBLISHER_PROPORTION_CACHE_KEY + info.getUserId(), PUBLISHER_PROPORTION_CACHE_EXPIRE, info.getProportion());
                List<Site> sites = siteService.getUuidSiteByUserId(99);
                List<byte[]> uuidList = new ArrayList<byte[]>();
                for (Site site : sites) {
                    uuidList.add(UuidUtil.uuidToByteArray(site.getUuid()));
                }
                // update related pubComm via by new thread
                new ProportionThread(uuidList, info.getProportion(), statsAdDailyDao, statsAdOrderDailyDao, statsCampaignDailyDao,
                         statsPublisherDailyDao, statsAdminDailyDao).start();
            }
            
            //记录日志
            Map<String, String> params = new HashMap<String, String>();
            params.put("sourcePub", orgPublisher.toString());
            params.put("newPub", info.toString());
            this.addOperationLog(LogConstants.OPTYPE_PUBLISHER_UPDATE, LogConstants.OBJNAME_PUBLISHER, 
                    String.valueOf(info.getUserId()), String.valueOf(info.getUserId()), params);
        }
        
    }
    
    class ProportionThread extends Thread {
        List<byte[]> uuidList = new ArrayList<byte[]>();
        int proportion = 0;
        private StatsAdDailyDao statsAdDailyDao;
        private StatsAdOrderDailyDao statsAdOrderDailyDao;
        private StatsCampaignDailyDao statsCampaignDailyDao;
        private StatsPublisherDailyDao statsPublisherDailyDao;
        private StatsAdminDailyDao statsAdminDailyDao;
        
        public ProportionThread(List<byte[]> uuidList, int proportion,
                StatsAdDailyDao statsAdDailyDao,
                StatsAdOrderDailyDao statsAdOrderDailyDao,
                StatsCampaignDailyDao statsCampaignDailyDao,
                StatsPublisherDailyDao statsPublisherDailyDao,
                StatsAdminDailyDao statsAdminDailyDao) {
            super();
            this.uuidList = uuidList;
            this.proportion = proportion;
            this.statsAdDailyDao = statsAdDailyDao;
            this.statsAdOrderDailyDao = statsAdOrderDailyDao;
            this.statsCampaignDailyDao = statsCampaignDailyDao;
            this.statsPublisherDailyDao = statsPublisherDailyDao;
            this.statsAdminDailyDao = statsAdminDailyDao;
        }
        
        @Override
        public void run () {
            freshPubCommByPublisherId(uuidList, proportion);
        }
        
        private void freshPubCommByPublisherId(List<byte[]> uuidList, int proportion) {
            Date firstDayOfMonth = DateTimeUtil.getDateByString(DateTimeUtil.getFirstDayOfCurrentMonth(), DateTimeUtil.FMT_DATE_YYYY_MM_DD);
            
            Date nowTime = DateTimeUtil.plusDays(new Date(), 1);
            // ❶更新adDaily信息
            if (uuidList.size() <= 0) {
                return;
            }
            statsAdDailyDao.updateAdDailyStatProportion(uuidList, firstDayOfMonth, nowTime, proportion);
            // data sortion：
            //        orderid     
            //        dateDay     
            //        network     
            //        cpcPubComm
            //        cpsPubComm
            //        cpmPubComm
            
            // ②取得需要更新的order信息并更新
            List<Object[]> orderInfoList = statsAdDailyDao.getOrderInfoByProportion(uuidList, firstDayOfMonth, nowTime);
            if (orderInfoList.size() <= 0) {
                return;
            }
            List<Integer> orderIdArr = new ArrayList<Integer>();
            
            for (Object[] tmpArr : orderInfoList) {
                Integer orderid = (Integer) tmpArr[0];
                orderIdArr.add(orderid);
                Date dateDay = (Date) tmpArr[1];
                Integer network = (Integer) tmpArr[2];
                Integer cpcPubComm = (Integer) tmpArr[3];
                Integer cpsPubComm = (Integer) tmpArr[4];
                BigDecimal cpmPubComm = (BigDecimal) tmpArr[5];
                statsAdOrderDailyDao.updateOrderDailyProportion(orderid, dateDay, network, cpcPubComm, cpsPubComm, cpmPubComm);
            }
            
            // ③取得需要更新的campaign信息并更新
            List<Object[]> CapmInfoList = statsAdOrderDailyDao.getCampInfoByProportion(orderIdArr, firstDayOfMonth, nowTime);
            if (CapmInfoList.size() <= 0) {
                return;
            }
            
            for (Object[] tmpArr : CapmInfoList) {
                Integer campId = (Integer) tmpArr[0];
                Date dateDay = (Date) tmpArr[1];
                Integer network = (Integer) tmpArr[2];
                Integer cpcPubComm = (Integer) tmpArr[3];
                Integer cpsPubComm = (Integer) tmpArr[4];
                BigDecimal cpmPubComm = (BigDecimal) tmpArr[5];
                statsCampaignDailyDao.updateCampDailyProportion(campId, dateDay, network, cpcPubComm, cpsPubComm, cpmPubComm);
            }
            
            // ④更新publish关联的pubComm
            statsPublisherDailyDao.updateAllPublisherDailyStatistics(uuidList, firstDayOfMonth, nowTime, proportion);
            // add up the daily data of admin.
            // ⑤取得需要更新的campaign信息并更新
            List<Object[]> AdminInfoList = statsPublisherDailyDao.getAdminInfoByProportion(uuidList, firstDayOfMonth, nowTime);
            if (AdminInfoList.size() <= 0) {
                return;
            }
            
            for (Object[] tmpArr : AdminInfoList) {
                Date dateDay = (Date) tmpArr[1];
                Integer network = (Integer) tmpArr[2];
                Integer cpcPubComm = (Integer) tmpArr[3];
                Integer cpsPubComm = (Integer) tmpArr[4];
                BigDecimal cpmPubComm = (BigDecimal) tmpArr[5];
                statsAdminDailyDao.updateAdminDailyProportion(dateDay, network, cpcPubComm, cpsPubComm, cpmPubComm);
            }
            
        }
    }

    private void validate(PublisherContactInfo info) {
        if (StringUtils.isBlank(info.getName()) ||
                        StringUtils.isBlank(info.getEmail()) ||
                        StringUtils.isBlank(info.getMobile()) ||
                        info.getReceiveMethod() == null ||
                        StringUtils.isBlank(info.getReceiveAccount()) ||
                        StringUtils.isBlank(info.getReceiveName())) {
            throw new ServiceException("网站主信息不完整");
        }
    }
    
    /**
     * 
     * 通过站长id取得站长的分成比例
     * 如果通过数据库取得的分成比率小于或等于0，
     * 则取得配置文件中默认的分成比率
     * 
     * @param publisherId
     *          站长id
     * @return int
     *          站长分成比例
     * */
    public int getPublisherProportionById(Integer publisherId) {
        String publisherKey = PUBLISHER_PROPORTION_CACHE_KEY + publisherId;
        if (redisClient.getObject(publisherKey) != null) {
            return (Integer) redisClient.getObject(publisherKey);
        } else {
            PublisherContactInfo pubInfo = publisherContactInfoDao.read(publisherId);
            int proportion = DEFAULT_PROPORTION;
            if (pubInfo != null) {
                proportion = pubInfo.getProportion();
                if (proportion <= 0) {
                    proportion = DEFAULT_PROPORTION;
                }
            }
            redisClient.set(publisherKey, PUBLISHER_PROPORTION_CACHE_EXPIRE, proportion);
            return proportion;
        }
    }
    
    /**
     * 
     * 通过站长uuid取得站长的分成比例
     * 如果通过数据库取得的分成比率小于或等于0，
     * 则取得配置文件中默认的分成比率
     * 
     * @param publisherUuid
     *          站长uuid
     * @return int
     *          站长分成比例
     * */
    public int getPublisherProportionByUUID(String publisherUuid) {
        Site site = siteService.getUuidSiteByUuid(publisherUuid);
        if (site != null) {
            return getPublisherProportionById(site.getUserId());
        } else {
            // 如果通过uuid找不到网站的情况，则会在做分成时候校验排除。
            return DEFAULT_PROPORTION;
        }
    }

}
