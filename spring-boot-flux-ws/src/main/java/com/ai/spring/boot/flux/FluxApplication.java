package com.ai.spring.boot.flux;

import com.ai.spring.boot.ds.annotation.EnableJpaDataSource;
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
@EnableJpaDataSource(basePackages = "com.ai.spring.boot.flux.dao.repository",entityBasePackages = "com.ai.spring.boot.flux.dao.bean")
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
