package com.ai.spring.boot.content;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动类
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
@SpringBootApplication
@EnableFeignClients
public class ContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class,args);
    }
}
