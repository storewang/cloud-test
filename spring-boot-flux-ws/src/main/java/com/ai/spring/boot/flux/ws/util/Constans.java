package com.ai.spring.boot.flux.ws.util;

/**
 * 常量类
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
public final class Constans {
    public static final String PARAM_SPLIT    = "&";
    public static final String PARAM_KV_SPLIT = "=";
    public static final String PARAM_TOKEN    = "token";
    public static final String EMPTY_STR      = "";
    public static final String SERVICE_NAME   = "MSG-SERVICE-";
    public static final String MESSAGE_TOPIC  = "MSG-DIRECT-TOPIC";
    /**消息未发送*/
    public static final Integer MSG_STATUS_NO_SEND = 0;
    /**消息已经发送*/
    public static final Integer MSG_STATUS_SENDED  = 1;
    /**消息已读*/
    public static final Integer MSG_STATUS_READED  = 2;
}
