package com.buzzinate.buzzads.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.AdsTypeEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.buzzads.enums.WeekDay;
import com.buzzinate.buzzads.stats.BaseDailyStats;
import com.buzzinate.common.util.DateTimeUtil;
/**
 * 订单
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-11-22
 */
public class AdOrder extends BaseDailyStats implements Serializable {

    private static final long serialVersionUID = 4067688886568616905L;
    private static final Logger LOG = LoggerFactory.getLogger(AdOrder.class);
    
    private int id;
    private int campaignId;
    private int advertiserId;
    private String name;
    private AdStatusEnum status = AdStatusEnum.ENABLED;
    private AdsTypeEnum adsType = AdsTypeEnum.AVERAGE;
    //逗号分隔的媒体id
    private String channelsTarget;
    private int orderFrequency;
    private int entryFrequency;
    private Integer bidPrice;
    private String keywords;
    private Date startDate;
    private Date endDate;
    private Set<WeekDay> scheduleDay = EnumSet.noneOf(WeekDay.class);
    private ScheduleTime scheduleTime;
    private Date updateAt = new Date();
    private String statusDesc = "";
    //用逗号分隔开的categoryid串
    private String audienceCategories;
    //用逗号分隔开的category名字串
    private String audienceCategoriesName;

    // 补充信息
    private String campName;
    private String companyName;
    private BidTypeEnum campBidType;
    private List<String> channelDomains;

    public int getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(int advertiserId) {
        this.advertiserId = advertiserId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(Integer bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Set<WeekDay> getScheduleDay() {
        return scheduleDay;
    }

    public void setScheduleDay(Set<WeekDay> scheduleDay) {
        this.scheduleDay = scheduleDay;
    }
    
    public void setScheduleDayStr(String scheduleDayStr) {
        if (StringUtils.isNotBlank(scheduleDayStr)) {
            Set<WeekDay> weekDay = EnumSet.noneOf(WeekDay.class);
            String[] scheduleDay = scheduleDayStr.split(",");
            for (int i = 0; i < scheduleDay.length; i++) {
                weekDay.add(WeekDay.findByValue(Integer.valueOf(scheduleDay[i])));
            }
            this.scheduleDay = weekDay;
        }
    }

    public String getScheduleTimeStr() {
        if (scheduleTime != null)
            return scheduleTime.getTimeStr();
        return null;
    }

    public void setScheduleTimeStr(String scheduleTimeSTr) {
        if (StringUtils.isNotBlank(scheduleTimeSTr)) {
            try {
                String[] times = scheduleTimeSTr.split("-");
                ScheduleTime st = new ScheduleTime();
                st.setStart(LocalTime.parse(times[0]));
                st.setEnd(LocalTime.parse(times[1]));
                this.scheduleTime = st;
            } catch (Exception e) {
                LOG.error("wrong scheduleTime string" + scheduleTimeSTr);
            }
        }
    }
    
    public String getViewDay() {
        StringBuilder sb = new StringBuilder();
        if (scheduleDay != null && !scheduleDay.isEmpty()) {
            for (WeekDay wd : scheduleDay) {
                sb.append(WeekDay.getCNView(wd.name())).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } else {
            return "全天候展示";
        }
    }
    
    public String getDayValue() {
        StringBuilder sb = new StringBuilder();
        if (scheduleDay != null && !scheduleDay.isEmpty()) {
            for (WeekDay wd : scheduleDay) {
                sb.append(wd.getCode()).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } else {
            return null;
        }
    }

    public ScheduleTime getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(ScheduleTime scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AdStatusEnum getStatus() {
        return status;
    }

    public void setStatus(AdStatusEnum status) {
        this.status = status;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getCampName() {
        return campName;
    }

    public void setCampName(String campName) {
        this.campName = campName;
    }
    
    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getBidPriceDouble() {
        if (bidPrice == null)
            bidPrice = Integer.valueOf(0);
        return doubleformat.format((double) bidPrice / 100);
    }
    
    public String getBidPriceDoubleNoComma() {
        if (bidPrice == null)
            bidPrice = Integer.valueOf(0);
        return doubleNoCommaFormat.format((double) bidPrice / 100);
    }
    
    public String getStartDateStr() {
        if (startDate != null)
            return DateTimeUtil.formatDate(startDate);
        return "";
    }
    
    public String getEndDateStr() {
        if (endDate != null)
            return DateTimeUtil.formatDate(endDate);
        return "";
    }
    
    public String getStatusName() {
        return this.statusDesc + AdStatusEnum.getCnName(this.status);
    }
    
    public String getAudienceCategories() {
        return audienceCategories;
    }
    
    public void setAudienceCategories(String audienceCategories) {
        this.audienceCategories = audienceCategories;
    }
    
    public String getAudienceCategoriesName() {
        return audienceCategoriesName;
    }
    
    public void setAudienceCategoriesName(String audienceCategoriesName) {
        this.audienceCategoriesName = audienceCategoriesName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public AdsTypeEnum getAdsType() {
        return adsType;
    }

    public void setAdsType(AdsTypeEnum adsType) {
        this.adsType = adsType;
    }

    public String getChannelsTarget() {
        return channelsTarget;
    }

    public void setChannelsTarget(String channelsTarget) {
        this.channelsTarget = channelsTarget;
    }

    public int getOrderFrequency() {
        return orderFrequency;
    }

    public void setOrderFrequency(int orderFrequency) {
        this.orderFrequency = orderFrequency;
    }

    public int getEntryFrequency() {
        return entryFrequency;
    }

    public void setEntryFrequency(int entryFrequency) {
        this.entryFrequency = entryFrequency;
    }

    public BidTypeEnum getCampBidType() {
        return campBidType;
    }

    public void setCampBidType(BidTypeEnum campBidType) {
        this.campBidType = campBidType;
    }

    public List<String> getChannelDomains() {
        return channelDomains;
    }

    public void setChannelDomains(List<String> channelDomains) {
        this.channelDomains = channelDomains;
    }

    public int getFrequencyCap() {
        if (orderFrequency != 0) {
            return orderFrequency;
        } else {
            return entryFrequency;
        }
    }

}
