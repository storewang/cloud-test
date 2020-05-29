package com.ai.spring.mq.kafka;

import com.ai.spring.mq.kafka.conf.KafkaConfigurationSelector;
import com.ai.spring.mq.kafka.enums.KafkaTypeEnum;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启动kafka
 *
 * @author 石头
 * @Date 2020/5/29
 * @Version 1.0
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(KafkaConfigurationSelector.class)
public @interface EnableKafkaMq {
    KafkaTypeEnum value() default KafkaTypeEnum.PRODUCER;
}
