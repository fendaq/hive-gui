package com.webkettle.core.hive.connection;

import com.webkettle.core.commons.CommAttr;
import com.webkettle.core.hive.datasource.HiveDataSource;
import org.apache.hive.jdbc.HiveDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据操作源连接池封装
 * @author gsk
 */
@Component
public class ConnectionTools {

    private HiveDataSource hiveDataSource;

    private Driver driver = new HiveDriver();

    private String isDestroy = "N";

    // 数据源连接
    private List<ConnectionCache> connections = new ArrayList<>();

    private boolean isInit = false;

    protected static final Logger logger = LoggerFactory.getLogger(ConnectionTools.class);

    /**
     * 初始化连接池
     * @param dataSource
     *          数据源
     */
    public synchronized void init(HiveDataSource dataSource){
        if (isInit){
            return;
        }
        this.hiveDataSource = dataSource;
//        System.setProperty("java.security.krb5.conf", "D:\\huawei\\FusionInsight_Services_ClientConfig\\Hive\\jdbc-examples\\conf\\krb5.conf");
        String krb5Path = System.getProperty("user.dir") + "/" + this.hiveDataSource.getKrb5();
        logger.info("Krb5 Path:[{}]", krb5Path);
        System.setProperty("java.security.krb5.conf", krb5Path);
        try {
            DriverManager.registerDriver(driver);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        Connection connection = getConnection();
        returnConnection(connection);
        isInit = true;
    }


    /**
     * 从连接池里取出一个连接
     * @return
     *          连接对象
     */
    public synchronized Connection getConnection(){
    	String url = hiveDataSource.getJdbcUrl();
        logger.info("Hive Url:[{}]", url);
        try {
            logger.info("开始获取HIVE连接");
            Connection connection = DriverManager.getConnection(url, hiveDataSource.getUsername(), hiveDataSource.getPassword());
            logger.info("获取HIVE连接成功");
            return connection;
        }catch (SQLException e){
            logger.info("获取HIVE连接失败");
            logger.info("获取HIVE连接失败", e);
            logger.error("获取HIVE连接失败", e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 归还一个连接给连接池
     * @param connection
     */
    public void returnConnection(Connection connection){
        try {
            synchronized (CommAttr.LOCK.QUERY_LOG){
                if (!connection.isClosed()){
                    connection.close();
                    logger.info("HIVE连接关闭");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * 数据连接缓存程序
     */
    private class ConnectionCache{
        private Connection connection;
        private long cacheTime;
        private int status;         // 0 = 正在连接, 1 = 正常连接, 2 = 使用中, -1 = 链接失效
    }


    public static void main(String[] args){
        HiveDataSource hiveDataSource = new HiveDataSource();
        hiveDataSource.setUsername("riskcon2");
        hiveDataSource.setPassword("Risk.123");
        Driver driver = null;
        hiveDataSource.setJdbcname("org.apache.hive.jdbc.HiveDriver");
        hiveDataSource.setJdbcUrl("jdbc:hive2://103.160.108.161:24002,103.160.108.162:24002,103.160.108.163:24002/");
        hiveDataSource.setKrb5("xxxxxkrb5.cnf路径");
//        hiveDataSource.setZooKeeperNamespace("hiveserver2");
//        hiveDataSource.setServiceDiscoveryMode("zooKeeper");
        try {
            driver = (Driver)Class.forName(hiveDataSource.getJdbcname()).newInstance();
        }catch (ClassNotFoundException | InstantiationException | IllegalAccessException e){
            throw new RuntimeException(e);
        }
        hiveDataSource.setDriver(driver);
        ConnectionTools tools = new ConnectionTools();
        tools.init(hiveDataSource);
        try{
            Connection connection = tools.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT OVERWRITE DIRECTORY '/cust_upload/admin/2018/10/23/acf3f0df675d405fb830fcad99f7a414/2222.txt' ROW FORMAT DELIMITED FIELDS TERMINATED BY '，' SELECT * FROM DB_10_22.TABLE1");
            statement.execute();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


}
