/*
package com.arnold.SmartWeb.helper;

import com.arnold.SmartFramework.util.PropsUtil;
import com.arnold.SmartWeb.model.Customer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DatabaseHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
    private static ThreadLocal<Connection> threadConnection = new ThreadLocal<>();

    private static final String DRIVER;

    private static final String URL;

    private static final String USERNAME;

    private static final String PASSWORD;



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
    }


    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList = new ArrayList<>();
        Connection connection = getConnection();
        try {
            entityList = QUERY_RUNNER.query(connection, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("queryEntity fail", e);
        } finally {
            closeConnetion(connection);
        }
        return entityList;
    }

    public static void closeConnetion(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("close connection fail");
            } finally {
                threadConnection.remove();
            }
        }
    }

    public static Connection getConnection() {
        Connection connection = threadConnection.get();
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                LOGGER.error("get connection fail");
            } finally {
                threadConnection.set(connection);
            }
        }
        return connection;
    }

    public static void beginTransaction() {
        Connection connection = getConnection();
        if (connection!= null){
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                LOGGER.error("begin transaction fail", e);
            } finally {
                //为什么在finally中
                threadConnection.set(connection);
            }
        }
    }

    public static void commitTransaction() {
        Connection connection = getConnection();
        try {
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error("commit transaction fail",e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("conn close fail");
            } finally {
                threadConnection.remove();
            }
        }
    }

    public static void rollbackTransaction() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.rollback();
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("rollback transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                threadConnection.remove();
            }
        }
    }


    public static boolean insertEntity(Class<Customer> customerClass, Map<String, Object> fieldMap) {
        return Boolean.parseBoolean(null);
    }
}
*/
