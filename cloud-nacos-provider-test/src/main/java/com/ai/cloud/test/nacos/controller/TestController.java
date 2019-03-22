package com.ai.cloud.test.nacos.controller;/**
 * Created by 石头 on 2019/3/21.
 */

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
    @Value("${server.test}")
    private String testStr;
    @GetMapping("/hello")
    public String hello(@RequestParam String name){
        return testStr + " " + name;
    }
}
