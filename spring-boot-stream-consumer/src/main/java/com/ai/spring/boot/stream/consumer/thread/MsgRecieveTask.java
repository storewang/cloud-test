package com.ai.spring.boot.stream.consumer.thread;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * 消息接收者处理任务
 *
 * @author 石头
 * @Date 2020/5/27
 * @Version 1.0
 **/
public interface MsgRecieveTask extends Runnable{
    ConsumerRecord<String,String> getRecord();
}
