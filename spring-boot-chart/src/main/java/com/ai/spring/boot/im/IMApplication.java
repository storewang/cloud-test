package com.ai.spring.boot.im;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 启动类
 *
 * @author 石头
 * @Date 2020/3/3
 * @Version 1.0
 **/
@SpringBootApplication
@Slf4j
public class IMApplication implements ApplicationListener<ApplicationReadyEvent> {

    public static void main(String[] args) {
        SpringApplication.run(IMApplication.class,args);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("-------------应用启动完成---------------");
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
//    @Bean
//    public HttpMessageConverters fastJsonHttpMessageConverters(){
//        MyFastJsonHttpMessageConverter converter = new MyFastJsonHttpMessageConverter();
//        FastJsonConfig config = new FastJsonConfig();
//        config.setSerializerFeatures(SerializerFeature.PrettyFormat);
//        converter.setFastJsonConfig(config);
//        return new HttpMessageConverters(converter);
//    }
}
