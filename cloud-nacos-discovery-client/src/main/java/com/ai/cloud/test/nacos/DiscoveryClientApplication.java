package com.ai.cloud.test.nacos;/**
 * Created by 石头 on 2019/3/5.
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

/**
 * 注册服务消费者启动类
 *
 * @Author 石头
 * @Date 2019/3/5
 * @Version 1.0
 **/
@EnableDiscoveryClient
@SpringBootApplication
@Slf4j
public class DiscoveryClientApplication implements ApplicationListener<ApplicationReadyEvent> {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryClientApplication.class,args);
    }
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        String serverPort = applicationReadyEvent.getApplicationContext().getEnvironment().getProperty("server.port");
        if (StringUtils.isEmpty(serverPort)){
            serverPort = "8080";
        }
        log.info("-------------注册服务消费者应用启动成功:{}--------------------",serverPort);
    }
}
