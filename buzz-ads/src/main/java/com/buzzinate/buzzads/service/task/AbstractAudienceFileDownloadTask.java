package com.buzzinate.buzzads.service.task;

import com.buzzinate.buzzads.core.service.EventServices;
import com.buzzinate.common.util.DateTimeUtil;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-5-28
 * Time: 下午3:39
 * 下载Audience文件的抽象类
 */
public abstract class AbstractAudienceFileDownloadTask implements Callable<Boolean> {
    protected static final String TAG = "AbstractAudienceFileDownloadTask";
    protected static Log LOG = LogFactory.getLog(AbstractAudienceFileDownloadTask.class);
    protected String filePath;
    protected HttpClient httpClient;
    protected EventServices eventServices;
    protected long timestamp;

    AbstractAudienceFileDownloadTask(String filePath, HttpClient httpClient, EventServices eventServices) {
        this.filePath = filePath;
        this.httpClient = httpClient;
        this.eventServices = eventServices;

    }

    @Override
    public Boolean call() throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug(TAG + "::download start,path=[" + filePath + "]");
        }
        this.timestamp = DateTimeUtil.getCurrentDate().getTime();
        InputStream inputStream = null;
        try {
            HttpUriRequest request = beforeDownload(filePath);
            inputStream = download(request);
            if (inputStream != null) {
                afterDownload(inputStream);
            }
        } catch (Exception e) {
            LOG.error(TAG, e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                //do nothing
            }
        }
        return Boolean.FALSE;
    }

    /**
     * @param request
     * @return
     */
    protected InputStream download(HttpUriRequest request) {
        HttpEntity entity = null;
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                entity = response.getEntity();
                if (entity.getContentEncoding() != null &&
                        entity.getContentEncoding().getValue().toLowerCase().contains("gzip")) {
                    entity = new GzipDecompressingEntity(entity);
                }
                return entity.getContent();
            }
        } catch (IOException e) {
            LOG.error(TAG + "::download file error!,filePath=[" + filePath + "]", e);
        }
        return null;
    }

    protected abstract HttpUriRequest beforeDownload(String filePath);

    protected abstract void afterDownload(InputStream inputStream);

}
