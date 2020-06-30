package com.ai.spring.boot.netty.ws;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 启动类
 * @author 石头
 * @Date 2020/6/28
 * @Version 1.0
 **/
@SpringBootApplication
public class NettyWsApplication {
    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(NettyWsApplication.class);
        builder.web(WebApplicationType.NONE).run(args);
    }
}
