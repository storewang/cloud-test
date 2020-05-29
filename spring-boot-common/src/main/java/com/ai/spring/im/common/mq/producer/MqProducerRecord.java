package com.ai.spring.im.common.mq.producer;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息记录
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Slf4j
@Data
public class MqProducerRecord {
    private String topic;
    private String content;
    private String msgId;
    private String msgKey;
}
