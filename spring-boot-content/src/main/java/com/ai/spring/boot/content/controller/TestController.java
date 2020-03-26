package com.ai.spring.boot.content.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测试接口
 *
 * @author 石头
 * @Date 2020/3/26
 * @Version 1.0
 **/
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/users")
    public List<ServiceInstance> getInstances(){
        List<ServiceInstance> instances = discoveryClient.getInstances("spring-boot-user");
        return instances;
    }
}
