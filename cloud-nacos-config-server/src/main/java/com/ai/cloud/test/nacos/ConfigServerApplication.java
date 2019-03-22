package com.ai.cloud.test.nacos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

/**
 * 配置中心启动类
 *
 * @Author 石头
 * @Date 2019/3/21
 * @Version 1.0
 **/
@SpringBootApplication
@EnableConfigServer
@Slf4j
public class ConfigServerApplication implements ApplicationListener<ApplicationReadyEvent> {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class,args);
    }
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        String serverPort = applicationReadyEvent.getApplicationContext().getEnvironment().getProperty("server.port");
        if (StringUtils.isEmpty(serverPort)){
            serverPort = "8080";
        }
        log.info("-------------配置中心应用启动成功:{}--------------------",serverPort);
    }
}
