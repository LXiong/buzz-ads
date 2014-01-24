package com.buzzinate.buzzads.data.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TTransportException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.buzzinate.buzzads.analytics.AdAnalyticsConstants;
import com.buzzinate.buzzads.analytics.AdClick;
import com.buzzinate.buzzads.analytics.AdShowUps;
import com.buzzinate.buzzads.analytics.processor.AdxDisplayReportWriterImpl;
import com.buzzinate.buzzads.analytics.processor.AdxDspScoreProcess;
import com.buzzinate.buzzads.analytics.processor.AdxReportWriter;
import com.buzzinate.buzzads.analytics.processor.AdxReportWriterImpl;
import com.buzzinate.buzzads.analytics.processor.AdxReportWriterProcessor;
import com.buzzinate.buzzads.analytics.processor.DataNotifyTask;
import com.buzzinate.buzzads.analytics.processor.StatisticProcessor;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.data.services.TAdataAccessServicesImpl;
import com.buzzinate.buzzads.data.thrift.TAdDataAccessServices;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.kestrel.KestrelClient;
import com.buzzinate.common.util.serialize.HessianSerializer;
import com.buzzinate.common.util.task.AbstractProcessor;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.finagle.thrift.ThriftServerFramedCodec;
import com.twitter.util.Duration;

import flexjson.JSONSerializer;


/**
 * Main class.
 *
 * @author zyeming
 */
public final class Server {

    private static final int KESTREL_SLEEP_INTERVAL = 10;
    private static ApplicationContext context = new ClassPathXmlApplicationContext(
            new String[]{"application-analytics.xml"});
    private static Log log = LogFactory.getLog(Server.class);

    private Server() {
    }

    /**
     * main function.
     *
     * @param args args
     * @throws TTransportException TTransportException
     */
    public static void main(final String[] args) throws TTransportException {
        startThriftServer();
        startAnalyticsThreads();
    }

