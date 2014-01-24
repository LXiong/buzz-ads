package com.buzzinate.buzzads.struts2;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.buzzinate.buzzads.user.LoginHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.buzzinate.buzzads.common.log.LocalLogBuffer;
import com.buzzinate.buzzads.common.log.LogBaseConstants;
import com.buzzinate.buzzads.common.log.LogConfig;
import com.buzzinate.buzzads.common.log.OperationLogDao;
import com.buzzinate.buzzads.domain.OperationLog;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-12-24
 */
public class LogInterceptor extends AbstractInterceptor implements LogBaseConstants {

    private static final long serialVersionUID = 4287412108690834551L;
    private static final Log LOG = LogFactory.getLog(LogInterceptor.class);
    
    private static final int MAX_LOG_COUNT = 500;
    private static final int MAX_TIME_OUT = 500;
    
    private BlockingQueue<List<OperationLog>> tasks = new LinkedBlockingQueue<List<OperationLog>>(MAX_LOG_COUNT);
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private LogTask task = new LogTask();
    
    @Autowired
    private OperationLogDao appLogService;
    @Autowired
    private LoginHelper loginHelper;
    
    private Map<String, LogConfig> config;

    @SuppressWarnings("rawtypes")
    public void init() {
        InputStream is = null;
        Document doc = null;
        SAXReader reader = new SAXReader();
        try {
            service.execute(task);
            LOG.info("================读取日志配置文件开始===================");
            Resource configLocation = new ClassPathResource("adsOpLog.xml");
            is = configLocation.getInputStream();
            doc = reader.read(is);
            Element root = doc.getRootElement();
            List list = root.elements("log");
            if (list.size() > 0) {
                config = new HashMap<String, LogConfig>();
                for (Iterator it = list.iterator(); it.hasNext();) {
                    Element log = (Element) it.next();
                    LogConfig lc = new LogConfig();
                    lc.setType(log.attributeValue("type"));
                    lc.setObjtype(log.attributeValue("objtype"));
                    lc.setObjtypename(log.attributeValue("objtypename"));
                    lc.setName(log.attributeValue("name"));
                    lc.setDescription(log.attributeValue("description"));
                    config.put(lc.getType(), lc);
                }
            }
        } catch (Exception e) {
            LOG.error("读取日志配置文件出错", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                is = null;
            }
        }
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        String res = invocation.invoke();
        if (loginHelper.isLoggedIn()) {
            int operId = loginHelper.getUserId();
            try {
                List<Map<String, Object>> list = LocalLogBuffer.get();
                if (list != null && !list.isEmpty()) {
                    List<OperationLog> logList = new ArrayList<OperationLog>(list.size());
                    for (Map<String, Object> map : list) {
                        OperationLog log = new OperationLog();
                        log.setOpUserId(Long.valueOf(operId));
                        log.setDescription(parseDesc(config.get(map.get(LOG_OP_TYPE)).getDescription(), 
                                (Map) map.get(LOG_PARAMS)));
                        log.setOpType(Integer.valueOf(config.get(map.get(LOG_OP_TYPE)).getType()));
                        log.setOpName(config.get(map.get(LOG_OP_TYPE)).getName());
                        if (map.get(LOG_OP_TARGET_USERID) != null)
                            log.setTargetUserId(Long.valueOf((String) map.get(LOG_OP_TARGET_USERID)));
                        log.setOutId(Long.valueOf((String) map.get(LOG_OBJECT_ID)));
                        log.setCreateTime(new Date());
                        logList.add(log);
                    }
                    try {
                        writeRemoteLog(logList);
                    } catch (Exception e) {
                        LOG.error("", e);
                        writeLog(logList);
                    } finally {
                        LocalLogBuffer.clear();
                    }
                }
                return res;
            } finally {
                LocalLogBuffer.clear();
            }
        }
        return res;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private String parseDesc(String description, Map map) {
        Set<String> keySet = map.keySet();
        String result = description;
        for (String key : keySet) {
            result = result.replaceAll("\\{" + key + "}", safeRegexReplacement((String) map.get(key)));
        }
        return result;
    }
    
    private String safeRegexReplacement(String replacement) {
        if (replacement == null) {
            return replacement;
        }
        return replacement.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$",
                "\\\\\\$");
    }


    private void writeRemoteLog(List<OperationLog> list) throws Exception {
        try {
            if (!tasks.offer(list, MAX_TIME_OUT, TimeUnit.MILLISECONDS)) {
                for (OperationLog log : list) {
                    appLogService.create(log);
                }
            }
        } catch (InterruptedException e) {
            for (OperationLog log : list) {
                appLogService.create(log);
            }
        }
    }
    
    private void writeLog(List<OperationLog> list) {
        for (OperationLog log : list) {
            LOG.info(log.toString());
        }
    }
    
    /**
     * 日志入库线程类
     * @author Harry Feng<xiaobo.feng@buzzinate.com>
     *
     * 2012-12-24
     */
    private class LogTask implements Runnable {
        @Override
        public void run() {
            LOG.info("===============记录日志线程启动===================");
            while (true) {
                try {
                    List<OperationLog> list = tasks.take();
                    for (OperationLog log : list) {
                        try {
                            appLogService.create(log);
                        } catch (Exception e) {
                            LOG.error("日志入库异常:" + log.getDescription(), e);
                        }
                    }
                } catch (InterruptedException e) {
                    LOG.error("日志入库线程遇到打扰", e);
                } 
            }
        }
    }
}
