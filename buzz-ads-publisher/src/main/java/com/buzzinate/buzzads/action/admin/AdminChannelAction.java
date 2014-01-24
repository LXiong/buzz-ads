package com.buzzinate.buzzads.action.admin;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.common.exception.ServiceException;
import com.buzzinate.buzzads.core.service.ChannelService;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.Channel;
import com.buzzinate.buzzads.enums.ChannelStatusEnum;
import com.buzzinate.buzzads.enums.ChannelTypeEnum;
import com.buzzinate.common.util.JsonResults;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *         <p/>
 *         2013-5-13
 */
@Controller
@Scope("request")
public class AdminChannelAction extends ActionSupport implements ServletResponseAware {

    private static final long serialVersionUID = 8305366347607530990L;
    private static Log log = LogFactory.getLog(AdminChannelAction.class);
    @Autowired
    private transient ChannelService channelService;
    private transient HttpServletResponse response;
    private JsonResults results;
    private List<Channel> channels;
    private Channel channel;
    private long channelCount;
    private long activeChannelCount;
    private Pagination page;
    private Map<Integer, String> channelStatusMap = ChannelStatusEnum.getSelector();
    private Map<Integer, String> channelTypeMap = ChannelTypeEnum.getSelector();
    private Integer selectStatus = ChannelStatusEnum.OPENED.getCode();
    private Integer selectType;

    /**
     * 管理列表
     *
     * @return
     */
    public String listChannes() {
        if (page == null)
            page = new Pagination();

        if (channel == null) {
            channel = new Channel();
            channel.setStatus(null);
            channel.setType(null);
        }
        if (selectStatus != null) {
            channel.setStatus(ChannelStatusEnum.findByValue(selectStatus.intValue()));
        }
        if (selectType != null) {
            channel.setType(ChannelTypeEnum.findByValue(selectType.intValue()));
        }
        channelCount = channelService.countChannels(new ChannelStatusEnum[]{ChannelStatusEnum.OPENED, ChannelStatusEnum.FROZENED, ChannelStatusEnum.CLOSED});
        activeChannelCount = channelService.countChannels(new ChannelStatusEnum[]{ChannelStatusEnum.OPENED});
        channels = channelService.listChannel(channel, page);
        return SUCCESS;
    }

    public String exportCSV() {
        if (channel == null) {
            channel = new Channel();
            channel.setStatus(null);
            channel.setType(null);
        }
        if (selectStatus != null) {
            channel.setStatus(ChannelStatusEnum.findByValue(selectStatus.intValue()));
        }
        if (selectType != null) {
            channel.setType(ChannelTypeEnum.findByValue(selectType.intValue()));
        }
        channels = channelService.listChannel(channel, null);
        String csv = channelService.buildChannelInfoCSV(channels);
        OutputStreamWriter osw;
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=channel_export.csv");
            response.setHeader("bufferSize", "4096");
            osw = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
            osw.write("\uFEFF");
            osw.write(csv);
            osw.flush();
        } catch (UnsupportedEncodingException e) {
            log.error("encoding error", e);
        } catch (IOException e) {
            log.error("encoding error", e);
        }
        return null;
    }

    /**
     * 媒体状态变更
     * <p>删除和冻结媒体需要输入操作理由</p>
     *
     * @return
     */
    public String updateChannelStatus() {
        results = new JsonResults(false);
        if (channel == null) {
            results.fail("系统异常");
            return JsonResults.JSON_RESULT_NAME;
        }
        try {
            channelService.updateChannelStatus(channel.getId(), channel.getStatusSelect(), channel.getOperateResult());
            results.success();
        } catch (ServiceException e) {
            results.fail(e.getMessage());
        } catch (Exception e) {
            results.fail("系统异常");
        }
        return JsonResults.JSON_RESULT_NAME;
    }

    public JsonResults getResults() {
        return results;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public Map<Integer, String> getChannelStatusMap() {
        channelStatusMap.put(null, "ALL");
        return channelStatusMap;
    }

    public Map<Integer, String> getChannelTypeMap() {
        Map<Integer, String> resMap = new LinkedHashMap<Integer, String>();
        resMap.put(null, "ALL");
        resMap.putAll(channelTypeMap);
        return resMap;
    }

    public long getChannelCount() {
        return channelCount;
    }

    public long getActiveChannelCount() {
        return activeChannelCount;
    }

    public Integer getSelectStatus() {
        return selectStatus;
    }

    public void setSelectStatus(Integer selectStatus) {
        this.selectStatus = selectStatus;
    }

    public Integer getSelectType() {
        return selectType;
    }

    public void setSelectType(Integer selectType) {
        this.selectType = selectType;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }
}

