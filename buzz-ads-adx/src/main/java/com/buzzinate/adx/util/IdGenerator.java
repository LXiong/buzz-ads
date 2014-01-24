package com.buzzinate.adx.util;

import com.buzzinate.buzzads.core.util.ConfigurationReader;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: kun
 * Date: 13-7-28
 * Time: 下午11:04
 * id计数器,单例模式
 */
public final class IdGenerator {
    private static String computerName;
    //最大计数值，目前设为10000就够了
    private static final int MAX_VALUE = ConfigurationReader.getInt("id.generator.max.value", 10000);
    private volatile AtomicInteger id = new AtomicInteger(0);

    private IdGenerator() {
    }

    public static IdGenerator getInstance() {
        return IdGeneratorGen.instance;
    }

    public String getRequestId() {
        return computerName + ":" + System.currentTimeMillis() + ":" + IdGenerator.getInstance().getNext();
    }

    /**
     * 无阻塞自增，线程安全，到达最大值从0开始重新计算
     *
     * @return
     */
    public int getNext() {
        int expected = id.get();
        int update;
        if (expected == MAX_VALUE) {
            update = 0;
        } else {
            update = expected + 1;
        }
        while (!id.compareAndSet(expected, update)) {
            expected = id.get();
            if (expected == MAX_VALUE) {
                update = 0;
            } else {
                update = expected + 1;
            }
        }
        return update;
    }

    private static class IdGeneratorGen {
        private static IdGenerator instance;

        static {
            instance = new IdGenerator();
        }
    }

    static {
        try {
            InetAddress address = InetAddress.getLocalHost();
            computerName = address.getHostName();
        } catch (UnknownHostException e) {
            computerName = ConfigurationReader.getString("default.computer.name", "pc-unknown");
        }
    }


}
