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
    public static final String STR_SPLIT         = ":";
    public static final String URL_SPLIT         = "?";
    public static final String URL_TOKEN_KEY     = "token";
    public static final String CHANNEL_TOKEN_KEY = "netty.channel.token";
    public static final String MSG_PREFIX        = "MSG-";

    public static String getMsgReqId(){
        return MSG_PREFIX + IdWorker.getIdStr();
    }
}
