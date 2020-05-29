package com.ai.spring.boot.flux.ws.conf;

import com.ai.spring.boot.flux.ws.mapping.WebSocketHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

/**
 * ws 配置类
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Configuration
public class WebSocketConf {
    @Bean
    public HandlerMapping webSocketMapping(){
        return new WebSocketHandlerMapping();
    }
    @Bean
    public WebSocketHandlerAdapter handlerAdapter(){
        //HandshakeInterceptor c;
        return new WebSocketHandlerAdapter();
    }
}
