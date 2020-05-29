package com.ai.spring.im.common.mq;

/**
 * 发者回调
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
public interface MqCallBack {
    void onCompletion(MqRecordMetadata metadata,Throwable e);
}
