package com.ai.spring.mq.kafka.conf;

import com.ai.spring.im.common.mq.consumer.MqConsumer;
import com.ai.spring.im.common.util.Constans;
import com.ai.spring.im.common.util.IdWorker;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
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
@EnableConfigurationProperties(KafkaConsumerProperties.class)
@ConditionalOnClass(KafkaConsumer.class)
@Configuration
public class KafkaConsumerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(MqConsumer.class)
    public MqConsumer mqConsumer(KafkaConsumerProperties consumerProperties){
        Properties properties = new Properties();
        properties.put("bootstrap.servers",consumerProperties.getBrokerList());
        properties.put("group.id",getStrId(consumerProperties.getGroupId(),Constans.KAFKA_GROUP));
        properties.put("client.id",getStrId(consumerProperties.getClientId(),Constans.KAFKA_CLIENT));
        properties.put("enable.auto.commit",consumerProperties.getAutoCommit() == null?true:consumerProperties.getAutoCommit());
        properties.put("auto.commit.interval.ms",getIntegerVal(consumerProperties.getAutoCommitInterval(),10000));
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer",StringDeserializer.class.getName());
        properties.put("auto.offset.reset",StringUtils.isEmpty(consumerProperties.getOffsetReset())?"earliest":consumerProperties.getOffsetReset());
        // 在单次调用poll()中返回的最大记录数。
        properties.put("max.poll.records",getIntegerVal(consumerProperties.getMaxPoolNum(),200));

        // 线程池参数
        properties.put("thread.pool.core.size",getIntegerVal(consumerProperties.getWorkPoolCoreSize(),1));
        properties.put("thread.pool.max.pool.size",getIntegerVal(consumerProperties.getWorkPoolMaxSize(),2));
        properties.put("thread.pool.alive.sec",getIntegerVal(consumerProperties.getWorkPoolThreadAlive(),30));
        properties.put("thread.pool.queue.size",getIntegerVal(consumerProperties.getWorkPoolQueueSize(),500));

        com.ai.spring.mq.kafka.consumer.KafkaConsumer kafkaConsumer = new com.ai.spring.mq.kafka.consumer.KafkaConsumer(properties);
        return kafkaConsumer;
    }
    private String getStrId(String strId,String prefix){
        if (StringUtils.isEmpty(strId)){
            strId = prefix + IdWorker.getIdStr();
        }
        return strId;
    }
    private int getIntegerVal(Integer val,Integer def){
        return val == null?def:val;
    }
}
