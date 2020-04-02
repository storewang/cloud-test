package com.ai.spring.boot.im.ws;

import com.ai.spring.boot.im.dto.CharMessageDTO;
import com.ai.spring.boot.im.util.JacksonJsonParser;
import com.ai.spring.boot.im.util.WebSocketUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息处理
 *
 * @author 石头
 * @Date 2020/3/3
 * @Version 1.0
 **/
@Component
@Slf4j
public class MessageHandler extends TextWebSocketHandler {
    /**
     * 建立连接
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 这里可以获取连接鉴权时存放的信息
        Integer code = WebSocketUtil.getWebSocketSessionCode(session);
        if (WebSocketUtil.isSucCode(session)){
            Long uid = WebSocketUtil.getWebSocketSessionUid(session);
            session.sendMessage(new TextMessage("欢迎["+uid+"]连接到ws服务"));

            WebSocketUtil.addWebSocketSession(uid,session);
        }else {
            log.warn("建立连接校验失败:{}",code);
            session.close();
        }

    }

    /**
     * 接收到消息
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long uid = WebSocketUtil.getWebSocketSessionUid(session);
        String receivedMsg = message.getPayload();
        log.info("["+uid+"]获取到消息>> {}",receivedMsg);

        try {
            CharMessageDTO messageDTO = JacksonJsonParser.getInstance().parseCharMessageDTO(receivedMsg);
            WebSocketUtil.sendMessageTo(messageDTO);
        } catch (Exception e) {
            CharMessageDTO errmsg = new CharMessageDTO();
            errmsg.setFromUid(-99L);
            errmsg.setMsg("消息格式错误");
            errmsg.setSendTime(Calendar.getInstance().getTime());
            session.sendMessage(new TextMessage(JSON.toJSONString(errmsg)));
        }
    }

    /**
     * 关闭连接
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long uid = WebSocketUtil.getWebSocketSessionUid(session);
        log.warn("["+uid+"]已断开连接");
        WebSocketUtil.removeWebSocketSession(uid);
    }
}
