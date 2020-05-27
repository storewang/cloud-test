package com.ai.spring.boot.stream.produce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 消息发送者
 *
 * @author 石头
 * @Date 2020/5/27
 * @Version 1.0
 **/
@SpringBootApplication
public class ProduceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProduceApplication.class,args);
    }
}
