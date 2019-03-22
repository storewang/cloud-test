package com.ai.cloud.test.nacos;/**
 * Created by 石头 on 2019/3/21.
 */

import com.ai.cloud.test.nacos.runner.StartApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 消费者测试类
 *
 * @Author 石头
 * @Date 2019/3/21
 * @Version 1.0
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class ConsumerTestApplication extends StartApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerTestApplication.class,args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
