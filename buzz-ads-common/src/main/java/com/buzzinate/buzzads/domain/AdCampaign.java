package com.buzzinate.buzzads.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buzzinate.buzzads.data.converter.ProvinceNameConverter;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.buzzads.enums.AdStatusEnum;
import com.buzzinate.buzzads.enums.BidTypeEnum;
import com.buzzinate.buzzads.stats.BaseDailyStats;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.ip.ProvinceName;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2013-2-27
 */
public class AdCampaign extends BaseDailyStats implements Serializable {

    private static final long serialVersionUID = -7233302515733333408L;
    private static final Logger LOG = LoggerFactory.getLogger(AdCampaign.class);

    private int id;
    private int advertiserId;
    private String name;
    private AdStatusEnum status = AdStatusEnum.ENABLED;
    private Set<AdNetworkEnum> network = EnumSet.of(AdNetworkEnum.LEZHI);
    private BidTypeEnum bidType = BidTypeEnum.CPC;
    private Date startDate;
    private Date endDate;
    private Set<ProvinceName> locations = EnumSet.noneOf(ProvinceName.class);
    private Date updateAt = new Date();
    
    private List<CampaignDayBudget> dayBudgets;

    // 预算设置数据
    private long maxDayBudget;
    private long maxBudgetTotal;

    private List<AdOrder> adOrders;
    
    private String companyName;

    private int adOrderCount;

    private int adEntryCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(int advertiserId) {
        this.advertiserId = advertiserId;
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

    public Set<AdNetworkEnum> getNetwork() {
        return network;
    }

    public void setNetwork(Set<AdNetworkEnum> network) {
        this.network = network;
    }

    public BidTypeEnum getBidType() {
        return bidType;
    }

    public void setBidType(BidTypeEnum bidType) {
        this.bidType = bidType;
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

    public String getLocationStr() {
        StringBuilder sb = new StringBuilder();
        if (locations != null && !locations.isEmpty()) {
            for (ProvinceName pn : locations) {
                sb.append(pn.getCode()).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public void setLocationStr(String locationStr) {
        if (StringUtils.isNotBlank(locationStr)) {
            try {
                String[] locationStrs = locationStr.split(",");
                this.locations = ProvinceNameConverter.fromStringSet(locationStrs);
            } catch (Exception e) {
                LOG.error("wrong location sttring" + locationStr);
            }
        }
    }

    public String getViewLocation() {
        StringBuilder sb = new StringBuilder();
        if (locations != null && !locations.isEmpty()) {
            for (ProvinceName pn : locations) {
                sb.append(ProvinceName.getProvinceName(pn.getCode())).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public Set<ProvinceName> getLocations() {
        return locations;
    }

    public void setLocations(Set<ProvinceName> locations) {
        this.locations = locations;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public long getMaxDayBudget() {
        return maxDayBudget;
    }

    public void setMaxDayBudget(long maxDayBudget) {
        this.maxDayBudget = maxDayBudget;
    }

    public long getMaxBudgetTotal() {
        return maxBudgetTotal;
    }

    public void setMaxBudgetTotal(long maxBudgetTotal) {
        this.maxBudgetTotal = maxBudgetTotal;
    }

    public String getNetWorkStr() {
        StringBuilder sb = new StringBuilder();
        if (network != null && network.size() > 0) {
            for (AdNetworkEnum n : network) {
                sb.append(n.name()).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public List<AdOrder> getAdOrders() {
        return adOrders;
    }

    public void setAdOrders(List<AdOrder> adOrders) {
        this.adOrders = adOrders;
    }
    
    public String getMaxDayBudgetDouble() {
        return doubleformat.format((double) maxDayBudget / 100);
    }
    
    public String getMaxTotalBudgetDouble() {
        return doubleformat.format((double) maxBudgetTotal / 100);
    }
    
    public String getMaxDayBudgetDoubleNoComma() {
        return doubleNoCommaFormat.format((double) maxDayBudget / 100);
    }
    
    public String getMaxTotalBudgetDoubleNoComma() {
        return doubleNoCommaFormat.format((double) maxBudgetTotal / 100);
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
        return AdStatusEnum.getCnName(this.status);
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<CampaignDayBudget> getDayBudgets() {
        return dayBudgets;
    }

    public void setDayBudgets(List<CampaignDayBudget> dayBudgets) {
        this.dayBudgets = dayBudgets;
    }

    public int getAdOrderCount() {
        return adOrderCount;
    }

    public void setAdOrderCount(int adOrderCount) {
        this.adOrderCount = adOrderCount;
    }

    public int getAdEntryCount() {
        return adEntryCount;
    }

    public void setAdEntryCount(int adEntryCount) {
        this.adEntryCount = adEntryCount;
    }
}
