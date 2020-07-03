package com.ai.spring.boot.netty.ws.util;

import com.ai.spring.im.common.util.IdWorker;

/**
 * 常量
 *
 * @author 石头
 * @Date 2020/6/29
 * @Version 1.0
 **/
public final class Consts {
    public static final String HTTP_SCHEMA       = "http://";
    public static final String HTTPS_SCHEMA      = "https://";
    public static final String FORWARD_METHOD    = "/sendMsg/%s";
    public static final String HOME_METHOD       = "/";
    public static final String RSP_OK            = "ok";
    public static final String RSP_IGNORE        = "Ignore";
    public static final String STR_SPLIT         = ":";
    public static final String URL_SPLIT         = "?";
    public static final String URL_TOKEN_KEY     = "token";
    public static final String CHANNEL_TOKEN_KEY = "netty.channel.token";
    public static final String MSG_PREFIX        = "MSG-";

    /**消息未发送*/
    public static final Integer MSG_STATUS_NO_SEND = 0;
    /**消息已经发送*/
    public static final Integer MSG_STATUS_SENDED  = 1;
    /**消息已读*/
    public static final Integer MSG_STATUS_READED  = 2;

    public static String getMsgReqId(){
        return MSG_PREFIX + IdWorker.getIdStr();
    }
}
