package com.ai.spring.boot.gateway.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * datasource配置
 *
 * @author 石头
 * @Date 2020/6/4
 * @Version 1.0
 **/
@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class DataSourceProperties {
    /**驱动*/
    @Value("${spring.datasource.type:com.zaxxer.hikari.HikariDataSource}")
    private String type;
    @Value("${spring.datasource.driverClassName:com.mysql.jdbc.Driver}")
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    @Value("${spring.datasource.initialSize:10}")
    private Integer initialSize;
    @Value("${spring.datasource.minIdle:10}")
    private Integer minIdle;
    @Value("${spring.datasource.maxIdle:30}")
    private Integer maxIdle;
    @Value("${spring.datasource.maxActive:50}")
    private Integer maxActive;
    private Integer maxWait;
    @Value("${spring.datasource.decrypt:false}")
    private Boolean decrypt;
    private String pwdkey;
}
