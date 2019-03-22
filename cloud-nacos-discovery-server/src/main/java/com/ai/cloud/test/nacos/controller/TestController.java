package com.ai.cloud.test.nacos.controller;/**
 * Created by 石头 on 2019/3/5.
 */

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试服务类
 *
 * @Author 石头
 * @Date 2019/3/5
 * @Version 1.0
 **/
@RestController
@Slf4j
@RefreshScope
public class TestController {
    @Value("${com.ai.test:aaaa}")
    private String test;

    @GetMapping("/hello")
    @SentinelResource(value = "hello",blockHandler="helloBlock",fallback = "helloFallback")
    public String hello(@RequestParam String name){
        log.info("invoke name = {}",name);
        return test + name;
    }
    public String helloFallback(String name){
        return "helloFallback at " + name;
    }
    public String helloBlock(String name, BlockException ex){
        log.error("Oops,error occurred at {}" ,ex);
        return "Oops,error occurred at " + name;
    }
}
