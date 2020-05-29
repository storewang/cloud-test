package com.ai.spring.mq.kafka.conf;

import com.ai.spring.im.common.mq.producer.MqProducer;
import com.ai.spring.mq.kafka.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * kafka 自动配置
 *
 * @author 石头
 * @Date 2020/5/29
 * @Version 1.0
 **/
@Configuration
@EnableConfigurationProperties(KafkaProducerProperties.class)
@ConditionalOnClass(Producer.class)
public class KafkaProduceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MqProducer.class)
    public MqProducer mqProducer(KafkaProducerProperties kafkaProperties){
        KafkaProducer producer = new KafkaProducer();
        Properties properties = new Properties();
        properties.put("bootstrap.servers",kafkaProperties.getBrokerList());
        properties.put("acks",kafkaProperties.getAck());
        properties.put("retries",kafkaProperties.getRetries());
        // 一旦我们达到分区的batch.size值的记录，将立即发送,但是，如果比这个小，我们将在指定的“linger”时间内等待更多的消息加入
        properties.put("batch.size",getIntegerVal(kafkaProperties.getBatchSize(),16384));
        // 生产者将等待一个给定的延迟，以便和其他的消息可以组合成一个批次
        properties.put("linger.ms",getIntegerVal(kafkaProperties.getLinger(),1));
        properties.put("buffer.memory",getIntegerVal(kafkaProperties.getBufferSize(),33554432));
        properties.put("max.block.ms",getIntegerVal(kafkaProperties.getMaxBlockMs(),60000));
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer",StringSerializer.class.getName());
        if (StringUtils.isEmpty(kafkaProperties.getPartitioner())){
            properties.put("partitioner.class",kafkaProperties.getPartitioner());
        }

        producer.initProducer(properties);

        return producer;
    }

    private int getIntegerVal(Integer val,Integer def){
        return val == null?def:val;
    }
}
