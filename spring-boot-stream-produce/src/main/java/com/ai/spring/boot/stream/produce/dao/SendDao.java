package com.ai.spring.boot.stream.produce.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.messaging.Source;
//import org.springframework.integration.support.MessageBuilder;

/**
 * 消息发送者
 *
 * @author 石头
 * @Date 2020/5/27
 * @Version 1.0
 **/
//@EnableBinding(Source.class)
@Component
@Slf4j
public class SendDao {
    private Producer<String,String> producer;
//    @Autowired
//    private Source source;
    @PostConstruct
    public void init(){
        Properties properties = new Properties();
        properties.put("bootstrap.servers","192.168.60.103:9093,192.168.60.103:9094,192.168.60.103:9095");
        properties.put("acks","all");
        properties.put("retries","3");
        properties.put("batch.size",16384);
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
