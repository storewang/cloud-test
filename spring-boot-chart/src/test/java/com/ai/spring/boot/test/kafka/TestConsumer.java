package com.ai.spring.boot.test.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/4/13
 * @Version 1.0
 **/
@Slf4j
public class TestConsumer {
    private Properties getConsumerConf(String clientId){
        Properties properties = new Properties();
        properties.put("bootstrap.servers","192.168.60.103:9093,192.168.60.103:9094,192.168.60.103:9095");
        properties.put("group.id","test-group111");
        properties.put("client.id",clientId);
        properties.put("enable.auto.commit","true");
        properties.put("auto.commit.interval.ms","10000");
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer",StringDeserializer.class.getName());
        properties.put("auto.offset.reset","earliest");

        return properties;
    }

    /**
     * kafka新APi（0.10以上）
     * subscribe 自动监听消息，其实就是循环poll，没有消息时，达到超时时间返回，有消息时立即返回
     */
    @Test
    public void testConsumer(){
        String clientId = "1000003";
        KafkaConsumer<String,String> consumer = new KafkaConsumer(getConsumerConf(clientId));
        consumer.subscribe(Arrays.asList("test-topic1"), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                partitions.forEach(topicPartition -> {
                    // 之前客户端消息这个partition,现在不消息这个partition
                    log.info("Revoked partition for client {}:{}-{}",clientId,topicPartition.topic(),topicPartition.partition());
                });
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                partitions.forEach(topicPartition -> {
                    // 之前客户端不消息这个partition,而现在消费这个partition
                    log.info("assigined partition for client {}:{}-{}",clientId,topicPartition.topic(),topicPartition.partition());
                });
            }
        });

        while (true){
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(10000));
            records.forEach(record -> {
                log.info("消费消息 {}：topic={},partition={},offset={},key={},value={}",
                        clientId,record.topic(),record.partition(),record.key(),record.value());
            });
        }
    }

    /**
     * kafka新APi（0.10以上）
     * assign 需要自己管理offset,不会自动提交offset
     */
    @Test
    public void testAssignConsumer(){
        String clientId = "1000004";
        KafkaConsumer<String,String> consumer = new KafkaConsumer(getConsumerConf(clientId));
        consumer.assign(Arrays.asList(new TopicPartition("test-topic1",0),new TopicPartition("test-topic1",1)));

        while (true){
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(10000));
            records.forEach(record -> {
                log.info("消费消息 {}：topic={},partition={},offset={},key={},value={}",
                        clientId,record.topic(),record.partition(),record.key(),record.value());
            });
        }
    }
}
