package com.ai.spring.im.common.mq.consumer;

import com.ai.spring.im.common.mq.MqRecordMetadata;
import lombok.Data;

import java.util.List;
import java.util.function.Consumer;

/**
 * 消息记录集合
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Data
public class MqConsumerRecords{
    private int count;
    private List<MqRecordMetadata> records;

    public void forEach(Consumer<? super MqRecordMetadata> action) {
        records.stream().forEach(action);
    }

}
