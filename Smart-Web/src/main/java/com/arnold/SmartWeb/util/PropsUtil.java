package com.arnold.SmartWeb.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropsUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载配置文件
     *
     * @param fileName
     * @return
     */
    public static Properties loadProps(String fileName) {
        InputStream is =
                //PropsUtil.class.getClassLoader().getResourceAsStream(fileName);//ok
                //PropsUtil.class.getResourceAsStream(fileName);//ok
                Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);//ok

        Properties properties = new Properties();
        try {
            if (is != null) {
                properties.load(is);
            } else {
                throw new FileNotFoundException(fileName + "file not found");
            }
        } catch (IOException e) {
            LOGGER.error("load properties file fail");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("close input stream fail", e);
                }
            }
        }
        return properties;
    }

    public static String getString(Properties properties, String key, String defaultValue) {
        return properties.getProperty(key,defaultValue);
    }

    public static String getString(Properties properties, String key) {
        return properties.getProperty(key);
    }

    /**
     * 获取数值型属性（默认值为 0）
     */
    public static int getInt(Properties props, String key) {
        return getInt(props, key, 0);
    }

    // 获取数值型属性（可指定默认值）
    public static int getInt(Properties props, String key, int defaultValue) {
        int value = defaultValue;
        if (props.containsKey(key)) {
            value = CastUtil.castInt(props.getProperty(key));
        }
        return value;
    }

    /**
     * 获取布尔型属性（默认值为 false）
     */
    public static boolean getBoolean(Properties props, String key) {
        return getBoolean(props, key, false);
    }

    /**
     * 获取布尔型属性（可指定默认值）
     */
    public static boolean getBoolean(Properties props, String key, boolean defaultValue) {
        boolean value = defaultValue;
        if (props.containsKey(key)) {
            value = CastUtil.castBoolean(props.getProperty(key));
        }
        return value;
    }

}
