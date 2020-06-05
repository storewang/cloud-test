package com.ai.spring.boot.gateway.conf;

import com.ai.spring.boot.gateway.datasource.SecHikariDataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

/**
 * JPA相关配置
 *
 * @author 石头
 * @Date 2020/6/4
 * @Version 1.0
 **/
@Configuration
@ConditionalOnClass(value = {HikariDataSource.class})
@EnableConfigurationProperties(value = {DataSourceProperties.class})
@EnableJpaRepositories(basePackages = {"com.ai.spring.boot.gateway.dao.repository.route"})
@EntityScan(basePackages = {"com.ai.spring.boot.gateway.dao.bean"})
@Slf4j
public class DataSourceConf {
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
