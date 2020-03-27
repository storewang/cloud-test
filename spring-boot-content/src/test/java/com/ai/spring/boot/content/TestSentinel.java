package com.ai.spring.boot.content;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/3/27
 * @Version 1.0
 **/

public class TestSentinel {
    public static void main1(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        for (int i =0;i<100000;i++){
            String forObject = restTemplate.getForObject("http://localhost:9999/actuator/sentinel", String.class);
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }
    public static void main(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        for (int i =0;i<100000;i++){
            String forObject = restTemplate.getForObject("http://localhost:9999/test/test-a", String.class);
            System.out.println(forObject);
        }
    }
}
