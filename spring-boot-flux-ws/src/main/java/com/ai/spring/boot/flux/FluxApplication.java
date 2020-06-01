package com.ai.spring.boot.flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 启动类
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@SpringBootApplication
//@EnableKafkaMq(KafkaTypeEnum.PRODUCER)
public class FluxApplication {
    public static void main(String[] args) {
        SpringApplication.run(FluxApplication.class,args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
}
