package com.ai.spring.im.common.mq;

import lombok.Data;

/**
 * 消息元数据
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Data
public class MqRecordMetadata {
    private String topic;
    private long offset;
    private String key;
    private String msg;
    private int partition;
    private String msgId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MqRecordMetadata that = (MqRecordMetadata) o;

        return msgId != null ? msgId.equals(that.msgId) : that.msgId == null;
    }

    @Override
    public int hashCode() {
        return msgId != null ? msgId.hashCode() : 0;
    }
}
