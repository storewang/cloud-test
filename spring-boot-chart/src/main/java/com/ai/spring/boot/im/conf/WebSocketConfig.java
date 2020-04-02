package com.ai.spring.boot.im.conf;

import com.ai.spring.boot.im.ws.MessageHandler;
import com.ai.spring.boot.im.ws.MessageHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * ws配置
 *
 * @author 石头
 * @Date 2020/3/3
 * @Version 1.0
 **/
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Resource
    private MessageHandler messageHandler;
    @Resource
    private MessageHandshakeInterceptor messageHandshakeInterceptor;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(this.messageHandler,"/ws/{uid}")
                .setAllowedOrigins("*")
                .addInterceptors(messageHandshakeInterceptor);
    }
}
