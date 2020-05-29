package com.ai.spring.mq.kafka.producer;

import com.ai.spring.im.common.mq.MqCallBack;
import com.ai.spring.im.common.mq.MqRecordMetadata;
import com.ai.spring.im.common.mq.producer.MqProducer;
import com.ai.spring.im.common.mq.producer.MqProducerRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * kafka 消息发送者
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Slf4j
public class KafkaProducer extends MqProducer {
    private Producer<String,String> producer;
    private Properties properties;
    @Override
    protected void doSend(MqProducerRecord record, MqCallBack callBack) {
        ProducerRecord<String,String> kafkaRecord = null;
        if (StringUtils.isEmpty(record.getMsgKey())){
            kafkaRecord = new ProducerRecord(record.getTopic(),record.getContent());
        }else {
            kafkaRecord = new ProducerRecord(record.getTopic(),record.getMsgKey(),record.getContent());
        }

        producer.send(kafkaRecord, (metadata ,e) -> {
            MqRecordMetadata recordMetadata = new MqRecordMetadata();
            recordMetadata.setMsgId(record.getMsgId());
            recordMetadata.setMsg(record.getContent());
            recordMetadata.setTopic(record.getTopic());
            recordMetadata.setPartition(metadata.partition());
            recordMetadata.setOffset(metadata.offset());
            recordMetadata.setKey(record.getMsgKey());

            callBack.onCompletion(recordMetadata,e);
        });
    }

    @Override
    public void initProducer(Properties properties) {
        this.properties = properties;
        producer = new org.apache.kafka.clients.producer.KafkaProducer(properties);
    }
}
