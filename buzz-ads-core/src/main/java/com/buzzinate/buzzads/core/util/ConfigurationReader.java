package com.buzzinate.buzzads.core.util;

import java.io.Serializable;
import java.net.URL;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation for Configuration files reader.
 * @author zyeming
 */
public final class ConfigurationReader implements Serializable {

    private static final long serialVersionUID = -7245754360466343732L;

    private static Log log = LogFactory.getLog(ConfigurationReader.class);

    /**
     * Path to configuration file.
     */
    private static final String CONFIG_PATH = "/configuration.xml";

    private static Configuration configration;

    private ConfigurationReader() { }
    
    public static Configuration getConfiguration() {
        return configration;
    }

    /**
     * loading the XML configuration file, it creates a configuration factory
     * instance for given configuration file.
     * 
     */
    static {
        try {
            ConfigurationFactory factory = new ConfigurationFactory();
            URL configURL = ConfigurationReader.class.getResource(CONFIG_PATH);
            factory.setConfigurationURL(configURL);
            AbstractConfiguration.setDelimiter('~');
            configration = factory.getConfiguration();
        } catch (Exception exc) {
            if (log.isDebugEnabled()) {
                log.debug("Exception in configuration reading, can not load configuration files");
            }
            exc.printStackTrace();
        }
    }

    /**
     * Returns the String value of the given key.
     * 
     * @param paramName key
     * @return Stirng value corresponding to given key
     * @throws NoSuchElementException
     *             if the given key is not present
     */
    public static String getString(String paramName) {
        return configration.getString(paramName);
    }

    /**
     * Returns the String value of the given key if key is present otherwise
     * returns defalut value.
     * 
     * @param paramName
     *            key
     * @param defaultValue
     *            defalut value to return if the given key is not present
     * @return String value
     * @throws NoSuchElementException
     *             if the given key is not present
     */
    public static String getString(String paramName, String defaultValue) {
        return configration.getString(paramName, defaultValue);
    }
    
    /**
     * Returns a String value of a path with forward slash at the end.
     * 
     * @param paramName
     * @return
     */
    public static String getStringPath(String paramName) {
        String s = configration.getString(paramName);
        if (s.endsWith("/")) return s;
        return s + "/";
    }
    
    /**
     * Returns the Boolean value of the given key.
     * 
     * @param paramName
     * @return Boolean value corresponding to given key
     * @throws NoSuchElementException
     *             if the given key is not present
     */
    public static boolean getBoolean(String paramName) {
        return configration.getBoolean(paramName, false);
    }
    
    /**
     * Returns the Boolean value of the given key.
     * 
     * @param paramName
     * @param defaultValue
     * @return Boolean value corresponding to given key
     * @throws NoSuchElementException
     *             if the given key is not present
     */
    public static boolean getBoolean(String paramName, boolean defaultValue) {
        return configration.getBoolean(paramName, defaultValue);
    }

    /**
     * Returns the Long value of the given key.
     * 
     * @param paramName
     *            key
     * @return Long value corresponding to given key
     * @throws NoSuchElementException
     *             if the given key is not present
     */
    public static long getLong(String paramName) {
        return configration.getLong(paramName);
    }

    /**
     * Returns the Long value of the given key if key is present otherwise
     * returns defalut value.
     * 
     * @param paramName
     *            key
     * @param defaultValue
     *            defalut value to return if the given key is not present
     * @return Long value
     * @throws NoSuchElementException
     *             if the given key is not present
     */
    public static long getLong(String paramName, long defaultValue) {
        return configration.getLong(paramName, defaultValue);
    }

    /**
     * Returns the Integer value of the given key.
     * 
     * @param paramName
     *            key
     * @return Long value corresponding to given key
     * @throws NoSuchElementException
     *             if the given key is not present
     */
    public static int getInt(String paramName) {
        return configration.getInt(paramName);
    }

    /**
     * Returns the Integer value of the given key if key is present otherwise
     * returns defalut value.
     * 
     * @param paramName
     *            key
     * @param defaultValue
     *            defalut value to return if the given key is not present
     * @return Long value
     * @throws NoSuchElementException
     *             if the given key is not present
     */
    public static int getInt(String paramName, int defaultValue) {
        return configration.getInt(paramName, defaultValue);
    }

    /**
     * Returns the Float value of the given key.
     * 
     * @param paramName
     *            key
     * @return Float value corresponding to given key
     * @throws NoSuchElementException
     *             if the given key is not present
     */
    public static float getFloat(String paramName) {
        return configration.getFloat(paramName);
    }

    /**
     * Returns the Float value of the given key if key is present otherwise
     * returns defalut value.
     * 
     * @param paramName
     *            key
     * @param defaultValue
     *            defalut value to return if the given key is not present
     * @return Float value
     * @throws NoSuchElementException
     *             if the given key is not present
     */
    public static float getFloat(String paramName, float defaultValue) {
        return configration.getFloat(paramName, defaultValue);
    }

    /**
     * Returns the Double value of the given key.
     * 
     * @param paramName
     *            key
     * @return double value
     * @throws NoSuchElementException
     *             if the given key is not present
     */
    public static double getDouble(String paramName) {
        return configration.getDouble(paramName);
    }

    /**
     * Returns the Double value of the given key if key is present otherwise
     * returns defalut value.
     * 
     * @param paramName
     *            key
     * @param defaultValue
     *            defalut value to return if the given key is not present
     * @return double value
     * @throws NoSuchElementException
     *             if the given key is not present
     */
    public static double getDouble(String paramName, double defaultValue) {
        return configration.getDouble(paramName, defaultValue);
    }

}
