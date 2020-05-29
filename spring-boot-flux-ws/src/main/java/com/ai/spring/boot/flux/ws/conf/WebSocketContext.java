package com.ai.spring.boot.flux.ws.conf;

import com.ai.spring.boot.flux.ws.dto.EchoMessage;
import com.ai.spring.boot.flux.ws.util.EchoMessageJsonUtil;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * web socket context
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Component
public class WebSocketContext {
    private ConcurrentHashMap<String,WebSocketSessionContext> socketContext = new ConcurrentHashMap<>(16);
    private ConcurrentHashMap<String,String> sessionUserIdMap = new ConcurrentHashMap<>(16);


    public WebSocketSessionContext addSocketSession(String uid,WebSocketSessionContext sessionContext){
        return socketContext.putIfAbsent(uid, sessionContext);
    }

    public void addSocketSession(String sessionId,String uid){
        sessionUserIdMap.putIfAbsent(sessionId,uid);
    }

    public void removeSocketSessionWithSessionId(String sessionId){
        String uid = sessionUserIdMap.get(sessionId);
        Optional.ofNullable(uid).ifPresent(id -> {
            sessionUserIdMap.remove(sessionId);
            socketContext.remove(id);
        });
    }

    public WebSocketSessionContext getSocketSessionWithSessionId(String sessionId){
        String uid = sessionUserIdMap.get(sessionId);
        return Optional.ofNullable(getSocketSession(uid)).orElse(null);
    }

    public WebSocketSessionContext getSocketSession(String uid){
        return socketContext.get(uid);
    }

    public void sendMessage(EchoMessage echoMessage){
        WebSocketSessionContext socketSession = getSocketSession(echoMessage.getTo());
        if (socketSession!=null){
            socketSession.sendData(EchoMessageJsonUtil.toJson(echoMessage));
        }else {
            // 连接会话不在这台机器上，有可能是用户已经下线，或是连接在其他机器上，
            // 这里可以使用注册中心进行判断是否在线，来判断这个消息是否需要保存到离线消息表中
            // 如果在线则转发消息.1: 使用kafka等分布式消息，2: 获取具体的连接进行消息分发.
            // 如果不在线，则存入离线消息表，等用户下次启动重新建立连接后，获取离线消息，并推送给客户端。
        }
    }
}
