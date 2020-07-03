package com.ai.spring.boot.netty.ws.conf;

import com.ai.spring.boot.netty.ws.server.NettyWebsocketServer;
import com.ai.spring.boot.netty.ws.service.impl.DefaultServerHandlerService;
import com.ai.spring.boot.netty.ws.service.ServerHandlerService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * websocket conf
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
@Configuration
@EnableConfigurationProperties(value={ServerProperties.class,ThreadProperties.class})
public class NettyWebsocketConf {
    @Bean
    @ConditionalOnMissingBean(ServerHandlerService.class)
    public ServerHandlerService serverHandlerService(){
        return new DefaultServerHandlerService();
    }

    @Bean(initMethod = "start",destroyMethod = "stop")
    @ConditionalOnMissingBean(NettyWebsocketServer.class)
    public NettyWebsocketServer websocketServer(ServerHandlerService serverHandlerService,ServerProperties serverProperties){
        return new NettyWebsocketServer(serverProperties,serverHandlerService);
    }

    @Bean
    public WebClient webClient(){
        return WebClient.builder().build();
    }
}
