package com.buzzinate.buzzads.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.google.common.base.Function;

/**
 * 
 * @author johnson
 *
 */
public class RedisClient {
    
    private static Log log = LogFactory.getLog(RedisClient.class);
    
    private static final int MAX_ACTIVE = ConfigurationReader.getInt("redis.max.active", 200);
    private static final boolean TEST_WHILE_IDLE = ConfigurationReader.getBoolean("redis.testWhileIdle", true);
    private static final long TIME_BETWEEN_EVICTION_RUNMILLIS = 
            ConfigurationReader.getLong("redis.timeBetweenEvictionRunsMillis", 1000L * 120);
    
    private final String ips;
    
    private ShardedJedisPool pool;
    private JedisPool jedisPool;
    private JedisPoolConfig poolConfig;

    public RedisClient(String ips) {
        this.ips = ips;
        init();
    }
    
    public void init() {
        try {
            poolConfig = new JedisPoolConfig();
            poolConfig.setMaxActive(MAX_ACTIVE);
            poolConfig.setTestWhileIdle(TEST_WHILE_IDLE);
            poolConfig.setTimeBetweenEvictionRunsMillis(TIME_BETWEEN_EVICTION_RUNMILLIS);
            poolConfig.setTestOnBorrow(true);
            
            ArrayList<JedisShardInfo> jedisShardInfos = new ArrayList<JedisShardInfo>();
            JedisShardInfo jinfo = null;
            for (String ip : ips.split(",")) {
                String[] hostPort = ip.split(":");
                jinfo = new JedisShardInfo(hostPort[0], Integer.parseInt(hostPort[1]));
                jedisShardInfos.add(jinfo);
            }
            pool = new ShardedJedisPool(poolConfig, jedisShardInfos);
            // 取得最后一个ip配置pub/sub redis
            jedisPool = new JedisPool(poolConfig, jinfo.getHost(), jinfo.getPort());
        } catch (Exception e) {
            log.fatal("Failed to init redis client. ");
            throw new IllegalStateException("Failed to init redis client.", e);
        }
        
    }
    
    /**
     * Set an object in redis cache
     * @param key
     * @param expire
     * @param val
     */
    public void set(final String key, final int expire, final Object val) {
        try {
            final byte[] bs = serialize(val);
            if (bs != null) {
                JedisUtil.using(pool).call(new Function<ShardedJedis, String>() {
                    @Override
                    public String apply(ShardedJedis jedis) {
                        jedis.setex(key.getBytes(), expire, bs);
                        return null;
                    }
                });
            }
        } catch (Exception e) {
            log.warn("Failed to set object to redis. [key=" + key + ", val=" + val + "]", e);
        }
    }
    
    /**
     * Set a string in redis cache
     * @param key
     * @param expire
     * @param val
     */
    public void set(final String key, final int expire, final String val) {
        try {
            JedisUtil.using(pool).call(new Function<ShardedJedis, String>() {
                @Override
                public String apply(ShardedJedis jedis) {
                    jedis.setex(key, expire, val);
                    return null;
                }
            });
        } catch (Exception e) {
            log.warn("Failed to set to redis. [key=" + key + ", val=" + val + "]", e);
        }
    }
    
