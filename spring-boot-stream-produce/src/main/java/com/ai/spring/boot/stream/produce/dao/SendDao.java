package com.ai.spring.boot.stream.produce.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * 消息发送者
 *
 * @author 石头
 * @Date 2020/5/27
 * @Version 1.0
 **/
@Component
@Slf4j
public class SendDao {
    private Producer<String,String> producer;
    @PostConstruct
    public void init(){
        Properties properties = new Properties();
        properties.put("bootstrap.servers","192.168.60.103:9093,192.168.60.103:9094,192.168.60.103:9095");
        properties.put("acks","all");
        properties.put("retries","3");
        // 一旦我们达到分区的batch.size值的记录，将立即发送,但是，如果比这个小，我们将在指定的“linger”时间内等待更多的消息加入
        properties.put("batch.size",16384);
        // 生产者将等待一个给定的延迟，以便和其他的消息可以组合成一个批次
        properties.put("linger.ms",1);
        properties.put("buffer.memory",33554432);
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer",StringSerializer.class.getName());
        producer = new KafkaProducer(properties);
    }

    public void sendMsg(String msg){
        ProducerRecord<String,String> record = new ProducerRecord("stream-demo1",msg);

        producer.send(record, (metadata ,e) -> {
            if (e!=null){
                log.info("发送消息失败:",e);
                return;
            }

            log.info("发送消息 partition={},offset={},keysize={},valuesize={}",
                    metadata.partition(),metadata.offset(),
                    metadata.serializedKeySize(),
                    metadata.serializedValueSize());
        });
    }
}
