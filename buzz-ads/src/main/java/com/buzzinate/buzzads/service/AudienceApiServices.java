package com.buzzinate.buzzads.service;

import com.buzzinate.buzzads.core.service.EventServices;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.core.util.UrlUtil;
import com.buzzinate.buzzads.enums.PartnerEnum;
import com.buzzinate.buzzads.service.task.UQAudienceFileDownloadTask;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-5-28
 * Time: 下午12:29
 * 提供下载文件或解析服务
 */
@Service
public class AudienceApiServices {
    private ExecutorService executorService;
    private DefaultHttpClient httpClient;
    @Autowired
    private EventServices eventServices;

    public AudienceApiServices() {
        executorService = Executors.newFixedThreadPool(ConfigurationReader.getInt("audience.thread.max", 3));


        init(ConfigurationReader.getInt("audience.http.max.per.route", 1),
                ConfigurationReader.getInt("audience.http.max.total", 5),
                ConfigurationReader.getInt("audience.http.socket.timeout", 36000000),
                ConfigurationReader.getInt("audience.http.connection.timeout", 36000000));
    }

    public Future receiveNotify(PartnerEnum partnerEnum, String filePath) {
        if (UrlUtil.isUrlHttp(filePath)) {
            switch (partnerEnum) {
                case UQ:
                    UQAudienceFileDownloadTask task = new UQAudienceFileDownloadTask(filePath, httpClient, eventServices);
                    return executorService.submit(task);
                default:
                    break;
            }
        }
        return null;
    }

    private void init(int maxPerRoute, int maxTotal, int socketTimeout, int connectionTimeout) {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager(schemeRegistry);
        connectionManager.setDefaultMaxPerRoute(maxPerRoute);
        connectionManager.setMaxTotal(maxTotal);

        HttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter("http.socket.timeout", socketTimeout);
        httpParams.setParameter("http.connection.timeout", connectionTimeout);

        httpClient = new DefaultHttpClient(connectionManager, httpParams);
        httpClient.setHttpRequestRetryHandler(new StandardHttpRequestRetryHandler(3, true));
    }

}
