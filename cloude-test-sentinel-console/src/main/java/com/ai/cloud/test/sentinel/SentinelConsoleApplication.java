package com.ai.cloud.test.sentinel;

import com.alibaba.csp.sentinel.init.InitExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

@SpringBootApplication
@ServletComponentScan
@Slf4j
public class SentinelConsoleApplication implements ApplicationListener<ApplicationReadyEvent> {
    public static void main(String[] args) {
        triggerSentinelInit();

        SpringApplication.run(SentinelConsoleApplication.class,args);
    }


    private static void triggerSentinelInit(){
        new Thread(() -> InitExecutor.doInit()).start();
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        String serverPort = applicationReadyEvent.getApplicationContext().getEnvironment().getProperty("server.port");
        if (StringUtils.isEmpty(serverPort)){
            serverPort = "8080";
        }
        log.info("-------------sentinel控制台应用启动成功:{}--------------------",serverPort);
    }
}
