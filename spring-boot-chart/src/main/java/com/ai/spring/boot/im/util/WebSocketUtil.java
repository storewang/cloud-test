package com.ai.spring.boot.im.util;

import com.ai.spring.boot.im.dto.CharMessageDTO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 工具类
 *
 * @author 石头
 * @Date 2020/4/2
 * @Version 1.0
 **/
@Slf4j
public final class WebSocketUtil {
    private static final ConcurrentHashMap<Long,WebSocketSession> LOCAL_SESSIONS = new ConcurrentHashMap<>();
    private static final String SESSION_CODE = "code";
    private static final String SESSION_UID  = "uid";
    private static final Integer SESSION_CODE_SUC = 10000;
    private static final Integer SESSION_CODE_ERR = 10001;

    public static void sendMessageTo(CharMessageDTO messageDTO) throws IOException {
        WebSocketSession webSocketSession = LOCAL_SESSIONS.get(messageDTO.getToUid());
        if (webSocketSession!=null && webSocketSession.isOpen()){
            webSocketSession.sendMessage(new TextMessage(JSON.toJSONString(messageDTO)));
        }else {
            // 没有查询到连接，有可能是已经下线了，也有可能连接在其他机器上，
            // 1。这时可以发送广播消息或是调用具有广播的接口
            // 2。使用注册中心记录客户端连接在哪台机器，再直连那台机器进行消息转发。
        }
    }
    public static void removeWebSocketSession(Long uid){
        LOCAL_SESSIONS.remove(uid);
    }

    public static void addWebSocketSession(Long uid,WebSocketSession session){
        LOCAL_SESSIONS.putIfAbsent(uid,session);
    }

    public static Long getWebSocketSessionUid(WebSocketSession session){
        return  (Long) session.getAttributes().get(SESSION_UID);
    }
    public static Integer getWebSocketSessionCode(WebSocketSession session){
        return  (Integer) session.getAttributes().get(SESSION_CODE);
    }

    public static void setWebSocketSessionUid(Map<String, Object> attributes,Long userId){
        attributes.put(SESSION_UID,userId);
    }
    public static void setWebSocketSessionSucCode(Map<String, Object> attributes){
        attributes.put(SESSION_CODE,SESSION_CODE_SUC);
    }
    public static void setWebSocketSessionErrCode(Map<String, Object> attributes){
        attributes.put(SESSION_CODE,SESSION_CODE_ERR);
    }

    public static boolean isSucCode(WebSocketSession session){
        Integer code = WebSocketUtil.getWebSocketSessionCode(session);
        return SESSION_CODE_SUC.equals(code);
    }
}
