package com.buzzinate.buzzads.analytics.processor;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.common.util.task.AbstractProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: kun
 * Date: 13-7-9
 * Time: 下午11:09
 * 从队列中获取adx信息并输出到文本。
 */
public final class AdxReportWriterProcessor extends AbstractProcessor {
    private static Log log = LogFactory.getLog(AdxReportWriterProcessor.class);
    //服务器名
    private String serverName = ConfigurationReader.getString("adx.report.server.name");
    //输出目录
    private String directory = ConfigurationReader.getString("adx.report.output.directory");
    private int sleepInterval = ConfigurationReader.getInt("adx.report.sleep.interval", 2000);
    private AdxReportWriter writer;
    private volatile Future<Boolean> isComplete;


    public AdxReportWriterProcessor() {
        super(1000, ConfigurationReader.getInt("adx.report.buffer.size"), ConfigurationReader.getInt("adx.report.max.waiting.interval"));
        init();
    }

    public AdxReportWriterProcessor(int capacity, int batchSize, int interval, String serverName, String directory, AdxReportWriter writer) {
        super(capacity, batchSize, interval);
        this.serverName = serverName;
        this.directory = directory;
        this.writer = writer;
        init();
    }

    @Override
    public void stop() {
        if (writer != null) {
            writer.stop();
        }
        super.stop();
    }

    /**
     *
     */
    public void init() {
        if (writer == null) {
            writer = new AdxReportWriterImpl(directory);
        }
    }

    @Override
    protected void workerFunction(List<Object> stats) {
        try {
            //未处理或已经处理完上一次的任务则继续执行下面的任务
            if (isComplete == null || isComplete.isDone() || isComplete.isCancelled()) {
                isComplete = writer.process(stats);
            } else {
                //否则等待上一次处理完成或超時后再继续执行
                try {
                    isComplete.get(sleepInterval, TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                }
                workerFunction(stats);
            }
        } catch (Exception e) {
            log.debug("write thread is interrupted");
        }
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public AdxReportWriter getWriter() {
        return writer;
    }

    public void setWriter(AdxReportWriter writer) {
        this.writer = writer;
    }

    public Future<Boolean> getComplete() {
        return isComplete;
    }
}
