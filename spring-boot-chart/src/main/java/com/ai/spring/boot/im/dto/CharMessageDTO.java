package com.ai.spring.boot.im.dto;

import lombok.Data;

import java.util.Date;

/**
 * 消息实体
 *
 * @author 石头
 * @Date 2020/4/2
 * @Version 1.0
 **/
@Data
public class CharMessageDTO {
    /**消息发送者*/
    private Long fromUid;
    /**消息发接受者*/
    private Long toUid;
    /**消息内容*/
    private String msg;
    /**消息发送时间*/
    private Date sendTime;
}
