package com.ai.spring.im.common.mq.queue;

import com.ai.spring.im.common.mq.MqRecordMetadata;

/**
 * 消息接收者处理任务
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
public interface ConsumerRecodeTask extends Runnable{
    MqRecordMetadata getRecord();
}
