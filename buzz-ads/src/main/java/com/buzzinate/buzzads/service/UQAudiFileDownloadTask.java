package com.buzzinate.buzzads.service;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.enums.PartnerEnum;
import com.buzzinate.buzzads.service.task.UQAudienceFileDownloadTask;
import com.buzzinate.common.util.task.AbstractSingleServerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-5-29
 * Time: 下午7:48
 * 定时下载UQ的Audience文件
 */
public class UQAudiFileDownloadTask extends AbstractSingleServerTask {
    private static final String TAG = "UQAudiFileDownloadTask";
    private static Log LOG = LogFactory.getLog(UQAudienceFileDownloadTask.class);
    private String baseUrl = ConfigurationReader.getString("uq.audience.file.download.url");
    @Autowired
    private AudienceApiServices audienceApiServices;

    public UQAudiFileDownloadTask() {
        //TODO
        super(ConfigurationReader.getString("uq.audience.file.download.task.host"));
    }

    @Override
    protected void doJob() {
        Future<Boolean> result = audienceApiServices.receiveNotify(PartnerEnum.UQ, getRealUrl());
        if (result.isDone() && LOG.isDebugEnabled()) {
            try {
                LOG.debug(TAG + "::task result is " + result.get());
            } catch (InterruptedException e) {
                // do nothing.
            } catch (ExecutionException e) {
                LOG.error(TAG + "::uq audience download error", e);
            }
        }
    }

    private String getRealUrl() {
        //TODO
        return baseUrl;
    }
}
