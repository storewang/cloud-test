package com.alibaba.nacos.console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Nacos控制台
 *
 * @author 石头
 * @Date 2020/4/21
 * @Version 1.0
 **/
@SpringBootApplication(scanBasePackages = "com.alibaba.nacos")
@ServletComponentScan
@EnableScheduling
public class NacosApplication {
    public static void main(String[] args) {
        System.setProperty("nacos.standalone","true");
        SpringApplication.run(NacosApplication.class,args);
    }
}
