package com.ai.spring.boot.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author 石头
 * @Date 2020/3/24
 * @Version 1.0
 **/
@SpringBootApplication
@Slf4j
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
        log.info("================>{}",System.getProperty("spring.profiles.active","没有获取到"));
    }
}
