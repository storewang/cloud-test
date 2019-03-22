package com.ai.cloud.test.nacos;/**
 * Created by 石头 on 2019/3/4.
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringUtils;

import static com.alibaba.nacos.common.util.SystemUtils.NACOS_HOME_KEY;
import static com.alibaba.nacos.common.util.SystemUtils.STANDALONE_MODE_PROPERTY_NAME;

/**
 * Nacos控制台应用启动类
 * @Author 石头
 * @Date 2019/3/4
 * @Version 1.0
 **/
@SpringBootApplication(scanBasePackages = {"com.ai.cloud.test.nacos","com.alibaba.nacos"})
@ServletComponentScan
@EnableScheduling
@Slf4j
public class NacosConsoleApplication implements ApplicationListener<ApplicationReadyEvent>{
    public static void main(String[] args) {
        System.setProperty(STANDALONE_MODE_PROPERTY_NAME,Boolean.TRUE.toString());
        System.setProperty(NACOS_HOME_KEY,"G:\\tp_config");
        System.setProperty("nacos.server.ip","192.168.49.115");
        SpringApplication.run(NacosConsoleApplication.class,args);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        String serverPort = applicationReadyEvent.getApplicationContext().getEnvironment().getProperty("server.port");
        if (StringUtils.isEmpty(serverPort)){
            serverPort = "8080";
        }
        log.info("-------------Nacos控制台应用启动成功:{}--------------------",serverPort);
    }
}
