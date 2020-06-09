package com.ai.spring.boot.flux.dto;

import lombok.Data;

import java.util.Date;

/**
 * 消息发送记录
 *
 * @author 石头
 * @Date 2020/6/9
 * @Version 1.0
 **/
@Data
public class FluxEchoMessageDTO {
    private Long id;
    /**消息发送者*/
    private String sender;
    /**消息接收者*/
    private String receiver;
    /**消息内容*/
    private String msg;
    /**数据有效性*/
    private Integer status;
    /**消息时间*/
    private Date updateTime;
}
