package com.ai.spring.boot.netty.ws.util;

import lombok.Getter;

/**
 * 消息类型
 *
 * @author 石头
 * @Date 2020/6/29
 * @Version 1.0
 **/
public enum MessageType {
    /**授权失败*/
    ACCESS_DENIED(10),
    /**授权成功*/
    ACCESS_ALLOWED(11),
    /**消息格式不正确*/
    MSG_JSON_ERROR(12),
    /**用户信息错误*/
    USER_ERROR(13),

    /**用户上线*/
    USER_ONLINE(1),
    /**用户下线*/
    USER_OFFLINE(2),
    /**用户群发*/
    USER_GROUP(3),

    /**聊天消息*/
    MSG_CONTENT(6),
    /**离线聊天消息*/
    MSG_OFFLINE_CONTENT(7),

    /**心跳消息*/
    HEART_BEAT(8),
    ;
    @Getter
    private Integer msgType;
    MessageType(Integer msgType){
        this.msgType = msgType;
    }
}
