package com.ai.cloud.test.nacos.service;/**
 * Created by 石头 on 2019/3/21.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 测试服务类
 *
 * @Author 石头
 * @Date 2019/3/21
 * @Version 1.0
 **/
@Service
public class HelloService {
    @Autowired
    RestTemplate restTemplate;
    public String hello(String name){
        return restTemplate.getForObject("http://CLOUD-NACOS-PROVIDER-TEST/hello?name="+name,String.class);
    }
}
