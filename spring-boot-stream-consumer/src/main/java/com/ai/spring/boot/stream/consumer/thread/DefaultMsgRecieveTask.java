package com.ai.spring.boot.stream.consumer.thread;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.TimeUnit;

/**
 * 消息接收者具体处理任务
 *
 * @author 石头
 * @Date 2020/5/27
 * @Version 1.0
 **/
@Slf4j
public class DefaultMsgRecieveTask implements MsgRecieveTask{
    private ConsumerRecord<String, String> record;
    private String clientId;
    public DefaultMsgRecieveTask(String clientId,ConsumerRecord<String, String> record){
        this.record   = record;
        this.clientId = clientId;
    }
    @Override
    public ConsumerRecord<String, String> getRecord() {
        return record;
    }

    @Override
    public void run() {
        log.info("消费消息 {}：topic={},partition={},offset={},key={},value={}",
                clientId,record.topic(),record.partition(),record.offset(),record.key(),record.value());

        // 模拟消息时间
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }
    }
}
