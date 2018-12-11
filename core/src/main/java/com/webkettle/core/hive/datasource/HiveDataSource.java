package com.webkettle.core.hive.datasource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Driver;

/**
 * 数据操作数据源封装类
 * @author gsk
 */
@Component
public class HiveDataSource {

    @Value("${bigdata.db.username}")
    private String username;
    @Value("${bigdata.db.password}")
    private String password;
    @Value("${bigdata.db.zookeper}")
    private String zookeper;
    @Value("${bigdata.db.jdbcname}")
    private String jdbcname;
    @Value("${bigdata.db.jdbc-url}")
    private String jdbcUrl;
    @Value("${bigdata.hdfs.krb5-path}")
    private String krb5;
//    @Value("${bigdata.db.zooKeeper-namespace}")
//    private String zooKeeperNamespace;
//    @Value("${bigdata.db.service-discovery-mode}")
//    private String serviceDiscoveryMode;


    //=-=-=-=-=-=-=-=-

    private Driver driver;
    
    
	public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    public String getZookeper(){
        return this.zookeper;
    }
    public String getJdbcname(){
        return this.jdbcname;
    }
    public String getJdbcUrl(){
        return this.jdbcUrl;
    }

    public String getKrb5() {
        return krb5;
    }

    public void setKrb5(String krb5) {
        this.krb5 = krb5;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setZookeper(String zookeper) {
        this.zookeper = zookeper;
    }

    public void setJdbcname(String jdbcname) {
        this.jdbcname = jdbcname;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /**
     * 实例化数据库驱动(单例)
     * @return
     *          数据库驱动
     */
    private Driver instantiationDriver(){
        if (null != driver){
            return driver;
        }
        try {
            driver = (Driver)Class.forName(this.getJdbcname()).newInstance();
        }catch (ClassNotFoundException | InstantiationException | IllegalAccessException e){
            throw new RuntimeException(e);
        }
        return driver;
    }
}
