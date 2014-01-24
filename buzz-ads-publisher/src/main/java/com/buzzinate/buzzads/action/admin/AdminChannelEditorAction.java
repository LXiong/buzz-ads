package com.buzzinate.buzzads.action.admin;

import com.buzzinate.buzzads.core.service.ChannelService;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.core.util.UpYun;
import com.buzzinate.buzzads.core.util.UrlUtil;
import com.buzzinate.buzzads.domain.Channel;
import com.buzzinate.buzzads.enums.*;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.JsonResults;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-5-14
 * Time: 下午12:39
 * 一/二级媒体详情页
 */
@Controller
@Scope("request")
public class AdminChannelEditorAction extends ActionSupport implements ServletRequestAware {
    private static final long serialVersionUID = 4291869424194185488L;
    private static final String LOG_TAG = "AdminChannelEditorAction";
    private static final String SPLIT_STRING_DOT = ".";
    private static final int MAX_FILE_SIZE = 512 * 1024;
    private static final String[] IMAGE_NORMAL_EXTENSION = {"jpg", "jpeg", "gif", "png"};
    private static final String UP_BUCKETNAME = ConfigurationReader.getString("up.yun.bucket.name", "lezhi");
    private static final String UP_USERNAME = ConfigurationReader.getString("up.yun.username", "buzzinate");
    private static final String UP_PASSWORD = ConfigurationReader.getString("up.yun.password", "buzzinate");
    private static Log LOG = LogFactory.getLog(AdminChannelEditorAction.class);
    private Channel channel;
    private Channel topChannel;
    //for image upload
    private transient InputStream inputStream;
    private String imageFileName;
    private String imageFileNameExtension;
    private JsonResults results;
    private transient HttpServletRequest request;
    private List<Channel> subChannels;
    private String reason;
    private Map<ChannelStatusEnum, String> statusSelector = ChannelStatusEnum.statusSelector();
    private Map<ChannelTypeEnum, String> typeSelector = ChannelTypeEnum.typeSelector();
    private Map<ChannelStyleEnum, String> styleSelector = ChannelStyleEnum.channelStyleSelector();
    private Map<AdNetworkEnum, String> networkSelector = AdNetworkEnum.networkSelector();
    private Map<AdEntryTypeEnum, String> adTypeSelector = AdEntryTypeEnum.adEntryTypeSelector();
    @Autowired
    private transient ChannelService channelService;

    /*
    * 新文件名
    */
    private static String generateFileName(String imageName) {
        String dir = DateTimeUtil.formatDate(DateTimeUtil.getCurrentDate(), DateTimeUtil.FMT_DATE_YYYYMMDD);
        String formatDate = DateTimeUtil.formatDate(DateTimeUtil.getCurrentDate(),
                DateTimeUtil.FMT_DATE_YYYYMMDDHHMMSS);
        int random = new Random().nextInt(10000);
        int position = imageName.lastIndexOf(SPLIT_STRING_DOT);
        String extension = imageName.substring(position);
        return "/bfp/channel/" + dir + "/" + formatDate + random + extension;
    }

    @SkipValidation
    public String uploadPic() {
        results = new JsonResults();
        // validation:
        if (StringUtils.isEmpty(imageFileName) || StringUtils.isEmpty(imageFileNameExtension) ||
                !ArrayUtils.contains(IMAGE_NORMAL_EXTENSION, imageFileNameExtension)) {
            results.fail("图片不符合标准格式");
            return SUCCESS;
        }

        // get file:
        MultiPartRequestWrapper multipartRequest = (MultiPartRequestWrapper) request;
        File[] files = multipartRequest.getFiles("tempFile");
        if (files.length <= 0) {
            results.fail("No file found!");
            return SUCCESS;
        }
        // check for file size:
        File fileSrc = files[0];
        if (fileSrc.length() > MAX_FILE_SIZE) {
            results.fail("文件太大");
            return SUCCESS;
        }

        String targetFileName = generateFileName(imageFileName);
        UpYun yun = new UpYun(UP_BUCKETNAME, UP_USERNAME, UP_PASSWORD);
        try {
            yun.writeFile(targetFileName, fileSrc, true);
            results.addContent("imageUrl", yun.getClientReadUrl(targetFileName, "buzz"));
        } catch (Exception e) {
            LOG.error(LOG_TAG, e);
            results.fail("上传又拍云出错");
        }
        inputStream = new ByteArrayInputStream(results.toJsonString().getBytes(Charset.forName("utf-8")));
        return SUCCESS;
    }

    @SkipValidation
    public String show() {
        if (channel == null) {
            addActionError("缺少查询参数");
            return INPUT;
        }
        if (channel.getId() <= 0) {
            addActionError("未知的媒体ID");
            return INPUT;
        }
        channel = channelService.getChannelById(channel.getId());
        if (channel == null) {
            addActionError("无效的媒体ID");
            return ERROR;
        }
        if (channel.getLevel() == 1) {
            subChannels = channelService.getSubChannelsByUUID(channel.getUuid());
            topChannel = channel;
        } else {
            //否则显示对应的二级媒体
            topChannel = channelService.getTopChannelsByUUID(channel.getUuid());
        }
        return SUCCESS;
    }

