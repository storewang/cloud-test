package com.ai.spring.boot.content.feignclient;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * feign配置类，(代码方式配置)
 * 不需要要加Configuration注解或是spring其他标记为spring托管的注解，
 * 否则需要移到启动类扫描包的外面（父子上下文问题）
 *
 * @author 石头
 * @Date 2020/3/26
 * @Version 1.0
 **/
public class UserServiceFeignConf {
    @Bean
    public Logger.Level level(){
        return Logger.Level.FULL;
    }
}