    /**
     * Get an object from redis cache
     * @param key
     * @return
     */
    public Object getObject(final String key) {
        try {
            byte[] bs = JedisUtil.using(pool).call(new Function<ShardedJedis, byte[]>() {
                @Override
                public byte[] apply(ShardedJedis jedis) {
                    return jedis.get(key.getBytes());
                }
            });
            if (bs != null) {
                return deserialize(bs);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("Failed to get object from redis. [key=" + key + "]", e);
            return null;
        }
    }
    
    /**
     * Get a string from redis cache
     * @param key
     * @return
     */
    public String get(final String key) {
        try {
            return JedisUtil.using(pool).call(new Function<ShardedJedis, String>() {
                @Override
                public String apply(ShardedJedis jedis) {
                    return jedis.get(key);
                }
            });
        } catch (Exception e) {
            log.warn("Failed to get from redis. [key=" + key + "]", e);
            return null;
        }
    }
    
    /**
     * Add a string into a redis set
     * @param key
     * @param members
     * @return
     */
    public long sadd(final String key, final String... members) {
        try {
            Long res = JedisUtil.using(pool).call(new Function<ShardedJedis, Long>() {
                @Override
                public Long apply(ShardedJedis jedis) {
                    return jedis.sadd(key, members);
                }
            });
            return res == null ? 0L : res.longValue();
        } catch (Exception e) {
            log.warn("Failed to sadd to redis. [key=" + key + "]", e);
            return 0L;
        }
    }
    
    /**
     * Remove a string from a redis set
     * @param key
     * @param members
     * @return
     */
    public long srem(final String key, final String... members) {
        try {
            Long res = JedisUtil.using(pool).call(new Function<ShardedJedis, Long>() {
                @Override
                public Long apply(ShardedJedis jedis) {
                    return jedis.srem(key, members);
                }
            });
            return res == null ? 0L : res.longValue();
        } catch (Exception e) {
            log.warn("Failed to srem from redis. [key=" + key + "]", e);
            return 0L;
        }
    }
    
    /**
     * Return the number of members in a redis set
     * @param key
     * @return
     */
    public long scard(final String key) {
        try {
            Long res = JedisUtil.using(pool).call(new Function<ShardedJedis, Long>() {
                @Override
                public Long apply(ShardedJedis jedis) {
                    return jedis.scard(key);
                }
            });
            return res == null ? 0L : res.longValue();
        } catch (Exception e) {
            log.warn("Failed to scard from redis. [key=" + key + "]", e);
            return 0L;
        }
    }
    
    /**
     * Return all the members in a redis set
     * @param key
     * @return
     */
    public Set<String> smembers(final String key) {
        try {
            Set<String> res = JedisUtil.using(pool).call(new Function<ShardedJedis, Set<String>>() {
                @Override
                public Set<String> apply(ShardedJedis jedis) {
                    return jedis.smembers(key);
                }
            });
            if (res == null) {
                res = new HashSet<String>();
            }
            return res;
        } catch (Exception e) {
            log.warn("Failed to smember from redis. [key=" + key + "]", e);
            return new HashSet<String>();
        }
    }
    
    /**
     * Delete an key in redis
     * @param key
     */
    public void delete(final String key) {
        try {
            JedisUtil.using(pool).call(new Function<ShardedJedis, String>() {
                @Override
                public String apply(ShardedJedis jedis) {
                    jedis.del(key);
                    return null;
                }
            });
        } catch (Exception e) {
            log.warn("Failed to delete from redis. [key=" + key + "]", e);
        }
    }
    
    public void shutdown() {
        pool.destroy();
    }
    
    private byte[] serialize(Object obj) {
        if (obj != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(bos);
                oos.writeObject(obj);
                return bos.toByteArray();
            } catch (Exception ex) {
                log.fatal("Failed to serialize obj." + obj.toString());
                return null;
            } finally {
                try {
                    bos.close();
                    oos.close();
                } catch (Exception e) {
                    log.error("Exception: " + e.getMessage());
                }
            }
        } else {
            return null;
        }
        
    }
    
    private Object deserialize(byte[] bs) {
        if (bs != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(bs);
            ObjectInputStream inputStream = null;
            try {
                inputStream = new ObjectInputStream(bis);
                Object obj = inputStream.readObject();
                return obj;
            } catch (Exception ex) {
                log.fatal("Failed to deserialize.");
                return null;
            } finally {
                try {
                    bis.close();
                    inputStream.close();
                } catch (Exception e) {
                    log.error("Exception: " + e.getMessage());
                }
            }
        } else {
            return null;
        }
    }
    
    /**
     * publish a channel in redis for sub to pub
     * @param   channelName
     * @param   info
     */
    public void publish(final String channel, final String message) {
        Jedis jedis = jedisPool.getResource();
        boolean returned = false;
        try {
            jedis.publish(channel, message);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
            returned = true;
            log.warn("Failed to publish a channel in redis. [channel=" + channel + " message=" + message + "]", e);
        } finally {
            if (!returned) jedisPool.returnResource(jedis);
        }
    }

    /**
     * subscribe a channel in redis for sub to pub
     * @param   channelName
     * @param   info
     */
    public void subscribe(final JedisPubSub listner, final String channel) {
        Jedis jedis = jedisPool.getResource();
        boolean returned = false;
        try {
            jedis.subscribe(listner, channel);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
            returned = true;
            log.warn("Failed to subscribe a channel in redis. [channel=" + channel + "]", e);
        } finally {
            if (!returned) jedisPool.returnResource(jedis);
        }
    }

}