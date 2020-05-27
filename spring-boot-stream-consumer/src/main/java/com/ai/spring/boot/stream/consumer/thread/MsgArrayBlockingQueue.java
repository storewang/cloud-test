package com.ai.spring.boot.stream.consumer.thread;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 消息接收队列
 *
 * @author 石头
 * @Date 2020/5/27
 * @Version 1.0
 **/
@Slf4j
public class MsgArrayBlockingQueue extends ArrayBlockingQueue<ConsumerRecord<String,String>> {
    public MsgArrayBlockingQueue(int capacity) {
        super(capacity);
    }

    @Override
    public void put(ConsumerRecord<String,String> record) throws InterruptedException {
        if (contains(record)){
            log.info("----忽略添加数据{}，待处理队列中已经存在此数据。",record);
            return;
        }
        super.put(record);
    }
}
