package com.ai.spring.boot.netty.ws.conf;

import com.ai.spring.boot.netty.ws.discovery.nacos.WebSocketNacosAutoServiceRegistration;
import com.ai.spring.boot.netty.ws.discovery.nacos.WebSocketNacosServiceRegistry;
import com.ai.spring.boot.netty.ws.server.NettyWebsocketServer;
import com.ai.spring.boot.netty.ws.service.impl.DefaultServerHandlerService;
import com.ai.spring.boot.netty.ws.service.ServerHandlerService;
import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.alibaba.cloud.nacos.registry.NacosRegistration;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationAutoConfiguration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationConfiguration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * websocket conf
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
@Configuration
@EnableConfigurationProperties(value={ServerProperties.class,ThreadProperties.class,NacosDiscoveryProperties.class,AutoServiceRegistrationProperties.class})
@ConditionalOnNacosDiscoveryEnabled
@ConditionalOnProperty(value = "spring.cloud.service-registry.auto-registration.enabled", matchIfMissing = true)
@AutoConfigureAfter({ AutoServiceRegistrationConfiguration.class,AutoServiceRegistrationAutoConfiguration.class })
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

    /***服务注册发现自动装配*/
    @Bean
    @Primary
    public NacosServiceRegistry nacosServiceRegistry(
            NacosDiscoveryProperties nacosDiscoveryProperties) {
        return new NacosServiceRegistry(nacosDiscoveryProperties);
    }

    @Bean
    public NacosRegistration nacosRegistration(
            NacosDiscoveryProperties nacosDiscoveryProperties,
            ApplicationContext context) {
        return new NacosRegistration(nacosDiscoveryProperties, context);
    }

    @Bean
    public NacosAutoServiceRegistration nacosAutoServiceRegistration(
            NacosServiceRegistry registry,
            AutoServiceRegistrationProperties autoServiceRegistrationProperties,
            NacosRegistration registration) {
        return new NacosAutoServiceRegistration(registry,
                autoServiceRegistrationProperties, registration);
    }

    @Bean
    public WebSocketNacosServiceRegistry webSocketNacosServiceRegistry(NacosDiscoveryProperties nacosDiscoveryProperties,ServerProperties serverProperties){
        return new WebSocketNacosServiceRegistry(nacosDiscoveryProperties,serverProperties);
    }
    @Bean
    public WebSocketNacosAutoServiceRegistration webSocketNacosAutoServiceRegistration(WebSocketNacosServiceRegistry serviceRegistry,
                                                                                       AutoServiceRegistrationProperties autoServiceRegistrationProperties,
                                                                                       NacosRegistration registration){
        return new WebSocketNacosAutoServiceRegistration(serviceRegistry,autoServiceRegistrationProperties,registration);
    }
}
