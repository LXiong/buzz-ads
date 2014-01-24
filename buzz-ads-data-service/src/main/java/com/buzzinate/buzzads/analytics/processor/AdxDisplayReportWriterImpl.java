package com.buzzinate.buzzads.analytics.processor;

import com.buzzinate.adx.message.AdDisplayMessage;
import com.buzzinate.common.util.DateTimeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-7-31
 * Time: 下午2:40
 * 输出
 */
public class AdxDisplayReportWriterImpl extends AbstractBaseReportWriter<AdDisplayMessage> {
    private static Log log = LogFactory.getLog(AdxDisplayReportWriterImpl.class);

    public AdxDisplayReportWriterImpl(String path) {
        super(path);
    }

    private Boolean writeFile(String fileName, List<AdDisplayMessage> messages) {
        File file = new File(fileName);
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        boolean isNew = false;
        try {
            if (!file.exists()) {
                file.createNewFile();
                isNew = true;
            }
            fos = new FileOutputStream(file, true);
            bos = new BufferedOutputStream(fos, 1024);
            if (isNew) {
                StringBuffer sb = new StringBuffer("Request Id").append(",")
                        .append("AD Id").append(",")
                        .append("Display Time").append("\n");
                bos.write(sb.toString().getBytes());
            }
            for (AdDisplayMessage info : messages) {
                StringBuffer sb = new StringBuffer(info.getRequestId()).append(",")
                        .append(info.getAdId())
                        .append(",")
                        .append(DateTimeUtil.formatDate(info.getTimestamp())).append("\n");
                bos.write(sb.toString().getBytes());
            }
            bos.flush();
        } catch (Exception e) {
            log.error("error while writing file", e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
        return Boolean.TRUE;
    }

    @Override
    protected List<Future<Boolean>> doJob(Map<String, List<AdDisplayMessage>> messages) {
        List<Future<Boolean>> futureList = new ArrayList<Future<Boolean>>(messages.keySet().size());
        for (Map.Entry<String, List<AdDisplayMessage>> set : messages.entrySet()) {
            final String fileName = path + "/" + set.getKey();
            final List<AdDisplayMessage> adInfos = set.getValue();
            futureList.add(pool.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return writeFile(fileName, adInfos);
                }
            }));
        }
        return futureList;
    }

    @Override
    protected Map<String, List<AdDisplayMessage>> sortData(List<Object> data) {
        if (data == null || data.isEmpty()) {
            return new HashMap<String, List<AdDisplayMessage>>(0);
        }
        Map<String, List<AdDisplayMessage>> result = new HashMap<String, List<AdDisplayMessage>>(data.size() / 10, 0.95f);
        for (Object obj : data) {
            if (obj instanceof AdDisplayMessage) {
                AdDisplayMessage message = (AdDisplayMessage) obj;
                String keySuffix = DateTimeUtil.formatDate(DateTimeUtil.getDateDay(message.getTimestamp())) + ".csv";
                String key = message.getDspId() + "-display-" + keySuffix;
                List<AdDisplayMessage> list = result.get(key);
                if (list == null) {
                    list = new ArrayList<AdDisplayMessage>(500);
                    list.add(message);
                    result.put(key, list);
                } else {
                    list.add(message);
                }
            }
        }
        return result;
    }
}
