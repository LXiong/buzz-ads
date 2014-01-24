package com.buzzinate.adx.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;

import com.buzzinate.buzzads.core.util.ConfigurationReader;

/**
 * @author Martin
 *
 */
public class HBaseClient {

    private static String zookeeperIps = ConfigurationReader.getString("hbase.zookeeper.quorum");

    private static HTablePool pool;

    static {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", zookeeperIps);
        // if zookeeper is empty, not create hbase pool
        if (StringUtils.isNotEmpty(zookeeperIps)) {
            pool = new HTablePool(conf, 150);
        }
    }

    public static HTableInterface getTable(String tableName) {
        if (pool != null) {
            HTableInterface table = pool.getTable(tableName);
            return table;
        }
        return null;
    }
}