    private static void startAnalyticsThreads() {
        final KestrelClient kestrelClient = context.getBean(KestrelClient.class);
        StatisticProcessor statisticProcessor = (StatisticProcessor)
                context.getBean(StatisticProcessor.class);
        final AdxReportWriter adxReportWriter = new AdxReportWriterImpl(ConfigurationReader.getString("adx.report.output.directory"));
        final AdxReportWriter adxDisplayWriter = new AdxDisplayReportWriterImpl(ConfigurationReader.getString("adx.report.output.directory"));
        final AdxReportWriterProcessor reportWriterProcessor = new AdxReportWriterProcessor();
        reportWriterProcessor.setWriter(adxReportWriter);
        final AdxReportWriterProcessor displayReportWriterProcessor = new AdxReportWriterProcessor();
        displayReportWriterProcessor.setWriter(adxDisplayWriter);
        final AdxDspScoreProcess adxDspScoreProcess = new AdxDspScoreProcess();

        log.info("Starting the analytics threads...");

        Queue<Object> queue = new ConcurrentLinkedQueue<Object>();

        int showupThreads = ConfigurationReader
                .getInt("analytics.showup.thread.size");
        int clickThreads = ConfigurationReader
                .getInt("analytics.click.thread.size");
        int cpsThreads = ConfigurationReader
                .getInt("analytics.cps.thread.size");
        int pvThreads = ConfigurationReader
                .getInt("analytics.pv.thread.size");
        int adxThreads = ConfigurationReader
                .getInt("analytics.adx.winnermessage.thread.size");
        int adDisplayMsgThreads = ConfigurationReader.getInt("analytics.adx.displaymessage.thread.size");
        ExecutorService excutorService = Executors.newFixedThreadPool(
                showupThreads + clickThreads + cpsThreads + pvThreads + adxThreads + adDisplayMsgThreads);
        log.info("Thread Pool size is " + (showupThreads + clickThreads + cpsThreads + pvThreads + adxThreads + adDisplayMsgThreads));

        boolean keepAdRawData = ConfigurationReader.getBoolean("buzzads.raw.data.keep");

        log.info("Starting the analytics (" + AdAnalyticsConstants.ADSHOWUP_QUEUE + ") threads");
        for (int i = 0; i < showupThreads; i++) {
            excutorService.execute(
                    new AnalyticsTask(kestrelClient,
                            statisticProcessor, keepAdRawData, queue, AdAnalyticsConstants.ADSHOWUP_QUEUE));
        }

        log.info("Starting the analytics (" + AdAnalyticsConstants.ADCLICK_QUEUE + ") threads");
        for (int i = 0; i < clickThreads; i++) {
            excutorService.execute(
                    new AnalyticsTask(kestrelClient,
                            statisticProcessor, keepAdRawData, queue, AdAnalyticsConstants.ADCLICK_QUEUE));
        }

        log.info("Starting the analytics (" + AdAnalyticsConstants.ADCPS_QUEUE + ") threads");
        for (int i = 0; i < cpsThreads; i++) {
            excutorService.execute(
                    new AnalyticsTask(kestrelClient,
                            statisticProcessor, keepAdRawData, queue, AdAnalyticsConstants.ADCPS_QUEUE));
        }

        log.info("Starting the analytics (" + AdAnalyticsConstants.ADPV_QUEUE + ") threads");
        for (int i = 0; i < pvThreads; i++) {
            excutorService.execute(
                    new AnalyticsTask(kestrelClient,
                            statisticProcessor, keepAdRawData, queue, AdAnalyticsConstants.ADPV_QUEUE));
        }


        log.info("Starting the analytics (" + AdAnalyticsConstants.ADXWINMSG_QUEUE + ") threads");
        for (int i = 0; i < adxThreads; i++) {
            excutorService.submit(new Runnable() {
                @Override
                public void run() {

                    while (true) {
                        try {
                            Object stat = kestrelClient.get(AdAnalyticsConstants.ADXWINMSG_QUEUE);
                            if (stat instanceof byte[]) {
                                stat = HessianSerializer.deserialize((byte[]) stat);
                            }
                            if (stat == null) {
                                Thread.sleep(KESTREL_SLEEP_INTERVAL);
                                continue;
                            }
                            reportWriterProcessor.addStats(stat);
                            adxDspScoreProcess.addStats(stat);
                        } catch (Exception e) {
                            log.error("Exceptino in handling stats: ", e);
                        }
                    }
                }
            });
        }

        log.info("Starting the analytics (" + AdAnalyticsConstants.ADXDISPLAYMSG_QUEUE + ") threads");
        for (int i = 0; i < adDisplayMsgThreads; i++) {
            excutorService.submit(new AnalyticsTask(kestrelClient,
                    displayReportWriterProcessor, keepAdRawData, queue, AdAnalyticsConstants.ADXDISPLAYMSG_QUEUE));
        }

        if (keepAdRawData) {
            ExecutorService singleExcutorService = Executors.newSingleThreadExecutor();
            singleExcutorService.submit(new AdRawDataWriter(queue));
        }
        
        //设置任务计划，启动和间隔时间
        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
        final DataNotifyTask dataNotifyTask = context.getBean(DataNotifyTask.class);
        service.scheduleAtFixedRate(dataNotifyTask, 0, ConfigurationReader
                .getInt("adx.dsp.score.period"), TimeUnit.MINUTES);

    }

    private static void startThriftServer() {
        TAdDataAccessServices.FutureIface processor = context.getBean(TAdataAccessServicesImpl.class);

        int requestTimeout = ConfigurationReader.getInt("data.access.server.request.timeout");
        int port = ConfigurationReader.getInt("data.access.server.port");

        log.info("Starting the data access server...");
        ServerBuilder.safeBuild(new TAdDataAccessServices.FinagledService(processor,
                new TBinaryProtocol.Factory()),
                ServerBuilder.get().
                        name("AdDataAccessServices").
                        codec(ThriftServerFramedCodec.get()).
                        requestTimeout(Duration.fromSeconds(requestTimeout)).
                        bindTo(new InetSocketAddress(port)));
    }

    /**
     * analytics task class.
     *
     * @author Jacky
     */
    private static class AnalyticsTask implements Runnable {

        private KestrelClient kestrelClient;
        private AbstractProcessor statisticProcessor;
        private String queueName;
        private boolean keepAdRawData;
        private Queue<Object> queue;


        /**
         * construtor.
         *
         * @param kestrel   kestrel client
         * @param processor processor
         * @param qn        queue name
         */
        public AnalyticsTask(final KestrelClient kestrel,
                             final AbstractProcessor processor,
                             final boolean keepAdRawData,
                             final Queue<Object> queue,
                             final String qn) {
            this.kestrelClient = kestrel;
            this.statisticProcessor = processor;
            this.queueName = qn;
            this.queue = queue;
            this.keepAdRawData = keepAdRawData;
        }

        @Override
        public void run() {
            log.info("Starting the analytics (" + queueName + ") threads");

            while (true) {
                try {
                    Object stat = this.get();
                    if (stat == null) {
                        Thread.sleep(KESTREL_SLEEP_INTERVAL);
                        continue;
                    }
                    statisticProcessor.addStats(stat);
                    if (keepAdRawData) {
                        queue.add(stat);
                    }
                } catch (Exception e) {
                    log.error("Exceptino in handling stats: ", e);
                }
            }
        }

