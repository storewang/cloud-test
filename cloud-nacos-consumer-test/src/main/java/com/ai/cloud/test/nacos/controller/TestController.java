package com.ai.cloud.test.nacos.controller;/**
 * Created by 石头 on 2019/3/21.
 */

import com.ai.cloud.test.nacos.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试类
 *
 * @Author 石头
 * @Date 2019/3/21
 * @Version 1.0
 **/
@RestController
public class TestController {
    @Autowired
    private HelloService helloService;

    @GetMapping("/hello")
    public String hello(@RequestParam String name){
        return helloService.hello(name);
    }
}
