package com.buzzinate.buzzads.analytics.processor;

import com.buzzinate.adx.entity.AdInfo;
import com.buzzinate.common.util.DateTimeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: kun
 * Date: 13-7-31
 * Time: 上午9:03
 * 写入文档
 */
public class AdxReportWriterImpl extends AbstractBaseReportWriter<AdInfo> {
    private static Log log = LogFactory.getLog(AdxReportWriterImpl.class);

    public AdxReportWriterImpl(String path) {
        super(path);
    }

    private Boolean writeFile(String fileName, List<AdInfo> adInfos) {
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
                        .append("Banner Id").append(",")
                        .append("Cost").append(",")
                        .append("Status").append("\n");
                bos.write(sb.toString().getBytes());
            }
            for (AdInfo info : adInfos) {
                StringBuffer sb = new StringBuffer(info.getRequestId()).append(",")
                        .append(info.getBannerId())
                        .append(",")
                        .append(info.getCost())
                        .append(",")
                        .append(info.getStatus())
                        .append("\n");
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
    protected List<Future<Boolean>> doJob(Map<String, List<AdInfo>> result) {
        List<Future<Boolean>> futureList = new ArrayList<Future<Boolean>>(result.keySet().size());
        for (Map.Entry<String, List<AdInfo>> set : result.entrySet()) {
            final String fileName = path + "/" + set.getKey();
            final List<AdInfo> adInfos = set.getValue();
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
    protected Map<String, List<AdInfo>> sortData(List<Object> data) {
        if (data == null || data.isEmpty()) {
            return new HashMap<String, List<AdInfo>>(0);
        }
        Map<String, List<AdInfo>> result = new HashMap<String, List<AdInfo>>(data.size() / 10, 0.95f);
        for (Object obj : data) {
            if (obj instanceof AdInfo) {
                AdInfo adInfo = (AdInfo) obj;
                String keySuffix = DateTimeUtil.formatDate(DateTimeUtil.getDateDay(adInfo.getTimestamp())) + ".csv";
                String key = adInfo.getDspId() + "-" + keySuffix;
                List<AdInfo> list = result.get(key);
                if (list == null) {
                    list = new ArrayList<AdInfo>(500);
                    list.add(adInfo);
                    result.put(key, list);
                } else {
                    list.add(adInfo);
                }
            }
        }
        return result;
    }
}
