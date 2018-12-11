package com.aiyi.core;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@ConfigurationProperties
public class AiYiApplicationContiger implements TransactionManagementConfigurer {

	@Autowired
	private DataSource dataSource;
	
	
	@Bean(name = "sqlSessionTemplateASS")
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
	    return new SqlSessionTemplate(sqlSessionFactory);
	}
	
	@Bean
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
	    return new DataSourceTransactionManager(dataSource);
	}
}
