package com.buzzinate.adx.util;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.core.util.RedisClient;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-7-31
 * Time: 下午7:20
 */
public class RedisUtils {
    private RedisUtils() {
    }

    public static RedisClient getInstance() {
        return RedisClientGenerator.redisClient;
    }

    private static class RedisClientGenerator {
        private final static RedisClient redisClient;

        static {
            redisClient = new RedisClient(ConfigurationReader.getString("redis.ips"));
        }
    }

}