        /**
         * fetch object from kestrel.
         *
         * @return Object clck, view, cps
         */
        private Object get() {
            Object stat = kestrelClient.get(queueName);
            if (stat instanceof byte[]) {
                stat = HessianSerializer.deserialize((byte[]) stat);
            }
            return stat;
        }

    }

    private static class AdRawDataWriter implements Runnable {
        private static int sleepTime = ConfigurationReader.getInt("buzzads.setting.raw.data.sleep.time");
        private static int batchSize = ConfigurationReader.getInt("buzzads.setting.raw.data.batch.size");
        private static String path = ConfigurationReader.getString("buzzads.setting.raw.data.file.path");
        private static JSONSerializer json = new JSONSerializer();
        private Queue<Object> queue;

        public AdRawDataWriter(Queue<Object> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String date = DateTimeUtil.formatDate(new Date());
                    writeFiles(date);
                }
            } catch (Exception e) {
                log.error("Write ads raw data file thread error!" + e);
            }

        }

        private String getFileName(String date, String name) {
            return path + name + "-" + date + ".txt";
        }

        private boolean writeFiles(String date) {
            File showupsFile = new File(getFileName(date, "showups"));
            File clicksFile = new File(getFileName(date, "clicks"));

            try {
                int bufferSize = 1024 * 1024;
                Writer showupsWriter = new BufferedWriter(new FileWriter(showupsFile, true), bufferSize);
                Writer clicksWriter = new BufferedWriter(new FileWriter(clicksFile, true), bufferSize);
                int i = 0;

                while (true) {
                    Object o = queue.poll();
                    if (o != null) {
                        if (o instanceof AdShowUps) {
                            showupsWriter.write(generateShowupsJson((AdShowUps) o) + "\n");
                        } else if (o instanceof AdClick) {
                            clicksWriter.write(generateClickJson((AdClick) o) + "\n");

                        }
                        boolean isNextDay = !DateTimeUtil.formatDate(new Date()).equals(date);
                        if (i++ >= batchSize || isNextDay) {
                            showupsWriter.flush();
                            clicksWriter.flush();
                            i = 0;
                            if (isNextDay) {
                                showupsWriter.close();
                                clicksWriter.close();
                                return false;
                            }
                        }
                    } else {
                        log.debug("Writer file thread sleep time:" + sleepTime);
                        Thread.sleep(sleepTime);
                        showupsWriter.flush();
                        clicksWriter.flush();
                    }
                }

            } catch (Exception e) {
                log.error("Write ads raw data file thread error" + e);
            }
            return false;
        }

        private String generateShowupsJson(AdShowUps showups) {
            Map<String, Object> showupMap = new LinkedHashMap<String, Object>();
            Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
            showupMap.put("app", "buzzads");
            showupMap.put("type", "showup");
            dataMap.put("entryIds", showups.getAdEntryIds());
            dataMap.put("sourceUrl", showups.getSourceUrl());
            dataMap.put("network", showups.getNetwork().name());
            dataMap.put("uuid", showups.getPublisherUuid());
            dataMap.put("cookieId", showups.getCookieId());
            dataMap.put("ip", showups.getIp());
            dataMap.put("ua", showups.getUa());
            showupMap.put("data", dataMap);
            showupMap.put("time", DateTimeUtil.formatDate(
                    showups.getCreateAt(),
                    DateTimeUtil.FMT_DATE_YYYY_MM_DD_HH_MM_SS));
            return json.deepSerialize(showupMap);
        }

        private String generateClickJson(AdClick click) {
            Map<String, Object> clickMap = new LinkedHashMap<String, Object>();
            Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
            clickMap.put("app", "buzzads");
            clickMap.put("type", "click");
            dataMap.put("entryId", click.getAdEntryId());
            dataMap.put("sourceUrl", click.getSourceUrl());
            dataMap.put("network", click.getNetwork().name());
            dataMap.put("uuid", click.getPublisherUuid());
            dataMap.put("cookieId", click.getCookieId());
            dataMap.put("ip", click.getIp());
            dataMap.put("ua", click.getUa());
            clickMap.put("data", dataMap);
            clickMap.put("time", DateTimeUtil.formatDate(
                    click.getCreateAt(),
                    DateTimeUtil.FMT_DATE_YYYY_MM_DD_HH_MM_SS));
            return json.deepSerialize(clickMap);
        }

    }

}
