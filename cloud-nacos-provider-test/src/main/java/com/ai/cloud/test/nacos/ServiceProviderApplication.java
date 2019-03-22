package com.ai.cloud.test.nacos;/**
 * Created by 石头 on 2019/3/21.
 */

import com.ai.cloud.test.nacos.runner.StartApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 服务提供者
 *
 * @Author 石头
 * @Date 2019/3/21
 * @Version 1.0
 **/
@SpringBootApplication
@EnableEurekaClient
public class ServiceProviderApplication extends StartApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceProviderApplication.class,args);
    }
}
