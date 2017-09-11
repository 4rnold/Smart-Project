package com.arnold.SmartWeb.helper;

import com.arnold.SmartWeb.util.PropsUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabasePoolHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabasePoolHelper.class);

    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
    private static ThreadLocal<Connection> threadConnection = new ThreadLocal<>();

    private static final String DRIVER;

    private static final String URL;

    private static final String USERNAME;

    private static final String PASSWORD;

    private static final BasicDataSource DATA_SOURCE;
    

    static {
        Properties conf = PropsUtil.loadProps("config.properties");
        String driver = conf.getProperty("jdbc.driver");
        String url = conf.getProperty("jdbc.url");
        String username = conf.getProperty("jdbc.username");
        String password = conf.getProperty("jdbc.password");

        DRIVER = driver;
        URL = url;
        USERNAME = username;
        PASSWORD = password;

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);

    }


    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList = new ArrayList<>();
        Connection connection = getConnection();
        try {
            entityList = QUERY_RUNNER.query(connection, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("queryEntity fail", e);
        } finally {
            //closeConnetion(connection);
            threadConnection.remove();
        }
        return entityList;
    }

    //连接池没有了关闭连接方法
    /*public static void closeConnetion(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("close connection fail");
            } finally {
                threadConnection.remove();
            }
        }
    }*/

    public static Connection getConnection() {
        Connection connection = threadConnection.get();
        if (connection == null) {
            try {
                //connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                connection = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection fail");
            } finally {
                threadConnection.set(connection);
            }
        }
        return connection;
    }
}
