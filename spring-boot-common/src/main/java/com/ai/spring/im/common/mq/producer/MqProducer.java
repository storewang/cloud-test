package com.ai.spring.im.common.mq.producer;

import com.ai.spring.im.common.mq.MqRecordMetadata;
import com.ai.spring.im.common.mq.MqCallBack;
import com.ai.spring.im.common.util.IdWorker;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * 消息发送者
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Slf4j
public abstract class MqProducer {
    protected static final String MSGID_PREFIX = "MQ-";
    protected abstract void doSend(MqProducerRecord record, MqCallBack callBack);
    public abstract void initProducer(Properties properties);

    public void send(MqProducerRecord record,MqCallBack callBack){
        if (record == null || record.getContent() == null || record.getContent().length() == 0){
            log.warn("---------消息不能为空-------------");
            return;
        }
        if (record.getTopic() == null || record.getTopic().length() == 0){
            log.warn("---------消息主题(topic)不能为空-------------");
            return;
        }
        if (callBack == null){
            callBack = new ProducerCallBack();
        }
        if (record.getMsgId() == null || record.getMsgId().length() == 0){
            record.setMsgId(getRandomMsgId());
        }
        doSend(record,callBack);
    }
    protected String getRandomMsgId(){
        return  MSGID_PREFIX + IdWorker.getIdStr();
    }
    protected class ProducerCallBack implements MqCallBack {

        @Override
        public void onCompletion(MqRecordMetadata metadata, Throwable e) {
            if (e!=null){
                log.info("发送消息失败:",e);
                return;
            }
            log.info("发送消息 topic={},partition={},offset={},key={},msg={}",
                    metadata.getTopic(),
                    metadata.getPartition(),
                    metadata.getOffset(),
                    metadata.getKey(),
                    metadata.getMsg());
        }
    }
}
