package com.ai.spring.boot.stream.consumer.service;

import com.ai.spring.boot.stream.consumer.util.RecieveBuilder;
import com.ai.spring.im.common.util.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.annotation.StreamListener;
//import org.springframework.cloud.stream.messaging.Sink;

/**
 * 消费者
 *
 * @author 石头
 * @Date 2020/5/27
 * @Version 1.0
 **/
@Slf4j
//@EnableBinding(Sink.class)
@Service
public class RecieveService {
    private RecieveBuilder recieveBuilder;
    @PostConstruct
    public void init(){
        String clientId = IdWorker.getIdStr();
        Properties properties = new Properties();
        properties.put("bootstrap.servers","192.168.60.103:9093,192.168.60.103:9094,192.168.60.103:9095");
        properties.put("group.id","group-1265543522310262785");
        properties.put("client.id",clientId);
        properties.put("enable.auto.commit","true");
        properties.put("auto.commit.interval.ms","10000");
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer",StringDeserializer.class.getName());
        properties.put("auto.offset.reset","earliest");
        // 在单次调用poll()中返回的最大记录数。
        properties.put("max.poll.records","200");

        // thread pool conf
        properties.put("thread.pool.core.size","1");
        properties.put("thread.pool.max.pool.size","2");
        properties.put("thread.pool.alive.sec","10");
        properties.put("thread.pool.queue.size","100");
        recieveBuilder = RecieveBuilder.newBuilder(properties);

        log.info("----------消费GROUP={}--------------",properties.get("group.id"));
    }

    @Async
    public void recieve(String topic){
        log.info("----------{}：消费者已经启动--------------",topic);
        recieveBuilder.consumer(topic).build();
    }
}
