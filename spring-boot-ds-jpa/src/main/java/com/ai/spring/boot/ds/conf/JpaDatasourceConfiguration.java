package com.ai.spring.boot.ds.conf;

import com.ai.spring.boot.ds.datasource.SecHikariDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

/**
 * data source 自动配置类
 *
 * @author 石头
 * @Date 2020/6/8
 * @Version 1.0
 **/
@Configuration
@ConditionalOnClass(value = {HikariDataSource.class})
@EnableConfigurationProperties(value = {DataSourceProperties.class})
@ComponentScan(basePackages = "com.ai.spring.boot.ds.jpa")
public class JpaDatasourceConfiguration {
    @Bean(name = "dataSource")
    @ConditionalOnMissingBean(DataSource.class)
    @ConfigurationProperties(ignoreUnknownFields = false,prefix="spring.datasource.hikari")
    public HikariDataSource dataSource(DataSourceProperties dataSourceProperties){
        SecHikariDataSource hikariDataSource = new SecHikariDataSource();
        hikariDataSource.setDecrypt(dataSourceProperties.getDecrypt());
        hikariDataSource.setPwdkey(dataSourceProperties.getPwdkey());

        hikariDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        hikariDataSource.setUsername(dataSourceProperties.getUsername());
        hikariDataSource.setPassword(dataSourceProperties.getPassword());
        hikariDataSource.setJdbcUrl(dataSourceProperties.getUrl());
        hikariDataSource.setMinimumIdle(dataSourceProperties.getMinIdle());
        hikariDataSource.setMaximumPoolSize(dataSourceProperties.getMaxActive());
        hikariDataSource.setPoolName("DatebookHikariCP");

        return hikariDataSource;
    }
}
