package com.ai.spring.boot.stream.consumer.util;

import com.ai.spring.boot.stream.consumer.thread.DefaultMsgRecieveTask;
import com.ai.spring.boot.stream.consumer.thread.MsgArrayBlockingQueue;
import com.ai.spring.boot.stream.consumer.thread.MsgDiscardPolicy;
import com.ai.spring.boot.stream.consumer.thread.MsgThreadPoolExecutor;
import com.ai.spring.boot.stream.consumer.thread.ThreadQueueMonitor;
import com.ai.spring.im.common.util.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息者工具类
 *
 * @author 石头
 * @Date 2020/5/27
 * @Version 1.0
 **/
@Slf4j
public class RecieveBuilder {
    private Properties properties;
    private String clientId;
    private KafkaConsumer<String,String> consumer;
    private MsgThreadPoolExecutor poolExecutor;
    private MsgArrayBlockingQueue blockingQueue;
    private AtomicInteger fetchNum = new AtomicInteger(1);
    private RecieveBuilder(Properties properties){
        this.properties = properties;

        int corePoolSize    = getProperty("thread.pool.core.size",1);
        int maximumPoolSize = getProperty("thread.pool.max.pool.size",2);
        int keepAliveTime   = getProperty("thread.pool.alive.sec",10);
        int queueSize       = getProperty("thread.pool.queue.size",500);
        blockingQueue       = new MsgArrayBlockingQueue(queueSize * 2);
        poolExecutor = new MsgThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,new ArrayBlockingQueue<>(queueSize),new MsgDiscardPolicy(blockingQueue));

        // start monitor
        ThreadQueueMonitor.newBuilder(poolExecutor,blockingQueue).starMonitor();

    }
    private int getProperty(String key,int def){
        String val     = properties.getProperty(key,String.valueOf(def));
        Integer intVal = str2Int(val);
        return intVal == null?def:intVal;
    }
    private Integer str2Int(String str){
        try {
            return Integer.parseInt(str);
        }catch (Throwable e){
            return null;
        }
    }
    public static RecieveBuilder newBuilder(Properties properties){
        return new RecieveBuilder(properties);
    }

    public RecieveBuilder consumer(String topic){
        if (!properties.contains("client.id")){
            clientId = IdWorker.getIdStr();
            properties.put("client.id",clientId);
        }else {
            clientId = properties.getProperty("client.id");
        }
        consumer = new KafkaConsumer(properties);
        consumer.subscribe(Arrays.asList(topic),new RecieveRebalanceListener(clientId));

        return this;
    }

    public void build(){
        while (true){
            // 最多5秒获取一次
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(5000));
            log.info("--------------第{}次获取消息量:{}-------------------",fetchNum.getAndIncrement(),records.count());
            records.forEach(record -> poolExecutor.execute(new DefaultMsgRecieveTask(clientId,record)));

            dealWithBlockQueue();
        }
    }
    // 处理阻塞队列中的消息，此消息为线程池满时放入的，用于再消费
    private void dealWithBlockQueue(){
        try{
            ConsumerRecord<String, String> record = blockingQueue.poll(100, TimeUnit.MILLISECONDS);
            while (record!=null){
                poolExecutor.execute(new DefaultMsgRecieveTask(clientId,record));
                record = blockingQueue.poll(100, TimeUnit.MILLISECONDS);
            }
        }catch (Throwable e){
        }
    }

    private class RecieveRebalanceListener implements ConsumerRebalanceListener{
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
