package com.buzzinate.buzzads.domain;

import org.safehaus.uuid.UUID;
import com.buzzinate.buzzads.enums.AdEntryTypeEnum;
import com.buzzinate.buzzads.enums.AdNetworkEnum;
import com.buzzinate.buzzads.enums.ChannelStatusEnum;
import com.buzzinate.buzzads.enums.ChannelStyleEnum;
import com.buzzinate.buzzads.enums.ChannelTypeEnum;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * 广告单元
 *
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 *         <p/>
 *         2013-05-13
 */
public class Channel implements Serializable {

    private static final long serialVersionUID = 2798112832569679533L;
    private static DecimalFormat format = new DecimalFormat("#,###,##0.00%");
    private static DecimalFormat minCPMFormat = new DecimalFormat("#,###,##0.00");
    private int id;
    private String domain;
    private String uuid;
    //默认值为二级媒体
    private int level;
    private ChannelTypeEnum type;
    private ChannelStatusEnum status = ChannelStatusEnum.OPENED;
    private Set<AdNetworkEnum> netWork = EnumSet.of(AdNetworkEnum.LEZHI);
    private AdEntryTypeEnum adType = AdEntryTypeEnum.TEXT;
    //广告类别
    private Set<ChannelStyleEnum> style = EnumSet.of(ChannelStyleEnum.STYLE_120_120);
    // 开启时间
    private Date openTime;
    // 关闭时间
    private Date closeTime;
    // 最小CPM，范围为【0-100000】,含2位精度
    private int minCPM;
    // 广告位截图url
    private String adThumb;
    // 前一日的PV
    private int dailyViews;
    // 前一日的点击
    private int dailyClicks;
    // 操作理由
    private String operateResult;
    private ChannelStatusEnum statusSelect;
    //媒体名称
    private String name;
    // 媒体关联站长的userId
    private Integer userId;
    private String email;
    
    private List<Channel> subChannels;

    public ChannelStatusEnum getStatusSelect() {
        return statusSelect;
    }

    public void setStatusSelect(String statusSelect) {
        this.statusSelect = ChannelStatusEnum.findByKey(statusSelect);
    }

    public String getOperateResult() {
        return operateResult;
    }

    public void setOperateResult(String operateResult) {
        this.operateResult = operateResult;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Used by Hibernate to get UUID from database.
     *
     * @return
     */
    public byte[] getUuidBytes() {
        if (this.uuid != null) {
            UUID uuidx = new UUID(this.uuid);
            return uuidx.asByteArray();
        } else {
            return null;
        }
    }

    /**
     * Used by Hibernate to set UUID in database.
     *
     * @param uid
     */
    public void setUuidBytes(byte[] uid) {
        if (uid != null) {
            UUID uuidx = new UUID(uid);
            this.uuid = uuidx.toString();
        } else {
            this.uuid = null;
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDisplayType() {
        if (type == null) {
            return "";
        }
        return ChannelTypeEnum.getSelector().get(type.getCode());
    }
    
    public ChannelTypeEnum getType() {
        return type;
    }
    
    public void setType(ChannelTypeEnum type) {
        this.type = type;
    }

    public String getAdThumb() {
        return adThumb;
    }

    public void setAdThumb(String adThumb) {
        this.adThumb = adThumb;
    }

    public ChannelStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ChannelStatusEnum status) {
        this.status = status;
    }

    public Set<AdNetworkEnum> getNetWork() {
        return netWork;
    }

    public void setNetWork(Set<AdNetworkEnum> netWork) {
        if (netWork != null && !netWork.isEmpty()) {
            this.netWork = EnumSet.copyOf(netWork);
        }
    }

    public AdEntryTypeEnum getAdType() {
        return adType;
    }

    public void setAdType(AdEntryTypeEnum adType) {
        this.adType = adType;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public String getMinCPMText() {
        return minCPMFormat.format((double) minCPM / 100);
    }

    public int getMinCPM() {
        return minCPM;
    }

    public void setMinCPM(int minCPM) {
        this.minCPM = minCPM;
    }

    public int getDailyViews() {
        return dailyViews;
    }

    public void setDailyViews(int dailyViews) {
        this.dailyViews = dailyViews;
    }

    public int getDailyClicks() {
        return dailyClicks;
    }

    public void setDailyClicks(int dailyClicks) {
        this.dailyClicks = dailyClicks;
    }

    public String getDailyCTR() {
        if (dailyViews == 0)
            return "-";
        return format.format((double) dailyClicks / dailyViews);
    }

    public Set<ChannelStyleEnum> getStyle() {
        return style;
    }

    public void setStyle(Set<ChannelStyleEnum> style) {
        if (style != null && !style.isEmpty()) {
            this.style = EnumSet.copyOf(style);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Channel channel = (Channel) o;

        if (id != channel.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Channel> getSubChannels() {
        return subChannels;
    }

    public void setSubChannels(List<Channel> subChannels) {
        this.subChannels = subChannels;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
