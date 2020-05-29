package com.ai.spring.im.common.mq.queue;

import com.ai.spring.im.common.mq.MqRecordMetadata;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 消息接收队列
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Slf4j
public class MqArrayBlockingQueue extends ArrayBlockingQueue<MqRecordMetadata> {
    public MqArrayBlockingQueue(int capacity) {
        super(capacity);
    }

    @Override
    public void put(MqRecordMetadata record) throws InterruptedException {
        if (contains(record)){
            log.info("----忽略添加数据{}，待处理队列中已经存在此数据。",record);
            return;
        }
        super.put(record);
    }
}
