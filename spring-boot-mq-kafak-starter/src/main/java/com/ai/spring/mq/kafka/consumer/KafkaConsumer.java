package com.ai.spring.mq.kafka.consumer;

import com.ai.spring.im.common.mq.MqRecordMetadata;
import com.ai.spring.im.common.mq.consumer.MqConsumer;
import com.ai.spring.im.common.mq.consumer.MqConsumerRecords;
import com.ai.spring.im.common.util.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

/**
 * kafka 消息消费者
 *
 * @author 石头
 * @Date 2020/5/29
 * @Version 1.0
 **/
@Slf4j
public class KafkaConsumer extends MqConsumer{
    private org.apache.kafka.clients.consumer.KafkaConsumer<String,String> consumer;
    private Properties properties;
    private ReentrantLock lock = new ReentrantLock();
    public KafkaConsumer(Properties properties) {
        super(properties);
    }

    @Override
    protected void initConsumer(Properties properties) {
        this.properties = properties;
        if (!properties.contains("client.id")){
            getConsumerId();
        }
        consumer = new org.apache.kafka.clients.consumer.KafkaConsumer(this.properties);
    }

    @Override
    protected String getConsumerId() {
        String clientId = properties.getProperty("client.id");
        if (StringUtils.isEmpty(clientId)){
            lock.lock();
            clientId = properties.getProperty("client.id");
            if (StringUtils.isEmpty(clientId)){
                clientId = IdWorker.getIdStr();
                properties.put("client.id",clientId);
            }
            lock.unlock();
        }
        return clientId;
    }

    @Override
    protected void beforeConsumer(String topic ){
        consumer.subscribe(Arrays.asList(topic),new RecieveRebalanceListener(getConsumerId()));
    }

    @Override
    protected MqConsumerRecords pollRecords(String topic) {
        String pollTimeOut = properties.getProperty("consumer.poll.timeout.ms", "5000");
        ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(str2Long(pollTimeOut,5000)));
        List<MqRecordMetadata> recordMetadataList = new ArrayList<>();
        records.forEach(record -> {
            MqRecordMetadata recordMetadata = new MqRecordMetadata();
            recordMetadata.setTopic(topic);
            recordMetadata.setPartition(record.partition());
            recordMetadata.setOffset(record.offset());
            recordMetadata.setMsg(record.value());
            recordMetadata.setKey(record.key());
            recordMetadataList.add(recordMetadata);
        });

        MqConsumerRecords result = new MqConsumerRecords();
        result.setRecords(recordMetadataList);
        result.setCount(recordMetadataList.size());
        return result;
    }

    @Override
    protected boolean needPollNext(String topic) {
        return true;
    }

    private long str2Long(String str,long def){
        try{
            return Long.parseLong(str);
        }catch (Throwable e){
            return def;
        }
    }
    @Slf4j
    private static class RecieveRebalanceListener implements ConsumerRebalanceListener {
        private String clientId;
        public RecieveRebalanceListener(String clientId){
            this.clientId = clientId;
        }
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
    }
}
