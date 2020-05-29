package com.ai.spring.boot.flux;

import com.ai.spring.mq.kafka.EnableKafkaMq;
import com.ai.spring.mq.kafka.enums.KafkaTypeEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@SpringBootApplication
@EnableKafkaMq(KafkaTypeEnum.PRODUCER)
public class FluxApplication {
    public static void main(String[] args) {
        SpringApplication.run(FluxApplication.class,args);
    }
}