    @SkipValidation
    public String showCreateSubChannel() {
        if (topChannel == null || topChannel.getId() <= 0) {
            addActionError("缺少一级媒体信息");
            return INPUT;
        }
        topChannel = channelService.getChannelById(topChannel.getId());
        if (topChannel == null) {
            addActionError("缺少对应的一级媒体");
            return ERROR;
        }
        return SUCCESS;
    }

    public String updateChannel() {
        if (channel.getLevel() == 1) {
            return updateMainChannel();
        } else {
            return updateSubChannel();
        }
    }

    private String updateMainChannel() {
        if (StringUtils.isEmpty(channel.getUuid()) || StringUtils.isEmpty(channel.getDomain())) {
            addActionError("uuid或域名不能为空");
            return ERROR;
        }
        Channel srcChannel = channelService.getChannelByUUIDandDomain(channel.getUuid(), channel.getDomain());
        if (srcChannel == null || srcChannel.getLevel() != 1) {
            addActionError("不存在该一级媒体");
            return ERROR;
        }
        //一级媒体不更新域名，uuid和id，沿用老的信息
        channel.setId(srcChannel.getId());
        channel.setUuid(srcChannel.getUuid());
        channel.setDomain(srcChannel.getDomain());
        channel.setLevel(1);
        try {
            channelService.updateChannel(srcChannel, channel);
        } catch (Exception e) {
            LOG.error(LOG_TAG, e);
            addActionError("更新失败");
            return ERROR;
        }
        addActionMessage("更新成功");
        return SUCCESS;
    }

    private String updateSubChannel() {
        if (StringUtils.isEmpty(channel.getUuid()) || StringUtils.isEmpty(channel.getDomain())) {
            addActionError("uuid或域名不能为空");
            return ERROR;
        }
        Channel srcChannel = channelService.getChannelById(channel.getId());
        if (srcChannel == null || srcChannel.getLevel() != 2) {
            addActionError("不存在该二级媒体");
            return ERROR;
        }
        try {
            channelService.updateChannel(srcChannel, channel);
        } catch (Exception e) {
            LOG.error(LOG_TAG, e);
            addActionError("更新失败");
            return ERROR;
        }
        addActionMessage("更新成功");
        return SUCCESS;
    }

    public String createSubChannel() {
        if (channel.getStatus() != ChannelStatusEnum.OPENED) {
            addActionError("新建二级媒体初始状态错误");
            return ERROR;
        }
        Channel topChannel = channelService.getTopChannelsByUUID(channel.getUuid());
        if (topChannel == null) {
            addActionError("不存在的一级媒体");
            return ERROR;
        }
        if (StringUtils.equals(topChannel.getDomain(), channel.getDomain())) {
            addActionError("二级媒体的域名与一级媒体相同");
            return ERROR;
        }
        Channel tempChannel = channelService.getChannelByUUIDandDomain(topChannel.getUuid(), channel.getDomain());
        if (tempChannel != null) {
            addActionError("已存在相同的二级域名");
            return ERROR;
        }
        try {
            channel.setLevel(2);
            channel.setOpenTime(DateTimeUtil.getCurrentDate());
            channelService.createChannel(channel);
        } catch (Exception e) {
            LOG.error(LOG_TAG, e);
            return ERROR;
        }
        return SUCCESS;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public JsonResults getResults() {
        return this.results;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getImageFileNameExtension() {
        return imageFileNameExtension;
    }

    public void setImageFileNameExtension(String imageFileNameExtension) {
        this.imageFileNameExtension = imageFileNameExtension;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public List<Channel> getSubChannel() {
        return subChannels;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Channel getTopChannel() {
        return topChannel;
    }

    public void setTopChannel(Channel topChannel) {
        this.topChannel = topChannel;
    }

    @Override
    public void validate() {

        if (channel == null) {
            addActionError("未知的参数");
        } else if (StringUtils.isEmpty(channel.getUuid())) {
            addActionError("未知的uuid");
        } else if (channel.getMinCPM() < 0) {
            addActionError("最小CPM不能为负数");
        } else if (channel.getLevel() < 1 || channel.getLevel() > 2) {
            addActionError("不支持的媒体类型，只支持一级或二级媒体");
        } else if (channel.getAdType() == null) {
            addActionError("未知的广告版位类型");
        } else if (channel.getStatus() == null) {
            addActionError("未知的广告状态");
        } else if (channel.getType() == null) {
            addActionError("未知的广告类型");
        } else if (StringUtils.isEmpty(channel.getDomain())) {
            addActionError("媒体的域名不能为空");
        } else if (!UrlUtil.isUrlOptionalHttp(channel.getDomain())) {
            addActionError("媒体域名不是合法域名");
        } else if (channel.getNetWork() == null || channel.getNetWork().isEmpty()) {
            addActionError("媒体的投放网络不能为空");
        }
    }

    public Map<ChannelStatusEnum, String> getStatusSelector() {
        return statusSelector;
    }

    public Map<ChannelTypeEnum, String> getTypeSelector() {
        return typeSelector;
    }

    public Map<ChannelStyleEnum, String> getStyleSelector() {
        return styleSelector;
    }

    public Map<AdNetworkEnum, String> getNetworkSelector() {
        return networkSelector;
    }

    public Map<AdEntryTypeEnum, String> getAdTypeSelector() {
        return adTypeSelector;
    }
}
