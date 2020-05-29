package com.ai.spring.im.common.mq.queue;

import com.ai.spring.im.common.mq.MqCallBack;
import com.ai.spring.im.common.mq.MqRecordMetadata;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息接收者具体处理任务
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Slf4j
public class DefaultConsumerRecordTask implements ConsumerRecodeTask{
    private MqRecordMetadata record;
    private String clientId;
    private MqCallBack callBack;
    public DefaultConsumerRecordTask(String clientId,MqRecordMetadata record,MqCallBack callBack){
        this.record   = record;
        this.clientId = clientId;
        this.callBack = callBack;
    }
    @Override
    public MqRecordMetadata getRecord() {
        return record;
    }

    @Override
    public void run() {
        if (callBack!=null){
            callBack.onCompletion(record,null);
        }else {
            log.info("{}|收到消息 topic={},partition={},offset={},key={},msg={}",
                    clientId,
                    record.getTopic(),
                    record.getPartition(),
                    record.getOffset(),
                    record.getKey(),
                    record.getMsg());
        }
    }
}
