package com.ai.spring.boot.im.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

/**
 * 消息处理
 *
 * @author 石头
 * @Date 2020/3/3
 * @Version 1.0
 **/
//@Component
public class MessageHandler extends TextWebSocketHandler {
    /**
     * 建立连接
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 这里可以获取连接鉴权时存放的信息
        Map<String,?> attributes = session.getAttributes();


    }

    /**
     * 接收到消息
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    }

    /**
     * 关闭连接
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

    }
}
