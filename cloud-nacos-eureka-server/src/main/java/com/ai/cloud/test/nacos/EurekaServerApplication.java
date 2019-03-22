package com.ai.cloud.test.nacos;/**
 * Created by 石头 on 2019/3/21.
 */

import com.ai.cloud.test.nacos.runner.StartApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka注册中心
 *
 * @Author 石头
 * @Date 2019/3/21
 * @Version 1.0
 **/
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication extends StartApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class,args);
    }
}
