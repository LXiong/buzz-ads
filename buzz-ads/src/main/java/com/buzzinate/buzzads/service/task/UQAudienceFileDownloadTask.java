package com.buzzinate.buzzads.service.task;

import com.buzzinate.buzzads.core.service.EventServices;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.enums.PartnerEnum;
import com.buzzinate.buzzads.util.parser.UQAudienceParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.InputStream;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-5-28
 * Time: 下午2:55
 * 从UQ下载文件后通知服务端处理
 */
public class UQAudienceFileDownloadTask extends AbstractAudienceFileDownloadTask {
    private static final String TAG = "UQAudenienceFileDownloadTask";
    private static final int uqAdvertiserId = ConfigurationReader.getInt("uq.advertiser.id", 2763553);


    public UQAudienceFileDownloadTask(String filePath, HttpClient httpClient, EventServices eventServices) {
        super(filePath, httpClient, eventServices);
    }

    @Override
    protected HttpUriRequest beforeDownload(String filePath) {
        HttpGet get = new HttpGet(filePath);
        return get;
    }

    @Override
    protected void afterDownload(InputStream inputStream) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(TAG + "send event start!, current time=" + System.currentTimeMillis());
        }
        UQAudienceParser parser = new UQAudienceParser(inputStream);
        for (Iterator<String> it = parser; it.hasNext(); ) {
            String data = it.next();
            if (!StringUtils.isEmpty(data)) {
                eventServices.sendAudienceFileReceivedEvent(timestamp, PartnerEnum.UQ, uqAdvertiserId, data);
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(TAG + "send event completed!, current time=" + System.currentTimeMillis());
        }
    }
}
