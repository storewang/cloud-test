package com.ai.spring.boot.im.dto;

import lombok.Data;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/4/20
 * @Version 1.0
 **/
@Data
public class NotifyInfoData<T> {
    /**消息类型*/
    private String notifyType;
    /**时间戳	*/
    private String timestamp;
    /**跟踪ID*/
    private String traceId;
    /**接入ID*/
    private String appId;
    /**具体的消息数据*/
    private T data;
}
