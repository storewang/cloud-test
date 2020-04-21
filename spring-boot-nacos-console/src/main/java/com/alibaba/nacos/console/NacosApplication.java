package com.alibaba.nacos.console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Nacos控制台
 *
 * @author 石头
 * @Date 2020/4/21
 * @Version 1.0
 **/
@SpringBootApplication
public class NacosApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosApplication.class,args);
    }
}
