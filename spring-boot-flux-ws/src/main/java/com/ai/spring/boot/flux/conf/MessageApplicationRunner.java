package com.ai.spring.boot.flux.conf;

import com.ai.spring.boot.flux.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 应用启动后会触发
 *
 * @author 石头
 * @Date 2020/6/1
 * @Version 1.0
 **/
@Component
public class MessageApplicationRunner implements ApplicationRunner {
    @Autowired
    private RedisService redisService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        redisService.clearHostBindUsers();
    }
}
