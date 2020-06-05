package com.ai.spring.boot.gateway;

import com.ai.spring.boot.gateway.routes.DataBaseRouteDefinitionRepository;
import com.ai.spring.boot.gateway.service.RouteService;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Bean;

/**
 * 启动类
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }

    @Bean
    @ConditionalOnBean(value = {HikariDataSource.class,RouteService.class})
    public RouteDefinitionRepository routeDefinitionRepository(){
        return new DataBaseRouteDefinitionRepository();
    }
}
