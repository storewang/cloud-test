package com.ai.cloud.test.nacos.runner;/**
 * Created by 石头 on 2019/3/21.
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

/**
 * 启动基类
 * 添加启动监听打印
 * @Author 石头
 * @Date 2019/3/21
 * @Version 1.0
 **/
@Slf4j
public abstract class StartApplication implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        String serverPort      = applicationReadyEvent.getApplicationContext().getEnvironment().getProperty("server.port");
        String applicationName = applicationReadyEvent.getApplicationContext().getEnvironment().getProperty("spring.application.name");
        if (StringUtils.isEmpty(serverPort)){
            serverPort = "8080";
        }
        log.info("-------------{}应用启动成功:{}--------------------",applicationName,serverPort);
    }
}
