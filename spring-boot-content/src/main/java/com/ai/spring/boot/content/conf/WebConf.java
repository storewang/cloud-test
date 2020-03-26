package com.ai.spring.boot.content.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * 配置类
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
@Configuration
@Slf4j
public class WebConf {
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        return factory;
    }
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getInterceptors().add(new HttpInterceptor());
        return restTemplate;
    }


    public class HttpInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            log.info("请求地址：{}", request.getURI());
            log.info("请求方法： {}", request.getMethod());
            log.info("请求内容：{}", new String(body));
            log.info("请求头：{}", request.getHeaders());
            return execution.execute(request, body);
        }
    }
}
