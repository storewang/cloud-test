package com.ai.spring.boot.stream.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 消费者
 *
 * @author 石头
 * @Date 2020/5/27
 * @Version 1.0
 **/
@SpringBootApplication
@EnableAsync
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class,args);
    }
}
