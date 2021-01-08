package com.ai.spring.boot.netty.ws;

import com.ai.spring.boot.ds.annotation.EnableJpaDataSource;
import com.alibaba.cloud.nacos.NacosDiscoveryAutoConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationAutoConfiguration;

/**
 * 启动类
 * @author 石头
 * @Date 2020/6/28
 * @Version 1.0
 **/
@SpringBootApplication(exclude = {AutoServiceRegistrationAutoConfiguration.class,NacosDiscoveryAutoConfiguration.class})
@EnableJpaDataSource(basePackages = "com.ai.spring.boot.netty.ws.dao.repository",entityBasePackages = "com.ai.spring.boot.netty.ws.dao.bean")
public class NettyWsApplication {
    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(NettyWsApplication.class);
        builder.web(WebApplicationType.REACTIVE).run(args);
    }
}
