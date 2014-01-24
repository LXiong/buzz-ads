package com.buzzinate.adx.util;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.jolbox.bonecp.BoneCPDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcUtils {
    private static final Log log = LogFactory.getLog(JdbcUtils.class);

    public static JdbcTemplate getInstance() {
        return JdbcTemplateGenerator.jdbcTemplate;
    }

    private static class JdbcTemplateGenerator {
        private final static JdbcTemplate jdbcTemplate;

        static {
            BoneCPDataSource dataSource = new BoneCPDataSource();
            dataSource.setJdbcUrl(ConfigurationReader.getString("adx.database.master.url"));
            dataSource.setDriverClass(ConfigurationReader.getString("adx.database.driver.name"));
            dataSource.setUsername(ConfigurationReader.getString("adx.database.user.name"));
            dataSource.setPassword(ConfigurationReader.getString("adx.database.user.password"));
            dataSource.setIdleConnectionTestPeriodInMinutes(ConfigurationReader.getLong("adx.database.idle.connection.test.period.in.minutes", 60));
            dataSource.setIdleMaxAgeInMinutes(ConfigurationReader.getLong("adx.database.idle.max.age.in.minutes", 240));
            dataSource.setMaxConnectionsPerPartition(ConfigurationReader.getInt("adx.database.max.connections.per.partition", 5));
            dataSource.setMinConnectionsPerPartition(ConfigurationReader.getInt("adx.database.min.connections.per.partition", 1));
            dataSource.setPartitionCount(ConfigurationReader.getInt("adx.database.partition.count", 2));
            dataSource.setAcquireIncrement(ConfigurationReader.getInt("adx.database.acquire.increment", 10));
            dataSource.setStatementsCacheSize(ConfigurationReader.getInt("adx.database.statements.cache.size", 50));
            dataSource.setReleaseHelperThreads(ConfigurationReader.getInt("adx.database.release.helper.threads", 3));
            dataSource.setCloseConnectionWatch(ConfigurationReader.getBoolean("adx.database.close.connection.watch", false));
            jdbcTemplate = new JdbcTemplate(dataSource);
        }
    }
}