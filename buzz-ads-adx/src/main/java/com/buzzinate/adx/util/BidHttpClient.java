package com.buzzinate.adx.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.buzzinate.common.util.string.StringUtil;


/**
 * @author james.chen
 *         date 2013-7-30
 */
public class BidHttpClient {
    public static final String TIMEOUT = "timeout";
    private static Log log = LogFactory.getLog(BidHttpClient.class);
    private DefaultHttpClient httpClient;
    private String charset = "UTF-8";

    public BidHttpClient(int maxPerRoute, int maxTotal, int socketTimeout, int connectionTimeout) {
        init(maxPerRoute, maxTotal, socketTimeout, connectionTimeout);
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

    public String postWithGzip(String url, Map<String, String> params) {
        String content = "";
        HttpPost httpPost = new HttpPost(url);
        StringBuffer sb = new StringBuffer();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(StringUtil.urlEncode(entry.getValue(), this.charset))
                        .append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gos = new GZIPOutputStream(baos);
            gos.write(sb.toString().getBytes(this.charset));
            gos.flush();
            gos.close();
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Content-Encoding", "gzip");
            httpPost.setEntity(new ByteArrayEntity(baos.toByteArray()));
            HttpResponse response = httpClient.execute(httpPost);
            content = getResponseBodyAsString(response);
        } catch (ConnectTimeoutException e) {
            content = TIMEOUT;
        } catch (Exception e) {
            log.error("Failed to post request url: " + url, e);
        }
        return content;
    }

    public String post(String url, Map<String, String> params) {
        String content = "";
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, this.charset));
            HttpResponse response = httpClient.execute(httpPost);
            content = getResponseBodyAsString(response);
        } catch (InterruptedIOException e) {
            content = TIMEOUT;
        } catch (Exception e) {
            log.error("Failed to post request url: " + url, e);
        }
        return content;
    }

    private String getResponseBodyAsString(HttpResponse response) throws IOException {
        String content = "";
        HttpEntity entity = null;
        if (response.getEntity().getContentEncoding() != null &&
                response.getEntity().getContentEncoding().getValue().toLowerCase().indexOf("gzip") > -1) {
            entity = new GzipDecompressingEntity(response.getEntity());
        } else {
            entity = response.getEntity();
        }
        if (entity != null) {
            content = EntityUtils.toString(entity, this.charset);
            EntityUtils.consume(entity);
        }
        return content;
    }

}
