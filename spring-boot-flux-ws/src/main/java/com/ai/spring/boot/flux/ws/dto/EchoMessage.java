package com.ai.spring.boot.flux.ws.dto;

import lombok.Data;

/**
 * 消息体数据
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Data
public class EchoMessage {
    private String from;
    private String to;
    private String msg;
    private int msgType;
    /**消息ID,用于消息状态更新*/
    private transient Long messageId;
}
