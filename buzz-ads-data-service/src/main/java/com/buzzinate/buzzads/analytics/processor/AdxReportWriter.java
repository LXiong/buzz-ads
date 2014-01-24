package com.buzzinate.buzzads.analytics.processor;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: kun
 * Date: 13-7-31
 * Time: 上午9:00
 * 文件分拣并写入对应的文本中
 */
public interface AdxReportWriter {
    Future<Boolean> process(List<Object> data);
    void stop();
}
