package com.buzzinate.buzzads.data.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.kestrel.KestrelClient;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com>
 * 
 */
public final class GenomeFileWriter {
    private static Log log = LogFactory.getLog(GenomeFileWriter.class);

    private static int threadSize = ConfigurationReader.getInt("lezhi.genome.kestrel.thread.size");

    private GenomeFileWriter() {

    }

    /**
     * run as a single process
     * 
     * @param args
     */
    public static void main(String[] args) {
        log.info("genome file writer begin to start");
        Queue<String> queue = new ConcurrentLinkedQueue<String>();

        ExecutorService excutorService = Executors.newFixedThreadPool(threadSize);
        for (int i = 0; i < threadSize; i++) {
            excutorService.execute(new GenomeConsumer(queue));
        }

        ExecutorService singleExcutorService = Executors.newSingleThreadExecutor();
        singleExcutorService.execute(new GenomeFileWriterTask(queue));
        log.info("genome file writer start successfully");
    }
}

/**
 * 
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com>
 * 
 */
class GenomeConsumer implements Runnable {

    private static Log log = LogFactory.getLog(GenomeConsumer.class);

    private static String ips = ConfigurationReader.getString("lezhi.genome.kestrel.ips");
    private static int poolSize = ConfigurationReader.getInt("lezhi.genome.kestrel.pool.size");
    private static int sleepTime = ConfigurationReader.getInt("lezhi.genome.sleep.time");
    private static KestrelClient kestrelClient = new KestrelClient(ips, poolSize);

    private final Queue<String> queue;

    GenomeConsumer(Queue<String> q) {
        queue = q;
    }

    public void run() {
        try {
            log.info("start a genome thread to pop string from kestrel!");
            while (true) {
                String info = (String) kestrelClient.get("genome");
                if (info != null) {
                    queue.add(info);
                } else {
                    log.debug("genome writer file thread begin to sleep:" + sleepTime);
                    Thread.sleep(sleepTime);
                }
            }
        } catch (InterruptedException ex) {
            log.error("genome thread which pop string from kestrel is interrupted" + ex);
        }
    }
}

/**
 * 
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com>
 * 
 */
class GenomeFileWriterTask implements Runnable {
    private static Log log = LogFactory.getLog(GenomeFileWriterTask.class);

    private static int sleepTime = ConfigurationReader.getInt("lezhi.genome.sleep.time");
    private static int batchSize = ConfigurationReader.getInt("lezhi.genome.batch.size");
    private static String path = ConfigurationReader.getString("lezhi.genome.file.path");

    private final Queue<String> queue;

    GenomeFileWriterTask(Queue<String> q) {
        queue = q;
    }
    public void run() {
        try {
            log.info("start genome writer file thread!");
            while (true) {
                String date = DateTimeUtil.formatDate(new Date());
                if (writeFiles(date)) {
                    continue;
                }
            }
        } catch (Exception e) {
            log.error("error happened to genome write file thread!" + e);
        }
    }

    private String getFileName(String date) {
        return path + "genome-" + date + ".txt";
    }

    private boolean writeFiles(String date) {
        try {
            File file = new File(getFileName(date));
            int bufferSize = 1024 * 1024;
            Writer writer = new BufferedWriter(new FileWriter(file, true), bufferSize);
            int i = 0;
            while (true) {
                String info = queue.poll();
                if (info != null) {
                    writer.write(info + "\n");
                    boolean isNextDay = !DateTimeUtil.formatDate(new Date()).equals(date);
                    if (i++ >= batchSize || isNextDay) {
                        writer.flush();
                        i = 0;
                        if (isNextDay) {
                            writer.close();
                            return false;
                        }
                    }
                } else {
                    log.debug("Writer file thread sleep time:" + sleepTime);
                    Thread.sleep(sleepTime);
                    writer.flush();
                }
            }
        } catch (Exception e) {
            log.error("write file error" + e);
        }
        return false;
    }
}