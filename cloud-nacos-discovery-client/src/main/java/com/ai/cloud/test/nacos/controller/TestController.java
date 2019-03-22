package com.ai.cloud.test.nacos.controller;/**
 * Created by 石头 on 2019/3/5.
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * TODO
 *
 * @Author 石头
 * @Date 2019/3/5
 * @Version 1.0
 **/
@Slf4j
@RestController
public class TestController {
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @GetMapping("/test")
    public String test(){
        // 通过spring cloud common中的负载均衡接口选取服务提供节点实现接口调用
        ServiceInstance serviceInstance = loadBalancerClient.choose("cloud-nacos-discovery-server");
        String url = serviceInstance.getUri() + "/hello?name=haha";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url,String.class);
        return "Invoker url :" + url + ",result： "+result;

    }
}
