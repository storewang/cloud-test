package com.ai.spring.boot.test.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.util.Properties;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/4/13
 * @Version 1.0
 **/
@Slf4j
public class TestProducer {
    private Properties getProducerConf(){
        Properties properties = new Properties();
        properties.put("bootstrap.servers","192.168.60.103:9093,192.168.60.103:9094,192.168.60.103:9095");
        properties.put("acks","all");
        properties.put("retries","3");
        properties.put("batch.size",16384);
        properties.put("linger.ms",1);
        properties.put("buffer.memory",33554432);
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer",StringSerializer.class.getName());
        properties.put("partitioner.class","com.ai.spring.boot.test.kafka.HashPartioner");

        return properties;
    }

    /**
     * kafka新APi（0.10以上）
     */
    @Test
    public void testProducer(){
        Producer<String,String> producer = new KafkaProducer(getProducerConf());
        for (int i=0;i<10;i++){
            ProducerRecord<String,String> record = new ProducerRecord("test-topic1",Integer.toString(i),Integer.toString(i));

            producer.send(record, (metadata ,e) -> {
                log.info("发送消息 partition={},offset={},keysize={},valuesize={}",
                        metadata.partition(),metadata.offset(),
                        metadata.serializedKeySize(),
                        metadata.serializedValueSize());

                if (e!=null){
                    log.info("发送消息失败:",e);
                }
            });
        }

        producer.close();
    }
}
