package com.ai.spring.mq.kafka.conf;

import com.ai.spring.mq.kafka.EnableKafkaMq;
import com.ai.spring.mq.kafka.enums.KafkaTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 配置条件选择器
 *
 * @author 石头
 * @Date 2020/5/29
 * @Version 1.0
 **/
@Slf4j
public class KafkaConfigurationSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableKafkaMq.class.getName(), true));
        KafkaTypeEnum kafkaTypeEnum = annotationAttributes.getEnum("value");
        log.info("-------------当前MQ组件选择了：{}---------------",kafkaTypeEnum);
        switch (kafkaTypeEnum){
            case PRODUCER:
                return new String[]{KafkaProduceAutoConfiguration.class.getName()};
            case CONSUMER:
                return new String[]{KafkaConsumerAutoConfiguration.class.getName()};
            default:
                return new String[]{KafkaConsumerAutoConfiguration.class.getName()};
        }
    }
}
