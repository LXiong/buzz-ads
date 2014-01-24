package com.buzzinate.buzzads.analytics.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: kun
 * Date: 13-8-5
 * Time: 下午11:42
 */
public abstract class AbstractBaseReportWriter<T> implements AdxReportWriter {
    protected String path;
    protected ExecutorService pool = Executors.newCachedThreadPool();
    protected ExecutorService singleThread = Executors.newSingleThreadExecutor();

    public AbstractBaseReportWriter(String path) {
        this.path = path;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public Future<Boolean> process(List<Object> data) {
        Map<String, List<T>> result = sortData(data);
        final List<Future<Boolean>> futureList = doJob(result);
        Future<Boolean> future = singleThread.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                for (Future<Boolean> future : futureList) {
                    future.get();
                }
                return Boolean.TRUE;
            }
        });
        return future;
    }

    public void stop() {
        pool.shutdown();
    }

    protected abstract List<Future<Boolean>> doJob(Map<String, List<T>> map);

    protected abstract Map<String, List<T>> sortData(List<Object> data);
}
